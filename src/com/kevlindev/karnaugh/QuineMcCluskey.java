package com.kevlindev.karnaugh;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class QuineMcCluskey {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args != null && args.length > 0) {
			QuineMcCluskey minimizer = new QuineMcCluskey();

			for (String arg : args) {
				minimizer.minimize(arg);
			}
		}
	}

	private Partitions getEssentialPrimeImplicants(Function f, Partitions partitions) {
		Set<Integer> indexes = new HashSet<Integer>(f.getMinTermIndexes());
		Partitions solutions = new Partitions();

		// collect all terms
		List<MinTerm> allTerms = new ArrayList<MinTerm>();

		for (MinTermList partition : partitions) {
			allTerms.addAll(partition.getMinTerms());
		}

		boolean[] selectors = new boolean[allTerms.size()];

		while (true) {
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

			// advance
			int i;
			for (i = 0; i < selectors.length; i++) {
				if (selectors[i] == false) {
					selectors[i] = true;
					break;
				} else {
					selectors[i] = false;
				}
			}

			if (i == selectors.length) {
				break;
			}
		}

		solutions.sort();

		return solutions;
	}

	/**
	 * getMinimizedFunction
	 * 
	 * @param f
	 * @param primeImplicants
	 * @return
	 */
	private String getMinimizedFunction(Function f, List<String> primeImplicants) {
		List<String> products = new ArrayList<String>();
		List<String> arguments = f.getArguments();

		for (String primeImplicant : primeImplicants) {
			List<String> sums = new ArrayList<String>();

			for (int i = 0; i < arguments.size(); i++) {
				char c = primeImplicant.charAt(i);

				switch (c) {
				case '0':
					sums.add("~" + arguments.get(i));
					break;

				case '1':
					sums.add(arguments.get(i));
					break;
				}
			}

			products.add(Util.join(" & ", sums));
		}

		return f.getName() + " = " + Util.join(" | ", products);
	}

	/**
	 * minimize
	 * 
	 * @param file
	 */
	public void minimize(String file) {
		Function f = readFile(file);
		Partitions partitions = Util.partitionMinTerms(f.getBits(), f.getMinTerms());

		System.out.println("Initial Partitions");
		System.out.println("==================");
		System.out.println(partitions);

		// TODO: loop until no change
		for (int i = 0;; i++) {
			Partitions newPartitions = reducePartitions(partitions);
			boolean match = true;
			
			for (int j = 0; j < newPartitions.size(); j++) {
				if (newPartitions.get(j).equals(partitions.get(j)) == false) {
					match = false;
					break;
				}
			}
			
			if (match) {
				break;
			} else {
				System.out.println();
				System.out.println("Pass " + (i + 1));
				System.out.println("======");
				System.out.println(partitions);
				
				partitions = newPartitions;
			}
		}

		List<String> primeImplicants = partitions.getPrimeImplicants();
		Partitions solutions = getEssentialPrimeImplicants(f, partitions);

		printSection("Function", f.toString());
		printSection("Prime Implicants", Util.join(", ", primeImplicants));
		System.out.println();
		System.out.println("Essential Prime Implicants");
		System.out.println("==========================");
		System.out.println(solutions);

		if (solutions != null && solutions.size() > 0) {
			MinTermList solution = solutions.get(0);
			List<String> essentialPrimeImplicants = new ArrayList<String>();
			for (MinTerm term : solution) {
				essentialPrimeImplicants.add(Util.join("", term.getBits()));
			}
			Collections.sort(essentialPrimeImplicants, Partitions.IMPLICANT_COMPARATOR);
			printSection("Minimized Function", getMinimizedFunction(f, essentialPrimeImplicants));
		} else {
			System.out.println("No solution found. This most likely indicates an error condition in this application.");
		}
	}

	/**
	 * printSection
	 * 
	 * @param header
	 * @param content
	 */
	private void printSection(String header, String content) {
		System.out.println();
		System.out.println(header);
		System.out.println(Util.repeat("=", header.length()));
		System.out.println(content);
	}

	/**
	 * readFile
	 * 
	 * @param file
	 * @return
	 */
	public Function readFile(String file) {
		Scanner scanner = null;
		Function f = null;

		try {
			scanner = new Scanner(new FileInputStream(file), "utf-8");

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] parts = line.split("\\s*=\\s*");
				boolean valid = false;

				if (parts.length == 2) {
					String function = parts[0];
					int lparen = function.indexOf('(');
					int rparen = function.indexOf(')');

					if (lparen != -1 && rparen != -1) {
						String name = function.substring(0, lparen).trim();
						String[] args = function.substring(lparen + 1, rparen).split("\\s*,\\s*");
						List<TruthValue> values = new ArrayList<TruthValue>();

						for (String valueString : parts[1].split("\\s*,\\s*")) {
							values.add("1".equals(valueString) ? TruthValue.TRUE : TruthValue.FALSE);
						}

						int bits = (int) Math.ceil(Math.log(values.size()) / Math.log(2));

						f = new Function(name, bits);
						f.setArguments(Arrays.asList(args));
						f.setValues(values);

						valid = true;
					}
				}

				if (valid == false) {
					System.err.println("Invalid content: " + line);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}

		return f;
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
