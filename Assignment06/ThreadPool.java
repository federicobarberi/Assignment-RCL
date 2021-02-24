import java.util.concurrent.*;

/*
 * 	ThreadPool:
 * 		Classe che modella il ThreadPool, il numero di thread è generato casualmente nel main.
 * 		I thread del Pool hanno il compito di aggiornare i contatori globali.
 */

public class ThreadPool {
	
	private final int numeroThread;
	private final ExecutorService threadPool;
	private LinkedBlockingQueue<Runnable> waitingThreads;
	
	public ThreadPool(int n) {
		this.numeroThread = n;
		waitingThreads = new LinkedBlockingQueue<Runnable>();
		this.threadPool = new ThreadPoolExecutor(numeroThread, numeroThread, 0L, TimeUnit.MILLISECONDS, waitingThreads);
	}
	
	/*	Sottomissione task a un thread del pool	*/
	public void submit(ThreadAggiornaContatori t) throws RejectedExecutionException{
		try {
			this.threadPool.execute(t);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/*	Chiusura ThreadPool, con Graceful termination	*/
	public void closePool() {
		this.threadPool.shutdown();
		try {
			this.threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
	}

}
