import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.charset.StandardCharsets;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Iterator;

/*
 * 	Reader:
 * 		Classe che modella un thread Reader.
 * 		Reader, utilizzando Java NIO, recupera il contenuto di un file JSON.
 * 		Successivamente passa i vari oggetti 'movimenti' contenuti nel file ai thread di un pool.
 */

public class Reader extends Thread{
	
	private String nomeFile;
	ThreadPool tp;
	ContatoriGlobali myCont;
	
	public Reader(String nomeFile, ThreadPool tp, ContatoriGlobali myCont) {
		this.nomeFile = nomeFile;
		this.tp = tp;
		this.myCont = myCont;
	}
	
	/*	@Override	*/
	public void run() {
		
		/*	Interazione con file tramite NIO	*/
		ObjectMapper objectMapper = new ObjectMapper();
		String toParse = this.stringToParse(objectMapper);

		try {
			
			/*	Utilizziamo l'iteratore per scorrere tutti gli oggetti del file JSON	*/
			Iterator<JsonNode> it = objectMapper.readTree(toParse).iterator();
			while(it.hasNext()) {
				ThreadAggiornaContatori th = new ThreadAggiornaContatori((JsonNode)it.next().get("movimenti"), myCont);
				tp.submit(th);
			}
			
			/*	Chiudo il ThreadPool e stampo il risultato	*/
			tp.closePool();
			myCont.stampaRisultati();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	/*	Funzione che utilizzando Java NIO va a leggere e recuperare il contenuto del file JSON 	*/
	private String stringToParse(ObjectMapper objectMapper) {
		
		/*	In toParse alla fine avro il contenuto del file JSON che andrò a parsare	*/
		String toParse = "";
		
		try(
			FileChannel inChannel= FileChannel.open(Paths.get(nomeFile), StandardOpenOption.READ)
		){
			/*	Allocazione buffer e variabile stop	*/
			ByteBuffer buffer = ByteBuffer.allocateDirect(1024*1024);
			boolean stop = false;
			
			while(!stop) {
				int bytesRead = inChannel.read(buffer);
				
				/*	Non ci sono piu dati da leggere ci fermiamo	*/
				if(bytesRead == -1)
					stop = true;
				
				/*	Cambio da scrittura a lettura	*/
				buffer.flip();
				
				/*	Scrittura sulla stringa il contenuto del file	*/
				while(buffer.hasRemaining())
					toParse = toParse + StandardCharsets.UTF_8.decode(buffer).toString();	
				
				buffer.clear();
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		return toParse;
		
	}
	
}
