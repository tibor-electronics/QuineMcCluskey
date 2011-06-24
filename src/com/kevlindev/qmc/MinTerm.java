package com.kevlindev.qmc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MinTerm {
	private List<Integer> indexes = new ArrayList<Integer>();
	private List<TruthValue> bits = new ArrayList<TruthValue>();

	public MinTerm(int totalBits, int index) {
		this.indexes.add(index);

		for (int mask = 1 << (totalBits - 1); mask != 0; mask >>= 1) {
			TruthValue value = ((index & mask) == mask) ? TruthValue.TRUE : TruthValue.FALSE;

			this.bits.add(value);
		}
	}

	private MinTerm(List<Integer> indexes, List<TruthValue> bits) {
		this.indexes = indexes;
		this.bits = bits;
	}

	public int difference(MinTerm term) {
		int count = 0;

		for (int i = 0; i < bits.size(); i++) {
			if (bits.get(i) != term.bits.get(i)) {
				count++;
			}
		}

		return count;
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;

		if (obj instanceof MinTerm) {
			MinTerm other = (MinTerm) obj;

			result = indexes.equals(other.indexes) && bits.equals(other.bits);
		}

		return result;
	}

	@Override
	public int hashCode() {
		return indexes.hashCode() ^ bits.hashCode();
	}

	/**
	 * Return the number of TRUE bit values in this minterm
	 * 
	 * @return
	 */
	public int getBitCount() {
		int count = 0;

		for (TruthValue value : bits) {
			if (value == TruthValue.TRUE) {
				count++;
			}
		}

		return count;
	}

	public List<TruthValue> getBits() {
		return bits;
	}

	public List<Integer> getIndexes() {
		return indexes;
	}

	public MinTerm merge(MinTerm term) {
		MinTerm result = null;
		int count = 0;
		List<TruthValue> mergedBits = new ArrayList<TruthValue>();

		for (int i = 0; i < bits.size(); i++) {
			if (bits.get(i) != term.bits.get(i)) {
				count++;

				if (count > 1) {
					break;
				}

				mergedBits.add(TruthValue.UNDEFINED);
			} else {
				mergedBits.add(bits.get(i));
			}
		}

		if (count < 2) {
			List<Integer> indexes = new ArrayList<Integer>();

			indexes.addAll(this.indexes);
			indexes.addAll(term.indexes);

			Collections.sort(indexes);

			result = new MinTerm(indexes, mergedBits);
		}

		return result;
	}

	public String toString() {
		return "(" + Util.join(",", indexes) + "):" + Util.join("", bits);
	}
}
