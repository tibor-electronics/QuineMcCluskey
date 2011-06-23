package com.kevlindev.karnaugh;

import java.util.ArrayList;
import java.util.List;

public class Function {
	private String name;
	private List<String> arguments;
	private List<MinTerm> minterms;
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
	 * getValues
	 * 
	 * @return
	 */
	public List<MinTerm> getMinTerms() {
		return this.minterms;
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
		this.minterms = new ArrayList<MinTerm>();

		for (TruthValue value : values) {
			if (value == TruthValue.TRUE) {
				minterms.add(new MinTerm(bits, index));
			}
			
			index++;
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(name).append("(").append(Util.join(",", arguments)).append(") = ");

		List<String> values = new ArrayList<String>(this.minterms.size());

		for (MinTerm minterm : minterms) {
			int index = minterm.getIndexes().get(0);
			
			values.add(Integer.toString(index));
		}

		sb.append("\u2211(").append(Util.join(",", values)).append(")");

		return sb.toString();
	}
}
