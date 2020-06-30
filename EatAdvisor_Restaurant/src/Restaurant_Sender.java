
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
/**
 * @author Matteo Moi 737574 Varese<br>
 * <br>
 * La classe si occupa di inviare al server i dati dell'utente per la sua iscrizione tramite socket <br>
 */
public class Restaurant_Sender {
	
	  static Socket socket;
	  private BufferedReader in;
	  private PrintWriter out;

	
	public Restaurant_Sender(Socket socket) throws IOException {
		
		this.socket = socket;
		this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    this.out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
	    out.println("Restaurant");
	}
	

	/**
	 * il metodo invia i dati ottenuti al server
	 * @param tupla e' una stringa contenente i dati inseriti dall'utente nella gui per l'iscrizione
	 * @throws IOException
	 */
	public void send_data(String tupla) throws IOException {
		System.out.println(socket);
		System.out.println(tupla);
		out.print(tupla);
		out.flush();
		socket.close();
	}
	
}
