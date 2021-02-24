import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class TimeClient {

	private final InetAddress DATEGROUP;
	private final int DATEGROUP_PORT;
	private final int BUFFER_SIZE = 28;
	
	public TimeClient(String address, int port) throws UnknownHostException, IllegalArgumentException{
		
		this.DATEGROUP = InetAddress.getByName(address);
		if(!this.DATEGROUP.isMulticastAddress())
			throw new IllegalArgumentException();
		this.DATEGROUP_PORT = port;
		
	}
	
	/*	
	 * 	Client si unisce al gruppo multicast 'dategroup';
	 * 	Riceve per 10 volte dal server la data e l' ora attuale;
	 * 	Stampa a schermo quando ricevuto e poi termina.
	 */
	public void Start() {
		
		try( MulticastSocket multicastGroup = new MulticastSocket(DATEGROUP_PORT) ){
			
			/*	Ci uniamo al gruppo multicast	*/
			multicastGroup.joinGroup(DATEGROUP);
			
			/*	Creazione buffer ricezione	*/
			byte[] rcvBuffer = new byte[BUFFER_SIZE];
			DatagramPacket rcvPacket = new DatagramPacket(rcvBuffer, rcvBuffer.length);
			
			/*	Per 10 volte riceviamo e stampiamo la data e l' ora dal server	*/
			for(int i = 0; i < 10; i++) {
				
				multicastGroup.receive(rcvPacket);
				System.out.println("Richiesta Client #" + i + " : " + new String(rcvPacket.getData(), rcvPacket.getOffset(), rcvPacket.getLength()));
				
			}
			
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
}
