package inbentarioa;

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
 * Produktuen inbentario leihoaren kontrolagailua. Saltzaileak produktuak ikusi,
 * bilatu, produktu berriak gehitu eta dauden produktuen stocka eguneratzea
 * ahalbidetzen du. Datu-baseko V_INBENTARIOA ikuspegia erabiltzen du.
 *
 * @author E3T2
 * @version 1.0
 */
public class InbentarioaKontrolagailua implements Initializable {

	/** Produktuak ID edo izenaren arabera bilatzeko testu-eremua. */
	@FXML
	private TextField bilatuTextField;

	/** Inbentarioko produktuak erakusten dituen taula. */
	@FXML
	private TableView<Produktua> tableView;

	/** Produktuaren identifikatzailearen zutabea. */
	@FXML
	private TableColumn<Produktua, Integer> idColumn;

	/** Produktuaren izenaren zutabea. */
	@FXML
	private TableColumn<Produktua, String> izenaColumn;

	/** Produktuaren stock eskuragarriaren zutabea. */
	@FXML
	private TableColumn<Produktua, Integer> stockColumn;

	/** Produktuaren prezioaren zutabea. */
	@FXML
	private TableColumn<Produktua, Double> prezioaColumn;

	/** Produktu berriaren formularioa irekitzeko botoia. */
	@FXML
	private Button produktuBerriaButton;

	/** Saltzailearen menura itzultzeko botoia. */
	@FXML
	private Button itzuliButton;

	/** Dauden produktuei stocka gehitzeko botoia. */
	@FXML
	private Button stockGehituButton;

	/** Leihoaren uneko stage-a. */
	private Stage stage;

	/** Taulan erakusten den produktuen zerrenda behakorra. */
	private ObservableList<Produktua> produktuakList = FXCollections.observableArrayList();

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
		idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		izenaColumn.setCellValueFactory(new PropertyValueFactory<>("izena"));
		stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
		prezioaColumn.setCellValueFactory(new PropertyValueFactory<>("prezioa"));

		kargatuDatuak();
		tableView.setItems(produktuakList);

		bilatuTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			bilatuProduktuak(newValue);
		});

		produktuBerriaButton.setOnAction(e -> gehituProduktua());
		stockGehituButton.setOnAction(e -> stockGehitu());
		itzuliButton.setOnAction(e -> itzuli());
	}

	/**
	 * Datu-baseko V_INBENTARIOA ikuspegitik produktu guztiak kargatzen ditu.
	 */
	public void kargatuDatuak() {
		String query = "SELECT * FROM e2t2erronka.V_INBENTARIOA";
		DatuBasea db = new DatuBasea();

		try (Connection conn = db.konektoreaWorld();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {

			produktuakList.clear();

			while (rs.next()) {
				Produktua produktua = new Produktua(rs.getInt("ID"), rs.getString("IZENA"), rs.getInt("STOCK"),
						rs.getDouble("PREZIOA"));
				produktuakList.add(produktua);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			erakutsiAlert("Errorea", "Datuak kargatzerakoan errorea: " + e.getMessage());
		}
	}

	/**
	 * V_INBENTARIOA ikuspegian produktuak ID edo izenaren arabera bilatzen ditu.
	 * Bilaketa testua hutsik badago, produktu guztiak kargatzen ditu.
	 *
	 * @param bilaketaTestua erabiltzaileak sartutako bilaketa testua.
	 */
	private void bilatuProduktuak(String bilaketaTestua) {
		if (bilaketaTestua == null || bilaketaTestua.trim().isEmpty()) {
			kargatuDatuak();
			return;
		}

		String query = "SELECT * FROM e2t2erronka.V_INBENTARIOA WHERE ID LIKE ? OR IZENA LIKE ?";
		DatuBasea db = new DatuBasea();

		try (Connection conn = db.konektoreaWorld(); PreparedStatement pstmt = conn.prepareStatement(query)) {

			String pattern = "%" + bilaketaTestua + "%";
			pstmt.setString(1, pattern);
			pstmt.setString(2, pattern);

			ResultSet rs = pstmt.executeQuery();
			produktuakList.clear();

			while (rs.next()) {
				Produktua produktua = new Produktua(rs.getInt("ID"), rs.getString("IZENA"), rs.getInt("STOCK"),
						rs.getDouble("PREZIOA"));
				produktuakList.add(produktua);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			erakutsiAlert("Errorea", "Bilaketan errorea: " + e.getMessage());
		}
	}

	/**
	 * Inbentarioan produktu berria gehitzeko formulario modal bat irekitzen du.
	 */
	private void gehituProduktua() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ProduktuFormularioa.fxml"));
			javafx.scene.Parent root = loader.load();

			ProduktuFormularioaKontrolagailua controller = loader.getController();
			controller.setParentController(this);

			Stage modalStage = new Stage();
			modalStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
			modalStage.initOwner(stage);
			modalStage.setTitle("Gehitu produktua");
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
	 * Hautatutako produktuari stocka gehitzeko formulario modal bat irekitzen du.
	 * Formularioa ireki aurretik taulan produktu bat hautatuta dagoela balioztatzen
	 * du.
	 */
	private void stockGehitu() {
		Produktua selected = tableView.getSelectionModel().getSelectedItem();

		if (selected == null) {
			erakutsiAlert("Errorea", "Mesedez, hautatu produktu bat.");
			return;
		}

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("StockFormularioa.fxml"));
			javafx.scene.Parent root = loader.load();

			StockFormularioaKontrolagailua controller = loader.getController();
			controller.setParentController(this);
			controller.setProduktuDatuak(selected.getId(), selected.getIzena(), selected.getStock());

			Stage modalStage = new Stage();
			modalStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
			modalStage.initOwner(stage);
			modalStage.setTitle("Stock Gehitu");
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