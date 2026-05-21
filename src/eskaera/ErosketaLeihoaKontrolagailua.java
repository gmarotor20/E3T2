package eskaera;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * Erosketaren xehetasunen leiho modalaren kontrolagailua. Hautatutako
 * erosketaren datu guztiak erakusten ditu: identifikatzailea, bezeroa,
 * produktuak, totala, ordainketa metodoa eta data.
 *
 * @author E3T2
 * @version 1.0
 */
public class ErosketaLeihoaKontrolagailua {

	/** Erosketaren identifikatzailea erakusten duen etiketa. */
	@FXML
	private Label erosketaIdLabel;

	/** Erosketa egin duen bezeroaren izena erakusten duen etiketa. */
	@FXML
	private Label bezeroaLabel;

	/** Erositako produktuak erakusten dituen testu-eremua. */
	@FXML
	private TextArea produktuakArea;

	/** Erosketaren prezio totala erakusten duen etiketa. */
	@FXML
	private Label totalLabel;

	/** Erabilitako ordainketa metodoa erakusten duen etiketa. */
	@FXML
	private Label ordainketaMetodoaLabel;

	/** Erosketaren data eta ordua erakusten duen etiketa. */
	@FXML
	private Label dataLabel;

	/** Leihoa ixteko botoia. */
	@FXML
	private Button itxiButton;

	/** Leiho modalaren stage-a. */
	private Stage modalStage;

	/**
	 * Erosketaren datuak ezartzen ditu leihoaren elementuetan.
	 *
	 * @param erosketa erakutsi beharreko erosketaren datuak.
	 */
	public void setErosketaDatuak(EskaeraBean erosketa) {
		erosketaIdLabel.setText(erosketa.getErosketaId());
		bezeroaLabel.setText(erosketa.getBezeroa());
		produktuakArea.setText(erosketa.getProduktuak());
		totalLabel.setText(String.valueOf(erosketa.getTotal()) + " €");
		ordainketaMetodoaLabel.setText(erosketa.getOrdainketaMetodoa());
		dataLabel.setText(erosketa.getData());
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
	 * Leiho modala ixten du.
	 */
	@FXML
	private void itxi() {
		if (modalStage != null) {
			modalStage.close();
		}
	}
}