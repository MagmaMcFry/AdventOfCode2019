package aoc19.days;

import aoc19.Longcode;
import aoc19.util.FileReader;

import java.util.Arrays;

public class Day9Solver {
	public static void main(String[] args) {
		solvePart1();
		solvePart2();
	}

	private static long[] getCode() {
		return Arrays.stream(FileReader.readLines("/day9/code.txt").get(0).split(",")).mapToLong(Long::parseLong).toArray();
	}

	private static void solvePart1() {
		long[] outputs = Longcode.runAndCollectOutputsToArray(getCode(), new long[]{1});
		System.out.println("Number of outputs: " + outputs.length);
		System.out.println("Boost code: " + outputs[outputs.length-1]);
	}

	private static void solvePart2() {
		Longcode longcode = new Longcode(getCode());
		longcode.setInputArray(new long[]{2});
		long[] outputs = longcode.setOutputCollector();
		longcode.runCode();
		System.out.println("Distress signal coordinates: " + outputs[0]);
		System.out.println("Runtime: " + longcode.getRuntime());
	}
}
