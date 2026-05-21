package konexioa;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * MySQL datu-basearekin konexioa kudeatzeko ardura duen klasea. e2t2erronka
 * datu-basera konexioa lortzeko metodoak eskaintzen ditu.
 *
 * @author E2T3
 * @version 1.0
 */
public class DatuBasea {

	/** Datu-basearen konexio URL-a. */
	private static final String URL = "jdbc:mysql://localhost:3306/e2t2erronka";

	/** Datu-basearen erabiltzaile izena. */
	private static final String ERABILTZAILEA = "root";

	/** Datu-basearen pasahitza. */
	private static final String PASAHITZA = "root";

	/**
	 * e2t2erronka datu-basearekin konexioa ezartzen eta itzultzen du. MySQL JDBC
	 * driver-a erabiltzen du localhost-era 3306 portuan konektatzeko.
	 *
	 * @return Connection datu-basearen konexio objektua, edo null konexioak huts
	 *         egiten badu.
	 */
	public Connection konektoreaWorld() {
		Connection konekzioa = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			konekzioa = DriverManager.getConnection(URL, ERABILTZAILEA, PASAHITZA);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return konekzioa;
	}
}