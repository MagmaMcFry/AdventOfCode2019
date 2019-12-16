package aoc19.days;

import aoc19.util.FileReader;

import java.util.Arrays;

public class Day16Solver {
	public static void main(String[] args) {
		solvePart1();
		solvePart2();
	}

	private static int[] getInput() {
		return split(FileReader.readLines("/day16/signal.txt").get(0));
	}

	private static void solvePart1() {
		int[] signal = getInput();
		int[] result = repeatedFFT(signal, 100, 8);
		System.out.println("Signal size: " + signal.length);
		System.out.println("Signal beginning after 100 transforms: " + join(result, 0, 8));
	}

	private static void solvePart2() {
		int[] signal = getInput();
		int[] repeatedSignal = repeat(signal, 10000);
		int[] outputSignal = repeatedFFFT(repeatedSignal, 100);
		int outputOffset = Integer.parseInt(join(signal, 0, 7));
		System.out.println("Message: " + join(outputSignal, outputOffset, 8));
	}

	static int[] fft(int[] input) {
		int[] output = new int[input.length];
		for (int j = 0; j < output.length; ++j) {
			int oj = 0;
			for (int i = 0; i < input.length; ++i) {
				oj += input[i] * get_factor(i, j);
			}
			output[j] = Math.abs(oj % 10);
		}
		return output;
	}

	static int[] repeatedFFT(int[] input, int times, int outputSize) {
		for (int i = 0; i < times; ++i) {
			input = fft(input);
		}
		return Arrays.copyOf(input, outputSize);
	}

	static int[] ffft(int[] input) {
		// Uses the fact that most output signals can be represented as short sums of cumulations of the input signal.
		// Bad performance due to nonlocal memory access and tons of resulting cache faults.
		// Could be improved by optimizing iteration.
		int[] cumulInput = revCumul(input);
		int[] output = new int[input.length];
		for (int j = 0; j < output.length; ++j) {
			int oj = 0;
			for (int i = 1; (j + 1) * i <= input.length; i++) {
				oj += get_cumul_factor(i) * cumulInput[(j + 1) * i - 1];
			}
			output[j] = Math.abs(oj % 10);
		}
		return output;
	}

	static int[] repeatedFFFT(int[] input, int times) {
		for (int i = 0; i < times; ++i) {
			input = ffft(input);
		}
		return input;
	}

	private static int[] revCumul(int[] input) {
		int[] output = new int[input.length];
		int sum = 0;
		for (int i = input.length - 1; i >= 0; i--) {
			sum += input[i];
			output[i] = sum;
		}
		return output;
	}

	private final static int[] factors = {0, 1, 0, -1};

	private static int get_factor(int i, int j) {
		return factors[((i + 1) / (j + 1)) & 3];
	}

	private final static int[] cumulFactors = {1, 1, -1, -1};

	private static int get_cumul_factor(int i) {
		return cumulFactors[i & 3];
	}


	public static int[] split(String intSequence) {
		return intSequence.chars().map(i -> i - '0').toArray();
	}

	public static String join(int[] array, int offset, int length) {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < length; ++i) {
			s.append(array[offset + i]);
		}
		return s.toString();
	}

	static int[] repeat(int[] input, int copies) {
		int[] output = new int[input.length * copies];
		Arrays.setAll(output, i -> input[i % input.length]);
		return output;
	}
}
