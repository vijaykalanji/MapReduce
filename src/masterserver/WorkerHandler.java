package masterserver;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;
import util.PickledClass;

public class WorkerHandler implements Callable<Integer> {
	private String WORKER_NAME;
	private int port;
	private IMRTask theTask;
	private PickledClass pickeldClass;

	WorkerHandler(String worker, int port, IMRTask task, PickledClass theClass) {
		this.WORKER_NAME = worker;
		this.port = port;
		// this.partitionList = partition;
		this.theTask = task;
		this.pickeldClass = theClass;
	}

	@Override
	public Integer call() throws Exception {
		int retVal = 0;
		try (Socket sock = new Socket(WORKER_NAME, port)) {
			ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
			out.writeObject(theTask);
			out.writeObject(pickeldClass);
			out.flush();
			ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
			//retVal = in.readInt();
			System.out.println("___________" + retVal);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}

}
