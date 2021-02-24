
public class MainClass {
	
	public static void main(String[] args) {
		
		/*	Creazione file JSON, con un numero di correntisti generati casualmente	*/
		final String nomeCorrentista = "Correntista";
		final String nomeFile = "ContiCorrenti.json";
		final int rangeCorrentisti = 5;
		final int numCorrentisti = (int)(Math.random()*rangeCorrentisti) + 1;
		JSONFileCreator jsonFile = new JSONFileCreator(nomeCorrentista, nomeFile, numCorrentisti);
		jsonFile.createFile();
		
		/*	Creazione di un thredPool con un numero di thread generati casualmente	*/
		final int rangeThread = 5;
		final int nThread = (int)(Math.random()*rangeThread) + 1; 
		ThreadPool pool = new ThreadPool(nThread);
		
		/*	Creazione contatori globali	*/
		ContatoriGlobali contatoriGlobali = new ContatoriGlobali();
		
		/*	Creazione e lancio thread Reader che si occuperà anche dei thread per aggiornare i contatori	*/
		Reader reader = new Reader(nomeFile, pool, contatoriGlobali);
		reader.start();
		
	}
	
}
