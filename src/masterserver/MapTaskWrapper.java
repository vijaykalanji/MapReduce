package masterserver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

public class MapTaskWrapper implements IMRTask, Serializable {

	private List<Integer> partitionList;
	private String workerName;

	public String getWorkerName() {
		return workerName;
	}

	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}

	public List<Integer> getPartitionList() {
		return partitionList;
	}

	public void setPartitionList(List<Integer> partitionList) {
		this.partitionList = partitionList;
	}

	@Override
	public Object execute(Class cl) {
		// Perform the operation on the Map Task
		Scanner sc = null;
		Scanner s2 = null;
		String baseDir = System.getProperty("user.dir") + "\\assets\\" + workerName + "\\";
		try {
			Function<Object, Object> oneClassObj = (Function<Object, Object>) cl.newInstance();
			for (int partition : partitionList) {
				String intermediary = baseDir + "intermediate_results\\";
				String intermediaryPartition = intermediary + partition + "\\";
				System.out.println(System.getProperty("user.dir"));
				String dataPath = baseDir + "data\\";
				createDir(intermediaryPartition);
				StringBuffer sb = new StringBuffer();
				String partitionPath = dataPath + partition;
				File folderPath = new File(partitionPath);
				for (File file : folderPath.listFiles()) {
					// Apply map function here.
					String filePath = file.getAbsolutePath();
					String currentFileName = file.getName();
					try {
						sc = new Scanner(new File(filePath));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					while (sc.hasNextLine()) {
						s2 = new Scanner(sc.nextLine());
						while (s2.hasNext()) {
							String s = s2.next();
							sb.append(s + ",1" + System.lineSeparator());
						}
					}
					writeToFile(sb.toString(), intermediaryPartition + currentFileName);
				}

			}

		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			sc.close();
			s2.close();
		}
		System.out.print("Hi all. I am MaptaskWrapper");
		return new Integer(1);
	}

	private void createDir(String path) {
		File theDir = new File(path);
		if (!theDir.exists()) {
			System.out.println("creating directory: " + path);
			boolean result = false;
			try {
				theDir.mkdir();
				result = true;
			} catch (SecurityException se) {
				// handle it
			}
			if (result) {
				System.out.println("DIR created");
			}
		}

	}

	private void writeToFile(String content, String path) {
		try {
			File file = new File(path);
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
			System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
