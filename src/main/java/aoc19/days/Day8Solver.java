package aoc19.days;

import aoc19.util.FileReader;

import java.io.IOException;
import java.io.InputStream;

public class Day8Solver {
	public static void main(String[] args) {
		solvePart1();
		solvePart2();
	}

	private static InputStream getInput() {
		return FileReader.getInputStream("/day8/image.txt");
	}

	private static void solvePart1() {
		int width = 25, height = 6;
		InputStream input = getInput();
		int bestCriterion = Integer.MAX_VALUE;
		int bestValue = 0;
		int numLayers = 0;
		try {
			LOOP: while (true) {
				int[] counts = new int[3];
				for (int row = 0; row < height; ++row) {
					for (int col = 0; col < 25; ++col) {
						int val = input.read();
						if (val == -1 || val == '\n' || val == '\r') break LOOP;
						++counts[val - '0'];
					}
				}
				if (counts[0] < bestCriterion) {
					bestCriterion = counts[0];
					bestValue = counts[1] * counts[2];
				}
				++numLayers;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException e) {
			// done!
		}
		System.out.println("Best value: " + bestValue);
		System.out.println("Number of layers: " + numLayers);
	}

	private static void solvePart2() {
		int width = 25, height = 6;
		InputStream input = getInput();
		int[][] image = new int[height][width];
		try {
			LOOP: for (int layer = 0; ; ++layer) {
				for (int row = 0; row < height; ++row) {
					for (int col = 0; col < 25; ++col) {
						int val = input.read();
						switch(val) {
							case '0':
							case '1':
							case '2':
								if (layer == 0 || image[row][col] == 2) {
									image[row][col] = val - '0';
								}
								break;
							default: break LOOP;
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException e) {
			// done!
		}
		System.out.println("Message:");
		for (int row = 0; row < height; ++row) {
			for (int col = 0; col < 25; ++col) {
				System.out.print(image[row][col]);
			}
			System.out.println();
		}
	}
}
