package eskaera;

/**
 * Bezeroak egindako erosketa bat adierazten duen klasea. Erosketa saio bereko
 * produktu guztiak biltzen ditu. Datuak datu-baseko V_EROSKETAK ikuspegitik
 * lortzen dira.
 *
 * @author E3T2
 * @version 1.0
 */
public class EskaeraBean {

	/** Erosketaren identifikatzaile bakarra. */
	private String erosketaId;

	/** Erosketa egin duen bezeroaren izen osoa. */
	private String bezeroa;

	/** Erositako produktuen zerrenda komaz bereizita. */
	private String produktuak;

	/** Erosketaren prezio totala. */
	private double total;

	/** Erabilitako ordainketa metodoa (Txartela, PayPal, Transferentzia, Bizum). */
	private String ordainketaMetodoa;

	/** Erosketa egin zen data eta ordua. */
	private String data;

	/**
	 * EskaeraBean-en konstruktorea eremu guztiekin.
	 *
	 * @param erosketaId        erosketaren identifikatzaile bakarra.
	 * @param bezeroa           bezeroaren izena.
	 * @param produktuak        erositako produktuak.
	 * @param total             erosketaren prezio totala.
	 * @param ordainketaMetodoa erabilitako ordainketa metodoa.
	 * @param data              erosketaren data eta ordua.
	 */
	public EskaeraBean(String erosketaId, String bezeroa, String produktuak, double total, String ordainketaMetodoa,
			String data) {
		this.erosketaId = erosketaId;
		this.bezeroa = bezeroa;
		this.produktuak = produktuak;
		this.total = total;
		this.ordainketaMetodoa = ordainketaMetodoa;
		this.data = data;
	}

	/**
	 * Erosketaren identifikatzailea lortzen du.
	 *
	 * @return erosketaren identifikatzailea.
	 */
	public String getErosketaId() {
		return erosketaId;
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
	 * Erositako produktuak lortzen ditu.
	 *
	 * @return erositako produktuak komaz bereizita.
	 */
	public String getProduktuak() {
		return produktuak;
	}

	/**
	 * Erosketaren prezio totala lortzen du.
	 *
	 * @return erosketaren prezio totala.
	 */
	public double getTotal() {
		return total;
	}

	/**
	 * Erabilitako ordainketa metodoa lortzen du.
	 *
	 * @return ordainketa metodoa.
	 */
	public String getOrdainketaMetodoa() {
		return ordainketaMetodoa;
	}

	/**
	 * Erosketaren data eta ordua lortzen du.
	 *
	 * @return erosketaren data eta ordua.
	 */
	public String getData() {
		return data;
	}
}