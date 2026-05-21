package saltzaileAgenda;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import konexioa.DatuBasea;

/**
 * Saltzaile berria gehitzeko formulario modalaren kontrolagailua. Saltzailearen
 * datuak balioztatzen ditu eta InsertarSaltzaile stored procedure-a erabiliz
 * datu-basean gordetzen ditu.
 *
 * @author E3T2
 * @version 1.0
 */
public class SaltzaileFormularioaKontrolagailua {

	/** Saltzailearen erabiltzaile izena sartzeko testu-eremua. */
	@FXML
	private TextField erabiltzaileaField;

	/** Saltzailearen sarrera data hautatzeko data-hautatzailea. */
	@FXML
	private DatePicker sarreraDatePicker;

	/** Saltzailearen irteera data hautatzeko data-hautatzailea. */
	@FXML
	private DatePicker irteeraDatePicker;

	/** Saltzailearen rola edo oharrak sartzeko testu-eremua. */
	@FXML
	private TextArea aldaketakArea;

	/** Datuak gordetzeko botoia. */
	@FXML
	private Button gordeButton;

	/** Formularioa bertan behera uzteko botoia. */
	@FXML
	private Button utziButton;

	/** Gurasoen kontrolagailua taula freskatzeko. */
	private SaltzaileAgendaKontrolagailua parentController;

	/** Leiho modalaren stage-a. */
	private Stage modalStage;

	/**
	 * Gurasoen kontrolagailua ezartzen du.
	 *
	 * @param parentController agenda leihoaren kontrolagailua.
	 */
	public void setParentController(SaltzaileAgendaKontrolagailua parentController) {
		this.parentController = parentController;
	}

	/**
	 * Leiho modalaren stage-a ezartzen du.
	 *
	 * @param modalStage leiho modalaren stage-a.
	 */
	public void setModalStage(Stage modalStage) {
		this.modalStage = modalStage;
	}

	/**
	 * Formularioko datuak balioztatzen ditu eta saltzaile berria InsertarSaltzaile
	 * stored procedure-a erabiliz datu-basean gordetzen du. Erabiltzaile izena eta
	 * sarrera data bete behar dira derrigorrez. Erabiltzaile izena dagoeneko
	 * existitzen bada errorea erakusten du.
	 */
	@FXML
	private void gorde() {
		String erabiltzailea = erabiltzaileaField.getText().trim();
		LocalDate sarrera = sarreraDatePicker.getValue();

		if (erabiltzailea.isEmpty()) {
			erakutsiAlert("Errorea", "ERABILTZAILEA ezin da hutsik egon");
			return;
		}

		if (sarrera == null) {
			erakutsiAlert("Errorea", "SARRERA data ezin da hutsik egon");
			return;
		}

		if (userExists(erabiltzailea)) {
			erakutsiAlert("Errorea", "ERABILTZAILEA '" + erabiltzailea + "' jada existitzen da");
			return;
		}

		String sql = "{CALL InsertarSaltzaile(?, ?, ?, ?, ?)}";
		DatuBasea db = new DatuBasea();

		try (Connection conn = db.konektoreaWorld(); CallableStatement cstmt = conn.prepareCall(sql)) {

			cstmt.setString(1, erabiltzailea);
			cstmt.setString(2, erabiltzailea); // PASAHITZA
			cstmt.setDate(3, Date.valueOf(sarrera));

			if (irteeraDatePicker.getValue() != null) {
				cstmt.setDate(4, Date.valueOf(irteeraDatePicker.getValue()));
			} else {
				cstmt.setNull(4, java.sql.Types.DATE);
			}

			String aldaketak = aldaketakArea.getText().trim();
			if (!aldaketak.isEmpty()) {
				cstmt.setString(5, aldaketak);
			} else {
				cstmt.setNull(5, java.sql.Types.VARCHAR);
			}

			cstmt.execute();

			erakutsiAlert("Arrakasta", "Saltzailea ondo gehitu da!");

			if (parentController != null) {
				parentController.kargatuSaltzaileak();
			}

			if (modalStage != null) {
				modalStage.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
			erakutsiAlert("Errorea", "Datuak gordetzerakoan errorea: " + e.getMessage());
		}
	}

	/**
	 * Erabiltzaile izena datu-basean dagoeneko existitzen den egiaztatzen du.
	 *
	 * @param erabiltzailea egiaztatu beharreko erabiltzaile izena.
	 * @return true erabiltzailea existitzen bada, false bestela.
	 */
	private boolean userExists(String erabiltzailea) {
		String sql = "SELECT COUNT(*) FROM e2t2erronka.saltzaile WHERE ERABILTZAILEA = ?";
		DatuBasea db = new DatuBasea();

		try (Connection conn = db.konektoreaWorld(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, erabiltzailea);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Formularioa bertan behera uzten du eta leiho modala ixten du.
	 */
	@FXML
	private void utzi() {
		if (modalStage != null) {
			modalStage.close();
		}
	}

	/**
	 * Mezu informatibo bat alerta leiho batean erakusten du.
	 *
	 * @param titulua alertaren titulua.
	 * @param mezua   alertaren mezua.
	 */
	private void erakutsiAlert(String titulua, String mezua) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(titulua);
		alert.setHeaderText(null);
		alert.setContentText(mezua);
		alert.showAndWait();
	}
}