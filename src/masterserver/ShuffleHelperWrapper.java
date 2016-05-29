package masterserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class ShuffleHelperWrapper implements IMRTask {

	private String workerName;
	Scanner sc = null;
	Scanner s2 = null;
	List<Integer> partitionList;
	private String senderName;
	private int senderPort;

	ShuffleHelperWrapper(List<Integer> partitionList) {
		this.partitionList = partitionList;
	}

	@Override
	public Object execute(Class cl) {
		String baseDir = System.getProperty("user.dir") + "\\assets\\" + workerName + "\\";
		String intermediary = baseDir + "intermediate_results\\";
		File folderPath = new File(intermediary);
		HashMap<String, Integer> wordsMap = new HashMap<String, Integer>();
		ArrayList<String> al = new ArrayList<String>();
		for (int element : partitionList) {
			String partitionString = intermediary + Integer.toString(element);
			File partitionPath = new File(partitionString);
			for (File file : partitionPath.listFiles()) {
				String filePath = file.getAbsolutePath();
				try {
					sc = new Scanner(new File(filePath));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				while (sc.hasNextLine()) {
					String line = sc.nextLine();
					String[] strArr = line.split(",");
					int absHash = Math.abs(strArr[0].hashCode());
					int bucket = absHash % 4;
					int workerId = Integer.parseInt(workerName.charAt(6) + "");
					if (bucket % workerId == 0) {
						wordsMap.put(strArr[0], Integer.parseInt(strArr[strArr.length - 1]));
						al.add(strArr[0] + "," + 1);
					}
				}
			}

		}

		// Open a socket connection and write this information into it.

		return al;
	}

	public String getWorkerName() {
		return workerName;
	}

	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public int getSenderPort() {
		return senderPort;
	}

	public void setSenderPort(int senderPort) {
		this.senderPort = senderPort;
	}

}
