/**
 * Copyright 2011, Kevin Lindsey
 * See LICENSE file for licensing information
 */
package com.kevlindev.qmc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Function extends MinTermList {
	private String name;
	private List<String> arguments;
	private int bits;

	/**
	 * Function
	 * 
	 * @param name
	 */
	public Function(String name, int bits) {
		this.name = name;
		this.bits = bits;
	}

	public static Function fromFile(File file) {
		Scanner scanner = null;
		Function f = null;

		try {
			scanner = new Scanner(new FileInputStream(file), "utf-8");

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] parts = line.split("\\s*=\\s*");
				boolean valid = false;

				if (parts.length == 2) {
					String function = parts[0];
					int lparen = function.indexOf('(');
					int rparen = function.indexOf(')');

					if (lparen != -1 && rparen != -1) {
						String name = function.substring(0, lparen).trim();
						String[] args = function.substring(lparen + 1, rparen).split("\\s*,\\s*");
						List<BitValue> values = new ArrayList<BitValue>();

						for (String valueString : parts[1].split("\\s*,\\s*")) {
							values.add("1".equals(valueString) ? BitValue.TRUE : BitValue.FALSE);
						}

						int bits = (int) Math.ceil(Math.log(values.size()) / Math.log(2));

						f = new Function(name, bits);
						f.setArguments(Arrays.asList(args));
						f.setValues(values);

						valid = true;
					}
				}

				if (valid == false) {
					System.err.println("Invalid content: " + line);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}

		return f;
	}

	/**
	 * getArguments
	 * 
	 * @return
	 */
	public List<String> getArguments() {
		return this.arguments;
	}

	/**
	 * getBits
	 * 
	 * @return
	 */
	public int getBits() {
		return bits;
	}

	/**
	 * getMinTermIndexes
	 * 
	 * @return
	 */
	public List<Integer> getMinTermIndexes() {
		List<Integer> result = new ArrayList<Integer>();

		for (MinTerm term : this) {
			result.addAll(term.getIndexes());
		}

		return result;
	}

	/**
	 * getName
	 * 
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * setArguments
	 * 
	 * @param args
	 */
	public void setArguments(List<String> args) {
		this.arguments = args;
	}

	/**
	 * setValues
	 * 
	 * @param values
	 */
	public void setValues(List<BitValue> values) {
		int index = 0;

		for (BitValue value : values) {
			if (value == BitValue.TRUE) {
				this.add(new MinTerm(bits, index));
			}

			index++;
		}
	}

	/**
	 * toFunctionString
	 * 
	 * @param terms
	 * @return
	 */
	public String toFunctionString(MinTermList terms) {
		List<String> products = new ArrayList<String>();

		terms.sort();

		for (MinTerm term : terms) {
			List<String> sums = new ArrayList<String>();
			List<BitValue> values = term.getBits();

			for (int i = 0; i < values.size(); i++) {
				BitValue value = values.get(i);
				String argument = arguments.get(i);

				switch (value) {
					case FALSE:
						sums.add("~" + argument);
						break;

					case TRUE:
						sums.add(argument);
						break;
				}
			}

			products.add(Util.join(" & ", sums));
		}

		return name + " = " + Util.join(" | ", products);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(name).append("(").append(Util.join(",", arguments)).append(") = ");

		List<String> values = new ArrayList<String>(this.size());

		for (MinTerm minterm : this) {
			int index = minterm.getIndexes().get(0);

			values.add(Integer.toString(index));
		}

		sb.append("\u2211(").append(Util.join(",", values)).append(")");

		return sb.toString();
	}
}
