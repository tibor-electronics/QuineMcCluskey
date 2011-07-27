/**
 * Copyright 2011, Kevin Lindsey
 * See LICENSE file for licensing information
 */
package com.kevlindev.qmc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Partitions implements Iterable<MinTermList> {
	private List<MinTermList> partitions = new ArrayList<MinTermList>();

	/**
	 * Add a new MinTermList to this collection
	 * 
	 * @param terms
	 */
	public void add(MinTermList terms) {
		partitions.add(terms);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		boolean match = false;

		if (obj instanceof Partitions) {
			Partitions other = (Partitions) obj;

			if (size() == other.size()) {
				match = true;

				for (int i = 0; i < size(); i++) {
					if (partitions.get(i).equals(other.partitions.get(i)) == false) {
						match = false;
						break;
					}
				}
			}
		}

		return match;
	}

	/**
	 * Create a unique set of all MinTerms contained within this collection
	 * 
	 * @return
	 */
	public Set<MinTerm> flatten() {
		Set<MinTerm> result = new HashSet<MinTerm>();

		for (MinTermList partition : partitions) {
			result.addAll(partition.getMinTerms());
		}

		return result;
	}

	/**
	 * Return the MinTermList at the specified index
	 * 
	 * @param index
	 * @return
	 */
	public MinTermList get(int index) {
		return partitions.get(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return partitions.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<MinTermList> iterator() {
		return partitions.iterator();
	}

	/**
	 * Return the number of MinTermLists in this collection
	 * 
	 * @return
	 */
	public int size() {
		return partitions.size();
	}

	/**
	 * Sort the list of MinTermLists, shortest first, then by fewest states
	 */
	public void sort() {
		Collections.sort(partitions, new Comparator<MinTermList>() {
			@Override
			public int compare(MinTermList arg0, MinTermList arg1) {
				int result = arg0.size() - arg1.size();

				if (result == 0) {
					int indexCount0 = 0;
					int indexCount1 = 0;

					for (MinTerm term : arg0) {
						indexCount0 += term.getIndexes().size();
					}

					for (MinTerm term : arg1) {
						indexCount1 += term.getIndexes().size();
					}

					result = indexCount0 - indexCount1;
				}

				return result;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder buffer = new StringBuilder();

		for (int i = 0; i < partitions.size(); i++) {
			MinTermList partition = partitions.get(i);
			List<String> v = new ArrayList<String>();

			for (MinTerm minterm : partition) {
				String indexes = "[" + minterm.toString() + "]";

				v.add(indexes);
			}

			buffer.append(i).append(" : ");
			buffer.append(Util.join(", ", v));
		}

		return buffer.toString();
	}
}
