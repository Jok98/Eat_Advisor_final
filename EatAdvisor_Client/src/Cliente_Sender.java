
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JOptionPane;
/**
 *@author Matteo Moi 737574 Varese<br><br>
 * <br>
 * La classe si occupa di inviare al server i dati (tramite socket) dell'utente in base al tipo di azione/richiesta dell'utente : <br>
 * ad ogni nuova istanza della classe viene inviato al server (tramite socket) una stringa necessaria per identificare la richiesta(reconizer) del client
 * invia al server_db tramite send_data(String tupla) una stringa(tupla) contenente i dati necessari per gestirla<br>
 * - case "Cliente" : si occupa dell'invio dei dati <br>
 * - case "Cliente_accesso": gestisce il login dell'utente;<br>
 * - case "Client_comment": invia il commento dell'utente su un determinato ristorante al server per poi inserirlo nel db;<br>
 * - case "Cliente_accesso_free": permette l'accesso  al frame LogIn senza essere registrati ma non permette la funzione di recensire i ristoranti;<br>
 * <br>
 * Metodi : 
 * <br>
 * - send_data(String tupla) : tramite socket invia al srver i dati necessari per gestire la richiesta dell'utente<br>
 * - recive_data() : permette di ricevere info dal server, in particolare viene utilizato per verificare <br>
 *   se i dati inseriti dall'utente per il login corrispondono per poi permettere l'accesso al frame LogIn
 */
public class Cliente_Sender extends Thread{
	  static Cliente cliente = new Cliente();
	  static LogIn login = new LogIn();
	  static Registration reg = new Registration();
	  static Socket socket;
	  static Boolean continue_ = false;
	  private BufferedReader in;
	  private PrintWriter out;
	  static String reconizer;
	  static String tupla;
	  static String comment;
	  static Frame message = new Frame();
	  static Boolean continua =false;
	  
	  
	public Cliente_Sender(Socket socket, String reconizer) throws IOException {
		this.reconizer=reconizer;
		this.socket = socket;
		this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    this.out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
	   
	    out.println(reconizer);
	    out.flush();
	    start();
	    //out.close();
	}
	
	public void run() {
		try {
			//System.out.print(tupla);
			switch(reconizer) {
			case "Cliente" :
				tupla = reg.tupla;
				send_data(tupla);
				this.interrupt();
				break;
			case "Cliente_accesso":
				tupla = cliente.data;
				send_data(tupla);
				//sleep(1000);
				if(recive_data()==true) {
					continua= true;
					
					login.frame.setVisible(true);
					login.btnInvia.setEnabled(true);
					login.tf_comment.setEnabled(true);
					cliente.window.frame.setVisible(false);
					JOptionPane.showMessageDialog(message,"LogIn effettuato");
				}else{
					continua= false;
					cliente.window.frame.setVisible(true);
					JOptionPane.showMessageDialog(message,"Dati inseriti errati");
				}
				this.interrupt();
				break;
			case "Cliente_search":
				tupla = login.tupla;
				send_data(tupla);
				continue_=true;
				this.interrupt();
				break;
			
			case "Client_comment":
				comment = login.create_comment();
				send_data(comment);
				
				this.interrupt();
				break;
				
			case "Cliente_accesso_free":
				login.frame.setVisible(true);
				login.btnInvia.setEnabled(false);
				login.tf_comment.setEnabled(false);
				cliente.window.frame.setVisible(false);
				JOptionPane.showMessageDialog(message,"Accesso consentito");
				break;
			}
			
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		
	}
	

	
	/**
	 * il metodo invia i dati ottenuti al server
	 * @param tupla � una stringa contenente i dati inseriti dall'utente nella gui per l'iscrizione
	 * @throws IOException
	 */
	public void send_data(String tupla) throws IOException {
		
		//System.out.println(socket);
		//out.println("Cliente");
		
		out.println(tupla);
		out.flush();
		System.out.println("Cliente invia dati : "+tupla);
		//out.close();
		//socket.close();
	}
	
	public Boolean recive_data() throws IOException {
		String access = in.readLine();
		System.out.println(access);
		if(access.equals("consentito")) {
			return true;
		}
		return false;
	}
	
}
