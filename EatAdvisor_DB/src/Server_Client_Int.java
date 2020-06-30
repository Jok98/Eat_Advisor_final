
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Server_Client_Int extends Remote {
	public ArrayList<String> get_restaurant() throws RemoteException;
}
