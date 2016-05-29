package client;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class MapFunctionClass implements Function<String, List<String>> {

	@Override
	public List<String> apply(String filePath) {
		String sCurrentLine = null;
		BufferedReader br;
		List<String>list=new ArrayList();
		try {
			br = new BufferedReader(new FileReader(filePath));
			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(sCurrentLine);
				String[] strArr = sCurrentLine.split(" ");
				for (String word : strArr) {
					list.add(word);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return list;
	}

}
