package login;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import erregistratu.Erregistratu;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import konexioa.BezeroaBean;
import konexioa.DatuBasea;
import konexioa.SaltzaileBean;
import menuBezeroa.MenuBezeroaKontrolagailua;
import menuSaltzaile.MenuSaltzaileKontrolagailua;

/**
 * Saio hasierako leihoaren kontrolagailua. Saltzaileen eta bezeroen
 * autentikazioa kudeatzen du, sartutako kredentzialak datu-basearen aurka
 * balioztatuz.
 *
 * @author E3T2
 * @version 1.0
 */
public class LoginKontrolagailua {

	/** Erabiltzaile izenaren testu-eremua. */
	@FXML
	private TextField erabiltzaile;

	/** Saltzailearen pasahitzaren eremua. */
	@FXML
	private PasswordField pasahitza;

	/** Bezeroaren izenaren testu-eremua. */
	@FXML
	private TextField izena;

	/** Bezeroaren abizena pasahitzaren eremua. */
	@FXML
	private PasswordField abizena;

	/**
	 * LoginKontrolagailua-ren konstruktore hutsa.
	 */
	public LoginKontrolagailua() {
	}

	/**
	 * Saio hasierako prozesua kudeatzen du. Eremu hutsak balioztatzen ditu,
	 * kredentzialak datu-basearen aurka egiaztatzen ditu eta erabiltzaile motaren
	 * arabera dagokion menura bideratzen du.
	 */
	@FXML
	private void logeatu() {
		String jasoErabiltzaile = erabiltzaile.getText().trim();
		String jasoPasahitza = pasahitza.getText().trim();

		if (jasoErabiltzaile.isEmpty() && jasoPasahitza.isEmpty()) {
			erakutsiAlert("Errorea", "Erabiltzailea eta pasahitza ezin dira hutsik egon.");
			return;
		}

		if (jasoErabiltzaile.isEmpty()) {
			erakutsiAlert("Errorea", "Erabiltzailea ezin da hutsik egon.");
			return;
		}

		if (jasoPasahitza.isEmpty()) {
			erakutsiAlert("Errorea", "Pasahitza ezin da hutsik egon.");
			return;
		}

		SaltzaileBean saltzaile = balidatuKredentzialak(jasoErabiltzaile, jasoPasahitza);
		if (saltzaile != null) {
			try {
				Stage currentStage = (Stage) erabiltzaile.getScene().getWindow();
				currentStage.close();

				FXMLLoader loader = new FXMLLoader(getClass().getResource("/menuSaltzaile/MenuSaltzaile.fxml"));
				Scene scene = new Scene(loader.load());

				MenuSaltzaileKontrolagailua controller = loader.getController();
				Stage newStage = new Stage();
				controller.setStage(newStage);
				controller.setErabiltzailea(saltzaile.getErabiltzailea());
				controller.setAldaketak(saltzaile.getAldaketak());

				newStage.setScene(scene);
				newStage.setTitle("Menu Saltzaile");
				newStage.show();

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			BezeroaBean bezeroa = balidatuKredentzialak2(jasoErabiltzaile, jasoPasahitza);
			if (bezeroa != null) {
				try {
					Stage currentStage = (Stage) erabiltzaile.getScene().getWindow();
					currentStage.close();

					FXMLLoader loader = new FXMLLoader(getClass().getResource("/menuBezeroa/MenuBezeroa.fxml"));
					Scene scene = new Scene(loader.load());

					MenuBezeroaKontrolagailua controller = loader.getController();
					Stage newStage = new Stage();
					controller.setStage(newStage);
					controller.setUserData(bezeroa.getIzena(), bezeroa.getAbizena(), bezeroa.getEmaila());

					newStage.setScene(scene);
					newStage.setTitle("Menu Bezeroa");
					newStage.show();

				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				if (existeSaltzaile(jasoErabiltzaile) || existeBezeroa(jasoErabiltzaile)) {
					erakutsiAlert("Errorea", "Pasahitza okerra da. Saiatu berriro");
				} else {
					erakutsiAlert("Errorea", "Erabiltzailea ez da existitzen.");
				}
			}
		}
	}

	/**
	 * Emandako erabiltzaile izenarekin saltzailea existitzen den egiaztatzen du,
	 * pasahitza kontuan hartu gabe.
	 *
	 * @param erabiltzaileIzena egiaztatu beharreko erabiltzaile izena.
	 * @return true saltzailea existitzen bada, false bestela.
	 */
	private boolean existeSaltzaile(String erabiltzaileIzena) {
		ArrayList<SaltzaileBean> saltzaileak = jasoSaltzaile();
		for (SaltzaileBean sb : saltzaileak) {
			if (erabiltzaileIzena.equals(sb.getErabiltzailea())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Emandako izenarekin bezeroa existitzen den egiaztatzen du, abizena kontuan
	 * hartu gabe.
	 *
	 * @param izenaTestua egiaztatu beharreko bezeroaren izena.
	 * @return true bezeroa existitzen bada, false bestela.
	 */
	private boolean existeBezeroa(String izenaTestua) {
		ArrayList<BezeroaBean> bezeroak = jasoBezeroa();
		for (BezeroaBean bb : bezeroak) {
			if (izenaTestua.equalsIgnoreCase(bb.getIzena())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Bezero berri baten erregistro leihoa irekitzen du.
	 */
	@FXML
	private void erregistratu() {
		try {
			Stage currentStage = (Stage) erabiltzaile.getScene().getWindow();
			currentStage.close();
			Erregistratu erregistratu = new Erregistratu();
			Stage newStage = new Stage();
			erregistratu.start(newStage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Saltzaile baten kredentzialak datu-basearen aurka balioztatzen ditu.
	 *
	 * @param erabiltzaile saltzailearen erabiltzaile izena.
	 * @param pasahitza    saltzailearen pasahitza.
	 * @return SaltzaileBean kredentzialak zuzenak badira, null bestela.
	 */
	private SaltzaileBean balidatuKredentzialak(String erabiltzaile, String pasahitza) {
		ArrayList<SaltzaileBean> saltzaile = jasoSaltzaile();
		for (SaltzaileBean sb : saltzaile) {
			if (erabiltzaile.equals(sb.getErabiltzailea()) && pasahitza.equals(sb.getPasahitza())) {
				return sb;
			}
		}
		return null;
	}

	/**
	 * Datu-baseko saltzaile guztien zerrenda lortzen du.
	 *
	 * @return SaltzaileBean zerrenda saltzaile guztiekin.
	 */
	private ArrayList<SaltzaileBean> jasoSaltzaile() {
		DatuBasea db = new DatuBasea();
		Connection konexioa = null;
		Statement stmt = null;
		ResultSet rs = null;
		SaltzaileBean erregistroSaltzaile;
		ArrayList<SaltzaileBean> taulaSaltzaile = new ArrayList<SaltzaileBean>();
		String sql = "SELECT * FROM saltzaile";

		try {
			konexioa = db.konektoreaWorld();
			stmt = konexioa.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				erregistroSaltzaile = new SaltzaileBean();
				erregistroSaltzaile.setId(rs.getInt("ID"));
				erregistroSaltzaile.setErabiltzailea(rs.getString("ERABILTZAILEA"));
				erregistroSaltzaile.setPasahitza(rs.getString("PASAHITZA"));
				erregistroSaltzaile.setAldaketak(rs.getString("ALDAKETAK"));
				taulaSaltzaile.add(erregistroSaltzaile);
			}
			rs.close();
			stmt.close();
			konexioa.close();
		} catch (SQLException e) {
			System.out.println("Errorea: " + e);
		}
		return taulaSaltzaile;
	}

	/**
	 * Bezero baten kredentzialak datu-basearen aurka balioztatzen ditu. Izenaren
	 * konparaketa maiuskulak eta minuskulak bereizten ez dituena da.
	 *
	 * @param izena   bezeroaren izena.
	 * @param abizena bezeroaren abizena.
	 * @return BezeroaBean kredentzialak zuzenak badira, null bestela.
	 */
	private BezeroaBean balidatuKredentzialak2(String izena, String abizena) {
		ArrayList<BezeroaBean> bezeroa = jasoBezeroa();
		for (BezeroaBean sb : bezeroa) {
			if (izena.equalsIgnoreCase(sb.getIzena()) && abizena.equalsIgnoreCase(sb.getAbizena())) {
				return sb;
			}
		}
		return null;
	}

	/**
	 * Datu-baseko bezero guztien zerrenda lortzen du.
	 *
	 * @return BezeroaBean zerrenda bezero guztiekin.
	 */
	private ArrayList<BezeroaBean> jasoBezeroa() {
		DatuBasea db = new DatuBasea();
		Connection konexioa = null;
		Statement stmt = null;
		ResultSet rs = null;
		BezeroaBean erregistroBezeroa;
		ArrayList<BezeroaBean> taulaBezeroa = new ArrayList<BezeroaBean>();
		String sql = "SELECT id, izena, abizena, emaila FROM bezero";

		try {
			konexioa = db.konektoreaWorld();
			stmt = konexioa.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				erregistroBezeroa = new BezeroaBean();
				erregistroBezeroa.setId(rs.getInt("ID"));
				erregistroBezeroa.setIzena(rs.getString("IZENA"));
				erregistroBezeroa.setAbizena(rs.getString("ABIZENA"));
				erregistroBezeroa.setEmaila(rs.getString("EMAILA"));
				taulaBezeroa.add(erregistroBezeroa);
			}
			rs.close();
			stmt.close();
			konexioa.close();
		} catch (SQLException e) {
			System.out.println("Errorea: " + e);
		}
		return taulaBezeroa;
	}

	/**
	 * Errore mezu bat alerta leiho batean erakusten du.
	 *
	 * @param titulua alertaren titulua.
	 * @param mezua   alertaren mezua.
	 */
	private void erakutsiAlert(String titulua, String mezua) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(titulua);
		alert.setHeaderText(null);
		alert.setContentText(mezua);
		alert.showAndWait();
	}
}