
public class Utente extends Thread{
	private String nome;
	private int priorita;
	private int kEsecuzioni;
	private int numComputer;
	LaboratorioInformatica lab;

	/*	Costruttore per professori e studenti a cui si puo assegnare un computer casuale	*/
	public Utente(String nome, int priorita, int k, LaboratorioInformatica l) {
		this.nome = nome;
		this.priorita = priorita;
		this.kEsecuzioni = k;
		this.numComputer = -1;	/*	Se è -1 vuol dire che va bene un qualsiasi computer libero	*/
		this.lab = l;
	}

	/*	Costruttore per tesisti che hanno il suo computer specifico	*/
	public Utente(String nome, int priorita, int numComputer, int k, LaboratorioInformatica l) {
		this.nome = nome;
		this.priorita = priorita;
		this.numComputer = numComputer;
		this.kEsecuzioni = k;
		this.lab = l;
	}

	/*	@Override	*/
	public void run() {
		for(int i = 0; i < kEsecuzioni; i++) {
			/*	GESTIONE PROFESSORE	*/
			if(this.priorita == 1) {
				lab.lockLab.lock();
				try {
					try {
						while(!lab.isAllFree())
							lab.tuttiLiberi.await();
					}
					catch(InterruptedException x) {
						x.printStackTrace();
					}
					lab.occupaTutti();
					try {
						System.out.println("Sono " + nome + " e mi metto a fare prove di rete\n");
						Long attesa = (long)(Math.random()*1000);
						lab.lockLab.unlock();
						Thread.sleep(attesa);
						lab.lockLab.lock();
					}
					catch(InterruptedException x) {
						x.printStackTrace();
					}
					System.out.println("Sono " + nome + " ed ho finito le prove di rete\n");
					lab.liberaTutti();
					/*	A seconda di chi ho in coda sulla condition variable risveglio per priorità	*/
					if(lab.lockLab.hasWaiters(lab.tuttiLiberi)) lab.tuttiLiberi.signal();
					else if(lab.lockLab.hasWaiters(lab.postoRiservato))	lab.postoRiservato.signal();
					else if(lab.lockLab.hasWaiters(lab.postoLibero))	lab.postoLibero.signal();
				}
				finally { lab.lockLab.unlock();}
			}

			/*	GESTIONE TESISTA	*/
			if(this.priorita == 2) {
				lab.lockLab.lock();
				try {
					try {
						while(!lab.isFree(numComputer))
							lab.postoRiservato.await();
					}
					catch(InterruptedException x) {
						x.printStackTrace();
					}
					lab.occupaComputer(numComputer);
					try {
						System.out.println("Sono " + nome + " e mi metto a fare la tesi al computer "+ numComputer + "\n");
						Long attesa = (long)(Math.random()*1000);
						lab.lockLab.unlock();
						Thread.sleep(attesa);
						lab.lockLab.lock();
					}
					catch(InterruptedException x) {
						x.printStackTrace();
					}
					System.out.println("Sono " + nome + " ed ho finito il mio compito\n");
					lab.liberaComputer(numComputer);
					/*	A seconda di chi ho in coda sulla condition variable risveglio per priorità	*/
					if(lab.lockLab.hasWaiters(lab.tuttiLiberi)) lab.tuttiLiberi.signal();
					else if(lab.lockLab.hasWaiters(lab.postoRiservato))	lab.postoRiservato.signal();
					else if(lab.lockLab.hasWaiters(lab.postoLibero))	lab.postoLibero.signal();
				}
				finally { lab.lockLab.unlock(); }
			}

			/*	GESTIONE STUDENTE	*/
			if(this.priorita == 3) {
				lab.lockLab.lock();
				try {
					numComputer = lab.primoLibero();
					try {
						while(numComputer == -1) {
							lab.postoLibero.await();
							numComputer = lab.primoLibero();
						}
					}
					catch(InterruptedException x){
						x.printStackTrace();
					}
					lab.occupaComputer(numComputer);
					try {
						System.out.println("Sono " + nome + " e mi metto a studiare al computer "+ numComputer + "\n");
						Long attesa = (long)(Math.random()*1000);
						lab.lockLab.unlock();
						Thread.sleep(attesa);
						lab.lockLab.lock();
					}
					catch(InterruptedException x) {
						x.printStackTrace();
					}
					System.out.println("Sono " + nome + " ed ho finito di studiare al computer " + numComputer + "\n");
					lab.liberaComputer(numComputer);
					/*	A seconda di chi ho in coda sulla condition variable risveglio per priorità	*/
					if(lab.lockLab.hasWaiters(lab.tuttiLiberi)) lab.tuttiLiberi.signal();
					else if(lab.lockLab.hasWaiters(lab.postoRiservato))	lab.postoRiservato.signal();
					else if(lab.lockLab.hasWaiters(lab.postoLibero))	lab.postoLibero.signal();

				}
				finally { lab.lockLab.unlock(); }
			}

			/*	Attesa casuale tra un accesso e il successivo	*/
			try {
				long attesa = (long)(Math.random()*1000);
				Thread.sleep(attesa);
			}
			catch(InterruptedException x) {
				x.printStackTrace();
			}
		}
	}

	/*	Funzioni di utilità	*/
	public String getNome() {
		return this.nome;
	}

	public int getPriorita() {
		return this.priorita;
	}
}
