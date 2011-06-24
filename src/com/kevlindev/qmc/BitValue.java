package com.kevlindev.qmc;

public enum BitValue {
	UNDEFINED("-"), FALSE("0"), TRUE("1");

	private String text;

	/**
	 * TruthValue
	 * 
	 * @param value
	 */
	private BitValue(String text) {
		this.text = text;
	}

	/**
	 * getText
	 * 
	 * @return
	 */
	public String getText() {
		return text;
	}

	/**
	 * toString
	 * 
	 * @return
	 */
	public String toString() {
		return getText();
	}
}
