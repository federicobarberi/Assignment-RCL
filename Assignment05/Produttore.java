import java.io.File;

public class Produttore extends Thread{
	
	/*	id, nome del produttore, sarà sempre 1 perche c'è un unico produttore	*/
	private int id;
	
	/*	path della directory di partenza da cui iniziare a esplorare le sottodirectory	*/
	private String path;
	
	/*	buffer condiviso dove via via vengono scritte le cartelle che troviamo	*/
	private Buffer buffer = null;
	
	public Produttore(int id, String path, Buffer startDir) {
		this.id = id;
		this.buffer = startDir;
		this.path = path;
	}
	
	/*	@Override:
	 * 		Il produttore accede alla cartella di base con percorso 'path';
	 * 		Successimente visita ricorsivamente tutte le cartelle e le aggiunge al buffer condiviso.
	 */
	public void run() {
		
		System.out.println("Sono produttore" + id + " e inizio a navigare tra le cartelle\n");
		
		/*	Aggiungo anche la directory iniziale alla lista	*/
		File dirIniziale = new File(path);
		buffer.addDir(dirIniziale);

		/*	Aggiungiamo al buffer tutte le directory che troviamo	*/
	    exploreDir(path);
	              
	    /*	Aggiungo una marca per far capire che ho finito la visita	*/
	    File marcaFine = new File("FineVisita");
	    buffer.addDir(marcaFine);
	    
	}
	
	/*	Ricorsione per esplorare tutte le directory ed aggiungerle alla lista	*/
	public void exploreDir(String path) {
		File startDir = new File(path);
		File[] dirInCurrDirectory = startDir.listFiles();
		for(File curr_dir : dirInCurrDirectory) {
			
			if(curr_dir.isDirectory()) {
				buffer.addDir(curr_dir);
				exploreDir(curr_dir.getAbsolutePath());
			}

		}
	}
	
}
