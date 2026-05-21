package bezeroArreta;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import konexioa.DatuBasea;
import menuBezeroa.MenuBezeroaKontrolagailua;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Bezero arreta leihoaren kontrolagailua. Bezeroak kexaren motiboa hautatu,
 * mezu bat idatzi eta KEXA_SARTU stored procedure-a erabiliz datu-basera
 * bidaltzea ahalbidetzen du.
 *
 * @author E3T2
 * @version 1.0
 */
public class BezeroArretaKontrolagailua implements Initializable {

	/** Bezeroak kexaren mezua idazten duen testu-eremua. */
	@FXML
	private TextArea mezuaArea;

	/** Kexaren motiboa hautatzeko ComboBox-a. */
	@FXML
	private ComboBox<String> motiboarenComboBox;

	/**
	 * Kontrolagailua hasieratzen du ComboBox-ean eskuragarri dauden kexa motiboak
	 * kargatuz.
	 *
	 * @param location  FXMLaren kokapenaren URL-a.
	 * @param resources lokalizazio baliabideak.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		motiboarenComboBox.getItems().addAll("Entrega", "Produktua", "Ordainketa", "Beste bat");
	}

	/**
	 * Bezeroaren kexa datu-basera bidaltzen du. Motiboa eta mezua hutsik ez daudela
	 * balioztatzen du, saio aktibotik bezeroaren datuak lortzen ditu eta KEXA_SARTU
	 * stored procedure-a deitzen du.
	 */
	@FXML
	private void bidaliMezua() {
		String mezua = mezuaArea.getText().trim();
		String motiboa = motiboarenComboBox.getValue();

		if (motiboa == null || motiboa.isEmpty()) {
			mostrarAlerta("Errorea", "Mesedez, hautatu kexa motiboa.");
			return;
		}

		if (mezua.isEmpty()) {
			mostrarAlerta("Errorea", "Mesedez, idatzi mezu bat bidali aurretik.");
			return;
		}

		// Saio aktibotik bezeroaren datuak lortu
		String bezeroa = MenuBezeroaKontrolagailua.getErabiltzaileIzena() + " "
				+ MenuBezeroaKontrolagailua.getErabiltzaileAbizena();
		String emaila = MenuBezeroaKontrolagailua.getErabiltzaileEmaila();

		DatuBasea db = new DatuBasea();

		try (Connection conn = db.konektoreaWorld();
				CallableStatement cstmt = conn.prepareCall("{CALL KEXA_SARTU(?, ?, ?, ?)}")) {

			cstmt.setString(1, bezeroa);
			cstmt.setString(2, emaila);
			cstmt.setString(3, motiboa);
			cstmt.setString(4, mezua);
			cstmt.execute();

			mostrarAlerta("Arrakasta", "Zure mezua bidali da. Laster erantzungo dizugu.");
			mezuaArea.clear();
			motiboarenComboBox.setValue(null);

		} catch (SQLException e) {
			e.printStackTrace();
			mostrarAlerta("Errorea", "Mezua bidaltzean errorea: " + e.getMessage());
		}
	}

	/**
	 * Bezero arreta leihoa ixten du eta bezeroaren menu nagusira itzultzen da.
	 */
	@FXML
	private void itzuliMenuBezeroa() {
		try {
			Stage currentStage = (Stage) mezuaArea.getScene().getWindow();
			currentStage.close();

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/menuBezeroa/MenuBezeroa.fxml"));
			Scene scene = new Scene(loader.load());
			Stage menuStage = new Stage();
			menuStage.setScene(scene);
			menuStage.setTitle("Menu Bezeroa");
			menuStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Mezu informatibo bat alerta leiho batean erakusten du.
	 *
	 * @param titulo  alertaren titulua.
	 * @param mensaje alertaren mezua.
	 */
	private void mostrarAlerta(String titulo, String mensaje) {
		Alert alerta = new Alert(AlertType.INFORMATION);
		alerta.setTitle(titulo);
		alerta.setHeaderText(null);
		alerta.setContentText(mensaje);
		alerta.showAndWait();
	}
}