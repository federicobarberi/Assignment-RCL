import java.net.*;
import java.io.IOException;

public class ServerHandler {

	/*	Utility per comunicazione	*/
	private final int BUFFER_SIZE = 20; //Ottenuto da "PING N XXXXXXXXXXXXX".length()
	private final String SERVER_NAME;
	private final int SERVER_PORT;
	
	/*	Costruttore di PingServer, prende in input serverPort	*/
	public ServerHandler(String serverName, int serverPort) {
		
		this.SERVER_NAME = serverName;
		this.SERVER_PORT = serverPort;
		
	}
	
	public void start() {
		
		try( DatagramSocket server = new DatagramSocket(SERVER_PORT) ){
			
			/*	Strumenti per la ricezione	*/
			byte[] rcvBuffer = new byte[BUFFER_SIZE];
			DatagramPacket rcvPacket = new DatagramPacket(rcvBuffer, rcvBuffer.length);
			
			/*	Strumenti per l' invio	*/
			byte[] sendBuffer = new byte[BUFFER_SIZE];
			
			while(true) {
				
				/*	Riceviamo il pacchetto	*/
				server.receive(rcvPacket);
				String rcv = new String(rcvPacket.getData());
				
				/*		
				 * 	Simulazione perdita pacchetti al 25% percento:
				 * 		Estraggo un numero casuale tra 0 e 3 (estremi inclusi);
				 * 		Ognuno equiprobabile, probabilità 25% ognuno;
				 * 		Se esce : 0, 1, 2 	invio;
				 * 		Se esce : 3			NON invio;
				 */
				int sent = (int)(Math.random()*4);
				
				/*	Invio pacchetto	*/
				if(sent < 3) {	
					
					/*	Attendiamo un tempo casuale, < del timeout del client, e inviamo la risposta	*/
					long delay = (long)(Math.random()*300);
					Thread.sleep(delay);
					
					/*	Creazione dell' echo	*/
					sendBuffer = rcv.getBytes();
					
					/*	Invio echo	*/
					DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, InetAddress.getByName(SERVER_NAME), rcvPacket.getPort());
					server.send(sendPacket);
					
					/*	Stampare resoconto invio	*/
					FormatOutput(rcv, 0, rcvPacket.getAddress(), rcvPacket.getPort(), delay);
				}
				
				/*	Simulazione perdita	*/
				else {	
					FormatOutput(rcv, 1, rcvPacket.getAddress(), rcvPacket.getPort(), 0);
				}
				
			}
			
		}
		catch(BindException e) {
			System.out.println("Port already used, try another one");
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/*	Stampiamo a schermo le informazioni del PING di risposta del server	*/
	private void FormatOutput(String ping, int sent, InetAddress clientAddress, int clientPort, long delay) {

		String display = clientAddress.toString() + ":" + clientPort + "> ";
		switch(sent) {
		
			case 0 : display = display + ping + " ACTION: delayed " + delay + " ms";	break; 
			case 1 : display = display + ping + " ACTION: not sent"; 					break;
			
		}
		System.out.println(display);
		
	}
	
}
