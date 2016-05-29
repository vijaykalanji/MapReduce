package masterserver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import util.PickledClass;

public class ShuffleTaskWrapper implements IMRTask {
	private int workerNum;
	private ArrayList<OtherServerDetails> otherWorkers;

	public ArrayList<OtherServerDetails> getOtherWorkers() {
		return otherWorkers;
	}

	public void setOtherWorkers(ArrayList<OtherServerDetails> otherWorkers) {
		this.otherWorkers = otherWorkers;
	}

	ShuffleTaskWrapper(int wn, ArrayList<OtherServerDetails> othWrkrs) {
		this.workerNum = wn;
		this.otherWorkers = othWrkrs;
	}

	public int getWorkerName() {
		return workerNum;
	}

	public void setWorkerName(int workerN) {
		this.workerNum = workerN;
	}

	@Override
	public Object execute(Class cl) {
		String wordCollection = null;
		for (OtherServerDetails otherWorker : otherWorkers) {
			int port = otherWorker.port;
			try (Socket sock = new Socket("localhost", port)) {
				ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
				ShuffleHelperWrapper sh = new ShuffleHelperWrapper(otherWorker.partitionList);
				sh.setSenderName("localhost");
				sh.setWorkerName(otherWorker.getName());
				out.writeObject(sh);
				out.writeObject(new PickledClass(cl));
				ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
				ArrayList<String> words = (ArrayList<String>) in.readObject();
				Collections.sort(words);
				writeShuffleResults(words);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new Integer(1);
	}

	private void writeShuffleResults(ArrayList<String> wordCollection) {

		String baseDir = System.getProperty("user.dir") + "\\assets\\worker" + workerNum + "\\";
		String intermediary = baseDir + "intermediate_results\\";
		writeToFile(wordCollection, intermediary + "shuffleResults_worker" + workerNum);
	}

	private void writeToFile(ArrayList<String> wordCollection, String path) {
		try {
			File file = new File(path);
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			for(String element:wordCollection){
			bw.write(element);
			bw.newLine();
			}
			bw.close();
			System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
