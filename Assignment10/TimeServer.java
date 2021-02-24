import java.io.IOException;
import java.net.*;
import java.util.Date;

public class TimeServer {

	private final InetAddress DATEGROUP;
	private final int DATEGROUP_PORT;
	private final long WAIT = 2000;
	
	public TimeServer(String address, int port) throws UnknownHostException, IllegalArgumentException{
		
		this.DATEGROUP = InetAddress.getByName(address);
		if(!this.DATEGROUP.isMulticastAddress())
			throw new IllegalArgumentException();
		this.DATEGROUP_PORT = port;
		
	}
	
	/*	
	 * 	Server manda su un gruppo multicast 'dategroup' la data e l'ora;
	 * 	Gli invii vengono effettuati a intervalli regolari di 2 secondi.
	 */
	public void Start() {
		
		try( DatagramSocket server = new DatagramSocket() ){
			
			while(true) {
				
				/*	Recupero la data e l ora attuale	*/
				Date date = new Date();
				String currentDate = date.toString();
				
				/*	Creazione e invio data e ora	*/
				DatagramPacket sendPacket = new DatagramPacket(currentDate.getBytes(), currentDate.length(), DATEGROUP, DATEGROUP_PORT);
				server.send(sendPacket);
				
				/*	Stampa lato server del messaggio inviato al gruppo	*/
				System.out.println("Server to DateGroup : " + new String(sendPacket.getData(), sendPacket.getOffset(), sendPacket.getLength()));
				
				/*	Attesa di 2 secondi prima del prossimo invio	*/
				Thread.sleep(WAIT);
			}
			
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}
