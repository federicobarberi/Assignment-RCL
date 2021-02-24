
public class PingClient {

	/*
	 * 	USAGE	   : java PingClient hostname port
	 * 	INPUT      : hostname port
	 * 	ERR	  	   : ERR -arg x (x numero argomento errato)
	 * 	PROTOCOLLO : UDP
	 */
	
	public static void main(String[] args) {
		
		String serverName = null;
		int serverPort = -1;
		
		/*	Controllo sull' input ricevuto	*/
		if(args.length == 2) {
			
			try {
				serverName = args[0];
			}
			catch(Exception e) {
				System.out.println("ERR - arg 1");
				System.exit(-1);
			}
			
			try {
				serverPort = Integer.parseInt(args[1]);
			}
			catch(NumberFormatException e) {
				System.out.println("ERR - arg 2");
				System.exit(-1);
			}
			
		}
		else {
			System.out.println("Usage: java PingClient hostname port");
			System.exit(-1);
		}
		
		/*	Creazione e avvio PingClient	*/
		ClientHandler client = new ClientHandler(serverName, serverPort);
		client.start();
		
	}
	
}
