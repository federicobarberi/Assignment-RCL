/*	Classe Cliente che rappresenta un task per il threadpool biglietteria	*/

public class Cliente implements Runnable{
	private String nome;

	public Cliente(String nome){
		this.nome = nome;
	}

	public String getName() {
		return this.nome;
	}

	/*	@Override	*/
	public void run() {
		System.out.println("Sono " + nome + " e sono nella seconda sala in attesa di essere servito allo sportello\n");

		/*	Simulo l'operazione allo sportello attendendo un tempo casuale per avere ogni task un tempo diverso */
		try{
			Long attesa = (long)(Math.random()*1000);
			System.out.println("Sono " + nome + " e sto eseguendo un operazione allo sportello\n");
			Thread.sleep(attesa);
		}
		catch(InterruptedException x){
			x.printStackTrace();
		}

		System.out.println("Sono " + nome + " e ho finito il mio compito!\n");
	}
}
