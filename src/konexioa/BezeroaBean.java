package konexioa;

/**
 * Sistemako bezero bat adierazten duen klasea. Datu-basetik lortutako
 * bezeroaren datu pertsonalak ditu.
 *
 * @author E3T2
 * @version 1.0
 */
public class BezeroaBean {

	/** Bezeroaren identifikatzaile bakarra. */
	private int id;

	/** Bezeroaren izena. */
	private String izena;

	/** Bezeroaren abizena. */
	private String abizena;

	/** Bezeroaren helbidea. */
	private String helbidea;

	/** Bezeroaren emaila. */
	private String emaila;

	/**
	 * BezeroaBean-en konstruktore hutsa.
	 */
	public BezeroaBean() {
	}

	/**
	 * Bezeroaren identifikatzailea lortzen du.
	 *
	 * @return bezeroaren id-a.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Bezeroaren identifikatzailea ezartzen du.
	 *
	 * @param id bezeroaren identifikatzailea.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Bezeroaren izena lortzen du.
	 *
	 * @return bezeroaren izena.
	 */
	public String getIzena() {
		return izena;
	}

	/**
	 * Bezeroaren izena ezartzen du.
	 *
	 * @param izena bezeroaren izena.
	 */
	public void setIzena(String izena) {
		this.izena = izena;
	}

	/**
	 * Bezeroaren abizena lortzen du.
	 *
	 * @return bezeroaren abizena.
	 */
	public String getAbizena() {
		return abizena;
	}

	/**
	 * Bezeroaren abizena ezartzen du.
	 *
	 * @param abizena bezeroaren abizena.
	 */
	public void setAbizena(String abizena) {
		this.abizena = abizena;
	}

	/**
	 * Bezeroaren helbidea lortzen du.
	 *
	 * @return bezeroaren helbidea.
	 */
	public String getHelbidea() {
		return helbidea;
	}

	/**
	 * Bezeroaren helbidea ezartzen du.
	 *
	 * @param helbidea bezeroaren helbidea.
	 */
	public void setHelbidea(String helbidea) {
		this.helbidea = helbidea;
	}

	/**
	 * Bezeroaren emaila lortzen du.
	 *
	 * @return bezeroaren emaila.
	 */
	public String getEmaila() {
		return emaila;
	}

	/**
	 * Bezeroaren emaila ezartzen du.
	 *
	 * @param emaila bezeroaren emaila.
	 */
	public void setEmaila(String emaila) {
		this.emaila = emaila;
	}
}