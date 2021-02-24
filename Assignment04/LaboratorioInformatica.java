import java.util.Vector;

public class LaboratorioInformatica {
	/*	Numero di computer presenti nel laboratorio	*/
	private final int totComputer = 20;	
	
	/*	Vector che mi da informazioni sullo stato di occupazione di un computer, se 1 occupato se 0 libero	*/
	Vector<Integer> listaComputer;	
	
	public LaboratorioInformatica() {
		this.listaComputer = new Vector<Integer>(totComputer);
	}
	
	/*	Resituisce il numero di computer presenti nel laboratorio	*/
	public int getTotComp() {
		return this.totComputer;
	}
	
	/*	Inizializzo l'Array settendo tutti gli elementi a 0	*/
	public void inizializzaArray() {	
		for(int i = 0; i < totComputer; i++)
			listaComputer.add(i, 0);
	}
	
	/*	Setta a 1 tutti i computer, rendendo tutto il laboratorio occupato	*/
	public void occupaTutti() {
		for(int i = 0; i < totComputer; i++) {
			listaComputer.set(i, 1);
		}
	}
	
	/*	Setta a 0 tutti i computer, rendendo tutto il laboratorio libero	*/
	public void liberaTutti() {
		for(int i = 0; i < totComputer; i++) {
			listaComputer.set(i, 0);
		}
	}
	
	/*	Occupa il computer numero 'indice'	*/
	public void occupaComputer(int indice) {
		listaComputer.set(indice, 1);
	}
	
	/*	Libera il computer numero 'indice'	*/
	public void liberaComputer(int indice) {
		listaComputer.set(indice, 0);
	}
	
	/*	Restituisce:
	 * 		TRUE  -> se il computer numero 'indice' è libero;
	 * 		FALSO -> se il computer numero 'indice' è occupato;
	 */
	public boolean isFree(int indice) {
		if(listaComputer.get(indice) == 0)
			return true;
		else return false;
	}
	
	/*	Restituisce:
	 * 		TRUE  -> se tutti i computer sono liberi;
	 * 		FALSO -> altrimenti;
	 */
	public boolean isAllFree() {
		for(int i = 0; i < totComputer; i++) {
			if(listaComputer.get(i) == 1)
				return false;
		}
		return true;
	}
	
	/*	(Studenti ONLY) nel caso chiedano un computer gli passo il primo libero	*/
	public int primoLibero() {
		for(int i = 0; i < totComputer; i++) {
			if(listaComputer.get(i) == 0)
				return i;
		}
		
		/*	Nessun computer è disponibile al momento	*/
		return -1;	
	}

}