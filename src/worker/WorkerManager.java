package worker;

public class WorkerManager {

	public static void main(String[] args) {
		Worker1 w1 = new Worker1();
		Worker2 w2 = new Worker2();
		Worker3 w3 = new Worker3();
		Worker4 w4 = new Worker4();
		new Thread(w1).start();
		new Thread(w2).start();
		new Thread(w3).start();
		new Thread(w4).start();
	}

}
