package masterserver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;

public class ReduceTaskWrapper implements IMRTask {
	String workerName;

	public String getWorkerName() {
		return workerName;
	}

	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}

	@Override
	public Object execute(Class cl) {
		String baseDir = System.getProperty("user.dir") + "\\assets\\" + workerName + "\\";
		String shuffleFile = baseDir + "intermediate_results\\shuffleResults_" + workerName;
		String writePath = System.getProperty("user.dir") + "\\assets\\wordCountDetails.txt";
		Scanner sc = null;
		ArrayList<String> al = new ArrayList<String>();
		try {
			Function<ArrayList<String>, TreeMap<String, Integer>> reduceFunction = (Function<ArrayList<String>, TreeMap<String, Integer>>) cl
					.newInstance();
			sc = new Scanner(new File(shuffleFile));
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] strArr = line.split(",");
				al.add(strArr[0]);
			}
			TreeMap<String, Integer> retMap = reduceFunction.apply(al);
			writeToFile(retMap, writePath);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return true;
	}

	private void writeToFile(TreeMap<String, Integer> wordCollection, String path) {
		try {
			File file = new File(path);
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			Set<String> keySet = wordCollection.keySet();
			for (String element : keySet) {
				bw.write(element + ", ");
				int intVal=wordCollection.get(element);
				bw.write(new Integer(intVal).toString());
				bw.newLine();
			}
			bw.close();
			System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
