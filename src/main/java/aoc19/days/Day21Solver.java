package aoc19.days;

import aoc19.Longcode;
import aoc19.util.FileReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Day21Solver {
	public static void main(String[] args) {
		solvePart1();
		solvePart2();
	}

	private static void solvePart1() {
		goWalk(
				"NOT D T",
				"OR C T",
				"AND A T",
				"NOT T J");
	}

	private static void solvePart2() {
		System.out.println("Evolving!");
		evolve();
	}

	private static long[] getCode() {
		return FileReader.readLongcode("/day21/code.txt");
	}

	public static void goWalk(String... ops) {
		Longcode code = new Longcode(getCode());
		code.setInputArray((String.join("\n", ops) + "\nWALK\n").chars().mapToLong(i -> i).toArray());
		List<Long> output = code.setOutputList();
		code.runCode();
		System.out.println(output.size());
		if (output.size() <= 100) {
			System.out.println("Success! Output size: " + output.size() + ", total damage surveyed: " + output.get(output.size() - 1));
		} else {
			System.out.println("Failure!");
			for (long outputElement : output) {
				System.out.print((char) outputElement);
			}
		}
	}

	public static void goRun(String... ops) {
		Longcode code = new Longcode(getCode());
		code.setInputArray((String.join("\n", ops) + "\nRUN\n").chars().mapToLong(i -> i).toArray());
		List<Long> output = code.setOutputList();
		code.runCode();
		for (long outputElement : output) {
			System.out.print((char) outputElement);
		}
		if (output.size() <= 100) {
			System.out.println("Success! Output size: " + output.size() + ", total damage surveyed: " + output.get(output.size() - 1));
		} else {
			// Display Intcode output
//			System.out.println("Failure!");
//			for (long outputElement : output) {
//				System.out.print((char) outputElement);
//			}
		}
	}
	static String[] validOps;

	static {
		String[] starts = {"AND", "OR", "NOT"};
		String[] mids = {"J", "A", "B", "C", "D", "E", "F", "G", "H", "I", "T"};
		String[] ends = {"J", "T"};
		List<String> opsList = new ArrayList<>();
		for (String start : starts) {
			for (String mid : mids) {
				for (String end : ends) {
					opsList.add(start + " " + mid + " " + end);
				}
			}
		}
		validOps = opsList.toArray(new String[0]);
	}


	private final static int PROGRAM_SIZE = 15;
	private final static int POOL_SIZE = 100;
	private final static int REPRODUCTION_RATE = 3;
	private static final int FITNESS_SPREAD = 1000;
	private static final int AGE_PENALTY = 50;
	private final static int MAX_GENERATIONS = 10000;
	private final static int SUCCESS_THRESHOLD = 1_000_000_000;
	public static void evolve() {
		String[] initialProgram = new String[PROGRAM_SIZE];
		Arrays.fill(initialProgram, "AND J J");
		List<SpringDroidGenes> genePool = new ArrayList<>();
		for (int i = 0; i < POOL_SIZE; ++i) {
			genePool.add(SpringDroidGenes.of(initialProgram));
		}
		System.out.println("Initial fitness: " + genePool.get(0).fitness);
		for (int s = 0; s < MAX_GENERATIONS; ++s) {
			if (s % 10 == 0) {
//				goRun(genePool.get(0).program);
				System.out.println("Current step: " + s);
				System.out.println("Current fitness: " + genePool.get(0).fitness);
			}
			for (int r = 0; r < REPRODUCTION_RATE; ++r) {
				for (int i = 0; i < POOL_SIZE; ++i) {
					genePool.add(genePool.get(i).mutate());
				}
			}
			genePool.sort(SpringDroidGenes::compareTo);
			genePool = new ArrayList<>(genePool.subList(0, POOL_SIZE));
			if (genePool.get(0).fitness > SUCCESS_THRESHOLD) {
				System.out.println("Evolution complete! It took " + s + " generations.");
				goRun(genePool.get(0).program);
				System.out.println("Final fitness: " + genePool.get(0).fitness);
				System.out.println("Final program: \n" + String.join("\n", genePool.get(0).program));
				return;
			}
		}
	}

	private static class SpringDroidGenes implements Comparable<SpringDroidGenes> {
		private static final Random RAND = new Random();
		private final String[] program;
		private long fitness;

		public SpringDroidGenes(String[] program) {
			this.program = program;
			this.fitness = getFitness(program) - RAND.nextInt(FITNESS_SPREAD);
		}


		public static SpringDroidGenes of(String[] program) {
			return new SpringDroidGenes(program);
		}

		private static long getFitness(String[] program) {
			Longcode code = new Longcode(getCode());
			code.setInputArray((String.join("\n", program) + "\nRUN\n").chars().mapToLong(i -> i).toArray());
			code.setOutputCounter(100);
			code.runCode();
			if (code.isDone()) {
				return Long.MAX_VALUE;
			} else {
				return code.getRuntime();
			}
		}

		@Override
		public int compareTo(SpringDroidGenes o) {
			return Long.compare(o.fitness, fitness); // Higher fitness comes first
		}

		public SpringDroidGenes mutate() {
			fitness -= AGE_PENALTY;
			List<String> newProgramList = new ArrayList<>(Arrays.asList(this.program));
			int mutation = RAND.nextInt(5);
			switch(mutation) {
				case 0:
				case 1:
				case 2:
					// Point mutation
					newProgramList.set(RAND.nextInt(PROGRAM_SIZE), validOps[RAND.nextInt(validOps.length)]);
					break;
				case 3:
				case 4:
					// Delete + insert
					newProgramList.remove(RAND.nextInt(PROGRAM_SIZE));
					newProgramList.add(RAND.nextInt(PROGRAM_SIZE), validOps[RAND.nextInt(validOps.length)]);
					break;
			}
			return new SpringDroidGenes(newProgramList.toArray(new String[0]));
		}
	}
}
