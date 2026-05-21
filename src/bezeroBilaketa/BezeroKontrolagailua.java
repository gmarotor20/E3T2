package bezeroBilaketa;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import menuSaltzaile.MenuSaltzaileKontrolagailua;
import eskaera.EskaeraKontrolagailua;
import inbentarioa.InbentarioaKontrolagailua;
import saltzaileAgenda.SaltzaileAgendaKontrolagailua;
import konexioa.DatuBasea;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

/**
 * Bezeroen bilaketa leihoaren kontrolagailua. Bezeroak izena, abizena edo
 * egoeraren arabera bilatu eta iragaztea ahalbidetzen du, datu-baseko
 * V_BEZEROAK ikuspegia erabiliz.
 *
 * @author E3T2
 * @version 1.0
 */
public class BezeroKontrolagailua implements Initializable {

	/** Bilaketa testua sartzeko testu-eremua. */
	@FXML
	private TextField bilatuTextField;

	/** Bezeroaren egoeraren arabera iragazteko menu botoia. */
	@FXML
	private MenuButton egoeraMenuButton;

	/** Bilaketa exekutatzeko botoia. */
	@FXML
	private Button bilatuButton;

	/** Bilaketa iragazkiak garbitzeko botoia. */
	@FXML
	private Button garbituButton;

	/** Saltzailearen menura itzultzeko botoia. */
	@FXML
	private Button itzuliButton;

	/** Aurkitutako bezeroak erakusten dituen taula. */
	@FXML
	private TableView<BezeroaBean> tableView;

	/** Bezeroaren identifikatzailearen zutabea. */
	@FXML
	private TableColumn<BezeroaBean, Integer> idColumn;

	/** Bezeroaren izen eta abizena kateaturik duen zutabea. */
	@FXML
	private TableColumn<BezeroaBean, String> izenaAbizenaColumn;

	/** Bezeroaren NAN-aren zutabea. */
	@FXML
	private TableColumn<BezeroaBean, String> nanColumn;

	/** Bezeroaren egoeraren zutabea (AKTIBOA/INAKTIBOA). */
	@FXML
	private TableColumn<BezeroaBean, String> egoeraColumn;

	/** Bezeroaren alta dataren zutabea. */
	@FXML
	private TableColumn<BezeroaBean, String> sorreraDataColumn;

	/** Erosketen leihoa irekitzeko MenuItem-a. */
	@FXML
	private MenuItem bezeroEskaerakMenuItem;

	/** Agenda irekitzeko MenuItem-a. */
	@FXML
	private MenuItem formularioaMenuItem;

	/** Inbentarioa irekitzeko MenuItem-a. */
	@FXML
	private MenuItem iListaMenuItem;

	/** Saioa ixteko MenuItem-a. */
	@FXML
	private MenuItem saioaItxiMenuItem;

	/** Leihoaren uneko stage-a. */
	private Stage stage;

	/** Taulan erakusten den bezeroen zerrenda behakorra. */
	private ObservableList<BezeroaBean> bezeroakList = FXCollections.observableArrayList();

	/** Iragazteko hautatutako egoera (Denak, Aktiboa, Inaktiboa). */
	private String selectedEgoera = "Denak";

	/**
	 * Uneko leihoaren stage-a ezartzen du.
	 *
	 * @param stage leihoaren stage-a.
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	/**
	 * Kontrolagailua hasieratzen du taularen zutabeak konfiguratuz, iragazkien
	 * entzuleak ezarriz eta bezero guztiak kargatuz.
	 *
	 * @param location  FXMLaren kokapenaren URL-a.
	 * @param resources lokalizazio baliabideak.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		izenaAbizenaColumn.setCellValueFactory(new PropertyValueFactory<>("izenaAbizena"));
		nanColumn.setCellValueFactory(new PropertyValueFactory<>("nan"));
		egoeraColumn.setCellValueFactory(new PropertyValueFactory<>("egoera"));
		sorreraDataColumn.setCellValueFactory(new PropertyValueFactory<>("sorreraData"));

		for (MenuItem item : egoeraMenuButton.getItems()) {
			item.setOnAction(e -> {
				selectedEgoera = item.getText();
				egoeraMenuButton.setText(selectedEgoera);
				bilatuBezeroak();
			});
		}

		bilatuButton.setOnAction(e -> bilatuBezeroak());
		garbituButton.setOnAction(e -> garbituFilterrak());
		itzuliButton.setOnAction(e -> itzuli());

		if (bezeroEskaerakMenuItem != null)
			bezeroEskaerakMenuItem.setOnAction(e -> abrirEskaerak());
		if (formularioaMenuItem != null)
			formularioaMenuItem.setOnAction(e -> abrirAgenda());
		if (iListaMenuItem != null)
			iListaMenuItem.setOnAction(e -> abrirInbentarioa());
		if (saioaItxiMenuItem != null)
			saioaItxiMenuItem.setOnAction(e -> saioaItxi());

		kargatuBezeroGuztiak();
	}

	/**
	 * Datu-baseko V_BEZEROAK ikuspegitik bezero guztiak kargatzen ditu.
	 */
	private void kargatuBezeroGuztiak() {
		String query = "SELECT * FROM e2t2erronka.V_BEZEROAK ORDER BY ID";
		DatuBasea db = new DatuBasea();

		try (Connection conn = db.konektoreaWorld();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {

			bezeroakList.clear();

			while (rs.next()) {
				BezeroaBean bezeroa = new BezeroaBean(rs.getInt("ID"), rs.getString("IZENA_ABIZENA"),
						rs.getString("NAN"), rs.getString("EGOERA"), rs.getString("SORRERA_DATA"));
				bezeroakList.add(bezeroa);
			}

			tableView.setItems(bezeroakList);
			System.out.println("Bezeroak kargatuta: " + bezeroakList.size());

		} catch (SQLException e) {
			e.printStackTrace();
			erakutsiAlert("Errorea", "Datuak kargatzerakoan errorea: " + e.getMessage());
		}
	}

	/**
	 * Hautatutako testu eta egoera iragazkiak aplikatuz bezeroak bilatzen ditu.
	 * Datu-baseko BilatuBezeroak stored procedure-a erabiltzen du.
	 */
	private void bilatuBezeroak() {
		String bilaketaTestua = bilatuTextField.getText().trim();
		String egoeraParam = null;

		if (!selectedEgoera.equals("Denak")) {
			egoeraParam = selectedEgoera.equals("Aktiboa") ? "AKTIBOA" : "INAKTIBOA";
		}

		DatuBasea db = new DatuBasea();

		try (Connection conn = db.konektoreaWorld();
				CallableStatement cstmt = conn.prepareCall("{CALL BilatuBezeroak(?, ?)}")) {

			if (bilaketaTestua.isEmpty()) {
				cstmt.setNull(1, java.sql.Types.VARCHAR);
			} else {
				cstmt.setString(1, bilaketaTestua);
			}

			if (egoeraParam == null) {
				cstmt.setNull(2, java.sql.Types.VARCHAR);
			} else {
				cstmt.setString(2, egoeraParam);
			}

			ResultSet rs = cstmt.executeQuery();
			bezeroakList.clear();

			while (rs.next()) {
				BezeroaBean bezeroa = new BezeroaBean(rs.getInt("ID"), rs.getString("IZENA_ABIZENA"),
						rs.getString("NAN"), rs.getString("EGOERA"), rs.getString("SORRERA_DATA"));
				bezeroakList.add(bezeroa);
			}

			tableView.setItems(bezeroakList);
			System.out.println("Bilaketaren emaitza: " + bezeroakList.size() + " bezero");

		} catch (SQLException e) {
			e.printStackTrace();
			erakutsiAlert("Errorea", "Bilaketan errorea: " + e.getMessage());
		}
	}

	/**
	 * Bilaketa iragazkiak garbitzen ditu eta bezero guztiak berriro kargatzen ditu.
	 */
	private void garbituFilterrak() {
		bilatuTextField.clear();
		selectedEgoera = "Denak";
		egoeraMenuButton.setText("Egoera");
		kargatuBezeroGuztiak();
	}

	/**
	 * Saltzailearen menu nagusira itzultzen da.
	 */
	private void itzuli() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/menuSaltzaile/MenuSaltzaile.fxml"));
			Scene scene = new Scene(loader.load());
			Stage menuStage = new Stage();
			menuStage.setTitle("Menu Saltzailea");
			menuStage.setScene(scene);
			MenuSaltzaileKontrolagailua controller = loader.getController();
			controller.setStage(menuStage);
			if (stage != null)
				stage.close();
			menuStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Bezeroek egindako erosketen leihoa irekitzen du.
	 */
	private void abrirEskaerak() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/eskaera/Eskaerak.fxml"));
			Scene scene = new Scene(loader.load());
			Stage eskaeraStage = new Stage();
			eskaeraStage.setTitle("Eskaerak");
			eskaeraStage.setScene(scene);
			EskaeraKontrolagailua controller = loader.getController();
			controller.setStage(eskaeraStage);
			if (stage != null)
				stage.close();
			eskaeraStage.show();
		} catch (Exception e) {
			e.printStackTrace();
			erakutsiAlert("Errorea", "Ezin izan da Eskaerak ireki: " + e.getMessage());
		}
	}

	/**
	 * Saltzaileen agenda leihoa irekitzen du.
	 */
	private void abrirAgenda() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/saltzaileAgenda/SaltzaileAgenda.fxml"));
			Scene scene = new Scene(loader.load());
			Stage agendaStage = new Stage();
			agendaStage.setTitle("Agenda");
			agendaStage.setScene(scene);
			SaltzaileAgendaKontrolagailua controller = loader.getController();
			controller.setStage(agendaStage);
			if (stage != null)
				stage.close();
			agendaStage.show();
		} catch (Exception e) {
			e.printStackTrace();
			erakutsiAlert("Errorea", "Ezin izan da Agenda ireki: " + e.getMessage());
		}
	}

	/**
	 * Produktuen inbentario leihoa irekitzen du.
	 */
	private void abrirInbentarioa() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/inbentarioa/Inbentario.fxml"));
			Scene scene = new Scene(loader.load());
			Stage inbentarioStage = new Stage();
			inbentarioStage.setTitle("Inbentarioa");
			inbentarioStage.setScene(scene);
			InbentarioaKontrolagailua controller = loader.getController();
			controller.setStage(inbentarioStage);
			if (stage != null)
				stage.close();
			inbentarioStage.show();
		} catch (Exception e) {
			e.printStackTrace();
			erakutsiAlert("Errorea", "Ezin izan da Inbentarioa ireki: " + e.getMessage());
		}
	}

	/**
	 * Saioa ixten du eta loginera bideratzen du.
	 */
	private void saioaItxi() {
		try {
			if (stage != null)
				stage.close();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/login/Login.fxml"));
			Scene scene = new Scene(loader.load());
			Stage loginStage = new Stage();
			loginStage.setTitle("Login");
			loginStage.setScene(scene);
			loginStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Errore mezu bat alerta leiho batean erakusten du.
	 *
	 * @param titulua alertaren titulua.
	 * @param mezua   alertaren mezua.
	 */
	private void erakutsiAlert(String titulua, String mezua) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(titulua);
		alert.setHeaderText(null);
		alert.setContentText(mezua);
		alert.showAndWait();
	}
}