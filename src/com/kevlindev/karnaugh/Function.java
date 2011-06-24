package com.kevlindev.karnaugh;

import java.util.ArrayList;
import java.util.List;

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
	 * getName
	 * 
	 * @return
	 */
	public String getName() {
		return this.name;
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
	public void setValues(List<TruthValue> values) {
		int index = 0;

		for (TruthValue value : values) {
			if (value == TruthValue.TRUE) {
				this.add(new MinTerm(bits, index));
			}

			index++;
		}
	}

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
