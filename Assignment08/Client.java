import java.nio.*;
import java.nio.charset.StandardCharsets;
import java.nio.channels.*;
import java.net.*;
import java.io.IOException;

public class Client {
	
	final static int PORT = 5678;
	final static String[] SERVER_HELLO = { "Ciao", "Hello", "Salut", "Hola", "Hej", "Ola",  "Ahoj", "Namaste", "Privjet", "Ni hao", "Exit" };
	final static int RANGE = SERVER_HELLO.length;
	final static int BUFFER_CAPACITY = 1024;
	
	public static void main(String[] args) {
		
		try {
			
			/*	SET UP connessione con il server	*/
			SocketAddress address = new InetSocketAddress(PORT);
			SocketChannel client = SocketChannel.open(address);
			
			/*	Allocazione buffer	*/
			ByteBuffer buffer = ByteBuffer.allocate(BUFFER_CAPACITY);
			
			/*	Ciclo finche casualmente non esce la stringa 'Exit Server'	*/
			while(true) {
				
				String hello = RandomHello();
				//String test = IntensiveTest();
				System.out.println("CLIENT WRITE :: " + hello);
				
				/*	Comunichiamo la stringa al server	*/
				buffer = ByteBuffer.wrap(hello.getBytes());
				while(buffer.hasRemaining())
					client.write(buffer);
				buffer.clear();
				
				if(hello.equals("Exit Server")) {
					System.out.println("CLIENT ALERT :: Close connection with Server");
					break;
				}				
				else {
					
					/*	Riceviamo l' ECHO	*/
					boolean stop = false;
					String echoed = "";
					while(!stop) {
						
						int bytesRead = client.read(buffer);
						
						/*
						 *  Facendo il debug stampando ogni volta lo stato del buffer,
						 *  noto che vengono fatte varie letture, quando ne viene fatta una con un numero di byte 
						 *  minore stretto della buffer.capacity(), vuol dire che ho letto tutto e posso uscire.
						 */
						if(bytesRead == -1 || bytesRead < buffer.capacity())
							stop = true;
						
						buffer.flip();
						while(buffer.hasRemaining())
							echoed = echoed + StandardCharsets.UTF_8.decode(buffer).toString();
						buffer.clear();	
						
					}
					System.out.println("CLIENT READ  :: " + echoed);
					System.out.println("IS EQUALS?   :: " + StringCompare(hello, echoed));
					System.out.println();
					
				}
				
			}
			
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/*	DEBUG: fa il confronto tra la stringa inviata(Aggiungendo l' echo) e quella ricevuta, utile per IntesiveTest	*/
	private static boolean StringCompare(String str1, String str2) {
		String tmp = "Echoed by server : " + str1;
		return tmp.equals(str2);
	}
	
	/*	Funzione che scrive 'Ciao Server' in una lingua scelta casualmente	*/
	private static String RandomHello() {
		String randomHello = SERVER_HELLO[(int)(Math.random()*RANGE)] + " Server";
		return randomHello;
	}
	
	/*	Funzione per testare piu intensivamente le funzioni di lettura/scrittura, mandando stringhe di dimensione maggiore della capacità buffer Server	*/
	/*private static String IntensiveTest() {
		String test = "Lorem ipsum dolor sit amet, consectetur adipisci elit, sed do eiusmod tempor incidunt ut labore et!\n"
				    + "Lorem ipsum dolor sit amet, consectetur adipisci elit, sed do eiusmod tempor incidunt ut labore et!\n"
				    + "Lorem ipsum dolor sit amet, consectetur adipisci elit, sed do eiusmod tempor incidunt ut labore et!\n"
				    + "Lorem ipsum dolor sit amet, consectetur adipisci elit, sed do eiusmod tempor incidunt ut labore et!\n"
				    + "Lorem ipsum dolor sit amet, consectetur adipisci elit, sed do eiusmod tempor incidunt ut labore et!\n"
				    + "Lorem ipsum dolor sit amet, consectetur adipisci elit, sed do eiusmod tempor incidunt ut labore et!\n"
				    + "Lorem ipsum dolor sit amet, consectetur adipisci elit, sed do eiusmod tempor incidunt ut labore et!\n"
				    + "Lorem ipsum dolor sit amet, consectetur adipisci elit, sed do eiusmod tempor incidunt ut labore et!\n"
				    + "Lorem ipsum dolor sit amet, consectetur adipisci elit, sed do eiusmod tempor incidunt ut labore et!\n"
				    + "Lorem ipsum dolor sit amet, consectetur adipisci elit, sed do eiusmod tempor incidunt ut labore et!\n"
				  	+ "Lorem ipsum dolor sit amet, consectetur adipisci elit, sed do eiusmod tempor incidunt ut labore et!\n"
				  	+ "Lorem ipsum dolor sit amet, consectetur adipisci elit, sed do eiusmod tempor incidunt ut labore et!\n"
				  	+ "Lorem ipsum dolor sit amet, consectetur adipisci elit, sed do eiusmod tempor incidunt ut labore et!\n"
				  	+ "Lorem ipsum dolor sit amet, consectetur adipisci elit, sed do eiusmod tempor incidunt ut labore et!\n"
				  	+ "Lorem ipsum dolor sit amet, consectetur adipisci elit, sed do eiusmod tempor incidunt ut labore et!\n"
				  	+ "Lorem ipsum dolor sit amet, consectetur adipisci elit, sed do eiusmod tempor incidunt ut labore et!";
		return test;
	}*/
}
