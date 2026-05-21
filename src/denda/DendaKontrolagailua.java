package denda;

import eskaera.KarritoBean;
import inbentarioa.Produktua;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import konexioa.DatuBasea;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

/**
 * Denda leihoaren kontrolagailua. Bezeroak produktuak hautatu, saskira gehitu
 * eta ordainketa prozesua egitea ahalbidetzen du. Denbora errealean eskuragarri
 * dagoen stocka kudeatzen du.
 *
 * @author E3T2
 * @version 1.0
 */
public class DendaKontrolagailua implements Initializable {

	/** Eskuragarri dauden produktuak erakusten dituen taula. */
	@FXML
	private TableView<Produktua> produktuakTable;

	/** Produktuaren izenaren zutabea. */
	@FXML
	private TableColumn<Produktua, String> izenaColumn;

	/** Produktuaren stock eskuragarriaren zutabea. */
	@FXML
	private TableColumn<Produktua, Integer> stockColumn;

	/** Produktuaren prezioaren zutabea. */
	@FXML
	private TableColumn<Produktua, Double> prezioaColumn;

	/** Saskira gehitu beharreko produktuaren kantitatea hautatzeko elementua. */
	@FXML
	private Spinner<Integer> kantitateaSpinner;

	/** Saskian gehitutako produktuak erakusten dituen taula. */
	@FXML
	private TableView<KarritoBean> karritoTable;

	/** Saskiko produktuaren izenaren zutabea. */
	@FXML
	private TableColumn<KarritoBean, String> karritoProduktuColumn;

	/** Saskiko produktuaren kantitatearen zutabea. */
	@FXML
	private TableColumn<KarritoBean, Integer> karritoKantitateaColumn;

	/** Saskiko produktuaren prezio unitarioaren zutabea. */
	@FXML
	private TableColumn<KarritoBean, Double> karritoPrezioanColumn;

	/** Saskiko produktuaren prezio totalaren zutabea. */
	@FXML
	private TableColumn<KarritoBean, Double> karritoTotalaColumn;

	/** Saskiaren prezio totala erakusten duen etiketa. */
	@FXML
	private Label totalLabel;

	/** Leihoaren uneko stage-a. */
	private Stage stage;

	/** Eskuragarri dauden produktuen zerrenda behakorra. */
	private ObservableList<Produktua> produktuakList = FXCollections.observableArrayList();

	/** Saskiko produktuen zerrenda behakorra. */
	private ObservableList<KarritoBean> karritoList = FXCollections.observableArrayList();

	/**
	 * Uneko leihoaren stage-a ezartzen du.
	 *
	 * @param stage leihoaren stage-a.
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	/**
	 * Kontrolagailua hasieratzen du taulen zutabeak konfiguratuz, kantitate
	 * spinner-a ezarriz eta eskuragarri dauden produktuak kargatuz.
	 *
	 * @param location  FXMLaren kokapenaren URL-a.
	 * @param resources lokalizazio baliabideak.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		izenaColumn.setCellValueFactory(new PropertyValueFactory<>("izena"));
		stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
		prezioaColumn.setCellValueFactory(new PropertyValueFactory<>("prezioa"));

		karritoProduktuColumn.setCellValueFactory(new PropertyValueFactory<>("produktu"));
		karritoKantitateaColumn.setCellValueFactory(new PropertyValueFactory<>("kantitatea"));
		karritoPrezioanColumn.setCellValueFactory(new PropertyValueFactory<>("prezioa"));
		karritoTotalaColumn.setCellValueFactory(new PropertyValueFactory<>("totala"));

		SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
		kantitateaSpinner.setValueFactory(valueFactory);

		kargatuProduktuak();
	}

	/**
	 * V_INBENTARIOA ikuspegitik eskuragarri dauden produktuak kargatzen ditu, zero
	 * baino stock handiagoa dutenak soilik erakutsiz.
	 */
	private void kargatuProduktuak() {
		String query = "SELECT * FROM e2t2erronka.V_INBENTARIOA WHERE STOCK > 0";
		DatuBasea db = new DatuBasea();

		try (Connection conn = db.konektoreaWorld();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {

			produktuakList.clear();
			while (rs.next()) {
				produktuakList.add(new Produktua(rs.getInt("ID"), rs.getString("IZENA"), rs.getInt("STOCK"),
						rs.getDouble("PREZIOA")));
			}
			produktuakTable.setItems(produktuakList);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Hautatutako produktua adierazitako kantitatearekin saskira gehitzen du.
	 * Produktu bat hautatuta dagoela, stock nahikoa dagoela eta produktua oraindik
	 * saskian ez dagoela balioztatzen du.
	 */
	@FXML
	private void gehituKarritora() {
		Produktua selected = produktuakTable.getSelectionModel().getSelectedItem();

		if (selected == null) {
			erakutsiAlert("Errorea", "Mesedez, hautatu produktu bat.");
			return;
		}

		int kantitatea = kantitateaSpinner.getValue();

		if (kantitatea > selected.getStock()) {
			erakutsiAlert("Errorea", "Ez dago stock nahikorik. Eskuragarri: " + selected.getStock());
			return;
		}

		for (KarritoBean item : karritoList) {
			if (item.getProduktu().equals(selected.getIzena())) {
				erakutsiAlert("Errorea", "Produktu hau orgatxoan dago jada.");
				return;
			}
		}

		karritoList.add(new KarritoBean(selected.getIzena(), kantitatea, selected.getPrezioa()));
		karritoTable.setItems(karritoList);
		actualizarTotal();
	}

	/**
	 * Erosketa saskia husten du eta totala zerora eguneratzen du.
	 */
	@FXML
	private void garbituKarritoa() {
		karritoList.clear();
		actualizarTotal();
	}

	/**
	 * Saskiaren prezio totala eguneratzen du gehitutako produktu guztien prezio
	 * totalak batuz.
	 */
	private void actualizarTotal() {
		double total = karritoList.stream().mapToDouble(KarritoBean::getTotala).sum();
		totalLabel.setText(String.format("TOTALA: %.2f €", total));
	}

	/**
	 * Saskiaren edukiarekin ordainketa leihoa irekitzen du. Prozedurarekin hasi
	 * aurretik saskia ez dagoela hutsa balioztatzen du.
	 */
	@FXML
	private void ordaindu() {
		if (karritoList.isEmpty()) {
			erakutsiAlert("Errorea", "Orgatxoa hutsik dago.");
			return;
		}

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Ordainketa.fxml"));
			Scene scene = new Scene(loader.load());

			OrdainketaKontrolagailua controller = loader.getController();
			Stage ordainketaStage = new Stage();
			controller.setStage(ordainketaStage);
			controller.setKarritoa(karritoList);
			controller.setDendaStage(stage);

			ordainketaStage.setTitle("Ordainketa");
			ordainketaStage.setScene(scene);
			ordainketaStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Bezeroaren menu nagusira itzultzen da.
	 */
	@FXML
	private void itzuli() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/menuBezeroa/MenuBezeroa.fxml"));
			Scene scene = new Scene(loader.load());
			Stage menuStage = new Stage();
			menuStage.setTitle("Menu Bezeroa");
			menuStage.setScene(scene);
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