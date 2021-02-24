import java.util.concurrent.atomic.*;

public class Tutor {
	final private LaboratorioInformatica lab;

	/*	Definizione object su cui chiamare la synchronized	*/
	final private Object postoLibero;
	final private Object[] postoRiservato;
	final private Object tuttiLiberi;

	/*	Contatori atomici thread-safe per capire quanti professori/tesisti sono in attesa su una condizione	*/
	AtomicInteger atomicProf;
	AtomicInteger[] atomicTes;

	public Tutor(LaboratorioInformatica l) {
		this.lab = l;

		/*	Creo gli oggetti su cui sincronizzarmi	*/
		tuttiLiberi = new Object();
		postoLibero = new Object();
		postoRiservato = new Object[lab.getTotComp()];
		for(int i = 0; i < lab.getTotComp(); i++) {
			postoRiservato[i] = new Object();
		}

		/*	Creo i contatori atomici	*/
		atomicTes  = new AtomicInteger[lab.getTotComp()];
		for(int i = 0; i < lab.getTotComp(); i++) {
			atomicTes[i] = new AtomicInteger();
		}
		atomicProf = new AtomicInteger();
	}

	/*	Un utente chiede l' accesso al laboratorio al tutor, gli utenti sono di 3 tipi:
	 * 		professori -> priorità 1;
	 * 		tesisti    -> priorità 2;
	 * 		studenti   -> priorità 3;
	 *  A seconda del tipo di utente i posti del laboratorio vengono gestiti in modo diverso.
	 *  Questa funzione si occupa dell utente che fa delle operazioni al computer.
	 */
	public void utenteChiedeLab(Utente ut) {

		/*	GESTIONE PROFESSORE	*/
		if(ut.getPriorita() == 1) {
			synchronized(tuttiLiberi) {
				/*	Se non ho tutti i computer a mia disposizione mi interrompo	*/
				while(!lab.isAllFree()) {
					try {
						atomicProf.incrementAndGet();	//Un professore in piu in attesa sulla condizione di tutti liberi
						tuttiLiberi.wait();
					}
					catch(InterruptedException x) {
						x.printStackTrace();
					}
				}

				/*	Ho finalmente ottenuto l'accesso, posso eseguire il mio compito, occupando i/il computer	*/
				lab.occupaTutti();
				System.out.println("Sono " + ut.getNome() + " e mi metto a fare prove di rete\n");
			}
		}

		/*	GESTIONE TESISTA	*/
		if(ut.getPriorita() == 2) {
			synchronized(postoRiservato[ut.getNumComputer()]) {
				/*	Se il mio computer non è libero oppure c'è qualcuno di maggior priorità in attesa mi interrompo	*/
				while(!lab.isFree(ut.getNumComputer()) || atomicProf.get() != 0) {
					try {
						atomicTes[ut.getNumComputer()].incrementAndGet();
						postoRiservato[ut.getNumComputer()].wait();
					}
					catch(InterruptedException x) {
						x.printStackTrace();
					}
				}

				/*	Ho finalmente ottenuto l'accesso, posso eseguire il mio compito, occupando i/il computer	*/
				lab.occupaComputer(ut.getNumComputer());
				System.out.println("Sono " + ut.getNome() + " e mi metto a fare la tesi al computer "+ ut.getNumComputer() + "\n");
			}
		}

		/*	GESTIONE STUDENTE	*/
		if(ut.getPriorita() == 3) {
			synchronized(postoLibero) {
				ut.setComputer(lab.primoLibero());
				/*	Se non ci sono computer liberi o c'è qualcuno di maggior priorità in attesa mi interrompo	*/
				while(atomicProf.get() != 0 || ut.getNumComputer() == -1) {
					try {
						postoLibero.wait();
					}
					catch(InterruptedException x){
						x.printStackTrace();
					}
					ut.setComputer(lab.primoLibero());
				}

				/*	Ho finalmente ottenuto l'accesso, posso eseguire il mio compito, occupando i/il computer	*/
				lab.occupaComputer(ut.getNumComputer());
				System.out.println("Sono " + ut.getNome() + " e mi metto a studiare al computer "+ ut.getNumComputer() + "\n");
			}
		}
	}

	/*	Funzione che si occupa di quando un utente decide di lasciare il laboratorio;
	 *  Il tutor deve quindi risvegliare i thread in attesa secondo l' ordine di priorità.
	 */
	public void utenteLiberaLab(Utente ut) {

		/*	GESTIONE RILASCIO PROFESSORE	*/
		if(ut.getPriorita() == 1) {
			synchronized(tuttiLiberi) {
				System.out.println("Sono " + ut.getNome() + " ed ho finito le prove di rete\n");
				lab.liberaTutti();

				/*	Risveglio i professori, se ci sono	*/
				if(atomicProf.get() != 0) {
					atomicProf.decrementAndGet();
					tuttiLiberi.notify();
				}
				else{
					/*	Risveglio i tesisti, se ci sono	*/
					for(int i = 0; i < lab.getTotComp(); i++) {
						/*	Richiamo il synchronized altrimenti non posso fare la notify
						 *  su un oggetto di cui non sono prorpietario (IllegalMonitorStateException)
						 */
						synchronized(postoRiservato[i]) {
							if(atomicTes[i].get() != 0)
								atomicTes[i].decrementAndGet();
							postoRiservato[i].notify();
						}
					}

					/*	Richiamo il synchronized altrimenti non posso fare la notify
					 *  su un oggetto di cui non sono prorpietario (IllegalMonitorStateException)
					 */
					synchronized(postoLibero) {
						postoLibero.notifyAll();	//Alla fine risveglio tutti gli studenti
					}
				}
			}

		}

		/*	GESTIONE RILASCIO TESISTA	*/
		if(ut.getPriorita() == 2) {
			synchronized(postoRiservato[ut.getNumComputer()]) {
				System.out.println("Sono " + ut.getNome() + " ed ho finito il mio compito\n");
				lab.liberaComputer(ut.getNumComputer());

				/*	Risveglio i professori, se ci sono	*/
				if(atomicProf.get() != 0) {
					/*	Richiamo il synchronized altrimenti non posso fare la notify
					 *  su un oggetto di cui non sono prorpietario (IllegalMonitorStateException)
					 */
					synchronized(tuttiLiberi) {
						atomicProf.decrementAndGet();
						tuttiLiberi.notify();
					}
				}
				else {
					/*	Risveglio i tesisti, se ci sono	*/
					if(atomicTes[ut.getNumComputer()].get() != 0) {
						atomicTes[ut.getNumComputer()].decrementAndGet();
						postoRiservato[ut.getNumComputer()].notify();
					}
					else {
						/*	Richiamo il synchronized altrimenti non posso fare la notify
						 *  su un oggetto di cui non sono prorpietario (IllegalMonitorStateException)
						 */
						synchronized(postoLibero) {
							postoLibero.notify();	//Alla fine risveglio gli studenti
						}
					}
				}
			}
		}

		/*	GESTIONE RILASCIO STUDENTE	*/
		if(ut.getPriorita() == 3) {
			synchronized(postoLibero) {
				System.out.println("Sono " + ut.getNome() + " ed ho finito di studiare al computer " + ut.getNumComputer() + "\n");
				lab.liberaComputer(ut.getNumComputer());

				/*	Risveglio i professori, se ci sono	*/
				if(atomicProf.get() != 0) {
					/*	Richiamo il synchronized altrimenti non posso fare la notify
					 *  su un oggetto di cui non sono prorpietario (IllegalMonitorStateException)
					 */
					synchronized(tuttiLiberi) {
						atomicProf.decrementAndGet();
						tuttiLiberi.notify();
					}
				}
				else {
					/*	Risveglio i tesisti, se ci sono	*/
					if(atomicTes[ut.getNumComputer()].get() != 0) {
						/*	Richiamo il synchronized altrimenti non posso fare la notify
						 *  su un oggetto di cui non sono prorpietario (IllegalMonitorStateException)
						 */
						synchronized(postoRiservato[ut.getNumComputer()]) {
							atomicTes[ut.getNumComputer()].decrementAndGet();
							postoRiservato[ut.getNumComputer()].notify();
						}
					}
					else
						postoLibero.notify();	//Alla fine risveglio gli studenti
				}
			}
		}
	}
}
