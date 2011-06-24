package com.kevlindev.qmc;

import java.util.Arrays;
import java.util.Collection;
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

	/**
	 * Create a new string that is the result of repeating another string a
	 * specified number of times
	 * 
	 * @param text
	 * @param count
	 * @return
	 */
	public static String repeat(String text, int count) {
		StringBuilder buffer = new StringBuilder();

		if (text != null && text.length() > 0 && count > 0) {
			for (int i = 0; i < count; i++) {
				buffer.append(text);
			}
		}

		return buffer.toString();
	}

	public static Partitions partitionMinTerms(int bits, Collection<MinTerm> minterms) {
		Partitions result = new Partitions();

		for (int i = 0; i <= bits; i++) {
			result.add(new MinTermList());
		}

		for (MinTerm minterm : minterms) {
			int index = minterm.getBitCount();
			MinTermList partition = result.get(index);

			partition.add(minterm);
		}

		return result;
	}
	
	public static void printSection(String header, String content) {
		System.out.println(header);
		System.out.println(Util.repeat("-", header.length()));
		System.out.println(content);
		System.out.println();
	}
}
