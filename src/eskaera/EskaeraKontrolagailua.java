package eskaera;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import menuSaltzaile.MenuSaltzaileKontrolagailua;
import konexioa.DatuBasea;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

/**
 * Bezeroek egindako erosketen leihoaren kontrolagailua. V_EROSKETAK ikuspegitik
 * eskaeraren arabera taldekatutako erosketa guztiak erakusten ditu. Saltzaileak
 * klik bikoitzaren bidez erosketa bakoitzaren xehetasunak ikustea ahalbidetzen
 * du.
 *
 * @author E3T2
 * @version 1.0
 */
public class EskaeraKontrolagailua implements Initializable {

	/** Egindako erosketak erakusten dituen taula. */
	@FXML
	private TableView<EskaeraBean> tableView;

	/** Erosketaren identifikatzaile bakarraren zutabea. */
	@FXML
	private TableColumn<EskaeraBean, String> erosketaIdColumn;

	/** Erosketa egin duen bezeroaren izenaren zutabea. */
	@FXML
	private TableColumn<EskaeraBean, String> bezeroaColumn;

	/** Komaz bereizitako erositako produktuen zutabea. */
	@FXML
	private TableColumn<EskaeraBean, String> produktuakColumn;

	/** Erosketaren prezio totalaren zutabea. */
	@FXML
	private TableColumn<EskaeraBean, Double> totalColumn;

	/** Erabilitako ordainketa metodoaren zutabea. */
	@FXML
	private TableColumn<EskaeraBean, String> ordainketaMetodoaColumn;

	/** Erosketaren data eta orduaren zutabea. */
	@FXML
	private TableColumn<EskaeraBean, String> dataColumn;

	/** Saltzailearen menura itzultzeko botoia. */
	@FXML
	private Button itzuliButton;

	/** Leihoaren uneko stage-a. */
	private Stage stage;

	/** Taulan erakusten den erosketen zerrenda behakorra. */
	private ObservableList<EskaeraBean> erosketakList = FXCollections.observableArrayList();

	/**
	 * Uneko leihoaren stage-a ezartzen du.
	 *
	 * @param stage leihoaren stage-a.
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	/**
	 * Kontrolagailua hasieratzen du taularen zutabeak konfiguratuz, erosketak
	 * kargatuz eta xehetasunak ikusteko klik bikoitza konfiguratuz.
	 *
	 * @param location  FXMLaren kokapenaren URL-a.
	 * @param resources lokalizazio baliabideak.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		erosketaIdColumn.setCellValueFactory(new PropertyValueFactory<>("erosketaId"));
		bezeroaColumn.setCellValueFactory(new PropertyValueFactory<>("bezeroa"));
		produktuakColumn.setCellValueFactory(new PropertyValueFactory<>("produktuak"));
		totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
		ordainketaMetodoaColumn.setCellValueFactory(new PropertyValueFactory<>("ordainketaMetodoa"));
		dataColumn.setCellValueFactory(new PropertyValueFactory<>("data"));

		kargatuErosketak();
		itzuliButton.setOnAction(e -> itzuli());

		// Erosketaren xehetasunak ikusteko klik bikoitza
		tableView.setRowFactory(tv -> {
			TableRow<EskaeraBean> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					EskaeraBean selected = row.getItem();
					mostrarVentanaErosketa(selected);
				}
			});
			return row;
		});
	}

	/**
	 * Datu-baseko V_EROSKETAK ikuspegitik erosketa guztiak kargatzen ditu,
	 * eskaeraren identifikatzailearen arabera taldekatuta.
	 */
	private void kargatuErosketak() {
		String query = "SELECT * FROM e2t2erronka.V_EROSKETAK";
		DatuBasea db = new DatuBasea();

		try (Connection conn = db.konektoreaWorld();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {

			erosketakList.clear();

			while (rs.next()) {
				EskaeraBean erosketa = new EskaeraBean(rs.getString("EROSKETAID"), rs.getString("BEZEROA"),
						rs.getString("PRODUKTUAK"), rs.getDouble("TOTAL"), rs.getString("ORDAINKETA_METODOA"),
						rs.getString("DATA"));
				erosketakList.add(erosketa);
			}

			tableView.setItems(erosketakList);

		} catch (SQLException e) {
			e.printStackTrace();
			erakutsiAlert("Errorea", "Datuak kargatzerakoan errorea: " + e.getMessage());
		}
	}

	/**
	 * Hautatutako erosketaren xehetasun osoak dituen leiho modal bat irekitzen du.
	 *
	 * @param erosketa taulan hautatutako erosketa.
	 */
	private void mostrarVentanaErosketa(EskaeraBean erosketa) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ErosketaLeihoa.fxml"));
			Parent root = loader.load();

			ErosketaLeihoaKontrolagailua controller = loader.getController();
			controller.setErosketaDatuak(erosketa);

			Stage modalStage = new Stage();
			modalStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
			modalStage.initOwner(tableView.getScene().getWindow());
			modalStage.setTitle("Erosketa - " + erosketa.getErosketaId());
			modalStage.setScene(new Scene(root));
			modalStage.setResizable(false);

			controller.setModalStage(modalStage);
			modalStage.showAndWait();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Saltzailearen menu nagusira itzultzen da.
	 */
	private void itzuli() {
		try {
			if (stage != null)
				stage.close();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/menuSaltzaile/MenuSaltzaile.fxml"));
			Scene scene = new Scene(loader.load());
			Stage menuStage = new Stage();
			menuStage.setTitle("Menu Saltzailea");
			menuStage.setScene(scene);
			MenuSaltzaileKontrolagailua controller = loader.getController();
			controller.setStage(menuStage);
			menuStage.show();
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