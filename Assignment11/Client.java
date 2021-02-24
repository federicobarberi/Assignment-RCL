import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.rmi.RemoteException;

/*	
 * 	Client:
 * 		Recuperiamo l'oggetto remoto, successivamente mostriamo all utente un menu interattivo di operazioni da scegliere;
 * 		A seconda dell' operazione viene richiamata una funzione specifica (InserSpeaker, ShowCongressSchedule, RandomCongressPopulate);
 * 		Il client continua a ciclare finche non viene scelta dal menu l'operazione di 'exit' (#5).
 */

public class Client {

	final static int PORT = 5678;
	
	private static final String[] NOME = { 
			"Lucio", "Fabrizio", "Maurizio", "Cristiano",
			"Zlatan", "Ed", "Tyrion", "Zucchero", "Andrea",
			"Travis", "Machine Gun", "Pink", "David", "Antonello", "Luciano",
			"Vasco", "Canelo", "Mike", "Gerry", "Maria", "Bob", "Diego",
			"Sòcrates", "Notorius", "Cesare", "Carl", "Elton", "Paul"
	  	 };


	private static final String[] COGNOME = { 
			"Battisti", "De Andrè", "Costanzo", "Ronaldo",
			"Ibrahimovic", "Sheeran", "Lannister", "Fornaciari", "Pirlo",
			"Scott", "Kelly", "Floyd", "Bowie", "Venditti", "Ligabue",
			"Rossi", "Alvarez", "Tyson", "Scotti", "De Filippi", "Marley", "Maradona",
			"de Oliveira", "B.I.G", "Cremonini", "Brave", "Jhon", "Kalkbrenner"
		 };
	
	public static void main(String[] args) {
			
		try (Scanner in = new Scanner(System.in)){
			
			/*	Riferimento per il registro	*/
			Registry r = LocateRegistry.getRegistry(PORT);
			
			/*	Riferimento per l'oggetto	*/
			CongressInterface remoteCongress = (CongressInterface) r.lookup("CONGRESS");
			
			/*	Si cicla finche il client non decide di uscire, si mostra ad ogni iterazione un menu con cui il client puo scegliere un operazione da fare	*/
			boolean stop = false;
			while(!stop) {
				
				ShowMenu();
				int operation = Integer.parseInt(in.nextLine());
				String speaker = null;
				int day, session;
				
				switch(operation) {
				
					case 1:		
							System.out.println("Inserisci il nome dello speaker da registrare");
							speaker = in.nextLine();
							System.out.println("Specificare un giorno");
							day = Integer.parseInt(in.nextLine());
							System.out.println("Specificare una sessione");
							session = Integer.parseInt(in.nextLine());
							InsertSpeaker(speaker, day, session, remoteCongress);
					break;
					
					case 2:	
							RandomCongressPopulate(remoteCongress);		
					break;
					
					case 3: 
							ShowCongressSchedule(0, remoteCongress);	
					break;
					
					case 4:	
							System.out.println("Specificare un giorno");
							day = Integer.parseInt(in.nextLine());
							ShowCongressSchedule(day, remoteCongress);
					break;
					
					case 5: 
							stop = true; 
					break;	
					
					default: 
							System.out.println("Operazione inesistente, usa un numero tra 1 e 5!");	
					break;
					
				}
			}
			
			System.out.println("Exit selected, client close ...");
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/*	Registra un dato speaker ad una data sessione di un dato giorno	*/
	private static void InsertSpeaker(String speaker, int day, int session, CongressInterface remoteCongress) {
		
		try {
			
			if(remoteCongress.RegisterSpeaker(speaker, day, session))
				System.out.println("OK :: speaker : " + speaker + " giorno : " + day + " sessione : " + session);
			else
				System.out.println("ERROR :: speaker : " + speaker + " giorno : " + day + " sessione : " + session + " :: CAUSA : Speaker gia registrato");
			
		}
		catch(SessionUnavaibleException e) {
			System.out.println("ERROR :: speaker : " + speaker + " giorno : " + day + " sessione : " + session + " :: CAUSA : " + e);
		}
		catch(IllegalArgumentException e) {
			System.out.println("ERROR :: invalid parameters :: CAUSA " + e);
		}
		catch(RemoteException e) {
			System.out.println("Something goes wrong with remote call ...");
		}
	}
	
	/*	Stampa la programmazione di un dato giorno del congresso, se il giorno è 0 stampa la programmazione completa	*/
	private static void ShowCongressSchedule(int day, CongressInterface remoteCongress) {
		
		try {
			
			switch(day) {
			
				case 0: 
						System.out.println("\n::::::	PROGRAMMAZIONE DEL CONGRESSO ::::::\n");
						String congressSchedule = remoteCongress.GetSchedule();
						System.out.println(congressSchedule);
				break;
				
				case 1:	
						System.out.println("\n::::::	PROGRAMMAZIONE DEL CONGRESSO (Giorno 1) ::::::\n");
						String congressSchedule1 = remoteCongress.GetFirstDay();
						System.out.println(congressSchedule1);
				break;
				
				case 2:	
						System.out.println("\n::::::	PROGRAMMAZIONE DEL CONGRESSO (Giorno 2) ::::::\n");
						String congressSchedule2 = remoteCongress.GetSecondDay();
						System.out.println(congressSchedule2);
				break;
				
				case 3:	
						System.out.println("\n::::::	PROGRAMMAZIONE DEL CONGRESSO (Giorno 3) ::::::\n");
						String congressSchedule3 = remoteCongress.GetThirdDay();
						System.out.println(congressSchedule3);
				break;
				
				default:	
						System.out.println("Giorno inesistente, scegliere un giorno tra 1 e 3");
				break;
			
			}
		}
		catch(RemoteException e) {
			System.out.println("Something goes wrong with remote call ...");
		}	
		
	}
	
	/*	Creiamo speaker, giorni e sessioni casuali finche non abbiamo popolato tutto il congresso	*/
	private static void RandomCongressPopulate(CongressInterface remoteCongress) {
		
		try {
			
			while(!remoteCongress.IsCongressFull()) {
				
				String speaker = GetRandomSpeaker();
				int day = (int)((Math.random()*3) + 1);
				int session = (int) ((Math.random()*12) + 1);
				
				try {
					
					if(remoteCongress.RegisterSpeaker(speaker, day, session))
						System.out.println("OK :: speaker : " + speaker + " giorno : " + day + " sessione : " + session);
					else
						System.out.println("ERROR :: speaker : " + speaker + " giorno : " + day + " sessione : " + session + " :: CAUSA : Speaker gia registrato");
					
				}
				catch(SessionUnavaibleException e) {
					System.out.println("ERROR :: speaker : " + speaker + " giorno : " + day + " sessione : " + session + " :: CAUSA : " + e);
				}
				
			}

		}
		catch(RemoteException e) {
			System.out.println("Something goes wrong with remote call ...");
		}
		
	}
	
	private static void ShowMenu() {
		
		System.out.println("\n:::::: MENU ::::::");
		System.out.println("#1 :: Aggiungi uno speaker al congresso (fornire nomeSpeaker, giorno, sessione)");
		System.out.println("#2 :: Popola casualmente tutto il congresso");
		System.out.println("#3 :: Mostra il programma completo del congresso");
		System.out.println("#4 :: Mostra il programma di un giorno del congresso (fornire giorno)");
		System.out.println("#5 :: Exit");
		System.out.println("Scegli un operazione digitando il suo numero");
		
	}
	
	/*	Funzione per la creazione di un nome casuale	*/
	private static String GetRandomSpeaker() {
		
		String speaker = NOME[(int)(Math.random()*NOME.length)] + " " + COGNOME[(int)(Math.random()*COGNOME.length)];
		return speaker;
		
	}
	
}
