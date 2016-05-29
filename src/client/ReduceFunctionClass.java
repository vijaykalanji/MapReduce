package client;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.function.Function;

public class ReduceFunctionClass implements Function<ArrayList<String>, TreeMap<String, Integer>> {

	@Override
	public TreeMap<String, Integer> apply(ArrayList<String> al) {
		TreeMap<String, Integer> retMap = new TreeMap<String, Integer>();
		String prev = null;
		if (al.size() > 0) {
			prev = al.get(0);
		}
		String cur = null;
		int count = 0;
		for (int i = 0; i < al.size(); i++) {
			cur = al.get(i);
			if (cur.equals(prev)) {
				count++;
			} else {
				retMap.put(cur, count);
				count = 0;
			}
			prev = cur;
		}

		return retMap;
	}

}
