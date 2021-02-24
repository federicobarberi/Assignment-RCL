import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.util.HashMap;
import java.util.Vector;

public class Congress extends RemoteServer implements CongressInterface{

	private final static long serialVersionUID = 4L;
	
	/*
	 * 	Struttura dati:
	 * 
	 * 		Rappresentiamo l' organizzazione del congresso come 3 distinte HashMap;
	 * 		Una HashMap per ogni giorno del congresso;
	 * 		Per ogni HashMap la chiave sarà il numero di sessione, il valore un vettore di stringhe contenenti i nomi degli speaker;
	 */
	
	private HashMap<Integer, Vector<String>> day1;
	private HashMap<Integer, Vector<String>> day2;
	private HashMap<Integer, Vector<String>> day3;
	
	public Congress() throws RemoteException{
		
		day1 = new HashMap<Integer, Vector<String>>();
		day2 = new HashMap<Integer, Vector<String>>();
		day3 = new HashMap<Integer, Vector<String>>();
		
		InitializeMap();
		
	}
	
	/*	Funzione di utilità per inizializzare i vettori delle HashMap	*/
	private void InitializeMap() {
		
		for(int day = 1; day <= 3; day++) {
			
			for(int session = 1; session <= 12; session++) {
				
				if(day == 1)
					day1.put(session, new Vector<String>());
				
				if(day == 2)
					day2.put(session, new Vector<String>());
				
				if(day == 3)
					day3.put(session, new Vector<String>());
			}
					
		}
	}
	
	public synchronized boolean IsCongressFull() throws RemoteException{
				
		for(int session = 1; session <= 12; session++) {
				
			if(day1.get(session).size() < 5)
				return false;
				
			if(day2.get(session).size() < 5)
				return false;
			
			if(day3.get(session).size() < 5)
				return false;
			
		}
		return true;
		
	}
	
	public synchronized boolean RegisterSpeaker(String speakerName, int day,int session)  throws RemoteException, SessionUnavaibleException, IllegalArgumentException{
		
		/*	Controllo sui parametri passati	*/
		if(speakerName == null)			  throw new IllegalArgumentException();
		if(day < 1 || day > 3)			  throw new IllegalArgumentException("Giorno inesistente");
		if(session > 12 || session < 1)   throw new SessionUnavaibleException("Sessione inesistente");
		
		if(day == 1) {
			
			if(day1.get(session).size() == 5) throw new SessionUnavaibleException("Sessione completa");
			if(day1.get(session).contains(speakerName)) return false;
			Vector<String> update = day1.get(session);
			update.addElement(speakerName);
			day1.put(session, update);
			
		}
		
		else if(day == 2) {
			
			if(day2.get(session).size() == 5) throw new SessionUnavaibleException("Sessione completa");
			if(day2.get(session).contains(speakerName)) return false;
			Vector<String> update = day2.get(session);
			update.addElement(speakerName);
			day2.put(session, update);
			
		}
		
		else if(day == 3) {
			
			if(day3.get(session).size() == 5) throw new SessionUnavaibleException("Sessione completa");
			if(day3.get(session).contains(speakerName)) return false;
			Vector<String> update = day3.get(session);
			update.addElement(speakerName);
			day3.put(session, update);
			
		}
		
		return true;
		
	}
	
	public synchronized String GetSchedule() throws RemoteException{
		
		String day1 = GetFirstDay();
		String day2 = GetSecondDay();
		String day3 = GetThirdDay();
		String schedule = day1 + day2 + day3;
		return schedule;
	
	}
	
	public synchronized String GetFirstDay() throws RemoteException{
		
		String firstDaySchedule = "Programma Giorno 1\n";
		for(int session = 1; session <= 12; session++) {
			firstDaySchedule = firstDaySchedule + "Sessione #" + session + " : " + day1.get(session).toString() +"\n";
		}
		firstDaySchedule = firstDaySchedule + "\n";
		return firstDaySchedule;
		
	}
	
	public synchronized String GetSecondDay() throws RemoteException{
		
		String secondDaySchedule = "Programma Giorno 2\n";
		for(int session = 1; session <= 12; session++) {
			secondDaySchedule = secondDaySchedule + "Sessione #" + session + " : " + day2.get(session).toString() + "\n";
		}
		secondDaySchedule = secondDaySchedule + "\n";
		return secondDaySchedule;
		
	}
	
	public synchronized String GetThirdDay() throws RemoteException{
		
		String thirdDaySchedule = "Programma Giorno 3\n";
		for(int session = 1; session <= 12; session++) {
			thirdDaySchedule = thirdDaySchedule + "Sessione #" + session + " : " + day3.get(session).toString() + "\n";
		}
		thirdDaySchedule = thirdDaySchedule + "\n";
		return thirdDaySchedule;
		
	}
	
}
