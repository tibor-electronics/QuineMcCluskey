package com.kevlindev.karnaugh;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MinTermList implements Iterable<MinTerm> {
	private List<MinTerm> minterms = new ArrayList<MinTerm>();

	public void add(MinTerm term) {
		if (term != null) {
			minterms.add(term);
		}
	}

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

	@Override
	public int hashCode() {
		return minterms.hashCode();
	}

	public List<MinTerm> getMinTerms() {
		return minterms;
	}

	@Override
	public Iterator<MinTerm> iterator() {
		return minterms.iterator();
	}
	
	public int size() {
		return minterms.size();
	}
}
