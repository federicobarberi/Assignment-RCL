import java.util.concurrent.*;

public class UfficioPostale {
	private final int numeroSportelli = 4;
	private int postiSecondaSala;	
	private final ExecutorService threadPoolSportelli;
	private ArrayBlockingQueue<Runnable> secondaSalaAttesa;
	
	public UfficioPostale(int K) {
		this.postiSecondaSala = K;
		secondaSalaAttesa = new ArrayBlockingQueue<Runnable>(postiSecondaSala);
		this.threadPoolSportelli = new ThreadPoolExecutor(numeroSportelli, numeroSportelli, 0L, TimeUnit.MILLISECONDS, secondaSalaAttesa);
	}
	
	public void serviCliente(Cliente c) throws RejectedExecutionException{
		try {
			this.threadPoolSportelli.execute(c);
		}
		catch(RejectedExecutionException x) {
			System.out.println("Cliente " + c.getName() + " respinto!\n");
		}
	}
	
	public int getQueueSize() {
		return secondaSalaAttesa.size();
	}
	
	/*	Chiudo il threadpool con una graceful termination applicando attesa passiva	*/
	public void chiudiUfficio() {
		this.threadPoolSportelli.shutdown();
		try {
			/*	Con questo format (Long.MAX_VALUE) non ho vincoli di timeout	*/
			this.threadPoolSportelli.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		}
		catch(InterruptedException x) {
			System.out.println("Awaiting Termination interrotta!\n");
		}
	}	
	
}
