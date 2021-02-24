import java.util.Scanner;

public class MainClass {
	public static void main(String[] args) {
		final int n_studenti;
		final int n_tesisti;
		final int n_professori;
		Scanner myInput = new Scanner(System.in);

		/*	Raccolta dati in input e, successivamente, chisura scanner	*/
		System.out.println("Inserire numero studenti : ");
		n_studenti = myInput.nextInt();
		System.out.println("\nInserire numero tesisti : ");
		n_tesisti = myInput.nextInt();
		System.out.println("\nInserire numero professori : ");
		n_professori = myInput.nextInt();
		myInput.close();

		/*	Creazione laboratorio e inizializzazione array di computer	*/
		LaboratorioInformatica poloMarzotto = new LaboratorioInformatica();
		poloMarzotto.inizializzaArray();

		/*	Creazione tutor	*/
		Tutor tutor = new Tutor(poloMarzotto);

		/*	Numero di volte che ogni persona prova ad accedere al laboratorio, lo genero casualmente tra 0 e 'range' per i test	*/
		int kEsecuzioni;

		/*	Al variare di range aumento l intervallo con cui si estrae il numero casuale, per i test fissato il range tra 0 e 10 (estremi inclusi)	*/
		int range = 11;

		/*	Creazione professori, tesisti e studenti, una volta creati vengono lanciati	*/
		for(int i = 0; i < n_professori; i++) {
			kEsecuzioni = (int)(Math.random()*range);
			Utente professore = new Utente("professore"+i, 1, kEsecuzioni, poloMarzotto, tutor);
			professore.start();
		}

		for(int i = 0; i < n_tesisti; i++) {
			kEsecuzioni = (int)(Math.random()*range);
			Utente tesista = new Utente("tesista"+i, 2, i%20, kEsecuzioni,  poloMarzotto, tutor);	//Operazione modulo 20 perche i computer sono 20 e i tesisti possono essere di piu
			tesista.start();
		}

		for(int i = 0; i < n_studenti; i++) {
			kEsecuzioni = (int)(Math.random()*range);
			Utente studente = new Utente("studente"+i, 3, kEsecuzioni,  poloMarzotto, tutor);
			studente.start();
		}
	}
}
