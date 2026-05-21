package saltzaileAgenda;

/**
 * Saltzaileen agenda taulan erabiltzen den saltzailea adierazten duen klasea.
 * Datu-baseko V_SALTZAILEAK ikuspegitik lortutako saltzailearen datuak ditu.
 *
 * @author E3T2
 * @version 1.0
 */
public class SaltzaileBean {

	/** Saltzailearen identifikatzaile bakarra. */
	private int id;

	/** Saltzailearen erabiltzaile izena. */
	private String erabiltzailea;

	/** Saltzailearen sarrera data. */
	private String sarrera;

	/** Saltzailearen irteera data. */
	private String irteera;

	/** Saltzailearen rola edo oharrak. */
	private String aldaketak;

	/**
	 * SaltzaileBean-en konstruktorea eremu guztiekin.
	 *
	 * @param id            saltzailearen identifikatzailea.
	 * @param erabiltzailea saltzailearen erabiltzaile izena.
	 * @param sarrera       saltzailearen sarrera data.
	 * @param irteera       saltzailearen irteera data.
	 * @param aldaketak     saltzailearen rola edo oharrak.
	 */
	public SaltzaileBean(int id, String erabiltzailea, String sarrera, String irteera, String aldaketak) {
		this.id = id;
		this.erabiltzailea = erabiltzailea;
		this.sarrera = sarrera;
		this.irteera = irteera;
		this.aldaketak = aldaketak;
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
	 * Saltzailearen erabiltzaile izena lortzen du.
	 *
	 * @return saltzailearen erabiltzaile izena.
	 */
	public String getErabiltzailea() {
		return erabiltzailea;
	}

	/**
	 * Saltzailearen sarrera data lortzen du.
	 *
	 * @return saltzailearen sarrera data.
	 */
	public String getSarrera() {
		return sarrera;
	}

	/**
	 * Saltzailearen irteera data lortzen du.
	 *
	 * @return saltzailearen irteera data.
	 */
	public String getIrteera() {
		return irteera;
	}

	/**
	 * Saltzailearen rola edo oharrak lortzen ditu.
	 *
	 * @return saltzailearen rola edo oharrak.
	 */
	public String getAldaketak() {
		return aldaketak;
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
	 * Saltzailearen erabiltzaile izena ezartzen du.
	 *
	 * @param erabiltzailea saltzailearen erabiltzaile izena.
	 */
	public void setErabiltzailea(String erabiltzailea) {
		this.erabiltzailea = erabiltzailea;
	}

	/**
	 * Saltzailearen sarrera data ezartzen du.
	 *
	 * @param sarrera saltzailearen sarrera data.
	 */
	public void setSarrera(String sarrera) {
		this.sarrera = sarrera;
	}

	/**
	 * Saltzailearen irteera data ezartzen du.
	 *
	 * @param irteera saltzailearen irteera data.
	 */
	public void setIrteera(String irteera) {
		this.irteera = irteera;
	}

	/**
	 * Saltzailearen rola edo oharrak ezartzen ditu.
	 *
	 * @param aldaketak saltzailearen rola edo oharrak.
	 */
	public void setAldaketak(String aldaketak) {
		this.aldaketak = aldaketak;
	}
}