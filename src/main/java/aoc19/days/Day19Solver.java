package aoc19.days;

import aoc19.Longcode;
import aoc19.util.FileReader;

import java.util.Arrays;

public class Day19Solver {
	public static void main(String[] args) {
		solvePart1();
		solvePart2();
	}

	private static long[] code = null;
	private static long[] getCode() {
		if (code == null) {
			code = FileReader.readLongcode("/day19/code.txt");
		}
		return Arrays.copyOf(code, code.length);
	}

	private static int scanPos(int px, int py) {
		Longcode longcode = new Longcode(getCode());
		longcode.setInputArray(new long[]{px, py});
		long[] output = longcode.setOutputCollector();
		longcode.runCode();
		//System.out.println("Code is done? " + (longcode.isDone() ? "Yes" : "No"));
		return (int) output[0];
	}

	private static void solvePart1() {
		int numAffectedPoints = 0;
		for (int y = 0; y < 50; ++y) {
			for (int x = 0; x < 50; ++x) {
				int pointFound = scanPos(x, y);
				System.out.print(pointFound == 1 ? "#" : ".");
				numAffectedPoints += scanPos(x, y);
			}
			System.out.println();
		}
		System.out.println("Points affected by tractor beam: " + numAffectedPoints);
	}

	private static void solvePart2() {
		// Assumes the beam is cone-shaped (formed by two linear inequalities) and both side slopes are greater than 1
		int minSlopeTimes50 = 0, maxSlopeTimes50 = 0;
		for (int x = 0; x <= 50; ++x) {
			if (scanPos(x, 50) == 1) {
				if (minSlopeTimes50 == 0) {
					minSlopeTimes50 = x-1;
				}
				maxSlopeTimes50 = x+1;
			}
		}
		for (int y = 0; y < 10000; ++y) {
			for (int x = ((y+99)*minSlopeTimes50)/50; 50*(x+99) < maxSlopeTimes50*y; ++x) { // Only look for x in a certain range
				if (scanPos(x+99, y) * scanPos(x,y+99) > 0) { // Only need to check these two corners
					System.out.println("Found the ship at location code " + (10000 * x + y));
					return;
				}
			}
		}
		System.out.println("Santa not found :(");
	}

}
