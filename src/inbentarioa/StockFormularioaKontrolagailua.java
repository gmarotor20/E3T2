package inbentarioa;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import konexioa.DatuBasea;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Produktuari stocka gehitzeko formulario modalaren kontrolagailua. Hautatutako
 * produktuaren uneko stocka erakusten du eta STOCK_GEHITU stored procedure-a
 * erabiliz stocka eguneratzen du.
 *
 * @author E3T2
 * @version 1.0
 */
public class StockFormularioaKontrolagailua {

	/** Produktuaren izena erakusten duen etiketa. */
	@FXML
	private Label produktuaLabel;

	/** Produktuaren uneko stocka erakusten duen etiketa. */
	@FXML
	private Label stockOraLabel;

	/** Gehitu beharreko stock kopurua sartzeko testu-eremua. */
	@FXML
	private TextField stockField;

	/** Leiho modalaren stage-a. */
	private Stage modalStage;

	/** Gurasoen kontrolagailua taula freskatzeko. */
	private InbentarioaKontrolagailua parentController;

	/** Stocka eguneratu beharreko produktuaren identifikatzailea. */
	private int produktuId;

	/**
	 * Leiho modalaren stage-a ezartzen du.
	 *
	 * @param modalStage leiho modalaren stage-a.
	 */
	public void setModalStage(Stage modalStage) {
		this.modalStage = modalStage;
	}

	/**
	 * Gurasoen kontrolagailua ezartzen du.
	 *
	 * @param parentController inbentario leihoaren kontrolagailua.
	 */
	public void setParentController(InbentarioaKontrolagailua parentController) {
		this.parentController = parentController;
	}

	/**
	 * Produktuaren datuak ezartzen ditu leihoaren elementuetan.
	 *
	 * @param id    produktuaren identifikatzailea.
	 * @param izena produktuaren izena.
	 * @param stock produktuaren uneko stocka.
	 */
	public void setProduktuDatuak(int id, String izena, int stock) {
		this.produktuId = id;
		produktuaLabel.setText(izena);
		stockOraLabel.setText(String.valueOf(stock));
	}

	/**
	 * Sartutako stock kopurua balioztatzen du eta STOCK_GEHITU stored procedure-a
	 * erabiliz produktuaren stocka eguneratzen du. Stock kopurua zero baino
	 * handiagoa izan behar da.
	 */
	@FXML
	private void gorde() {
		String stockStr = stockField.getText().trim();

		if (stockStr.isEmpty()) {
			erakutsiAlert("Errorea", "Mesedez, sartu gehitu nahi duzun stock kopurua.");
			return;
		}

		int kantitatea;
		try {
			kantitatea = Integer.parseInt(stockStr);
			if (kantitatea <= 0) {
				erakutsiAlert("Errorea", "Stock kopurua 0 baino handiagoa izan behar da.");
				return;
			}
		} catch (NumberFormatException e) {
			erakutsiAlert("Errorea", "Stock kopurua zenbaki bat izan behar da.");
			return;
		}

		DatuBasea db = new DatuBasea();

		try (Connection conn = db.konektoreaWorld();
				CallableStatement cstmt = conn.prepareCall("{CALL STOCK_GEHITU(?, ?)}")) {

			cstmt.setInt(1, produktuId);
			cstmt.setInt(2, kantitatea);
			cstmt.execute();

			erakutsiAlert("Arrakasta", "Stock eguneratu da!");

			if (parentController != null) {
				parentController.kargatuDatuak();
			}

			if (modalStage != null) {
				modalStage.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
			erakutsiAlert("Errorea", "Stock eguneratzean errorea: " + e.getMessage());
		}
	}

	/**
	 * Formularioa bertan behera uzten du eta leiho modala ixten du.
	 */
	@FXML
	private void utzi() {
		if (modalStage != null)
			modalStage.close();
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