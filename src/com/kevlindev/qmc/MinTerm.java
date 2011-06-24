package com.kevlindev.qmc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MinTerm {
	private List<Integer> indexes = new ArrayList<Integer>();
	private List<BitValue> bits = new ArrayList<BitValue>();

	public MinTerm(int totalBits, int index) {
		this.indexes.add(index);

		for (int mask = 1 << (totalBits - 1); mask != 0; mask >>= 1) {
			BitValue value = ((index & mask) == mask) ? BitValue.TRUE : BitValue.FALSE;

			this.bits.add(value);
		}
	}

	private MinTerm(List<Integer> indexes, List<BitValue> bits) {
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

		for (BitValue value : bits) {
			if (value == BitValue.TRUE) {
				count++;
			}
		}

		return count;
	}

	public List<BitValue> getBits() {
		return bits;
	}

	public List<Integer> getIndexes() {
		return indexes;
	}

	public MinTerm merge(MinTerm term) {
		MinTerm result = null;
		int count = 0;
		List<BitValue> mergedBits = new ArrayList<BitValue>();

		for (int i = 0; i < bits.size(); i++) {
			if (bits.get(i) != term.bits.get(i)) {
				count++;

				if (count > 1) {
					break;
				}

				mergedBits.add(BitValue.UNDEFINED);
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
