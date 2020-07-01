
import java.awt.Frame;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;
/**
 * 
 * @author Matteo Moi 737574 Varese<br></br>
 * La classe EA_DB si occupa di gestire: </br>
 * - db_start_conn() : creazione e connessione al database </br>
 * - new_table(String table_str) : creare le tablelle Restaurant e Cliente</br>
 * - cliente_registration(String[] data) : inserisce i dati dell'utente nella tabella Cliente</br>
 * - restourant_registration(String[] data) : inserisce i dati del ristorante nella tabella Restaurant</br>
 * - check_data_login(String[] data) : verifica che i dati inseriti dall'utente oer il logIn corrispondano a quelli presenti nel DB</br>
 * - insert_comment(String[] comment) : inserisce all'interno della tabella Restaurant la recensione dell'utente riguardante uno specifico ristorante</br>
 * - search_restaurant(String[] query) : dai dati inviati dall'utente il metodo ricerca uno o piu' ristoranti aventi uguali dati(nome,comune,tipologia)</br>
 * - show_table(String query) : mostra su console le due tabelle(Cliente e Restaurant) per avere un feedback al programmatore sul attuale stato e contenuto del DB</br>
 */
public class EA_DB {
	
	public static final String driver = "org.apache.derby.jdbc.EmbeddedDriver";
	public static  String jbdc_url ;
	static ArrayList<String> restaurant_list;
	static Connection conn ;
	static String commento="";
	static Frame message = new Frame();
	
	public EA_DB(){

	}
	
	public static void main(String[] Args) throws SQLException {
		//db_start_conn();

	}
	
	/**
	 * Il metodo apre una connessione ad un server embedded
	 * creato tramite derby se la connessione risulta non esistente
	 * @throws SQLException
	 */
	public static void db_start_conn() throws SQLException {
		jbdc_url = "jdbc:derby:C:\\Users\\jokmo\\git\\Eat_Advisor\\EatAdvisor_DB\\DB_EM;create=true";
		if ((conn==null)) {
	    	conn = DriverManager.getConnection(jbdc_url);
			System.out.println("Connessione al database aperta !");
		} else {
			System.out.println("Connessione al database gia' aperta !");
		}	
	}
	
	/**
	 * il metodo viene chiamato 2 volte per creare le tabelle Cliente & Restaurant se esistono gia' viene segnalato su console
	 * @param table_str e' una stringa contenete le istruzioni per creare le tabelle neccessarie
	 * 
	 * @throws SQLException
	 */
	public void new_table(String table_str) throws SQLException{
		String table = table_str;
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(jbdc_url);
			conn.createStatement().execute(table);
			System.out.println("Tabella creata");
			conn.close();
		} catch (SQLException e) {
			
			System.out.println("La tabella esiste gia' ");
			//conn.close();
		}
		//conn.close();

	}
	
	/**
	 * il metodo utilizza i dati ricevuti per salvarli nella tabella Cliente
	 * @param data e' un array contenente in ogni posizione un dato significante dell'utente
	 * @throws SQLException
	 */
	public void cliente_registration(String[] data) throws SQLException {
		Connection conn = DriverManager.getConnection(jbdc_url);
		String nome = data[0];
		String cognome = data[1];
		String comune_residenza = data[2];
		String sigla_provincia_residenza = data[3];
		String email = data[4];
		String nickname = data[5];
		String password = data[6];
		String value = "INSERT INTO Cliente(nome, cognome, comune_residenza, sigla_provincia_residenza, email, nickname, password )"
				+ "VALUES" +"(?,?,?,?,?,?,?)";
		PreparedStatement p_stmt = conn.prepareStatement(value);
		
			p_stmt.setString(1, nome);
			p_stmt.setString(2, cognome);
			p_stmt.setString(3, comune_residenza);
			p_stmt.setString(4, sigla_provincia_residenza);
			p_stmt.setString(5, email);
			p_stmt.setString(6, nickname);
			p_stmt.setString(7, password);
			p_stmt.executeUpdate();
		
		JOptionPane.showMessageDialog(message,"Utente registrato");
		//System.out.println("Valori cliente inseriti");
		//conn.close();
	}
	
	public void restourant_registration(String[] data) throws SQLException {
		Connection conn = DriverManager.getConnection(jbdc_url);
		String nome = data[0];
		String comune = data[1];
		String tipologia = data[2];
		String indirizzo = data[3];
		String cell = data[4];
		String sito = data[5];
		
		String value = "INSERT INTO Restaurant(nome, comune, tipologia, indirizzo, cell, sito, commento)"
				+ "VALUES" +"(?,?,?,?,?,?,?)";
		PreparedStatement p_stmt = conn.prepareStatement(value);
		
			p_stmt.setString(1, nome);
			p_stmt.setString(2, comune);
			p_stmt.setString(3, tipologia);
			p_stmt.setString(4, indirizzo);
			p_stmt.setString(5, cell);
			p_stmt.setString(6, sito);
			p_stmt.setString(7, "Inizio");
			p_stmt.executeUpdate();
			//System.out.println("Valori ristorante inseriti "+nome);
			JOptionPane.showMessageDialog(message,"Ristorante registrato");
			//conn.close();
	}
	//WHERE nickname = jok AND password = nos
	public String check_data_login(String[] data) throws SQLException {
		
		Statement stmt = conn.createStatement();
		ResultSet rslt_set = stmt.executeQuery("SELECT * FROM Cliente");
		while(rslt_set.next()){
			String ID = (rslt_set.getString("nickname"));
			String pass =(rslt_set.getString("password"));
			
		if((ID.equals(data[0]))&&(pass.equals(data[1]))){
			System.out.print(ID+" |");
			System.out.println(pass+" |");
			System.out.println("Dati trovati login consentito");
			//conn.close();
			return "consentito";
		}
		}
		System.out.println("Dati login non trovati");
		//conn.close();
		return "negato";

		
	}
	
	public void insert_comment(String[] comment) throws SQLException {
		String nome = comment[0];
		Connection conn = DriverManager.getConnection(jbdc_url);
		Statement stmt = conn.createStatement();
		ResultSet rslt_set = stmt.executeQuery("SELECT * FROM Restaurant");
		while(rslt_set.next()){
		String tmp_name = rslt_set.getString("nome");
		//String tmp_star = rslt_set.getString("nome");
		if(nome.equals(tmp_name)) {commento = rslt_set.getString("commento")+"\r\n"+"-------"+"\r\n"+"Utente : "+comment[3]+"\r\n"+"stelle : "+comment[2]+" su 5"+"\r\n"+comment[1];}
		}
		//Statement statement = connection.createStatement();	 
		//"UPDATE test_table SET test_col='new_test_value' WHERE test_col = 'test_value'");
		String value = "UPDATE Restaurant SET commento = ? WHERE nome = ?";
		PreparedStatement p_stmt = conn.prepareStatement(value);
		
			p_stmt.setString(1, commento);
			p_stmt.setString(2, nome);
			p_stmt.executeUpdate();
			JOptionPane.showMessageDialog(message,"Commento inserito");
			System.out.println("Valori ristorante inseriti"+commento);
		
		
		
	}
	
	public void search_restaurant(String[] query) throws SQLException {
		String query_nome = query[0];
		String query_comune = query[1];
		String query_tipologia = query[2];
		restaurant_list = new ArrayList<String>();
		Statement stmt = conn.createStatement();
		ResultSet rslt_set = stmt.executeQuery("SELECT * FROM Restaurant");
		//ArrayList<String> restaurant = new ArrayList<String>();
		String nome = "null";
		String comune = "null";
		String tipologia = "null";
		Boolean continua = true;
		//Boolean show = true;
		while(rslt_set.next()){
			nome = rslt_set.getString("nome");
			comune = rslt_set.getString("comune");
			tipologia = rslt_set.getString("tipologia");

		//ricerca per nome
		if((nome.equals(query[0]))&&(query_comune.equals("null"))&&(query_tipologia.equals("null"))){
			restaurant_list.add(nome);
			restaurant_list.add(comune);
			restaurant_list.add(tipologia);
			restaurant_list.add(rslt_set.getString("indirizzo"));
			restaurant_list.add(rslt_set.getString("cell"));
			restaurant_list.add(rslt_set.getString("sito"));
			restaurant_list.add(rslt_set.getString("commento"));
			//System.out.println("Dati ricerca nome trovati ");
			//if(show==true)JOptionPane.showMessageDialog(message,"Dati ricerca per nome trovati");
			//show=false;
			//break;
			//conn.close();
		
		}
		//ricerca per comune
		if((comune.equals(query[1]))&&(query_nome.equals("null"))&&(query_tipologia.equals("null"))){
			restaurant_list.add(nome);
			restaurant_list.add(comune);
			restaurant_list.add(tipologia);
			restaurant_list.add(rslt_set.getString("indirizzo"));
			restaurant_list.add(rslt_set.getString("cell"));
			restaurant_list.add(rslt_set.getString("sito"));
			restaurant_list.add(rslt_set.getString("commento"));
			//System.out.println("Dati ricerca comune trovati ");
			//if(show==true)JOptionPane.showMessageDialog(message,"Dati ricerca per comune trovati");
			//show=false;
			//break;
			//conn.close();
		
		}
		//ricerca per tipologia 
		if((tipologia.equals(query[2]))&&(query_comune.equals("null"))&&(query_nome.equals("null"))){
			restaurant_list.add(nome);
			restaurant_list.add(comune);
			restaurant_list.add(tipologia);
			restaurant_list.add(rslt_set.getString("indirizzo"));
			restaurant_list.add(rslt_set.getString("cell"));
			restaurant_list.add(rslt_set.getString("sito"));
			restaurant_list.add(rslt_set.getString("commento"));
			//System.out.println("Dati ricerca tipologia trovati ");
			//if(show==false)JOptionPane.showMessageDialog(message,"Dati ricerca per tipologia trovati");
			continua = false;
			//show=false;
			//break;
			//conn.close();
		
		}
		//ricerca per comune & tipologia 
				if((comune.equals(query[1])&&(tipologia.equals(query[2]))&&(query_nome.equals("null"))&&(continua==true))){
					restaurant_list.add(nome);
					restaurant_list.add(comune);
					restaurant_list.add(tipologia);
					restaurant_list.add(rslt_set.getString("indirizzo"));
					restaurant_list.add(rslt_set.getString("cell"));
					restaurant_list.add(rslt_set.getString("sito"));
					restaurant_list.add(rslt_set.getString("commento"));
					//if(show==false)JOptionPane.showMessageDialog(message,"Dati ricerca per comune e tipologia trovati");
					//show=false;
					System.out.println("Dati ricerca tipologia e comune trovati ");
					//break;
					//conn.close();
				
				}
	
		}
		//if(show==true)JOptionPane.showMessageDialog(message,"Dati ricerca non trovati nel database");

	}
	
	
	/**
	 * metodo necessario per avere un feedback sui dati inseriti nel db
	 * @throws SQLException
	 */
	public void show_table(String query) throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet rslt_set = stmt.executeQuery(query);
		ResultSetMetaData rslt_set_mtdt = rslt_set.getMetaData();
		int clmn_lnght = rslt_set_mtdt.getColumnCount();
		for(int i = 1; i<=clmn_lnght;i++) {
			System.out.format("%20s", rslt_set_mtdt.getColumnName(i)+" |");
			
		}
		System.out.println("");
		
			while(rslt_set.next()){
			System.out.print(rslt_set.getString(1)+" |");
			System.out.print(rslt_set.getString(2)+" |");
			System.out.print(rslt_set.getString(3)+" |");
			System.out.print(rslt_set.getString(4)+ " |");
			System.out.print(rslt_set.getString(5)+" |");
			System.out.print(rslt_set.getString(6)+" |");
			System.out.println(rslt_set.getString(7)+" |");
			System.out.println("--------------------------------------------------------------");
	
			}
			//conn.close();
	}
	
}
