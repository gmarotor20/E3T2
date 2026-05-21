package kexak;

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
import konexioa.DatuBasea;
import menuSaltzaile.MenuSaltzaileKontrolagailua;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

/**
 * Bezeroen kexen leihoaren kontrolagailua. KEXAK_ATERA stored procedure-a
 * erabiliz jasotako kexa guztiak erakusten ditu. Saltzaileak klik bikoitzaren
 * bidez kexa bakoitzaren xehetasunak ikustea ahalbidetzen du.
 *
 * @author E3T2
 * @version 1.0
 */
public class KexakKontrolagailua implements Initializable {

	/** Jasotako kexak erakusten dituen taula. */
	@FXML
	private TableView<KexaBean> tableView;

	/** Kexaren identifikatzailearen zutabea. */
	@FXML
	private TableColumn<KexaBean, Integer> idColumn;

	/** Kexa bidali duen bezeroaren izenaren zutabea. */
	@FXML
	private TableColumn<KexaBean, String> bezeroaColumn;

	/** Bezeroaren emailaren zutabea. */
	@FXML
	private TableColumn<KexaBean, String> emailaColumn;

	/** Kexaren motiboaren zutabea. */
	@FXML
	private TableColumn<KexaBean, String> motiboarenColumn;

	/** Kexaren mezuaren zutabea. */
	@FXML
	private TableColumn<KexaBean, String> mezuaColumn;

	/** Kexaren data eta orduaren zutabea. */
	@FXML
	private TableColumn<KexaBean, String> dataColumn;

	/** Saltzailearen menura itzultzeko botoia. */
	@FXML
	private Button itzuliButton;

	/** Leihoaren uneko stage-a. */
	private Stage stage;

	/** Taulan erakusten den kexen zerrenda behakorra. */
	private ObservableList<KexaBean> kexakList = FXCollections.observableArrayList();

	/**
	 * Uneko leihoaren stage-a ezartzen du.
	 *
	 * @param stage leihoaren stage-a.
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	/**
	 * Kontrolagailua hasieratzen du taularen zutabeak konfiguratuz, kexak kargatuz
	 * eta xehetasunak ikusteko klik bikoitza konfiguratuz.
	 *
	 * @param location  FXMLaren kokapenaren URL-a.
	 * @param resources lokalizazio baliabideak.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		bezeroaColumn.setCellValueFactory(new PropertyValueFactory<>("bezeroa"));
		emailaColumn.setCellValueFactory(new PropertyValueFactory<>("emaila"));
		motiboarenColumn.setCellValueFactory(new PropertyValueFactory<>("motiboa"));
		mezuaColumn.setCellValueFactory(new PropertyValueFactory<>("mezua"));
		dataColumn.setCellValueFactory(new PropertyValueFactory<>("data"));

		itzuliButton.setOnAction(e -> itzuli());

		kargatuKexak();

		// Kexaren xehetasunak ikusteko klik bikoitza
		tableView.setRowFactory(tv -> {
			TableRow<KexaBean> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					KexaBean selectedKexa = row.getItem();
					mostrarVentanaKexa(selectedKexa);
				}
			});
			return row;
		});
	}

	/**
	 * Datu-basetik kexa guztiak kargatzen ditu KEXAK_ATERA stored procedure-a
	 * erabiliz.
	 */
	private void kargatuKexak() {
		DatuBasea db = new DatuBasea();

		try (Connection conn = db.konektoreaWorld();
				CallableStatement cstmt = conn.prepareCall("{CALL KEXAK_ATERA()}");
				ResultSet rs = cstmt.executeQuery()) {

			kexakList.clear();

			while (rs.next()) {
				KexaBean kexa = new KexaBean(rs.getInt("ID"), rs.getString("BEZEROA"), rs.getString("EMAILA"),
						rs.getString("MOTIBOA"), rs.getString("MEZUA"), rs.getString("DATA"));
				kexakList.add(kexa);
			}

			tableView.setItems(kexakList);

		} catch (SQLException e) {
			e.printStackTrace();
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Errorea");
			alert.setHeaderText(null);
			alert.setContentText("Kexak kargatzean errorea: " + e.getMessage());
			alert.showAndWait();
		}
	}

	/**
	 * Hautatutako kexaren xehetasun osoak dituen leiho modal bat irekitzen du.
	 *
	 * @param kexa taulan hautatutako kexa.
	 */
	private void mostrarVentanaKexa(KexaBean kexa) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("KexakLeihoa.fxml"));
			Parent root = loader.load();

			KexakLeihoaKontrolagailua controller = loader.getController();
			controller.setKexaDatuak(kexa);

			Stage modalStage = new Stage();
			modalStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
			modalStage.initOwner(tableView.getScene().getWindow());
			modalStage.setTitle("Kexa - " + kexa.getBezeroa());
			modalStage.setScene(new Scene(root));
			modalStage.setResizable(false);

			controller.setModalStage(modalStage);
			modalStage.showAndWait();

		} catch (Exception e) {
			e.printStackTrace();
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Errorea");
			alert.setHeaderText(null);
			alert.setContentText("Ezin izan da kexaren leihoa ireki: " + e.getMessage());
			alert.showAndWait();
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
}