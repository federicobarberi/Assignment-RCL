
public class PingServer {

	/*
	 * 	USAGE	   : java PingServer port
	 * 	INPUT      : port
	 * 	ERR	  	   : ERR -arg x (x numero argomento errato)
	 * 	PROTOCOLLO : UDP
	 */
	
	
	public static void main(String[] args) {
		
		final String serverName = "localhost";
		int serverPort = -1;
		
		/*	Controllo sull' input ricevuto	*/
		if(args.length == 1) {
			
			try {
				serverPort = Integer.parseInt(args[0]);
			}
			catch(NumberFormatException e) {
				System.out.println("ERR - arg 1");
				System.exit(-1);
			}
			
		}
		else {
			System.out.println("Usage: java PingServer port");
			System.exit(-1);
		}
		
		/*	Creazione e avvio PingServer	*/
		ServerHandler server = new ServerHandler(serverName, serverPort);
		server.start();
	}
}
