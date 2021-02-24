import java.io.File;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/*	
 * 	JSONFileGenerator:
 * 		Classe che mi crea e riempe un file JSON utilizzando la funzione createFile che viene richiamata nel Main.
 */

public class JSONFileCreator {
	
	private String correntista;		//Format: "CorrentistaX"
	private String nomeFile;
	private int numeroCorrentisti;
	private int numeroMovimenti;
	private final int range = 51;	//Range per le varie generazioni di movimenti casuali
	
	public JSONFileCreator(String correntista, String nomeFile, int numeroCorrentisti) {
		this.correntista = correntista;
		this.nomeFile = nomeFile;
		this.numeroCorrentisti = numeroCorrentisti;
	}
	
	/*	Funzione richiamata nel main che crea il file JSON e lo popola	*/
	public void createFile() {
		
		/*	Creazione stringa che mi rappresenta l' Array JSON poi da scrivere sul file	*/
		String toWrite = this.createJsonString();
		
		/*	Creazione file	*/
		this.createJsonFile(nomeFile);
		
		/*	Scrittura dell Array JSON sul file utilizzando Java NIO	*/
		this.myNioInteraction(nomeFile, toWrite);
		
	}
	
	/*	Funzione che gestisce l'interazione con il file usando NIO, scrive la stringa 'toWrite' sul file 'nomeFile'	*/
	private void myNioInteraction(String nomeFile, String toWrite) {
		
		try(
			FileChannel outChannel = FileChannel.open(Paths.get(nomeFile), StandardOpenOption.WRITE)
		) {
				
			ByteBuffer b = ByteBuffer.wrap(toWrite.getBytes("UTF-8"));
				
			while(b.hasRemaining())
				outChannel.write(b);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/*	Funzione che mi controlla se esiste gia un file con lo stesso nome e nel caso lo elimina, poi crea il file.json di nome 'nomeFile'	*/
	private void createJsonFile(String nomeFile) {
		
		File file = new File(nomeFile);
		try {
			if(file.exists())
				file.delete();
			file.createNewFile();
		}
		catch(IOException e) {
			e.printStackTrace();
		}	
	}
	
	/*	Funzione che restituisce una stringa di un array JSON che andrà scritta sul file usando NIO	*/
	private String createJsonString() {
		
		/*	Creazione di Array JSON che rappresentano i Conti Correnti	*/
		ObjectMapper objectMapper = new ObjectMapper();
		String json = null;
		ArrayNode arrayCorrentisti = objectMapper.createArrayNode();
		
		for(int i = 0; i < numeroCorrentisti; i++) {
			ObjectNode corr = objectMapper.createObjectNode();
			corr.put("correntista", correntista+i);
		
			this.numeroMovimenti = (int)(Math.random() * range) + 1; 
			ArrayNode arrayMovimenti = objectMapper.createArrayNode();
				
			for(int j = 0; j < numeroMovimenti; j++) {
				ObjectNode mov = objectMapper.createObjectNode();
								
				String data = this.creaDataCasuale();
				int codiceCausale = (int)(Math.random()*5);
				String causale = this.creaCausaleCasuale(codiceCausale);
					
				mov.put("data", data);
				mov.put("causale", causale);
					
				arrayMovimenti.add(mov);
			}
				
			corr.set("movimenti", arrayMovimenti);
			arrayCorrentisti.add(corr);
		}	
		
		/*	Sulla stringa json viene eseguita una formattazione leggibile per poi scriverla nel file	*/
		try {
			json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrayCorrentisti);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return json;
		
	}
	
	/*	Funzione che crea casualmente una data e ne restituisce la rappresentazione come stringa	*/
	private String creaDataCasuale() {
		int year =  2018 + (int)Math.round(Math.random() * (2020 - 2018));
		int month = (int)(Math.random()*11) + 1;
		int day = (int)(Math.random()*30) + 1;	
		String randomData = day + "/" + month + "/" + year;
		return randomData;
	}
	
	/*	Funzione che crea casualmente una causale tra le 5 possibili, utilizzando un codice creato random	*/
	private String creaCausaleCasuale(int codiceCausale) {
		String rCausale = null;
		
		switch(codiceCausale) {
			case 0:	rCausale = "Bonifico"; 	 	break;
			case 1: rCausale = "Accredito";	    break;
			case 2: rCausale = "Bollettino"; 	break;
			case 3: rCausale = "F24";		 	break;
			case 4: rCausale = "PagoBancomat";	break;
		}
		
		return rCausale;
	}
	
}
