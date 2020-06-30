
import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.Scanner;
/**
 * * @author Matteo Moi 737574 Varese<br>
 * La classe EA_Single_Server ha il compito di gestire le richieste da parte delle app Cliente e Restaurant :</br>
 * - case "Cliente": viene gestita l'iscrizione tramite l'inserimento dei dati inviati dall'app Cliente (tramite socket)nella table "Cliente" nel DB </br>
 * - case "Restaurant": viene gestita l'iscrizione tramite l'inserimento dei dati inviati dall'app Ristorante (tramite socket)nella table "Restaurant" nel DB </br>
 * - case "Cliente_accesso" verifica che i dati inseriti dall'utente nell'app Cliente (per effettuare il login) siano presenti e coincidano con quelli presenti sul DB</br>
 * - case "Cliente_search" : si occupa della ricerca nel database dei ristoranti aventi uguali caratteristiche richieste dall'utente</br>
 * - case "Client_comment" : inserisce nel DB il commento scritto dall'utente riguardante uno specifico ristorante</br>
 *
 */


public class EA_Single_Server extends Thread{
	private EA_DB db;
	static Socket server;
	private BufferedReader in ;
	private PrintWriter out;
	static String client;
	static String restaurant_name;
	static Scanner scan;
	//il valore di tupla varia in base all'app che si connette al server
	String[] tupla;
	
	/**
	 * 
	 * @param db
	 * @param server
	 * @throws IOException
	 */
	public EA_Single_Server(EA_DB db, Socket server) throws IOException {
		this.db = db;
		this.server = server;
		in = new BufferedReader(new InputStreamReader(server.getInputStream()));
	    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(server.getOutputStream())),true);
	    
	    start(); 
	}
	
	public void run() {
		
		try {
			db.db_start_conn();
			db.show_table("SELECT * FROM Restaurant");
			db.show_table("SELECT * FROM Cliente");
			switch(recognize_client()) {
			case "Cliente":
				tupla = new String[7];
				tupla = client_data();
				db.cliente_registration(tupla);
				System.out.println("ricevuto cliente");
				System.out.println();
				db.show_table("SELECT * FROM Restaurant");
				System.out.println();
				db.show_table("SELECT * FROM Cliente");
				break;
			case "Restaurant":
				
				tupla = new String[6];
				tupla = client_data();
				db.restourant_registration(tupla);
				System.out.println("ricevuto ristorante");
				System.out.println();
				db.show_table("SELECT * FROM Restaurant");
				System.out.println();
				db.show_table("SELECT * FROM Cliente");
				break;
				
			case "Cliente_accesso":
				tupla = new String[2];
				tupla = client_data();
				System.out.println("ricevuti dati cliente : "+tupla[0]+"&"+tupla[1]);
				String access = db.check_data_login(tupla);
				out.println(access);
				System.out.println();
				db.show_table("SELECT * FROM Restaurant");
				System.out.println();
				db.show_table("SELECT * FROM Cliente");
				break;
				
			case "Cliente_search":
				tupla = new String[3];
				String[] query = client_data();
				System.out.println("ricerca cliente : "+query[0]);
				db.search_restaurant(query);			
				break;
			
			case "Client_comment":
				tupla = new String[2];
				String[] commento = client_data();
				System.out.println("Server riceve commento : "+commento[0]+commento[1]);
				db.insert_comment(commento);
				System.out.println();
				db.show_table("SELECT * FROM Restaurant");
				System.out.println();
				db.show_table("SELECT * FROM Cliente");
				break;
			}
		
			server.close();
			
			
		} catch (IOException | SQLException | NullPointerException e) {
			
			System.out.println("Applicazione client disconnessa");
		}

	}
	
	
	/**
	 * legge dal client  il tipo richiesta
	 * @return "Cliente" | "Restaurant" | "Cliente_accesso" | "Client_comment"
	 * @throws IOException
	 */
	public String recognize_client() throws IOException {
		try {
		client = in.readLine();
		}catch(SocketException e) {
			
			System.out.println("errore connessione socket");
		}
		
		return client;
	}
	
	
	/**
	 * il metodo legge la stringa inviata dal client e la scompone in un array
	 * struttura necessaria per l'inserimento corretto dei dati nella tabella apposita
	 * @return tupla 
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public String[] client_data() throws IOException {
		
		String tmp = in.readLine();
		if(!(client.equals("Client_comment"))) {
			tmp = tmp.trim();
			tmp = tmp.replaceAll(" {2,}", " ");
			System.out.println("Stringa inviata dal client : " +tmp);
			scan = new Scanner(tmp).useDelimiter(" ");
			int i = 0;
			while(scan.hasNext()) {
				tupla[i]=scan.next();
				i++;
				if(i>tupla.length)break;
		}
		}else {
			scan = new Scanner(tmp).useDelimiter("--");
			int i = 0;
			while(scan.hasNext()) {
				tupla[i]=scan.next();
				i++;
				if(i>tupla.length)break;
			}
		}
		return tupla;
	}
	
	
	
}
