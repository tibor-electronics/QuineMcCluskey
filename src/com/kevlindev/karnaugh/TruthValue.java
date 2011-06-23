package com.kevlindev.karnaugh;

public enum TruthValue {
	FALSE("0"), TRUE("1"), UNDEFINED("-");

	private String text;

	/**
	 * TruthValue
	 * 
	 * @param value
	 */
	private TruthValue(String text) {
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
