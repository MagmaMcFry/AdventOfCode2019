package aoc19.days;

import aoc19.Intcode;
import aoc19.util.FileReader;

import java.util.Arrays;

public class Day5Solver {
	public static void main(String[] args) {
		solvePart1();
		solvePart2();
	}

	private static int[] getCode() {
		return Arrays.stream(FileReader.readLines("/day5/code.txt").get(0).split(",")).mapToInt(Integer::valueOf).toArray();
	}

	private static void solvePart1() {
		Intcode code = new Intcode(getCode());
		code.setInputConstant(1);
		int[] diagCode = code.setOutputCollector();
		code.runCode();
		System.out.println("Diag code for system 1: " + diagCode[0]);
	}

	private static void solvePart2() {
		Intcode code = new Intcode(getCode());
		code.setInputConstant(5);
		int[] diagCode = code.setOutputCollector();
		code.runCode();
		System.out.println("Diag code for system 5: " + diagCode[0]);
	}


}
