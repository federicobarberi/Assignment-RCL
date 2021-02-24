import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CongressInterface extends Remote{

	/*	
	 * 	@param 	speakerName nome dello speaker da registrare alla sessione
	 * 			day			giorno del congresso al quale vogliamo registrare lo speaker
	 * 			session		numero della sessione alla quale registrare lo speaker
	 * 	
	 * 	@return true	 se lo speaker è stato registrato correttamente alla sessione
	 * 			false    se lo speaker è gia stato registrato in quella sessione
	 * 			(N.B: Si ammette la registrazione dello stesso speaker nella stessa giornata ma per una sessione diversa)
	 * 
	 * 	@throws	RemoteException				se si verificano errori durante la chiamata remota
	 * 	@throws SessionUnavaibleException	se la sessione non esiste oppure è gia al completo
	 * 	@throws IllegalArgumentException 	se lo speakerName è null o se il giorno non è compreso tra 1 e 3 (estremi inclusi)
	 */ 
	boolean RegisterSpeaker(String speakerName, int day, int session) throws RemoteException, SessionUnavaibleException, IllegalArgumentException;
	
	/*	
	 * 	@return	restituisce una stringa contenente la programmazione completa del congresso
	 * 	
	 * 	@throws RemoteException se si verificano errori durante la chiamata remota
	 */
	String GetSchedule() throws RemoteException;
	
	/*
	 * 	@return	restituisce una stringa contenente la programmazione del primo giorno del congresso
	 * 
	 * 	@throws RemoteException se si verificano errori durante la chiamata remota
	 */
	String GetFirstDay() throws RemoteException;
	
	
	/*
	 * 	@return	restituisce una stringa contenente la programmazione del secondo giorno del congresso
	 * 
	 * 	@throws RemoteException se si verificano errori durante la chiamata remota
	 */
	String GetSecondDay() throws RemoteException;
	
	/*
	 * 	@return	restituisce una stringa contenente la programmazione del terzo giorno del congresso
	 * 
	 * 	@throws RemoteException se si verificano errori durante la chiamata remota
	 */
    String GetThirdDay() throws RemoteException;
    
    /*	
     * 	@return true 	se tutte le sessioni del congresso, per ogni giorno, sono al completo
     * 			false	se c'è almeno una sessione libera
     * 
     * 	@throws	RemoteException	se si verificano errori durante la chiamata remota
     */
    boolean IsCongressFull() throws RemoteException;
    
}
