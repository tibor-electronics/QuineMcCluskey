package com.kevlindev.karnaugh;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class MinTermList implements Iterable<MinTerm> {
	private List<MinTerm> minterms = new ArrayList<MinTerm>();

	/**
	 * Add a MinTerm to this collection
	 * 
	 * @param term
	 */
	public void add(MinTerm term) {
		if (term != null) {
			minterms.add(term);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		boolean match = false;

		if (obj instanceof MinTermList) {
			MinTermList other = (MinTermList) obj;

			if (size() == other.size()) {
				match = true;

				for (int i = 0; i < size(); i++) {
					if (minterms.get(i).equals(other.minterms.get(i)) == false) {
						match = false;
						break;
					}
				}
			}
		}

		return match;
	}

	/**
	 * Return a list of MinTerms in this collection
	 * 
	 * @return
	 */
	public List<MinTerm> getMinTerms() {
		return minterms;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return minterms.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<MinTerm> iterator() {
		return minterms.iterator();
	}

	/**
	 * Return the number of MinTerms in this collection
	 * 
	 * @return
	 */
	public int size() {
		return minterms.size();
	}

	/**
	 * Sort the minterms in this list by their bits
	 */
	public void sort() {
		Collections.sort(minterms, new Comparator<MinTerm>() {
			@Override
			public int compare(MinTerm arg0, MinTerm arg1) {
				List<TruthValue> bits0 = arg0.getBits();
				List<TruthValue> bits1 = arg1.getBits();
				int result = 0;

				for (int i = 0; i < bits0.size(); i++) {
					result = bits0.get(i).compareTo(bits1.get(i));

					if (result != 0) {
						break;
					}
				}

				return result;
			}
		});
	}
}
