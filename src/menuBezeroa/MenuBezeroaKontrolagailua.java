package menuBezeroa;

import denda.DendaKontrolagailua;
import bezeroArreta.BezeroArreta;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.Optional;

/**
 * Bezeroaren menu nagusiaren kontrolagailua. Bezeroaren eskuragarri dauden atal
 * desberdinen arteko nabigazioa kudeatzen du eta saio aktiboko datuak
 * mantentzen ditu.
 *
 * @author E3T2
 * @version 1.0
 */
public class MenuBezeroaKontrolagailua {

	/** Saioan dagoen bezeroaren izena. */
	private static String erabiltzaileIzena;

	/** Saioan dagoen bezeroaren abizena. */
	private static String erabiltzaileAbizena;

	/** Saioan dagoen bezeroaren emaila. */
	private static String erabiltzaileEmaila;

	/**
	 * Uneko leihoaren stage-a ezartzen du. Stage-aren kudeaketa tituluaren bidezko
	 * bilaketaren bidez egiten da.
	 *
	 * @param stage menuaren leihoaren stage-a.
	 */
	public void setStage(Stage stage) {
	}

	/**
	 * Saioan dagoen bezeroaren datuak ezartzen ditu.
	 *
	 * @param izena   bezeroaren izena.
	 * @param abizena bezeroaren abizena.
	 * @param emaila  bezeroaren emaila.
	 */
	public void setUserData(String izena, String abizena, String emaila) {
		MenuBezeroaKontrolagailua.erabiltzaileIzena = izena;
		MenuBezeroaKontrolagailua.erabiltzaileAbizena = abizena;
		MenuBezeroaKontrolagailua.erabiltzaileEmaila = emaila;
	}

	/**
	 * Saioan dagoen bezeroaren izena lortzen du.
	 *
	 * @return bezeroaren izena.
	 */
	public static String getErabiltzaileIzena() {
		return erabiltzaileIzena;
	}

	/**
	 * Saioan dagoen bezeroaren abizena lortzen du.
	 *
	 * @return bezeroaren abizena.
	 */
	public static String getErabiltzaileAbizena() {
		return erabiltzaileAbizena;
	}

	/**
	 * Saioan dagoen bezeroaren emaila lortzen du.
	 *
	 * @return bezeroaren emaila.
	 */
	public static String getErabiltzaileEmaila() {
		return erabiltzaileEmaila;
	}

	/**
	 * Leihoaren tituluaren arabera bezeroaren menu stage-a bilatzen eta itzultzen
	 * du.
	 *
	 * @return bezeroaren menuaren Stage-a, edo null aurkitzen ez bada.
	 */
	private Stage getMenuStage() {
		return javafx.stage.Window.getWindows().stream()
				.filter(w -> w instanceof Stage && ((Stage) w).getTitle().equals("Menu Bezeroa")).map(w -> (Stage) w)
				.findFirst().orElse(null);
	}

	/**
	 * Bezeroak produktuak eros ditzakeen denda leihoa irekitzen du.
	 */
	@FXML
	private void dendaButtonClicked() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/denda/Denda.fxml"));
			Scene scene = new Scene(loader.load());
			Stage newStage = new Stage();

			DendaKontrolagailua controller = loader.getController();
			controller.setStage(newStage);

			newStage.setScene(scene);
			newStage.setTitle("Denda");
			newStage.show();

			Stage current = getMenuStage();
			if (current != null)
				current.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Bezeroak kexak bidal ditzakeen bezero arreta leihoa irekitzen du.
	 */
	@FXML
	private void bezeroArretaButtonClicked() {
		try {
			Stage current = getMenuStage();

			BezeroArreta bezeroArreta = new BezeroArreta();
			Stage newStage = new Stage();
			bezeroArreta.start(newStage);

			if (current != null)
				current.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Saioan dagoen bezeroaren datu pertsonalak informazio leiho batean erakusten
	 * ditu.
	 */
	@FXML
	private void datuakIkusi() {
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setStyle("-fx-padding: 20;");

		Label izenaLabel = new Label("Izena:");
		izenaLabel.setFont(Font.font("System", FontWeight.BOLD, 13));
		Label izenaValor = new Label(erabiltzaileIzena != null ? erabiltzaileIzena : "Ez dago daturik");

		Label abizenaLabel = new Label("Abizena:");
		abizenaLabel.setFont(Font.font("System", FontWeight.BOLD, 13));
		Label abizenaValor = new Label(erabiltzaileAbizena != null ? erabiltzaileAbizena : "Ez dago daturik");

		Label emailaLabel = new Label("Emaila:");
		emailaLabel.setFont(Font.font("System", FontWeight.BOLD, 13));
		Label emailaValor = new Label(erabiltzaileEmaila != null ? erabiltzaileEmaila : "Ez dago daturik");

		grid.add(izenaLabel, 0, 0);
		grid.add(izenaValor, 1, 0);
		grid.add(abizenaLabel, 0, 1);
		grid.add(abizenaValor, 1, 1);
		grid.add(emailaLabel, 0, 2);
		grid.add(emailaValor, 1, 2);

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Nire datuak");
		alert.setHeaderText("Saioan hasitako erabiltzailea");
		alert.getDialogPane().setContent(grid);
		alert.showAndWait();
	}

	/**
	 * Uneko bezeroaren saioa ixten du. Itxi aurretik berrespena erakusten du eta
	 * loginera bideratzen du.
	 */
	@FXML
	private void saioaItxi() {
		Alert confirm = new Alert(AlertType.CONFIRMATION);
		confirm.setTitle("Saioa itxi");
		confirm.setHeaderText("Ziur zaude saioa itxi nahi duzula?");
		confirm.setContentText("Saioa itxiz gero, berriro saioa hasi beharko duzu.");

		Optional<ButtonType> result = confirm.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			try {
				erabiltzaileIzena = null;
				erabiltzaileAbizena = null;
				erabiltzaileEmaila = null;

				Stage current = getMenuStage();
				if (current != null)
					current.close();

				FXMLLoader loader = new FXMLLoader(getClass().getResource("/login/Login.fxml"));
				Scene scene = new Scene(loader.load());
				Stage loginStage = new Stage();
				loginStage.setTitle("Login");
				loginStage.setScene(scene);
				loginStage.show();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}