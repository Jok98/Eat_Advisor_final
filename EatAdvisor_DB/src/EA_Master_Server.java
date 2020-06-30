
import java.io.IOException;
import java.net.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
/**
 * @author Matteo Moi 737574 Varese<br>
 * la classe inizializza il socket e lo mette in attesa di una richiesta da parte del cliente
 * per ogni richiesta ricevuta crea un secondo server(Slave) che si occupa di gestire la singola richiesta
 *
 */
public class EA_Master_Server {
	public static final int PORT=8080;
	static Registry registry; 
	private static EA_DB db = new EA_DB();
	
	public static void main(String[] Args) throws IOException, SQLException {
		db.db_start_conn();
		db.new_table("CREATE TABLE Cliente(nome VARCHAR(20),  cognome VARCHAR(20),  comune_residenza VARCHAR(30), "
				+ "sigla_provincia_residenza VARCHAR(30), email VARCHAR(40), nickname VARCHAR(15), password VARCHAR(30) )");
		db.new_table("CREATE TABLE Restaurant(nome VARCHAR(20), comune VARCHAR(30), tipologia VARCHAR(20), indirizzo VARCHAR(30), cell VARCHAR(10), sito VARCHAR(40), commento VARCHAR(250) )");
		
		create_registry();
		create_single_server();
		}
	
	public static void create_single_server() throws IOException {
		ServerSocket server = new ServerSocket(PORT);
		try {
		      while (true) {
		        Socket socket = server.accept();
		        try {
		        System.out.println("nuovo Single_Server creato");
		          new EA_Single_Server(db,socket);
		        } catch (IOException e) {
		 
		        socket.close();
		      }
		    }
		    } finally {
		      server.close();
		    }
		
	}
	
	public static void create_registry() throws RemoteException {
		registry = LocateRegistry.createRegistry(1099);
		EA_Server_Skeleton skeleton = new EA_Server_Skeleton();
		registry.rebind("Ristorante",skeleton);
		
	}
}


