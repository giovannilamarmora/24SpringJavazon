package org.generation.italy.javazon.SpringJavazon.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Questa classe ha lo scopo di fornire gli stumenti base per effettuare ORM.
 * Stabilita la connessione � in grado di eseguire query e restituire
 * in caso di necessit� una lista di mappe o una mappa che descrive un resultset.
 * La connessione di questo dao � sempre aperta!
 * @author trito
 */
public abstract class BasicDao {

	private Connection connection;
	
	public BasicDao(String dbAddress, String user, String password) {
		super();
		try {
			connection = DriverManager.getConnection(dbAddress, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Restituisce l'oggetto di tipo ResultSet derivante dall'esecuzione
	 * di una query
	 * @param sql la query da inviare
	 * @param conditions il/i valore/i da sostituire ai placeholders della query
	 * @return L'oggetto ResultSet
	 * @throws SQLException
	 */
	private ResultSet executeQuery(String sql, Object... conditions) throws SQLException {
		return prepareStm(sql, conditions).executeQuery();
	}

	/**
	 * Restituisce l'oggetto di tipo PreparedStatement per eseguire una query
	 * <p>
	 * <code>String sql = "SELECT * FROM tabella WHERE id = ?"</code> => conditions[0] => 1
	 * <br>
	 * ?(1) => conditions[0]
	 * <br>
	 * ?(2) => conditions[1]
	 * @param sql la query da inviare
	 * @param conditions il/i valore/i da sostituire ai placeholders della query
	 * @return il PreparedStatement contenente la query con i placeholders sostituiti
	 * @throws SQLException
	 */
	private PreparedStatement prepareStm(String sql, Object... conditions) throws SQLException {
		PreparedStatement stm = connection.prepareStatement(sql);
		
		// getAll("SELECT * FROM dipendenti WHERE mansione = ?, residenza = ?",
		//			"Operaio", "Milano")
		// getAll("SELECT * FROM dipendenti WHERE id = ?",
		//			5)
		for (int i = 0; i < conditions.length; i++) {
			stm.setObject(i + 1, conditions[i]);
		}
		
		return stm;
	}
	
	/**
	 * Restituisce la mappa di una singola tiga di un resultset dove la chiave
	 * corrisponde al nome della colonna della tabella e il valore quello nella cella
	 * di quella determinata riga
	 * @param rs Il ResultSet ottenuto dal DB eseguendo una determinata query
	 * @return la mappa che descrive la singola riga di un resultset
	 * @throws SQLException
	 */
	private Map<String, String> mapFromRS(ResultSet rs) throws SQLException {
		Map<String, String> map = new HashMap<>();
		
		ResultSetMetaData meta = rs.getMetaData();
		
		for (int i = 1; i <= meta.getColumnCount(); i++) {
			map.put(meta.getColumnName(i), rs.getString(i));
		}
		return map;
	}
	
	/**
	 * Lista contenente mappe che descrivono delle entit� nella persistenza
	 * La mappa � la rappresentazione di una RIGA di una tabella
	 * La lista quindi � l'insieme delle righe di una tabella
	 * @param sql la query da inviare
	 * @param conditions il/i valore/i da sostituire ai placeholders della query
	 * @return la lista contenente tutte le mappe restituite dal DB in base alla 
	 * query inserita
	 */
	public List<Map<String, String>> getAll(String sql, Object...conditions) {
		// ... indica che potranno esserci da 0 a infiniti parametri
		// Ho usato Object perch� voglio essere il pi� generico possibile
		List<Map<String, String>> ris = new ArrayList<>();
		
		try {
			ResultSet rs = executeQuery(sql, conditions);
			
			while (rs.next()) {
				ris.add(mapFromRS(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ris;
	}

	/**
	 * Mappa che descrive un'entit� nella persistenza
	 * @param sql la query da inviare
	 * @param conditions il/i valore/i da sostituire ai placeholders della query
	 * @return La mappa restituita dal DB in base alla query inserita
	 */
	public Map<String, String> getOne(String sql, Object...conditions) {
		Map<String, String> ris = null;
		
		try {
			ResultSet rs = executeQuery(sql, conditions);
			
			if (rs.next()) {
				ris = mapFromRS(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ris;
	}
	
	/**
	 * Esegue una query impostando prima le condizioni
	 * @param sql la query da inviare
	 * @param conditions il/i valore/i da sostituire ai placeholders della query
	 */
	public void execute(String sql, Object...conditions) {
		try {
			prepareStm(sql, conditions).execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Restituisce un PreparedStatement che � in grado di fornire l'id dell'inserimento
	 * @param la query da inviare
	 * @param conditions il/i valore/i da sostituire ai placeholders della query
	 * @return Il PreparedStatement contenente la query con i placeholders sostituiti
	 * @throws SQLException
	 */
	private PreparedStatement preparedStatementWithGeneratedKey(String sql, Object... conditions) throws SQLException {
		PreparedStatement stm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		
		for (int i = 0; i < conditions.length; i++) {
			stm.setObject(i + 1, conditions[i]);
		}
		
		return stm;
	}
	
	/**
	 * Metodo che effetua una insert e restituisce l'id che viene auto generato 
	 * dal DB
	 * @param la query da inviare
	 * @param conditions il/i valore/i da sostituire ai placeholders della query
	 * @return l'id generato dal db per questo insert
	 */
	public int insertAndGetId(String sql, Object... conditions) {
		int id = 0;
		
		try {
			PreparedStatement stm = preparedStatementWithGeneratedKey(sql, conditions);
			
			stm.executeUpdate();
			
			ResultSet rs = stm.getGeneratedKeys();
			
			if (rs.next()) {
				id = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return id;
	}
}














