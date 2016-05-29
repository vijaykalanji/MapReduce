package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import util.PickledClass;

public class Client {
	private static final int MAX_POOL_SIZE = Runtime.getRuntime().availableProcessors();
	private static final String SERVER_NAME = "localhost";
	private static final int port = 6789;

	public static void main(String[] args) {
		ExecutorService executor = Executors.newFixedThreadPool(MAX_POOL_SIZE);

		try (Socket sock = new Socket(SERVER_NAME, port)) {
			/** Create an ObjectOutputStream to write to the Server with. */
			ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
			out.writeObject(new PickledClass(MapFunctionClass.class));
			out.writeObject(new PickledClass(ReduceFunctionClass.class));
			out.flush();
			ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
			Object retObj = (Integer) in.readObject();
			System.out.println("At the client side -->" + retObj);

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
