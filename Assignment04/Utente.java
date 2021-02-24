
public class Utente extends Thread{
	private String nome;
	private int priorita;
	private int kEsecuzioni;
	private int numComputer;
	protected Tutor t;


	/*	Costruttore per professori e studenti a cui si puo assegnare un computer casuale	*/
	public Utente(String nome, int priorita, int k, LaboratorioInformatica l, Tutor tu) {
		this.nome = nome;
		this.priorita = priorita;
		this.kEsecuzioni = k;
		this.numComputer = -1;		//Se è -1 vuol dire che va bene un qualsiasi computer libero
		this.t = tu;
	}

	/*	Costruttore per tesisti che hanno il suo computer specifico	*/
	public Utente(String nome, int priorita, int numComputer, int k, LaboratorioInformatica l, Tutor tu) {
		this.nome = nome;
		this.priorita = priorita;
		this.numComputer = numComputer;
		this.kEsecuzioni = k;
		this.t = tu;
	}

	/*	@Override:
	 * 		L'utente tenta l' accesso al laboratorio k volte (k casuale);
	 * 		Prima di accedere l'utente fa richiesta a un tutor che gestisce l'accesso;
	 * 		Si aspetta un tempo casuale per simulare la permanenza in laboratorio;
	 * 		L'utente una volta terminato, fa richiesta al tutor per liberare la postazione;
     */
	public void run() {
		for(int i = 0; i < kEsecuzioni; i++) {
			/*	L'utente chiede l'accesso per il laboratorio al Tutor	*/
			t.utenteChiedeLab(this);

			/*	Attesa casuale di permanenza in laboratorio	*/
			try {
				Long attesa = (long)(Math.random()*1000);
				Thread.sleep(attesa);
			}
			catch(InterruptedException x) {
				x.printStackTrace();
			}

			/*	L'utente ha finito il suo compito e decide di lasciare il laboratorio	*/
			t.utenteLiberaLab(this);

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

	/*	Resitituisce il nome dell' Utente	*/
	public String getNome() {
		return this.nome;
	}

	/*	Resituisce la priorità dell' Utente:
	 *  1 -> professore;
	 *  2 -> tesista;
	 *  3 -> studente;
	 */
	public int getPriorita() {
		return this.priorita;
	}

	/*	Resituisce il numero di computer che ha utilizzato l' Utente	*/
	public int getNumComputer() {
		return this.numComputer;
	}

	/*	(Studenti ONLY) Assegno all'utente come numero computer il computer numero 'num'
	 * 	Usato solo nel tutor per assegnare allo studente il primo computer libero, se c'è.
	 */
	public void setComputer(int num) {
		this.numComputer = num;
	}
}
