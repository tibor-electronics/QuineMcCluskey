/**
 * Copyright 2011, Kevin Lindsey
 * See LICENSE file for licensing information
 */
package com.kevlindev.qmc;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Minimizer {

	/**
	 * main
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args != null && args.length > 0) {
			Minimizer minimizer = new Minimizer();

			for (String arg : args) {
				minimizer.minimize(arg);
			}
		}
	}

	/**
	 * getEssentialPrimeImplicants
	 * 
	 * @param f
	 * @param partitions
	 * @return
	 */
	private Partitions getEssentialPrimeImplicants(Function f, Partitions partitions) {
		Set<Integer> indexes = new HashSet<Integer>(f.getMinTermIndexes());
		Partitions solutions = new Partitions();

		List<MinTerm> allTerms = new ArrayList<MinTerm>(partitions.flatten());
		SelectionIterator iterator = new SelectionIterator(allTerms.size());

		while (iterator.hasNext()) {
			boolean[] selectors = iterator.next();
			Set<Integer> candidate = new HashSet<Integer>();
			MinTermList terms = new MinTermList();

			for (int i = 0; i < selectors.length; i++) {
				if (selectors[i]) {
					terms.add(allTerms.get(i));
					candidate.addAll(allTerms.get(i).getIndexes());
				}
			}

			if (candidate.equals(indexes)) {
				// TODO: keep track of best solution instead of collecting all
				// solutions
				solutions.add(terms);
			}
		}

		// sort so shortest (best) solutions are at the front of the list
		solutions.sort();

		return solutions;
	}

	/**
	 * Minimize the functions listed in teh specified file
	 * 
	 * @param file
	 */
	public void minimize(String file) {
		// TODO: check for existence and readability
		// load the function from the specified file
		Function f = Function.fromFile(new File(file));

		// TODO: move this method to Function
		// partition the minterms, grouped by 1-bit counts
		Partitions partitions = Util.partitionMinTerms(f.getBits(), f.getMinTerms());

		// loop until no change
		while (true) {
			// merge minterms that differ by only 1 bit and repartition
			Partitions newPartitions = reducePartitions(partitions);

			// quit if we ended up with the same partitions as before
			if (newPartitions.equals(partitions)) {
				break;
			} else {
				partitions = newPartitions;
			}
		}

		// get a list of solutions that equal the original function
		Partitions solutions = getEssentialPrimeImplicants(f, partitions);

		if (solutions != null && solutions.size() > 0) {
			// grab shortest (best) solution
			MinTermList solution = solutions.get(0);

			Util.printSection("Function", f.toString());
			Util.printSection("Minimized Function", f.toFunctionString(solution));
		} else {
			System.out.println("No solution was found, most likely indicating a fault in this application.");
		}
	}

	/**
	 * reducePartitions
	 * 
	 * @param partitions
	 * @return
	 */
	private Partitions reducePartitions(Partitions partitions) {
		Set<MinTerm> result = new HashSet<MinTerm>();
		Set<MinTerm> remainingMinTerms = partitions.flatten();

		for (int i = 0; i < partitions.size() - 1; i++) {
			MinTermList currentPartition = partitions.get(i);
			MinTermList nextPartition = partitions.get(i + 1);

			for (MinTerm term1 : currentPartition) {
				for (MinTerm term2 : nextPartition) {
					MinTerm merged = term1.merge(term2);

					if (merged != null) {
						result.add(merged);
						remainingMinTerms.remove(term1);
						remainingMinTerms.remove(term2);
					}
				}
			}
		}

		result.addAll(remainingMinTerms);

		return Util.partitionMinTerms(partitions.size() - 1, result);
	}
}
