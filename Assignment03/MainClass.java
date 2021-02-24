import java.util.Scanner;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.Comparator;

public class MainClass {
	public static void main(String[] args) {
		final int n_studenti;
		final int n_tesisti;
		final int n_professori;
		Scanner myInput = new Scanner(System.in);

		/*	Raccolta dati in input	*/
		System.out.println("Inserire numero studenti : ");
		n_studenti = myInput.nextInt();
		System.out.println("\nInserire numero tesisti : ");
		n_tesisti = myInput.nextInt();
		System.out.println("\nInserire numero professori : ");
		n_professori = myInput.nextInt();
		myInput.close();

		int dimQueue = n_studenti + n_tesisti + n_professori;
		/*
			*	Il tutor viene implementato come una coda di priorità dove,
			*	mano a mano che vengono creati gli utenti li ordina in coda per ordine di priorità,
			* successivamente li lancia seguendo questo ordine.
		*/
		Comparator<Utente> prioritySorter = new Comparator<Utente>() {
			public int compare(Utente ut1, Utente ut2) {
				return ut1.getPriorita() - ut2.getPriorita();
			}
		};
		PriorityBlockingQueue<Utente> tutor = new PriorityBlockingQueue<Utente>(dimQueue, prioritySorter);
		LaboratorioInformatica poloMarzotto = new LaboratorioInformatica();
		poloMarzotto.inizializzaArray();

		/*	Creazione professori, tesisti e docenti	*/
		int kEsecuzioni;	/*	Numero di volte che ogni persona prova ad accedere al laboratorio, lo genero casualmente tra 0 e range per i test	*/
		int range = 11;		/*	Al variare di range aumento l intervallo con cui si estrae il numero casuale, per i test fissato il range tra 0 e 10 (estremi inclusi)	*/
		for(int i = 0; i < n_professori; i++) {
			kEsecuzioni = (int)(Math.random()*range);
			Utente professore = new Utente("professore"+i, 1, kEsecuzioni, poloMarzotto);
			tutor.put(professore);
		}

		for(int i = 1; i <= n_tesisti; i++) {
			kEsecuzioni = (int)(Math.random()*range);
			Utente tesista = new Utente("tesista"+i, 2, i%20, kEsecuzioni,  poloMarzotto);	/*	Operazione modulo 20 perche i computer sono 20 e i tesisti possono essere di piu	*/
			tutor.put(tesista);
		}

		for(int i = 0; i < n_studenti; i++) {
			kEsecuzioni = (int)(Math.random()*range);
			Utente studente = new Utente("studente"+i, 3, kEsecuzioni,  poloMarzotto);
			tutor.put(studente);
		}

		/*	Lancio i thread in ordine di priorita	*/
		while(tutor.size() != 0) {
			tutor.poll().start();
		}
	}
}
