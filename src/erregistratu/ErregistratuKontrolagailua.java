package erregistratu;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import konexioa.DatuBasea;

/**
 * Bezero berria erregistratzeko leihoaren kontrolagailua. Bezeroaren datuak
 * balioztatzen ditu eta ErregistratuBezeroa stored procedure-a erabiliz
 * datu-basean gordetzen ditu. Erregistroa ongi egin ondoren loginera bideratzen
 * du.
 *
 * @author E3T2
 * @version 1.0
 */
public class ErregistratuKontrolagailua {

	/** Bezeroaren izena sartzeko testu-eremua. */
	@FXML
	private TextField izena;

	/** Bezeroaren abizena sartzeko testu-eremua. */
	@FXML
	private TextField abizena;

	/** Bezeroaren emaila sartzeko testu-eremua. */
	@FXML
	private TextField emaila;

	/** Abizena berresteko testu-eremua. */
	@FXML
	private TextField errepikatuAbizena;

	/**
	 * Bezero berria erregistratzen du. Eremu guztiak bete direla eta abizenak bat
	 * egiten dutela balioztatzen du. Erregistroa ongi egin ondoren leihoa ixten du
	 * eta loginera bideratzen du.
	 */
	@FXML
	private void erregistratu() {
		String jasoIzena = izena.getText().trim();
		String jasoAbizena = abizena.getText().trim();
		String jasoEmaila = emaila.getText().trim();
		String jasoErrepikatuAbizena = errepikatuAbizena.getText();

		if (!jasoIzena.isEmpty() && !jasoAbizena.isEmpty() && !jasoEmaila.isEmpty()
				&& jasoAbizena.equals(jasoErrepikatuAbizena)) {

			if (erregistratuBezeroa(jasoIzena, jasoAbizena, jasoEmaila)) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("EXITO");
				alert.setHeaderText("Bezeroa sortuta");
				alert.setContentText("Bezeroa ondo sortu egin da");
				alert.showAndWait();

				Stage currentStage = (Stage) izena.getScene().getWindow();
				currentStage.close();

				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/login/Login.fxml"));
					Scene scene = new Scene(loader.load());
					Stage newStage = new Stage();
					newStage.setTitle("Login");
					newStage.setScene(scene);
					newStage.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("ERROR");
			alert.setHeaderText("Datu okerrak");
			alert.setContentText("Egiaztatu eremu guztiak beteta daudela eta abizenak bat egiten duela.");
			alert.showAndWait();
		}
	}

	/**
	 * Erregistro leihoa ixten du eta loginera itzultzen da.
	 */
	@FXML
	private void itzuli() {
		try {
			Stage currentStage = (Stage) izena.getScene().getWindow();
			currentStage.close();

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/login/Login.fxml"));
			Scene scene = new Scene(loader.load());
			Stage newStage = new Stage();
			newStage.setTitle("Login");
			newStage.setScene(scene);
			newStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Bezero berria datu-basean gordetzen du ErregistratuBezeroa stored procedure-a
	 * erabiliz.
	 *
	 * @param izena   bezeroaren izena.
	 * @param abizena bezeroaren abizena.
	 * @param emaila  bezeroaren emaila.
	 * @return true erregistroa ongi egin bada, false errorea gertatzen bada.
	 */
	private boolean erregistratuBezeroa(String izena, String abizena, String emaila) {
		DatuBasea db = new DatuBasea();

		try (Connection konexioa = db.konektoreaWorld();
				CallableStatement cstmt = konexioa.prepareCall("{CALL ErregistratuBezeroa(?, ?, ?)}")) {

			cstmt.setString(1, izena);
			cstmt.setString(2, abizena);
			cstmt.setString(3, emaila);
			cstmt.executeUpdate();
			return true;

		} catch (SQLException e) {
			System.out.println("Error SQL: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}