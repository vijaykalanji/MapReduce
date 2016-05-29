package client;

import java.util.function.Function;

public class MapFunctionClass implements Function<String, String> {

	@Override
	public String apply(String str) {
		String ret=str + ",1" + System.lineSeparator();
		return ret;
	}

}
