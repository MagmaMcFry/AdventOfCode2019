package aoc19.days;

import aoc19.Longcode;
import aoc19.util.FileReader;

import java.util.Arrays;
import java.util.List;

public class Day11Solver {
	public static void main(String[] args) {
		solvePart1();
		solvePart2();
	}

	private static long[] getCode() {
		return Arrays.stream(FileReader.readLines("/day11/code.txt").get(0).split(",")).mapToLong(Long::parseLong).toArray();
	}

	private static int runPanelBot(long[] code, boolean startWhite, boolean print) {
		Longcode longcode = new Longcode(code);
		longcode.setInputArray(new long[0]);
		List<Long> outputList = longcode.setOutputList();
		int radius = 100;
		int[][] array = new int[2 * radius][2 * radius];
		int px = radius, py = radius;
		if (startWhite) {
			array[py][px] = 1;
		}
		int dx = 0, dy = -1;
		int panelCount = 0;
		while (!longcode.isDone()) {
			longcode.runCode();
			for (int i = 0; i < outputList.size(); i += 2) {
				long panelColor = outputList.get(i);
				if (panelColor == 0) {
					if (array[py][px] == 1) {
						array[py][px] = 2;
					}
				} else if (panelColor == 1) {
					if (array[py][px] == 0) {
						++panelCount;
					}
					array[py][px] = 1;
				}
				long rotation = outputList.get(i + 1);
				if (rotation == 0) {
					int temp = dx;
					dx = dy;
					dy = -temp;
				} else {
					int temp = dx;
					dx = -dy;
					dy = temp;
				}
				px += dx;
				py += dy;
			}
			outputList.clear();
			longcode.setInputArray(new long[]{array[py][px] % 2});
		}
		if (print) {
			char[] vals = new char[]{' ', '#', '.'};
			for (int[] row : array) {
				for (int val : row) {
					System.out.print(vals[val]);
				}
				System.out.println();
			}
		}
		return panelCount;
	}

	private static void solvePart1() {
		int panelCount = runPanelBot(getCode(), false, false);
		System.out.println("Number of panels painted: " + panelCount);
	}

	private static void solvePart2() {
		System.out.println("Panel image:");
		runPanelBot(getCode(), true, true);
	}
}
