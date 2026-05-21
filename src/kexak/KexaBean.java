package kexak;

/**
 * Bezeroak egindako kexa bat adierazten duen klasea. Datu-basetik lortutako
 * kexaren datuak ditu.
 *
 * @author E3T2
 * @version 1.0
 */
public class KexaBean {

	/** Kexaren identifikatzaile bakarra. */
	private int id;

	/** Kexa egin duen bezeroaren izen osoa. */
	private String bezeroa;

	/** Bezeroaren emaila, berarekin harremanetan jartzeko. */
	private String emaila;

	/** Kexaren motiboa (Entrega, Produktua, Ordainketa, Beste bat). */
	private String motiboa;

	/** Kexaren mezu zehatza. */
	private String mezua;

	/** Kexa egin zen data eta ordua. */
	private String data;

	/**
	 * KexaBean-en konstruktorea eremu guztiekin.
	 *
	 * @param id      kexaren identifikatzaile bakarra.
	 * @param bezeroa bezeroaren izena.
	 * @param emaila  bezeroaren emaila.
	 * @param motiboa kexaren motiboa.
	 * @param mezua   kexaren mezua.
	 * @param data    kexaren data eta ordua.
	 */
	public KexaBean(int id, String bezeroa, String emaila, String motiboa, String mezua, String data) {
		this.id = id;
		this.bezeroa = bezeroa;
		this.emaila = emaila;
		this.motiboa = motiboa;
		this.mezua = mezua;
		this.data = data;
	}

	/**
	 * Kexaren identifikatzailea lortzen du.
	 *
	 * @return kexaren id-a.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Bezeroaren izena lortzen du.
	 *
	 * @return bezeroaren izena.
	 */
	public String getBezeroa() {
		return bezeroa;
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
	 * Kexaren motiboa lortzen du.
	 *
	 * @return kexaren motiboa.
	 */
	public String getMotiboa() {
		return motiboa;
	}

	/**
	 * Kexaren mezua lortzen du.
	 *
	 * @return kexaren mezua.
	 */
	public String getMezua() {
		return mezua;
	}

	/**
	 * Kexaren data eta ordua lortzen du.
	 *
	 * @return kexaren data eta ordua.
	 */
	public String getData() {
		return data;
	}
}