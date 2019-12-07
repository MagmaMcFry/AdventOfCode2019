package aoc19.days;

import aoc19.Intcode;
import aoc19.PermutationEngine;
import aoc19.util.FileReader;

import java.util.Arrays;

public class Day7Solver {
	public static void main(String[] args) {
		solvePart1();
		solvePart2();
	}

	private static int[] getCode() {
		return Arrays.stream(FileReader.readLines("/day7/code.txt").get(0).split(",")).mapToInt(Integer::valueOf).toArray();
	}

	static void solvePart1() {
		int[] code = getCode();
		int numAmplifiers = 5;
		int[] scratchCode = new int[code.length];
		PermutationEngine pe = new PermutationEngine(numAmplifiers);
		int[] maxOutputSignal = new int[1];
		pe.run(perm -> {
			int signal = 0;
			for (int i = 0; i < numAmplifiers; ++i) {
				System.arraycopy(code, 0, scratchCode, 0, code.length);
				Intcode amplifier = new Intcode(scratchCode);
				amplifier.setInputArray(new int[]{perm[i], signal});
				int[] outputSignal = amplifier.setOutputCollector();
				amplifier.runCode();
				signal = outputSignal[0];
			}
			if (signal > maxOutputSignal[0]) {
				maxOutputSignal[0] = signal;
			}
		});
		System.out.println("Max possible output signal: " + maxOutputSignal[0]);
	}

	static void solvePart2() {
		int[] code = getCode();
		int numAmplifiers = 5;
		int[][] scratchCodes = new int[numAmplifiers][code.length];
		PermutationEngine pe = new PermutationEngine(numAmplifiers);
		int[] maxOutputSignal = new int[1];
		pe.run(perm -> {

			final int[] signal = new int[1];
			Intcode[] amplifiers = new Intcode[numAmplifiers];
			for (int i = 0; i < numAmplifiers; ++i) {
				System.arraycopy(code, 0, scratchCodes[i], 0, code.length);
				amplifiers[i] = new Intcode(scratchCodes[i]);
				amplifiers[i].setInputArray(new int[]{numAmplifiers+perm[i]});
				amplifiers[i].setOutputMethod(o->signal[0] = o);
				amplifiers[i].runCode();
			}
			int loops = 0;
			while (!amplifiers[numAmplifiers-1].isDone()) {
				++loops;
				for (int i = 0; i < numAmplifiers; ++i) {
					amplifiers[i].setInputArray(new int[]{signal[0]});
					amplifiers[i].runCode();
				}
			}
			//System.out.println("Loops run: " + loops);
			if (signal[0] > maxOutputSignal[0]) {
				maxOutputSignal[0] = signal[0];
			}
		});
		System.out.println("Max possible output signal: " + maxOutputSignal[0]);
	}
}
