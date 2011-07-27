/**
 * Copyright 2011, Kevin Lindsey
 * See LICENSE file for licensing information
 */
package com.kevlindev.qmc;

import java.util.Iterator;
import java.util.NoSuchElementException;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class SelectionIterator implements Iterator<boolean[]> {
	private boolean[] selectors;
	private int done = 0;

	public SelectionIterator(int size) {
		selectors = new boolean[size];
		
		for (int i = 0; i < size; i++) {
			selectors[i] = true;
		}
	}

	@Override
	public boolean hasNext() {
		return done < 2;
	}

	@Override
	public boolean[] next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		
		// increment the selector, with carries
		int i;

		for (i = 0; i < selectors.length; i++) {
			if (selectors[i] == false) {
				// no carry, so we can stop
				selectors[i] = true;
				break;
			} else {
				// carry, so propagate that
				selectors[i] = false;
			}
		}

		// if 'i' is the length of the selector list, then we wrapped all
		// the way around and are done
		if (i == selectors.length) {
			done++;
		}

		return selectors;
	}

	@Override
	public void remove() {
		throw new NotImplementedException();
	}
}
