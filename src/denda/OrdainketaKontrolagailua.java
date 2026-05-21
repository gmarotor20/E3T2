package denda;

import eskaera.KarritoBean;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import konexioa.DatuBasea;
import menuBezeroa.MenuBezeroaKontrolagailua;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.UUID;

/**
 * Ordainketa leihoaren kontrolagailua. Erosketa saskiaren laburpena erakusten
 * du, ordainketa metodoa hautatzen uzten du eta erosketa datu-basean gordez
 * baieztatzen du. EROSKETA_SARTU stored procedure-a eta stock trigger-ak
 * erabiltzen ditu.
 *
 * @author E3T2
 * @version 1.0
 */
public class OrdainketaKontrolagailua implements Initializable {

	/** Ordaindu beharreko produktuen laburpena erakusten duen taula. */
	@FXML
	private TableView<KarritoBean> laburpenaTable;

	/** Produktuaren izenaren zutabea. */
	@FXML
	private TableColumn<KarritoBean, String> produktuaColumn;

	/** Produktuaren kantitatearen zutabea. */
	@FXML
	private TableColumn<KarritoBean, Integer> kantitateaColumn;

	/** Produktuaren prezio totalaren zutabea. */
	@FXML
	private TableColumn<KarritoBean, Double> totalaColumn;

	/** Erosketaren prezio totala erakusten duen etiketa. */
	@FXML
	private Label totalLabel;

	/** Ordainketa metodoa hautatzeko ComboBox-a. */
	@FXML
	private ComboBox<String> ordainketaComboBox;

	/** Ordainketa leihoaren uneko stage-a. */
	private Stage stage;

	/** Ordaindu ondoren ixteko denda leihoaren stage-a. */
	private Stage dendaStage;

	/** Ordaindu beharreko saskiko produktuen zerrenda. */
	private ObservableList<KarritoBean> karritoa;

	/**
	 * Ordainketa leihoaren stage-a ezartzen du.
	 *
	 * @param stage ordainketa leihoaren stage-a.
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	/**
	 * Denda leihoaren stage-a ezartzen du.
	 *
	 * @param dendaStage denda leihoaren stage-a.
	 */
	public void setDendaStage(Stage dendaStage) {
		this.dendaStage = dendaStage;
	}

	/**
	 * Erosketa saskia ezartzen du eta laburpen taula eta totala eguneratzen ditu.
	 *
	 * @param karritoa saskiko produktuen zerrenda.
	 */
	public void setKarritoa(ObservableList<KarritoBean> karritoa) {
		this.karritoa = karritoa;
		laburpenaTable.setItems(karritoa);
		double total = karritoa.stream().mapToDouble(KarritoBean::getTotala).sum();
		totalLabel.setText(String.format("TOTALA: %.2f €", total));
	}

	/**
	 * Kontrolagailua hasieratzen du taularen zutabeak konfiguratuz eta ComboBox-ean
	 * eskuragarri dauden ordainketa metodoak ezarriz.
	 *
	 * @param location  FXMLaren kokapenaren URL-a.
	 * @param resources lokalizazio baliabideak.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		produktuaColumn.setCellValueFactory(new PropertyValueFactory<>("produktu"));
		kantitateaColumn.setCellValueFactory(new PropertyValueFactory<>("kantitatea"));
		totalaColumn.setCellValueFactory(new PropertyValueFactory<>("totala"));
		ordainketaComboBox.getItems().addAll("Txartela", "PayPal", "Transferentzia", "Bizum");
	}

	/**
	 * Erosketa ordainketa baieztatzen eta prozesatzen du. Erosketaren
	 * identifikatzaile bakarra (UUID) sortzen du, saskiko produktu bakoitzeko
	 * EROSKETA_SARTU stored procedure-a deitzen du eta bezeroaren menura bideratzen
	 * du. TR_STOCK_KONTROLA, TR_EROSKETA_DATA eta TR_STOCK_EGUNERATU trigger-ak
	 * automatikoki aktibatzen dira datu-basean.
	 */
	@FXML
	private void baieztatuOrdainketa() {
		String metodoa = ordainketaComboBox.getValue();

		if (metodoa == null) {
			erakutsiAlert("Errorea", "Mesedez, hautatu ordainketa metodoa.");
			return;
		}

		String bezeroa = MenuBezeroaKontrolagailua.getErabiltzaileIzena() + " "
				+ MenuBezeroaKontrolagailua.getErabiltzaileAbizena();

		// Eskaerako produktu guztiak taldekatzeko identifikatzaile bakarra
		String erosketaId = UUID.randomUUID().toString();

		DatuBasea db = new DatuBasea();

		try (Connection conn = db.konektoreaWorld()) {
			for (KarritoBean item : karritoa) {
				CallableStatement cstmt = conn.prepareCall("{CALL EROSKETA_SARTU(?, ?, ?, ?, ?, ?)}");
				cstmt.setString(1, erosketaId);
				cstmt.setString(2, bezeroa);
				cstmt.setString(3, item.getProduktu());
				cstmt.setInt(4, item.getKantitatea());
				cstmt.setDouble(5, item.getTotala());
				cstmt.setString(6, metodoa);
				cstmt.execute();
				cstmt.close();
			}

			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Arrakasta");
			alert.setHeaderText("Ordainketa eginda!");
			alert.setContentText("Zure erosketa prozesatu da.\n" + "Ordainketa metodoa: " + metodoa);
			alert.showAndWait();

			if (stage != null)
				stage.close();
			if (dendaStage != null)
				dendaStage.close();

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/menuBezeroa/MenuBezeroa.fxml"));
			Scene scene = new Scene(loader.load());
			Stage menuStage = new Stage();
			menuStage.setTitle("Menu Bezeroa");
			menuStage.setScene(scene);
			menuStage.show();

		} catch (SQLException e) {
			e.printStackTrace();
			if (e.getMessage().contains("Ez dago stock nahikorik")) {
				erakutsiAlert("Stock errorea", e.getMessage());
			} else {
				erakutsiAlert("Errorea", "Ordainketan errorea: " + e.getMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Ordainketa bertan behera uzten du eta ordainketa leihoa ixten du inolako
	 * erosketarik egin gabe.
	 */
	@FXML
	private void bertanBehera() {
		if (stage != null)
			stage.close();
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