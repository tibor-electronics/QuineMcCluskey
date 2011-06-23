package com.kevlindev.karnaugh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Util {
	private Util() {
	}

	public static String join(String delimiter, String... items) {
		return join(delimiter, Arrays.asList(items));
	}

	public static String join(String delimiter, List<? extends Object> items) {
		StringBuilder buffer = new StringBuilder();

		if (items != null && items.size() > 0) {
			buffer.append(items.get(0));

			for (int i = 1; i < items.size(); i++) {
				buffer.append(delimiter).append(items.get(i).toString());
			}
		}

		return buffer.toString();
	}
	
	public static String repeat(String text, int count) {
		StringBuilder buffer = new StringBuilder();
		
		for (int i = 0; i < count; i++) {
			buffer.append(text);
		}
		
		return buffer.toString();
	}

	public static List<List<MinTerm>> partitionMinTerms(int bits, List<MinTerm> minterms) {
		List<List<MinTerm>> result = new ArrayList<List<MinTerm>>();

		for (int i = 0; i <= bits; i++) {
			result.add(new ArrayList<MinTerm>());
		}

		for (MinTerm minterm : minterms) {
			int index = minterm.getBitCount();
			List<MinTerm> partition = result.get(index);

			partition.add(minterm);
		}

		return result;
	}
}
