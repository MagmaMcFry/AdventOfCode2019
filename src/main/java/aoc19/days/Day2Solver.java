package aoc19.days;

import aoc19.Intcode;
import aoc19.util.FileReader;

import java.util.Arrays;
import java.util.Locale;

public class Day2Solver {
	public static void main(String[] args) {
		solvePart1();
		solvePart2();
	}

	static int[] getCode() {
		return Arrays.stream(FileReader.readLines("/day2/code.txt").get(0).split(",")).mapToInt(Integer::valueOf).toArray();
	}

	static void solvePart1() {
		int[] code = getCode();
		code[1] = 12;
		code[2] = 2;
		int result = Intcode.runAndReturnFirstElement(code);
		System.out.println("A: Final result: " + result);
	}

	static void solvePart2() {
		int[] code = getCode();
		int[] testCode = new int[code.length];
		for (int noun = 0; noun < 100; ++noun) {
			for (int verb = 0; verb < 100; ++verb) {
				System.arraycopy(code, 0, testCode, 0, code.length);
				testCode[1] = noun;
				testCode[2] = verb;
				int result = Intcode.runAndReturnFirstElement(testCode);
				if (result == 19690720) {
					System.out.println(String.format(Locale.ROOT, "B: Required parameters: %02d%02d", noun, verb));
				}
			}
		}
	}
}
