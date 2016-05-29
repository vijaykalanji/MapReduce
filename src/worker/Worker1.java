package worker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import masterserver.IMRTask;
import util.PickledClass;

public class Worker1 implements Runnable {
	public static String WORKER_NAME = "Worker 1";
	public final static int WORKER_PORT = 12345;
	static Socket clientSocket = null;

	public static void main(String[] args) {

	}

	@Override
	public void run() {
		try (ServerSocket workerSocket = new ServerSocket(WORKER_PORT)) {
			while (true) {
				System.err.println("Waiting..." + WORKER_NAME);
				clientSocket = workerSocket.accept();
				System.err.println("Accepted connection worker  : " + clientSocket);
				new Thread(new MiniWorker(clientSocket)).start();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	class MiniWorker implements Runnable {
		private Socket socket;

		MiniWorker(Socket sock) {
			this.socket = sock;
		}

		@Override
		public void run() {
			try {
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				IMRTask imrObject = (IMRTask) ois.readObject();
				PickledClass mpickleMap = (PickledClass) ois.readObject();
				Class<?> mapOrReduceClass = mpickleMap.revive();
				Object retObj = imrObject.execute(mapOrReduceClass);
				ObjectOutputStream os = new ObjectOutputStream(clientSocket.getOutputStream());
				os.writeObject(retObj);
				os.flush();
				System.out.println("TO DO");
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}
}
