import java.net.UnknownHostException;

public class MainClassClient {

	private static String DATEGROUP;
	private static int DATEGROUP_PORT = 30000;
	
	public static void main(String[] args) {
		
		/*	Ricezione indirizzo IP da linea di comando	*/
		if(args.length == 1) {
			
			try {
				DATEGROUP = args[0];
			}
			catch(Exception e) {
				System.err.println("ERR - arg 1");
				System.exit(-1);
			}
			
		}
		
		else {
			System.err.println("Usage: java MainClassClient multicast_ip_address");
			System.exit(-1);
		}
		
		/*	Creiamo e avviamo il client	*/
		try {
			TimeClient timeClient = new TimeClient(DATEGROUP, DATEGROUP_PORT);
			timeClient.Start();
		}
		catch(UnknownHostException e) {
			System.err.println("L'indirizzo immesso non e' valido");
		}
		catch(IllegalArgumentException e) {
			System.err.println("L'indirizzo immesso non e' un indirizzo multicast");
		}
		
	}
	
}
