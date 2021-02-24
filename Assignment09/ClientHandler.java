import java.net.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;

public class ClientHandler {
	
	/*	Utility per comunicazione	*/
	private final int BUFFER_SIZE = 20; //Ottenuto da "PING N XXXXXXXXXXXXX".length()
	private final String SERVER_NAME;
	private final int SERVER_PORT;
	private final int TIMEOUT = 2000; //milliseconds
	
	/*	Utility per statistiche	*/
	private int LOSS_COUNTER = 0;
	private long MIN_RTT = Long.MAX_VALUE;
	private long MAX_RTT = 0;
	private float AVG_RTT = 0;
	
	/*	Costruttore di PingClient, prende in input serverName e serverPort	*/
	public ClientHandler(String serverName, int serverPort) {
		
		this.SERVER_NAME 	= serverName;
		this.SERVER_PORT 	= serverPort;
		
	}
	
	public void start() {	
		
		try( DatagramSocket client = new DatagramSocket() ){
			
			/*	Strumenti per l' invio	*/
			byte[] sendBuffer = new byte[BUFFER_SIZE];	
			InetAddress serverAddress = InetAddress.getByName(SERVER_NAME);
			
			/*	Strumenti per la ricezione	*/
			byte[] rcvBuffer = new byte[BUFFER_SIZE];
			DatagramPacket rcvPacket = new DatagramPacket(rcvBuffer, rcvBuffer.length);
			
			/*	10 invii di ping	*/
			for(int seqno = 0; seqno < 10; seqno++) {
				
				/*	Creazione PING da inviare	*/
				Date date = new Date();
				long timestamp = date.getTime();
				String PING = "PING " + seqno + " " + timestamp;
				
				/*	Creazione e invio pacchetto	*/
				sendBuffer = PING.getBytes();
				DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddress, SERVER_PORT);
				client.send(sendPacket);
				
				/*	Setto time-out e attendo ricezione	*/
				try {
					client.setSoTimeout(TIMEOUT);
					client.receive(rcvPacket);
					String resp = HandleResponse(PING,timestamp, 0);
					System.out.println(resp);
				}
				catch(SocketTimeoutException e) {
					String resp = HandleResponse(PING, timestamp, 1);
					System.out.println(resp);
					LOSS_COUNTER++;
				}
				
			}
			
			/*	Stampa statistiche	*/
			PrintStats();
			
		}		
		catch(BindException e) {
			System.out.println("Port already used");
			
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/*	Stampa il resoconto delle statistiche di PingClient	*/
	private void PrintStats() {
		
		/*	Formattazione float a 2 cifre decimali	*/
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		
		/*	Statistiche	*/
		int packetsReceived = 10 - LOSS_COUNTER;
		System.out.println();
		System.out.println("---- PING Statistics ----");
		System.out.println("10 packets transmitted, " 
						+ packetsReceived 
						+ " packets received, "
						+ (LOSS_COUNTER * 10) 
						+ "% packet loss");
		System.out.println("round-trip (ms) min/avg/max = "
						+ MIN_RTT
						+ "/"
						+ df.format(AVG_RTT/packetsReceived)
						+ "/"
						+ MAX_RTT);
		
	}
	
	/*	Formatta la risposta del PING per stamparla a schermo	*/
	private String HandleResponse(String ping, long timestamp, int check) {
		
		String result = null;
		Date date = new Date();
		long RTT = date.getTime() - timestamp;
		
		/*	Digressione per calcolo statistiche finali, prendo solo in considerazione i pacchetti ricevuti e non quando scade il timeout	*/
		if(check == 0) {
			if(RTT < MIN_RTT)
				MIN_RTT = RTT;
			if(RTT > MAX_RTT)
				MAX_RTT = RTT;
			AVG_RTT = AVG_RTT + RTT;
		}
		
		/*	Creazione messaggio da stampare	*/
		switch(check) {
		
			case 0 : result = ping + " RTT: " + RTT + " ms";	break;
			case 1 : result = ping + " RTT: " + '*';			break;
		}
		
		return result;
		
	}
	
}
