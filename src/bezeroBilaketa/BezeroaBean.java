package bezeroBilaketa;

/**
 * Bezeroen bilaketa taulan erabiltzen den bezeroa adierazten duen klasea.
 * Datu-baseko V_BEZEROAK ikuspegitik lortutako bezeroaren datuak ditu, izena
 * eta abizena kateaturik.
 *
 * @author E3T2
 * @version 1.0
 */
public class BezeroaBean {

	/** Bezeroaren identifikatzaile bakarra. */
	private int id;

	/** Bezeroaren izen eta abizena kateaturik. */
	private String izenaAbizena;

	/** Bezeroaren NAN-a. */
	private String nan;

	/** Bezeroaren egoera (AKTIBOA/INAKTIBOA). */
	private String egoera;

	/** Bezeroaren alta data. */
	private String sorreraData;

	/**
	 * BezeroaBean-en konstruktorea eremu guztiekin.
	 *
	 * @param id           bezeroaren identifikatzailea.
	 * @param izenaAbizena bezeroaren izen eta abizena kateaturik.
	 * @param nan          bezeroaren NAN-a.
	 * @param egoera       bezeroaren egoera.
	 * @param sorreraData  bezeroaren alta data.
	 */
	public BezeroaBean(int id, String izenaAbizena, String nan, String egoera, String sorreraData) {
		this.id = id;
		this.izenaAbizena = izenaAbizena;
		this.nan = nan;
		this.egoera = egoera;
		this.sorreraData = sorreraData;
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
	 * Bezeroaren izen eta abizena kateaturik lortzen du.
	 *
	 * @return bezeroaren izen eta abizena.
	 */
	public String getIzenaAbizena() {
		return izenaAbizena;
	}

	/**
	 * Bezeroaren izen eta abizena kateaturik ezartzen du.
	 *
	 * @param izenaAbizena bezeroaren izen eta abizena.
	 */
	public void setIzenaAbizena(String izenaAbizena) {
		this.izenaAbizena = izenaAbizena;
	}

	/**
	 * Bezeroaren NAN-a lortzen du.
	 *
	 * @return bezeroaren NAN-a.
	 */
	public String getNan() {
		return nan;
	}

	/**
	 * Bezeroaren NAN-a ezartzen du.
	 *
	 * @param nan bezeroaren NAN-a.
	 */
	public void setNan(String nan) {
		this.nan = nan;
	}

	/**
	 * Bezeroaren egoera lortzen du.
	 *
	 * @return bezeroaren egoera.
	 */
	public String getEgoera() {
		return egoera;
	}

	/**
	 * Bezeroaren egoera ezartzen du.
	 *
	 * @param egoera bezeroaren egoera.
	 */
	public void setEgoera(String egoera) {
		this.egoera = egoera;
	}

	/**
	 * Bezeroaren alta data lortzen du.
	 *
	 * @return bezeroaren alta data.
	 */
	public String getSorreraData() {
		return sorreraData;
	}

	/**
	 * Bezeroaren alta data ezartzen du.
	 *
	 * @param sorreraData bezeroaren alta data.
	 */
	public void setSorreraData(String sorreraData) {
		this.sorreraData = sorreraData;
	}
}