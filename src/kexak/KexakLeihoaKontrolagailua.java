package kexak;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * Kexaren xehetasunen leiho modalaren kontrolagailua. Hautatutako kexaren datu
 * guztiak erakusten ditu: bezeroa, emaila, motiboa, data eta mezua.
 *
 * @author E3T2
 * @version 1.0
 */
public class KexakLeihoaKontrolagailua {

	/** Kexa egin duen bezeroaren izena erakusten duen etiketa. */
	@FXML
	private Label bezeroaLabel;

	/** Bezeroaren emaila erakusten duen etiketa. */
	@FXML
	private Label emailaLabel;

	/** Kexaren motiboa erakusten duen etiketa. */
	@FXML
	private Label motiboaLabel;

	/** Kexaren data eta ordua erakusten duen etiketa. */
	@FXML
	private Label dataLabel;

	/** Kexaren mezua erakusten duen testu-eremua. */
	@FXML
	private TextArea mezuaArea;

	/** Leihoa ixteko botoia. */
	@FXML
	private Button itxiButton;

	/** Leiho modalaren stage-a. */
	private Stage modalStage;

	/**
	 * Kexaren datuak ezartzen ditu leihoaren elementuetan.
	 *
	 * @param kexa erakutsi beharreko kexaren datuak.
	 */
	public void setKexaDatuak(KexaBean kexa) {
		bezeroaLabel.setText(kexa.getBezeroa());
		emailaLabel.setText(kexa.getEmaila());
		motiboaLabel.setText(kexa.getMotiboa());
		dataLabel.setText(kexa.getData());
		mezuaArea.setText(kexa.getMezua());
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