import java.util.Vector;
import java.util.concurrent.locks.*;

public class LaboratorioInformatica {
	private final int numComputer = 21;	/*	Vector parte da 0 ma i computer sono numerati da 1 a 20, il computer 0 non verrà mai usato	*/
	Vector<Integer> listaComputer;	/*	Vector che mi da infomrazioni sullo stato di occupazione di un computer, se 1 occupato se 0 libero	*/
	
	/*	Definzione lock e condition variables	*/
	final ReentrantLock lockLab;
	final Condition postoLibero;
	final Condition postoRiservato;
	final Condition tuttiLiberi;
	
	public LaboratorioInformatica() {
		this.listaComputer = new Vector<Integer>(numComputer);
		lockLab = new ReentrantLock();
		postoLibero = lockLab.newCondition();
		postoRiservato = lockLab.newCondition();
		tuttiLiberi = lockLab.newCondition();
	}
	
	public void inizializzaArray() {	/*	Inizializzo l'Array settendo tutti gli elementi a 0	*/
		for(int i = 0; i < numComputer; i++)
			listaComputer.add(i, 0);
	}
	
	public void occupaTutti() {
		for(int i = 0; i < numComputer; i++) {
			listaComputer.set(i, 1);
		}
	}
	
	public void liberaTutti() {
		for(int i = 0; i < numComputer; i++) {
			listaComputer.set(i, 0);
		}
	}
	
	public void occupaComputer(int indice) {
		listaComputer.set(indice, 1);
	}
	
	public void liberaComputer(int indice) {
		listaComputer.set(indice, 0);
	}
	
	public boolean isFree(int indice) {
		if(listaComputer.get(indice) == 0)
			return true;
		else return false;
	}
	
	public boolean isAllFree() {
		for(int i = 0; i < numComputer; i++) {
			if(listaComputer.get(i) == 1)
				return false;
		}
		return true;
	}
	
	/*	(Studenti ONLY) nel caso chiedano un computer gli passo il primo libero	*/
	public int primoLibero() {
		for(int i = 1; i < numComputer; i++) {
			if(listaComputer.get(i) == 0)
				return i;
		}
		return -1;	/*	Nessun computer è disponibile al momento	*/
	}

}
