import java.util.Scanner;

public class MainClass {
	public static void main(String[] args) {
		
		/*	Raccolta informazioni dati in ingresso	*/
		Scanner myInput = new Scanner(System.in);
		System.out.println("Insert accuracy : ");
		double accouracy = myInput.nextDouble();
		System.out.println("\nInsert timing : ");
		long timing = myInput.nextLong();

		
		/*	Creazione thread e lancio	*/
		Pigreco pigreco = new Pigreco(accouracy);
		pigreco.start(); 
		
		/*	Controllo tempo di attesa	*/
		try {
			pigreco.join(timing);
		}
		catch(InterruptedException x){
			System.out.println("Join interrotta...");
		}
		
		/*	Controllo se il thread è sempre attivo dopo lo scadere del tempo e, nel caso lo termino	*/
		if(pigreco.isAlive()) {
			System.out.println("Tempo scaduto termino il thread\n");
			pigreco.interrupt();
		}
		else System.out.println("Thread terminato normalmente\n");
		
		myInput.close();
	}
}
