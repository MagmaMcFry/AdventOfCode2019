package aoc19;

import java.util.Arrays;
import java.util.function.Consumer;

public class PermutationEngine {
	private final int numSymbols;
	private final int[] mainArray;
	private boolean done = false;

	public PermutationEngine(int numSymbols) {
		this.numSymbols = numSymbols;
		this.mainArray = new int[numSymbols];
		for (int i = 0; i < numSymbols; ++i) {
			mainArray[i] = i;
		}
	}

	public void run(Consumer<int[]> consumer) {
		while (!done) {
			consumer.accept(mainArray);
			step();
		}
	}

	private void step() {
		int descSuffixStart = numSymbols-1;
		while(descSuffixStart > 0 && mainArray[descSuffixStart-1] > mainArray[descSuffixStart]) {
			descSuffixStart--;
		}
		if (descSuffixStart == 0) {
			done = true;
			return;
		}
		int prevElement = mainArray[descSuffixStart-1];
		for (int i = numSymbols-1; i >= descSuffixStart; --i) {
			if (prevElement < mainArray[i]) {
				mainArray[descSuffixStart-1] = mainArray[i];
				mainArray[i] = prevElement;
				Arrays.sort(mainArray, descSuffixStart, numSymbols);
				break;
			}
		}
	}

	private boolean done() {
		return done;
	}
}
