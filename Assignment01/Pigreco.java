public class Pigreco extends Thread{
	private double pi;
	private double accuracy;
	
	public Pigreco(double accuracy) {
		pi = 0;
		this.accuracy = accuracy;
	}
	
	/*	@Override	*/
	public void run() {
		/*	Qui viene implementata il calcolo della serie di Gregory - Leibniz	*/
		int parity = 0;		//Utilizzata per capire se sommare o sottrarre
		for(int i = 1; Math.abs(pi - Math.PI) >= accuracy && this.isInterrupted() == false; i+=2){
			if(parity == 0) {
				pi = pi + (double)4/i;
				parity = 1;
			}
			else {
				pi = pi - (double)4/i;
				parity = 0;
			}
			
			System.out.printf("%s : %f\n", Thread.currentThread().getName(), pi);
		}
	}
}
