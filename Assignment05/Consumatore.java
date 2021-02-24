import java.io.File;

public class Consumatore extends Thread{
	
	private int id;
	Buffer buffer = null;
	
	public Consumatore(int id, Buffer buff) {
		this.id = id;
		this.buffer = buff;
	}
	
	/*	@Override:
	 * 		Il consumatore preleva la prima directory presente nella lista e stampa
	 * 		il nome di tutti i file presenti all interno.
	 */
	public void run() {	
		
		while(true) {
				
			/*	Recupero una directory dal buffer condiviso	*/
			File checkDir = buffer.pollDir();
			
			/*	Controllo il contenuto della directory	*/
			if(checkDir.getName().equals("Non estraibile")) {
				return;
			}	
				
			/*	Per tutti i file contenuti nella directory, stampo il loro nome	*/
			else {
				File dir[] = checkDir.listFiles();
				for(File file : dir) {
					if(file.isFile()) {
						System.out.println("Sono consumatore" + id + " e nella directory '" + file.getParentFile().getName() + "' ho trovato file '" + file.getName() + "'\n");
					}
				}
			}
			
		}
		
	}
	
}
