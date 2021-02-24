import java.nio.*;
import java.nio.charset.StandardCharsets;
import java.nio.channels.*;
import java.net.*;
import java.util.*;
import java.io.IOException;

public class Server {
	
	final static int PORT = 5678;
	final static String ECHO = "Echoed by server : ";
	final static int BUFFER_CAPACITY = 1024;
	
	public static void main(String[] args){
		
		try(
			ServerSocketChannel serverChannel = ServerSocketChannel.open();
		){
			
			/*	SET UP Server, modalità non bloccante	*/
			ServerSocket ss = serverChannel.socket();
			InetSocketAddress address = new InetSocketAddress(PORT);
			ss.bind(address);
			serverChannel.configureBlocking(false);
			Selector selector = Selector.open();
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);
			System.out.println("SET UP COMPLETED, READY ON PORT " + PORT);
			
			/*	Attesa richieste	*/
			while(true) {
				
				if(selector.select() == 0)
					continue;
				
				/*	Recuperiamo l' insieme delle chiavi dei canali pronti, e iteriamo su essi	*/
				Set<SelectionKey> readyKeys = selector.selectedKeys();
				Iterator<SelectionKey> iterator = readyKeys.iterator();
				while(iterator.hasNext()) {
					
					SelectionKey key = iterator.next();
					iterator.remove();
					
					/*	Pronto per instaurare la connessione	*/
					if(key.isAcceptable()) {
						SetUpECHO(selector, key);
					}
					
					/*	Pronto per leggere	*/
					if(key.isReadable()) {
						if(ReadECHO(key) == 1)
							break;
					}
					
					/*	Pronto per scrivere	*/
					if(key.isWritable()) {
						WriteECHO(key);
					}
					
				}
				
			}
			
		}
		
		catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/*	SET UP connessione con il client e SET UP buffer	*/
	private static void SetUpECHO(Selector selector, SelectionKey key) throws IOException {
		
		ServerSocketChannel server = (ServerSocketChannel) key.channel();
		SocketChannel client = server.accept();
		client.configureBlocking(false);
		
		/*	Registro solo OP_READ (e non OP_READ | OP_WRITE) perche l' interazione inizierà sempre da una lettura	*/
		client.register(selector, SelectionKey.OP_READ, "");
		System.out.println("SERVER ALERT :: Client connected " + client.getRemoteAddress());
		
	}
	
	/*	Leggiamo ciò che ha scritto il client, se ritorna 1 vuol dire che il client ha chiuso la connessione	*/
	private static int ReadECHO(SelectionKey key) throws IOException{
		
		SocketChannel client = (SocketChannel)key.channel();
		String attachment = (String)key.attachment();
		ByteBuffer buffer = ByteBuffer.allocate(BUFFER_CAPACITY);
		buffer.clear();
		int bytesRead = client.read(buffer);
		
		/*	Raggiunta la fine dello stream la connessione viene chiusa	*/
		if(bytesRead == -1) {
			System.out.println("SERVER ALERT :: Close client on read");
			key.cancel();
			client.close();
			return 1;
		}
		
		/*	Leggiamo il contenuto del buffer, lo scriviamo nell attachment, e se abbiamo letto tutto, registriamo l' interesse di scrittura	*/
		else {
			
			buffer.flip();
			attachment = attachment + StandardCharsets.UTF_8.decode(buffer).toString();
			key.attach(attachment);
			
			/*	Se ricevo la stringa 'Exit Server' chiudo la connessione con il client	*/
			if(attachment.equals("Exit Server")) {
				System.out.println("SERVER ALERT :: Close client on 'Exit Server'");
				key.cancel();
				client.close();
				return 1;
			}
			
			/*	Se ho letto meno bytes della capacità del buffer, è stata l' ultima lettura quindi registro l' interesse di scritttura	*/
			if(bytesRead < BUFFER_CAPACITY) {
				System.out.println("SERVER READ  :: " + attachment);
				key.interestOps(SelectionKey.OP_WRITE);
			}
			return 0;

		}
		
	}
	
	/*	Recuperiamo la stringa letta, salvata nell attachment, aggiungiamo l' ECHO e la mandiamo al client	*/
	private static void WriteECHO(SelectionKey key) throws IOException{
		
		SocketChannel client = (SocketChannel)key.channel();
		ByteBuffer buffer = ByteBuffer.allocate(BUFFER_CAPACITY);
		String echo = ECHO + (String) key.attachment();
		System.out.println("SERVER WRITE :: "+ echo);
		buffer.clear();
		buffer = ByteBuffer.wrap(echo.getBytes());
		int bytesWrite = client.write(buffer);
		if(buffer.hasRemaining())
			return;
		
		/*	Se abbiamo scritto tutto resettiamo l' attachment, e registriamo un nuovo interesse in lettura	*/
		if(bytesWrite == echo.length()) {
			key.attach("");
			key.interestOps(SelectionKey.OP_READ);
			System.out.println("SERVER ALERT :: Waiting a new Readable key");
			System.out.println();
		}

	}
	
}
