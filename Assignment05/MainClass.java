import java.util.Scanner;
import java.io.File;

public class MainClass {
	public static void main(String[] args) {
		
		/*	Numero di consumatori, per i test ho usato 3	*/
		final int k = 3;	
		
		/*	Lettura da input del path di una directory di partenza	*/
		Scanner myInput = new Scanner(System.in);
		System.out.println("Inserire path di partenza di partenza : \n");
		final String path = myInput.nextLine();
		myInput.close();
		
		/*	Controlli sulla directory di partenza	*/
		File startDir = new File(path);
		
		if(!startDir.exists()) {
			System.out.println("Directory non trovata!\n");
			System.exit(-1);
		}
		
		if(!startDir.isDirectory()) {
			System.out.println("Non è stata passata una directory!\n");
			System.exit(-1);
		}
		
		/*	Creazione del buffer condiviso	*/
		Buffer buff = new Buffer();
		
		/*	Creazione e avvio thread produttore e k consumatori	*/
		Produttore prod = new Produttore(1, path, buff);
		prod.start();
		for(int i = 1; i <= k; i++) {
			Consumatore cons = new Consumatore(i, buff);
			cons.start();
		}
		
	}
}
