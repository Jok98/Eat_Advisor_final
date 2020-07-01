import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * @author Matteo Moi 737574 Varese<br>
 *La classe implementa l'interfaccia Server_Client_Int e estende UnicastRemoteObject</br>
 *cosi' da permettere l'utilizzo remoto del metodo get_restaurant() che ritorna una lista dei ristoranti(nome+tutti gli altri attributi)</br>
 *dopo che viene effettuata una ricerca sul DB con i dati inviati dal client</br>
 *
 */
public class EA_Server_Skeleton extends UnicastRemoteObject implements Server_Client_Int  {
	private static final long serialVersionUID = 1L;
	static EA_DB db = new EA_DB();
	
	protected EA_Server_Skeleton() throws RemoteException {super();}

	@SuppressWarnings("static-access")
	@Override
	//in caso di errore cambiare db in EA_DB
	public ArrayList<String> get_restaurant() throws RemoteException {return db.restaurant_list;}
	
	
}
