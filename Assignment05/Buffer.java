import java.util.LinkedList;
import java.io.File;

public class Buffer {
	
	/*	Lista di directory */
	private LinkedList<File> directoryList;
	
	public Buffer() {
		directoryList = new LinkedList<>();
	}
	
	/*	Il produttore aggiunge la directory 'dir' alla lista	*/
	public synchronized void addDir(File dir) {
		directoryList.add(dir);
		notifyAll();
	}
	
	/*	Il consumatore rimuove la prima directory della lista	*/
	public synchronized File pollDir() {
		try {
			
			/*	Finche il buffer è vuoto aspetto	*/
			while(directoryList.isEmpty()) {
				wait();
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		/*	Se sono arrivato alla marca finale oppure non ci sono altre directory da togliere dal buffer	*/
		if(directoryList.peek().getName().equals("FineVisita") || directoryList.peek() == null) {
			File f = new File("Non estraibile");
			return f;
		}
		else	
			return directoryList.poll();
	}
	
}
