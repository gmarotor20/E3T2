package saltzaileAgenda;

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
import konexioa.DatuBasea;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

/**
 * Saltzaileen agenda leihoaren kontrolagailua. Sistema administratzailearentzat
 * soilik eskuragarri dago. Datu-baseko V_SALTZAILEAK ikuspegia erabiliz
 * sistemako saltzaileak ikusi, bilatu eta gehitzea ahalbidetzen du.
 *
 * @author E3T2
 * @version 1.0
 */
public class SaltzaileAgendaKontrolagailua implements Initializable {

	/** Sistemako saltzaileak erakusten dituen taula. */
	@FXML
	private TableView<SaltzaileBean> tableView;

	/** Saltzailearen erabiltzaile izenaren zutabea. */
	@FXML
	private TableColumn<SaltzaileBean, String> erabiltzaileaColumn;

	/** Saltzailearen sarrera dataren zutabea. */
	@FXML
	private TableColumn<SaltzaileBean, String> sarreraColumn;

	/** Saltzailearen irteera dataren zutabea. */
	@FXML
	private TableColumn<SaltzaileBean, String> irteeraColumn;

	/** Saltzailearen rola edo oharren zutabea. */
	@FXML
	private TableColumn<SaltzaileBean, String> aldaketakColumn;

	/** Saltzailearen menura itzultzeko botoia. */
	@FXML
	private Button itzuliButton;

	/** Saltzaileak erabiltzaile izenaren arabera bilatzeko testu-eremua. */
	@FXML
	private TextField bilatuTextField;

	/** Saltzaile berriaren formularioa irekitzeko botoia. */
	@FXML
	private Button gehituButton;

	/** Leihoaren uneko stage-a. */
	private Stage stage;

	/** Taulan erakusten den saltzaileen zerrenda behakorra. */
	private ObservableList<SaltzaileBean> saltzaileakList = FXCollections.observableArrayList();

	/**
	 * Uneko leihoaren stage-a ezartzen du.
	 *
	 * @param stage leihoaren stage-a.
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	/**
	 * Kontrolagailua hasieratzen du taularen zutabeak konfiguratuz, bilaketa
	 * entzulea ezarriz eta ekintza botoiak konfiguratuz.
	 *
	 * @param location  FXMLaren kokapenaren URL-a.
	 * @param resources lokalizazio baliabideak.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		erabiltzaileaColumn.setCellValueFactory(new PropertyValueFactory<>("erabiltzailea"));
		sarreraColumn.setCellValueFactory(new PropertyValueFactory<>("sarrera"));
		irteeraColumn.setCellValueFactory(new PropertyValueFactory<>("irteera"));
		aldaketakColumn.setCellValueFactory(new PropertyValueFactory<>("aldaketak"));

		kargatuSaltzaileak();
		itzuliButton.setOnAction(e -> itzuli());

		bilatuTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			bilatuSaltzaileak(newValue);
		});

		gehituButton.setOnAction(e -> gehituSaltzailea());
	}

	/**
	 * Datu-baseko V_SALTZAILEAK ikuspegitik saltzaile guztiak kargatzen ditu.
	 */
	public void kargatuSaltzaileak() {
		String query = "SELECT * FROM e2t2erronka.V_SALTZAILEAK ORDER BY ID";
		DatuBasea db = new DatuBasea();

		try (Connection conn = db.konektoreaWorld();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {

			saltzaileakList.clear();

			while (rs.next()) {
				SaltzaileBean saltzailea = new SaltzaileBean(rs.getInt("ID"), rs.getString("ERABILTZAILEA"),
						rs.getString("SARRERA") != null ? rs.getString("SARRERA") : "",
						rs.getString("IRTEERA") != null ? rs.getString("IRTEERA") : "",
						rs.getString("ALDAKETAK") != null ? rs.getString("ALDAKETAK") : "");
				saltzaileakList.add(saltzailea);
			}

			tableView.setItems(saltzaileakList);

		} catch (SQLException e) {
			e.printStackTrace();
			erakutsiAlert("Errorea", "Datuak kargatzerakoan errorea: " + e.getMessage());
		}
	}

	/**
	 * V_SALTZAILEAK ikuspegian saltzaileak erabiltzaile izenaren arabera bilatzen
	 * ditu. Bilaketa testua hutsik badago, saltzaile guztiak kargatzen ditu.
	 *
	 * @param bilaketaTestua erabiltzaileak sartutako bilaketa testua.
	 */
	private void bilatuSaltzaileak(String bilaketaTestua) {
		if (bilaketaTestua == null || bilaketaTestua.trim().isEmpty()) {
			kargatuSaltzaileak();
			return;
		}

		String query = "SELECT * FROM e2t2erronka.V_SALTZAILEAK WHERE ERABILTZAILEA LIKE ? ORDER BY ID";
		DatuBasea db = new DatuBasea();

		try (Connection conn = db.konektoreaWorld(); PreparedStatement pstmt = conn.prepareStatement(query)) {

			String pattern = "%" + bilaketaTestua + "%";
			pstmt.setString(1, pattern);

			ResultSet rs = pstmt.executeQuery();
			saltzaileakList.clear();

			while (rs.next()) {
				SaltzaileBean saltzailea = new SaltzaileBean(rs.getInt("ID"), rs.getString("ERABILTZAILEA"),
						rs.getString("SARRERA") != null ? rs.getString("SARRERA") : "",
						rs.getString("IRTEERA") != null ? rs.getString("IRTEERA") : "",
						rs.getString("ALDAKETAK") != null ? rs.getString("ALDAKETAK") : "");
				saltzaileakList.add(saltzailea);
			}

			tableView.setItems(saltzaileakList);

		} catch (SQLException e) {
			e.printStackTrace();
			erakutsiAlert("Errorea", "Bilaketan errorea: " + e.getMessage());
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
	 * Sistemara saltzaile berria gehitzeko formulario modal bat irekitzen du.
	 */
	private void gehituSaltzailea() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("SaltzaileFormularioa.fxml"));
			javafx.scene.Parent root = loader.load();

			SaltzaileFormularioaKontrolagailua controller = loader.getController();
			controller.setParentController(this);

			Stage modalStage = new Stage();
			modalStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
			modalStage.initOwner(stage);
			modalStage.setTitle("Gehitu saltzailea");
			modalStage.setScene(new Scene(root));
			modalStage.setResizable(false);

			controller.setModalStage(modalStage);
			modalStage.showAndWait();

		} catch (Exception e) {
			e.printStackTrace();
			erakutsiAlert("Errorea", "Ezin izan da formularioa ireki: " + e.getMessage());
		}
	}

	/**
	 * Mezu informatibo bat alerta leiho batean erakusten du.
	 *
	 * @param titulua alertaren titulua.
	 * @param mezua   alertaren mezua.
	 */
	private void erakutsiAlert(String titulua, String mezua) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(titulua);
		alert.setHeaderText(null);
		alert.setContentText(mezua);
		alert.showAndWait();
	}
}