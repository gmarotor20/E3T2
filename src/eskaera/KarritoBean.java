package eskaera;

/**
 * Erosketa saskian gehitutako produktu bat adierazten duen klasea. Prezio
 * totala automatikoki kalkulatzen du kantitatearen eta prezio unitarioaren
 * arabera.
 *
 * @author E3T2
 * @version 1.0
 */
public class KarritoBean {

	/** Saskian gehitutako produktuaren izena. */
	private String produktua;

	/** Hautatutako produktuaren kantitatea. */
	private int kantitatea;

	/** Produktuaren prezio unitarioa. */
	private double prezioa;

	/** Prezio totala (kantitatea x prezioa). */
	private double totala;

	/**
	 * KarritoBean-en konstruktorea. Prezio totala automatikoki kalkulatzen du.
	 *
	 * @param produktua  produktuaren izena.
	 * @param kantitatea produktuaren kantitatea.
	 * @param prezioa    produktuaren prezio unitarioa.
	 */
	public KarritoBean(String produktua, int kantitatea, double prezioa) {
		this.produktua = produktua;
		this.kantitatea = kantitatea;
		this.prezioa = prezioa;
		this.totala = kantitatea * prezioa;
	}

	/**
	 * Produktuaren izena lortzen du.
	 *
	 * @return produktuaren izena.
	 */
	public String getProduktu() {
		return produktua;
	}

	/**
	 * Produktuaren kantitatea lortzen du.
	 *
	 * @return produktuaren kantitatea.
	 */
	public int getKantitatea() {
		return kantitatea;
	}

	/**
	 * Produktuaren prezio unitarioa lortzen du.
	 *
	 * @return produktuaren prezio unitarioa.
	 */
	public double getPrezioa() {
		return prezioa;
	}

	/**
	 * Produktuaren prezio totala lortzen du (kantitatea x prezioa).
	 *
	 * @return produktuaren prezio totala.
	 */
	public double getTotala() {
		return totala;
	}
}