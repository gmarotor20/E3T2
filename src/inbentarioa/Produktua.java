package inbentarioa;

/**
 * Inbentarioko produktu bat adierazten duen klasea. Datu-basetik lortutako
 * produktuaren datuak ditu.
 *
 * @author E3T2
 * @version 1.0
 */
public class Produktua {

	/** Produktuaren identifikatzaile bakarra. */
	private int id;

	/** Produktuaren izena. */
	private String izena;

	/** Produktuaren stock eskuragarria. */
	private int stock;

	/** Produktuaren prezioa. */
	private double prezioa;

	/**
	 * Produktua-ren konstruktorea eremu guztiekin.
	 *
	 * @param id      produktuaren identifikatzailea.
	 * @param izena   produktuaren izena.
	 * @param stock   produktuaren stock eskuragarria.
	 * @param prezioa produktuaren prezioa.
	 */
	public Produktua(int id, String izena, int stock, double prezioa) {
		this.id = id;
		this.izena = izena;
		this.stock = stock;
		this.prezioa = prezioa;
	}

	/**
	 * Produktuaren identifikatzailea lortzen du.
	 *
	 * @return produktuaren id-a.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Produktuaren identifikatzailea ezartzen du.
	 *
	 * @param id produktuaren identifikatzailea.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Produktuaren izena lortzen du.
	 *
	 * @return produktuaren izena.
	 */
	public String getIzena() {
		return izena;
	}

	/**
	 * Produktuaren izena ezartzen du.
	 *
	 * @param izena produktuaren izena.
	 */
	public void setIzena(String izena) {
		this.izena = izena;
	}

	/**
	 * Produktuaren stock eskuragarria lortzen du.
	 *
	 * @return produktuaren stock-a.
	 */
	public int getStock() {
		return stock;
	}

	/**
	 * Produktuaren stock eskuragarria ezartzen du.
	 *
	 * @param stock produktuaren stock-a.
	 */
	public void setStock(int stock) {
		this.stock = stock;
	}

	/**
	 * Produktuaren prezioa lortzen du.
	 *
	 * @return produktuaren prezioa.
	 */
	public double getPrezioa() {
		return prezioa;
	}

	/**
	 * Produktuaren prezioa ezartzen du.
	 *
	 * @param prezioa produktuaren prezioa.
	 */
	public void setPrezioa(double prezioa) {
		this.prezioa = prezioa;
	}
}