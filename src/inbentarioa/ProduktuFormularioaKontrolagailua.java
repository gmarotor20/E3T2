package inbentarioa;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import konexioa.DatuBasea;

/**
 * Produktu berria gehitzeko formulario modalaren kontrolagailua. Produktuaren
 * datuak balioztatzen ditu eta InsertarProduktu stored procedure-a erabiliz
 * datu-basean gordetzen ditu.
 *
 * @author E3T2
 * @version 1.0
 */
public class ProduktuFormularioaKontrolagailua {

	/** Produktuaren izena sartzeko testu-eremua. */
	@FXML
	private TextField izenaField;

	/** Produktuaren stock hasierakoa sartzeko testu-eremua. */
	@FXML
	private TextField stockField;

	/** Produktuaren prezioa sartzeko testu-eremua. */
	@FXML
	private TextField prezioaField;

	/** Datuak gordetzeko botoia. */
	@FXML
	private Button gordeButton;

	/** Formularioa bertan behera uzteko botoia. */
	@FXML
	private Button utziButton;

	/** Gurasoen kontrolagailua taula freskatzeko. */
	private InbentarioaKontrolagailua parentController;

	/** Leiho modalaren stage-a. */
	private Stage modalStage;

	/**
	 * Gurasoen kontrolagailua ezartzen du.
	 *
	 * @param parentController inbentario leihoaren kontrolagailua.
	 */
	public void setParentController(InbentarioaKontrolagailua parentController) {
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
	 * Formularioko datuak balioztatzen ditu eta produktu berria InsertarProduktu
	 * stored procedure-a erabiliz datu-basean gordetzen du. Izena, stock eta
	 * prezioa derrigorrez bete behar dira eta stock eta prezioa ezin dira
	 * negatiboak izan.
	 */
	@FXML
	private void gorde() {
		String izena = izenaField.getText().trim();
		String stockStr = stockField.getText().trim();
		String prezioaStr = prezioaField.getText().trim();

		if (izena.isEmpty()) {
			erakutsiAlert("Errorea", "IZENA ezin da hutsik egon");
			return;
		}

		if (stockStr.isEmpty()) {
			erakutsiAlert("Errorea", "STOCK ezin da hutsik egon");
			return;
		}

		if (prezioaStr.isEmpty()) {
			erakutsiAlert("Errorea", "PREZIOA ezin da hutsik egon");
			return;
		}

		int stock;
		double prezioa;

		try {
			stock = Integer.parseInt(stockStr);
			if (stock < 0) {
				erakutsiAlert("Errorea", "STOCK ezin da negatiboa izan");
				return;
			}
		} catch (NumberFormatException e) {
			erakutsiAlert("Errorea", "STOCK zenbaki bat izan behar da");
			return;
		}

		try {
			prezioa = Double.parseDouble(prezioaStr);
			if (prezioa < 0) {
				erakutsiAlert("Errorea", "PREZIOA ezin da negatiboa izan");
				return;
			}
		} catch (NumberFormatException e) {
			erakutsiAlert("Errorea", "PREZIOA zenbaki bat izan behar da");
			return;
		}

		String sql = "{CALL InsertarProduktu(?, ?, ?)}";
		DatuBasea db = new DatuBasea();

		try (Connection conn = db.konektoreaWorld(); CallableStatement cstmt = conn.prepareCall(sql)) {

			cstmt.setString(1, izena);
			cstmt.setInt(2, stock);
			cstmt.setDouble(3, prezioa);
			cstmt.execute();

			erakutsiAlert("Arrakasta", "Produktua ondo gehitu da!");

			if (parentController != null) {
				parentController.kargatuDatuak();
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