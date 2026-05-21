package konexioa;

/**
 * Sistemako saltzaile bat adierazten duen klasea. Datu-basetik lortutako
 * saltzailearen sarbide datuak eta rola ditu.
 *
 * @author E3T2
 * @version 1.0
 */
public class SaltzaileBean {

	/** Saltzailearen identifikatzaile bakarra. */
	private int id;

	/** Saltzailearen erabiltzaile izena. */
	private String erabiltzailea;

	/** Saltzailearen pasahitza. */
	private String pasahitza;

	/**
	 * Saltzailearen rola edo oharrak (adib: Administratzailea, Hasierako
	 * kontratazioa).
	 */
	private String aldaketak;

	/**
	 * SaltzaileBean-en konstruktore hutsa.
	 */
	public SaltzaileBean() {
	}

	/**
	 * Saltzailearen identifikatzailea lortzen du.
	 *
	 * @return saltzailearen id-a.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Saltzailearen identifikatzailea ezartzen du.
	 *
	 * @param id saltzailearen identifikatzailea.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Saltzailearen erabiltzaile izena lortzen du.
	 *
	 * @return saltzailearen erabiltzaile izena.
	 */
	public String getErabiltzailea() {
		return erabiltzailea;
	}

	/**
	 * Saltzailearen erabiltzaile izena ezartzen du.
	 *
	 * @param erabiltzailea saltzailearen erabiltzaile izena.
	 */
	public void setErabiltzailea(String erabiltzailea) {
		this.erabiltzailea = erabiltzailea;
	}

	/**
	 * Saltzailearen pasahitza lortzen du.
	 *
	 * @return saltzailearen pasahitza.
	 */
	public String getPasahitza() {
		return pasahitza;
	}

	/**
	 * Saltzailearen pasahitza ezartzen du.
	 *
	 * @param pasahitza saltzailearen pasahitza.
	 */
	public void setPasahitza(String pasahitza) {
		this.pasahitza = pasahitza;
	}

	/**
	 * Saltzailearen rola edo oharrak lortzen ditu.
	 *
	 * @return saltzailearen rola.
	 */
	public String getAldaketak() {
		return aldaketak;
	}

	/**
	 * Saltzailearen rola edo oharrak ezartzen ditu.
	 *
	 * @param aldaketak saltzailearen rola.
	 */
	public void setAldaketak(String aldaketak) {
		this.aldaketak = aldaketak;
	}
}