import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.*;
import java.nio.file.Paths;

public class MainClass {
	
	/*	
	 * 	Classe che modella un server HTTP che gestisce richieste di trasferimento di file di diverso tipo.
	 * 	How to test: localhost:6789/filename.ext
	 * 	DEBUG: Se da l'errore "address already used (Bind Failed)"
	 * 		   Kill process Terminal: kill <pid>
     *		   Find pid: Terminal: lsof -i:<port>		
	 */	
	
	public static void main(String[] args){
		
		/*	PREMESSA: Alcune osservazioni per il corretto funzionamento del programma	*/
		Premessa();
		
		/*	Creazione di porta e socket per ascoltare le richieste del client	*/
		final int port = 6789;
		try( ServerSocket listenSocket = new ServerSocket(port); ){
			
			/*	Mettiamo il server in attesa di richieste	*/
			while(true) {
				
				/*	Creazione della socket per gestire la richiesta del client, reader e writer per lettura richieste e scrittura risposte	*/
				try(
					Socket connectionSocket = listenSocket.accept();
					BufferedReader reader = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
					OutputStream writer = connectionSocket.getOutputStream();	
				){
					
					/*	Recuperiamo la richiesta	*/
					String request = reader.readLine();
					
					if(request == null)
						continue;
					
					/*	HTTP REQUEST: GET URL HTTP/1.	*/
					if(request.startsWith("GET")) {
						
						/* Parsiamo la richiesta per ottenere il nome del file	*/
						String[] parsed = request.split("\\s");
						
						/*	Dopo il GET recupero il filename dalla richiesta http, sarà al secondo posto dopo method, e recuperiamo il path	*/
						String path = System.getProperty("user.dir") + parsed[1];
						File file = new File(path);
						
						/*	Se file esiste, e non è una directory (cioè digito solo localhost:6789/) mostro il suo contenuto	*/
						if(file.exists() && !file.isDirectory()) {
							String responseHeader = MyHTTPResponse("OK", file.length(), path);
							writer.write(responseHeader.getBytes());
							VisualizzaFile(path, writer);
							writer.flush();
						}
						
						/*	Altrimenti creo una risposta http di errore passando "FNF" che starebbe per FileNotFound	*/
						else {
							String response = MyHTTPResponse("FNF", 0, null);
							writer.write(response.getBytes());
							writer.flush();
						}
					}
					
					/*	Il server non si occupa di questa richiesta	*/
					else{
						String response = MyHTTPResponse("ERR", 0, null);
						writer.write(response.getBytes());
						writer.flush();
					}	
				}
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/*	Osservazioni per il funzionamento del programma	*/
	public static void Premessa() {
		System.out.println("Per funzionare correttamente i file devono stare nella cartella " + System.getProperty("user.dir"));
		System.out.println("Browser riferimento : Google Chrome");
		System.out.println("Browser secondario : Safari, però molto piu lento che Chrome e quando si chiede la gif lancia l' eccezione Broken Pipe");
	}
	
	/*	Crea il messaggio di risposta HTTP, seguendo il protocollo, altrimenti sul browser non viene visualizzato niente	*/
	public static String MyHTTPResponse(String reasonPhrase, long length, String path) {
		
		String response = null;
		String FNF = "404 FILE NOT FOUND";
		String ERR = "405 THE METHOD IS NOT ALLOWED, ONLY GET PERMITTED";
		switch(reasonPhrase) {
		
			case "OK" 	: 	response = "HTTP/1.1 200 OK\r\n";
							response = response + "Content-Type: " + TypeResolver(path) + "\r\n";
							response = response + "Content-Lenght: " + length + "\r\n";
							response = response + "\r\n";
			break;
			
			case "FNF"	:	response = "HTTP/1.1 404 Not Found";
							response = response + "Content-Type: text/plain\r\n";
							response = response + "Content-Lenght: " + FNF.length() + "\r\n";
							response = response + "\r\n";
							response = response + FNF;
			break;
			
			default 	:	response = "HTTP/1.1 405 Method Not Allowed";
							response = response + "Content-Type: text/plain\r\n";
							response = response + "Content-Lenght: " + ERR.length() + "\r\n";
							response = response + "\r\n";
							response = response + ERR;
			break;
			
		}
		return response;
		
	}
	
	/*	Funzione che mi risolve il mime-type del file, senza specificare il content-type il browser non fa vedere niente	*/
	public static String TypeResolver(String path) {
		
		String mime = null;
		try {
			Path filePath = Paths.get(path);
			mime = Files.probeContentType(filePath);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		return mime;
		
	}
	
	/*	Funzione per la conversione del file in byte e scrittura sul writer, permette la visualizzazione del contenuto	*/
	public static void VisualizzaFile(String path, OutputStream writer) {
		
		try {
			FileInputStream fis = new FileInputStream(path);
			byte[] buf = new byte[1024];
			int read;
			while ((read = fis.read(buf)) != -1) 
				writer.write(buf, 0, read);
			fis.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
