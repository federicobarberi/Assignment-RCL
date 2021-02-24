import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {

	final static int PORT = 5678;
	
	public static void main(String[] args) {
		
		try {
			
			/*	Creazione dell' istanza del congresso	*/
			Congress congressService = new Congress();
			
			/*	Esportazione dell' oggetto	*/
			CongressInterface stub = (CongressInterface) UnicastRemoteObject.exportObject(congressService, PORT);
			
			/*	Creazione di un registry sulla porta 'PORT'	*/
			LocateRegistry.createRegistry(PORT);
			Registry r = LocateRegistry.getRegistry(PORT);
			
			/*	Pubblicazione dello stub nel registry	*/
			r.rebind("CONGRESS", stub);
			
			System.out.println("Server ready");
			
		}
		catch(RemoteException e) {
			e.printStackTrace();
		}
		
	}
	
}
