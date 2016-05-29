package masterserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.BiFunction;
import java.util.function.Function;

import util.PickledClass;

public class MasterServer {

	private static int SERVER_PORT = 6789;

	public static void main(String[] args) {
		Socket clientSocket = null;
		try (ServerSocket servsock = new ServerSocket(SERVER_PORT)) {
			while (true) {
				System.err.println("Waiting...");
				clientSocket = servsock.accept();
				System.err.println("Accepted connection Master Server: " + clientSocket);
				ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
				try {
					PickledClass mpickleMap = (PickledClass) ois.readObject();
					Class<?> oneClass = mpickleMap.revive();
					Function<Object, Object> mapObj = (Function<Object, Object>) oneClass.newInstance();
					PickledClass mpickleRed = (PickledClass) ois.readObject();
					Class<?> PlusClass = mpickleRed.revive();
					Function<ArrayList<String>, TreeMap<String, Integer>> reduceObj = (Function<ArrayList<String>, TreeMap<String, Integer>>) PlusClass
							.newInstance();
					ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
					// Logic to maintain map.
					assignMapToWorkers(mpickleMap);
					// Logic to shuffle.
					//performShuffle(mpickleMap);
					// Logic to reduce.
					performReduce(mpickleRed);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	private static void performReduce(PickledClass pickledClass) {

		ExecutorService es = Executors.newFixedThreadPool(4);
		ReduceTaskWrapper reduceWrapper1 = new ReduceTaskWrapper();
		reduceWrapper1.setWorkerName("worker1");
		ReduceTaskWrapper reduceWrapper2 = new ReduceTaskWrapper();
		reduceWrapper2.setWorkerName("worker2");
		ReduceTaskWrapper reduceWrapper3 = new ReduceTaskWrapper();
		reduceWrapper3.setWorkerName("worker3");
		ReduceTaskWrapper reduceWrapper4 = new ReduceTaskWrapper();
		reduceWrapper4.setWorkerName("worker4");

		WorkerHandler wh1 = new WorkerHandler("localhost", 12345, reduceWrapper1, pickledClass);
		WorkerHandler wh2 = new WorkerHandler("localhost", 12346, reduceWrapper2, pickledClass);
		WorkerHandler wh3 = new WorkerHandler("localhost", 12347, reduceWrapper3, pickledClass);
		WorkerHandler wh4 = new WorkerHandler("localhost", 12348, reduceWrapper4, pickledClass);

		List<WorkerHandler> list = new ArrayList<WorkerHandler>();
		list.add(wh1);
		list.add(wh2);
		list.add(wh3);
		list.add(wh4);

		try {
			List<Future<Integer>> returnFuture = es.invokeAll(list);
			for (Future<Integer> element : returnFuture) {
				System.out.println(element.isDone());
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private static void assignMapToWorkers(PickledClass pickledClass) {
		ExecutorService es = Executors.newFixedThreadPool(4);
		MapTaskWrapper mapWrapper1 = new MapTaskWrapper();
		List<Integer> list1 = new ArrayList<Integer>();
		list1.add(1);
		list1.add(4);
		mapWrapper1.setPartitionList(list1);
		mapWrapper1.setWorkerName("worker1");

		List<Integer> list2 = new ArrayList<Integer>();
		list2.add(6);
		list2.add(8);
		MapTaskWrapper mapWrapper2 = new MapTaskWrapper();
		mapWrapper2.setPartitionList(list2);
		mapWrapper2.setWorkerName("worker2");

		List<Integer> list3 = new ArrayList<Integer>();
		list3.add(2);
		list3.add(3);
		MapTaskWrapper mapWrapper3 = new MapTaskWrapper();
		mapWrapper3.setPartitionList(list3);
		mapWrapper3.setWorkerName("worker3");

		List<Integer> list4 = new ArrayList<Integer>();
		list4.add(5);
		list4.add(7);
		MapTaskWrapper mapWrapper4 = new MapTaskWrapper();
		mapWrapper4.setPartitionList(list4);
		mapWrapper4.setWorkerName("worker4");

		WorkerHandler wh1 = new WorkerHandler("localhost", 12345, mapWrapper1, pickledClass);
		WorkerHandler wh2 = new WorkerHandler("localhost", 12346, mapWrapper2, pickledClass);
		WorkerHandler wh3 = new WorkerHandler("localhost", 12347, mapWrapper3, pickledClass);
		WorkerHandler wh4 = new WorkerHandler("localhost", 12348, mapWrapper4, pickledClass);

		List<WorkerHandler> list = new ArrayList<WorkerHandler>();
		list.add(wh1);
		list.add(wh2);
		list.add(wh3);
		list.add(wh4);

		try {
			List<Future<Integer>> returnFuture = es.invokeAll(list);
			for (Future<Integer> element : returnFuture) {
				System.out.println(element.isDone());
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("The value retunred from the worker is --->");
		// } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (ExecutionException e) {
		// TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

	public static void performShuffle(PickledClass pickledClass) {
		ExecutorService es = Executors.newFixedThreadPool(4);

		ArrayList<OtherServerDetails> othrSrvrDtlsLst1 = new ArrayList<OtherServerDetails>();
		ArrayList<OtherServerDetails> othrSrvrDtlsLst2 = new ArrayList<OtherServerDetails>();
		ArrayList<OtherServerDetails> othrSrvrDtlsLst3 = new ArrayList<OtherServerDetails>();
		ArrayList<OtherServerDetails> othrSrvrDtlsLst4 = new ArrayList<OtherServerDetails>();

		ArrayList<Integer> partitionList = new ArrayList<Integer>();
		partitionList.add(1);
		partitionList.add(4);
		OtherServerDetails otherServer1 = new OtherServerDetails();
		otherServer1.setPartitionList(partitionList);
		otherServer1.setPort(12345);
		otherServer1.setName("worker1");
		othrSrvrDtlsLst2.add(otherServer1);
		othrSrvrDtlsLst3.add(otherServer1);
		othrSrvrDtlsLst4.add(otherServer1);

		OtherServerDetails otherServer2 = new OtherServerDetails();
		partitionList = new ArrayList<Integer>();
		partitionList.add(6);
		partitionList.add(8);
		otherServer2.setPort(12346);
		otherServer2.setPartitionList(partitionList);
		otherServer2.setName("worker2");
		othrSrvrDtlsLst1.add(otherServer2);
		othrSrvrDtlsLst3.add(otherServer2);
		othrSrvrDtlsLst4.add(otherServer2);

		OtherServerDetails otherServer3 = new OtherServerDetails();
		partitionList = new ArrayList<Integer>();
		partitionList.add(2);
		partitionList.add(3);
		otherServer3.setPort(12347);
		otherServer3.setName("worker3");
		otherServer3.setPartitionList(partitionList);
		othrSrvrDtlsLst1.add(otherServer3);
		othrSrvrDtlsLst2.add(otherServer3);
		othrSrvrDtlsLst4.add(otherServer3);

		OtherServerDetails otherServer4 = new OtherServerDetails();
		partitionList = new ArrayList<Integer>();
		partitionList.add(5);
		partitionList.add(7);
		otherServer4.setPort(12348);
		otherServer4.setName("worker4");
		otherServer4.setPartitionList(partitionList);
		othrSrvrDtlsLst1.add(otherServer4);
		othrSrvrDtlsLst2.add(otherServer4);
		othrSrvrDtlsLst3.add(otherServer4);

		ShuffleTaskWrapper shuffleTaskWrapper1 = new ShuffleTaskWrapper(1, othrSrvrDtlsLst1);

		ShuffleTaskWrapper shuffleTaskWrapper2 = new ShuffleTaskWrapper(2, othrSrvrDtlsLst2);

		ShuffleTaskWrapper shuffleTaskWrapper3 = new ShuffleTaskWrapper(3, othrSrvrDtlsLst3);

		ShuffleTaskWrapper shuffleTaskWrapper4 = new ShuffleTaskWrapper(4, othrSrvrDtlsLst4);

		WorkerHandler wh1 = new WorkerHandler("localhost", 12345, shuffleTaskWrapper1, pickledClass);
		WorkerHandler wh2 = new WorkerHandler("localhost", 12346, shuffleTaskWrapper2, pickledClass);
		WorkerHandler wh3 = new WorkerHandler("localhost", 12347, shuffleTaskWrapper3, pickledClass);
		WorkerHandler wh4 = new WorkerHandler("localhost", 12348, shuffleTaskWrapper4, pickledClass);

		List<WorkerHandler> list = new ArrayList<WorkerHandler>();
		list.add(wh1);
		list.add(wh2);
		list.add(wh3);
		list.add(wh4);

		try {
			List<Future<Integer>> returnFuture = es.invokeAll(list);
			for (Future<Integer> element : returnFuture) {
				System.out.println(element.isDone());
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("--------------------Completed shuffling -------------------");

	}

	public void performReduce() {

	}

}