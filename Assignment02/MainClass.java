import java.util.concurrent.*;
/*	Simulazione di funzionamento con 100 persone che entrano nella sala di attesa	*/

public class MainClass {
	public static void main(String[] args) {
		int i = 0;
		int K = 10; 	/*	Parametro che puo variare, indica i posti liberi nella seconda sala, per fare i test ho usato 10	*/
		LinkedBlockingQueue<Cliente> salaAttesa = new LinkedBlockingQueue<Cliente>();
		UfficioPostale stanza2 = new UfficioPostale(K);

		/*	Tutti i clienti che entrano vengono messi nella prima sala di attesa	*/
		while(i < 100) {
			Cliente c = new Cliente("Cliente"+i);
			System.out.println("Sono " + c.getName() + " e sono nella prima sala d' attesa\n");
			try {
				salaAttesa.put(c);
			}
			catch(InterruptedException x){
				x.printStackTrace();
			}

			/*	Se la coda che gestisce la seconda sala di attesa non è piena vengono trasferiti alla seconda sala di attesa	*/
			if(stanza2.getQueueSize()<K) {
				try {
					stanza2.serviCliente(salaAttesa.take());
				}
				catch(InterruptedException x) {
					x.printStackTrace();
				}
			}
			i++;
		}

		/*	Se ci sono servo gli ultimi clienti rimasti nella sala di attesa	*/
		while(salaAttesa.size() != 0) {
			if(stanza2.getQueueSize() < K){
				try {
					stanza2.serviCliente(salaAttesa.take());
				}
				catch(InterruptedException x) {
					x.printStackTrace();
				}
			}
		}

		/*	Chiusura del threadpool	*/
		stanza2.chiudiUfficio();
	}
}
