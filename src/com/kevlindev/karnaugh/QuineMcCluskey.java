package com.kevlindev.karnaugh;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
	 * getPrimeImplicants
	 * 
	 * @param partitions
	 * @return
	 */
	private List<String> getPrimeImplicants(List<List<MinTerm>> partitions) {
		Set<String> primeImplicantSet = new HashSet<String>();

		for (List<MinTerm> partition : partitions) {
			for (MinTerm term : partition) {
				primeImplicantSet.add(Util.join("", term.getBits()));
			}
		}

		ArrayList<String> primeImplicants = new ArrayList<String>(primeImplicantSet);
		Collections.sort(primeImplicants, new Comparator<String>() {
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
		});

		return primeImplicants;
	}

	/**
	 * minimize
	 * 
	 * @param file
	 */
	public void minimize(String file) {
		Function f = readFile(file);
		List<List<MinTerm>> partitions = Util.partitionMinTerms(f.getBits(), f.getMinTerms());

		System.out.println("Initial Partitions");
		System.out.println("==================");
		printPartitions(partitions);

		// TODO: loop until no change
		for (int i = 0; i < 2; i++) {
			System.out.println();
			System.out.println("Pass " + (i + 1));
			System.out.println("======");

			partitions = reducePartitions(partitions);
			printPartitions(partitions);
		}

		List<String> primeImplicants = getPrimeImplicants(partitions);

		printSection("Function", f.toString());
		printSection("Prime Implicants", Util.join(", ", primeImplicants));
		printSection("Essential Prime Implicants", "not yet supported");
		printSection("Minimized Function", getMinimizedFunction(f, primeImplicants));
	}

	/**
	 * printPartitions
	 * 
	 * @param partitions
	 */
	private void printPartitions(List<List<MinTerm>> partitions) {
		for (int i = 0; i < partitions.size(); i++) {
			List<MinTerm> partition = partitions.get(i);
			List<String> v = new ArrayList<String>();

			for (MinTerm minterm : partition) {
				String indexes = "[" + minterm.toString() + "]";

				v.add(indexes);
			}

			System.out.println(i + " : " + Util.join(", ", v));
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
	private List<List<MinTerm>> reducePartitions(List<List<MinTerm>> partitions) {
		List<MinTerm> result = new ArrayList<MinTerm>();
		List<MinTerm> usedMinTerms = new ArrayList<MinTerm>();

		for (int i = 0; i < partitions.size() - 1; i++) {
			List<MinTerm> currentPartition = partitions.get(i);
			List<MinTerm> nextPartition = partitions.get(i + 1);

			for (MinTerm term1 : currentPartition) {
				boolean used = false;

				for (MinTerm term2 : nextPartition) {
					MinTerm merged = term1.merge(term2);

					if (merged != null) {
						used = true;
						result.add(merged);
						usedMinTerms.add(term2);
					}
				}

				if (used == false) {
					result.add(term1);
				}
			}
		}

		result.removeAll(usedMinTerms);

		return Util.partitionMinTerms(partitions.size() - 1, result);
	}
}
