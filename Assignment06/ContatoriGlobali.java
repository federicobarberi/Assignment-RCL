
/*
 * 	ContatoriGlobali:
 * 		Classe che modella un insieme di contatori globali.
 * 		Un contatore per ogni causale(5 in tutto) che tengono conto delle occorrenze delle causali nel file JSON.
 * 		Per l' aggiornamento si accede via synchronized dato che ci deve lavorare un pool di thread e l incremento non è atomico.
 */

public class ContatoriGlobali {
	
	private int numBonifici;
	private int numAccrediti;
	private int numBollettini;
	private int numF24;
	private int numPagoBancomat;
	
	public ContatoriGlobali() {
		
		this.numBonifici 	 = 0;
		this.numAccrediti 	 = 0;
		this.numBollettini 	 = 0;
		this.numF24 		 = 0;
		this.numPagoBancomat = 0;
		
	}
	
	public synchronized void aggiornaContatore(String causale) {
		
		switch(causale) {
			case "Bonifico" 	: this.numBonifici++; 		break;
			case "Accredito" 	: this.numAccrediti++; 		break;
			case "Bollettino" 	: this.numBollettini++; 		break;
			case "F24" 			: this.numF24++; 			break;
			case "PagoBancomat" : this.numPagoBancomat++; 	break;
		}
		
	}
	
	public void stampaRisultati() {
		
		System.out.println("********* RISULTATO *********");
		System.out.println("Numero Bonifici     : " + this.numBonifici);
		System.out.println("Numero Accrediti    : " + this.numAccrediti);
		System.out.println("Numero Bollettini   : " + this.numBollettini);
		System.out.println("Numero F24          : " + this.numF24);
		System.out.println("Numero PagoBancomat : " + this.numPagoBancomat);
		
	}
}
