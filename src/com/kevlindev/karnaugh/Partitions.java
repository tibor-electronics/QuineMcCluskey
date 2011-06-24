package com.kevlindev.karnaugh;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Partitions implements Iterable<MinTermList> {
	public static final Comparator<String> IMPLICANT_COMPARATOR = new Comparator<String>() {
		@Override
		public int compare(String arg0, String arg1) {
			int result = 0;

			for (int i = 0; i < arg0.length(); i++) {
				char c1 = arg0.charAt(i);
				char c2 = arg1.charAt(i);

				if (c1 != c2) {
					// do nothing
					if (c1 == '-') {
						result = 1;
					} else if (c2 == '-') {
						result = -1;
					} else if (c1 == '0') {
						result = -1;
					} else if (c2 == '1') {
						result = 1;
					}
					break;
				}
			}

			return result;
		}
	};

	private List<MinTermList> partitions = new ArrayList<MinTermList>();

	public void add(MinTermList terms) {
		partitions.add(terms);
	}

	public Set<MinTerm> flatten() {
		Set<MinTerm> result = new HashSet<MinTerm>();

		for (MinTermList partition : partitions) {
			result.addAll(partition.getMinTerms());
		}

		return result;
	}

	public MinTermList get(int index) {
		return partitions.get(index);
	}

	// TODO: bad name here
	public List<String> getPrimeImplicants() {
		Set<String> primeImplicantSet = new HashSet<String>();

		for (MinTermList partition : partitions) {
			for (MinTerm term : partition) {
				primeImplicantSet.add(Util.join("", term.getBits()));
			}
		}

		ArrayList<String> primeImplicants = new ArrayList<String>(primeImplicantSet);
		Collections.sort(primeImplicants, IMPLICANT_COMPARATOR);

		return primeImplicants;
	}

	@Override
	public Iterator<MinTermList> iterator() {
		return partitions.iterator();
	}

	public int size() {
		return partitions.size();
	}

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
