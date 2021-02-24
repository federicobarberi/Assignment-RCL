import java.util.Iterator;
import com.fasterxml.jackson.databind.JsonNode;

/*
 * 	ThreadAggiornaContatori:
 * 		Classe che modella i thread del Pool;
 * 		Ad ogni thread viene passato un oggetto JSON che rappresenta i 'movimenti' di un certo Conto Corrente;
 * 		Ogni thread recupera tutte le causali dei movimenti, e le passa alla funzione per aggiornare i contatori delle occorrenze.
 */

public class ThreadAggiornaContatori implements Runnable{
	JsonNode objNode;
	ContatoriGlobali cg;
	
	public ThreadAggiornaContatori(JsonNode objNode, ContatoriGlobali cg) {
		this.objNode = objNode;
		this.cg = cg;
	}
	
	/*	@Override	*/
	public void run() {
		
		Iterator<JsonNode> it = objNode.iterator();
		while(it.hasNext()) {
			String toClean = it.next().get("causale").toString();
			String causale = this.eliminaDoppiApici(toClean);
			cg.aggiornaContatore(causale);
		}
		
	}
	
	/*	Funzione che mi elimina i doppi apici dalla stringa, che vengono aggiunti con il toString;
	 * 	Altrimenti non funziona lo switch case nella classe Contatori Globali.
	 */
	private String eliminaDoppiApici(String s) {
		
		char[] tmp = s.toCharArray();
		String causale = "";
		for(int i = 0; i < tmp.length; i++) {
			if(i != 0 && i != (tmp.length-1))
				causale = causale + tmp[i];
		}
		return causale;
		
	}
}
