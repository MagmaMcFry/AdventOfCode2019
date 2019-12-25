package aoc19.days;

import aoc19.util.FileReader;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day24Solver {
	public static void main(String[] args) {
		solvePart1();
		solvePart2();
	}

	public final static int SIZE = 5;
	public final static int PADDED_SIZE = 2+SIZE;

	static int[][] toBugArray(List<String> mazeStrings) {
		int[][] array = new int[2+SIZE][];
		array[0] = new int[2+SIZE];
		array[SIZE+1] = new int[2+SIZE];
		int j = 1;
		for (String s : mazeStrings) {
			int[] row = new int[2+SIZE];
			array[j++] = row;
			int i = 1;
			for (char c : s.toCharArray()) {
				row[i++] = (c == '#' ? 1 : 0);
			}
		}
		return array;
	}

	private static void solvePart1() {
		int[][] array = toBugArray(getInput());
		int[][] newArray = toBugArray(getInput());
		Set<Integer> diversities = new HashSet<>();
		diversities.add(getDiversity(array));
		int iters = 0;
		printBugArray(array);
		while (iters++ < 10000) {
			for (int y = 1; y < SIZE+1; ++y) {
				for (int x = 1; x < SIZE+1; ++x) {
					int neighbors = 2 * (array[y][x + 1] + array[y][x - 1] + array[y - 1][x] + array[y + 1][x]) + array[y][x];
					newArray[y][x] = (neighbors >= 2 && neighbors <= 4) ? 1 : 0;
				}
			}
			int[][] temp = array;
			array = newArray;
			newArray = temp;
			printBugArray(array);
			int diversity = getDiversity(array);
			if (!diversities.add(diversity)) {
				System.out.println("Iters: " + iters);
				System.out.println("Diversity: " + diversity);
				break;
			}
		}
	}

	static int getDiversity(int[][] array) {
		int mask = 1;
		int diversity = 0;
		for (int y = 1; y < SIZE+1; ++y) {
			for (int x = 1; x < SIZE+1; ++x) {
				if (array[y][x] == 1) {
					diversity += mask;
				}
				mask <<= 1;
			}
		}
		return diversity;
	}

	static void printBugArray(int[][] array) {
		for (int[] row : array) {
			for (int cell : row) {
				System.out.print(cell == 1 ? "#" : ".");
			}
			System.out.println();
		}
		System.out.println();
	}

	private static List<String> getInput() {
		return FileReader.readLines("/day24/bugs.txt");
//		return Arrays.asList(
//				"....#",
//				"#..#.",
//				"#..##",
//				"..#..",
//				"#...."
//		);
	}


	static int[][][] makeDeepArray(int[][] array, int spread) {
		int[][][] deepArray = new int[2*spread+3][2+SIZE][2+SIZE];
		deepArray[spread+1] = array;
		return deepArray;
	}

	private static void solvePart2() {
		System.out.println("Solution: " + simulateDeeply(getInput(), 200));
	}

	static int simulateDeeply(List<String> input, int numSteps) {
		int[][][] deepArray = makeDeepArray(toBugArray(input), numSteps);
		int[][][] newDeepArray = makeDeepArray(toBugArray(input), numSteps);
		int DEPTH = 2*numSteps+1;
		for (int time = 0; time < numSteps; ++time) {
			for (int z = 1; z < DEPTH+1; ++z) {
				for (int y = 1; y < SIZE+1; ++y) {
					for (int x = 1; x < SIZE+1; ++x) {
						int neighbors = 2*getNeighbors(deepArray, x, y, z) + deepArray[z][y][x];
						newDeepArray[z][y][x] = (neighbors >= 2 && neighbors <= 4) ? 1 : 0;
					}
				}
			}
			int[][][] temp = deepArray;
			deepArray = newDeepArray;
			newDeepArray = temp;
		}
		for (int[][] array : deepArray) {
			printBugArray(array);
		}
		return countBugs(deepArray);
	}

	private static int countBugs(int[][][] deepArray) {
		int total = 0;
		for (int[][] layer : deepArray) {
			for (int[] row : layer) {
				for (int cell : row) {
					total += cell;
				}
			}
		}
		return total;
	}


	private static int getNeighbors(int[][][] deepArray, int x, int y, int z) {
		switch (5*(y-1)+(x-1)) {
			case 0:
				return deepArray[z][1][2] + deepArray[z][2][1] + deepArray[z-1][2][3] + deepArray[z-1][3][2];
			case 4:
				return deepArray[z][1][4] + deepArray[z][2][5] + deepArray[z-1][2][3] + deepArray[z-1][3][4];
			case 20:
				return deepArray[z][5][2] + deepArray[z][4][1] + deepArray[z-1][4][3] + deepArray[z-1][3][2];
			case 24:
				return deepArray[z][5][4] + deepArray[z][4][5] + deepArray[z-1][4][3] + deepArray[z-1][3][4];
			case 1:
			case 2:
			case 3:
				return deepArray[z][1][x-1] + deepArray[z][1][x+1] + deepArray[z][2][x] + deepArray[z-1][2][3];
			case 21:
			case 22:
			case 23:
				return deepArray[z][5][x-1] + deepArray[z][5][x+1] + deepArray[z][4][x] + deepArray[z-1][4][3];
			case 5:
			case 10:
			case 15:
				return deepArray[z][y-1][1] + deepArray[z][y+1][1] + deepArray[z][y][2] + deepArray[z-1][3][2];
			case 9:
			case 14:
			case 19:
				return deepArray[z][y-1][5] + deepArray[z][y+1][5] + deepArray[z][y][4] + deepArray[z-1][3][4];
			case 7:
				return deepArray[z][1][3] + deepArray[z][2][2] + deepArray[z][2][4]
						+ deepArray[z+1][1][1] + deepArray[z+1][1][2] + deepArray[z+1][1][3] + deepArray[z+1][1][4] + deepArray[z+1][1][5];
			case 17:
				return deepArray[z][5][3] + deepArray[z][4][2] + deepArray[z][4][4]
						+ deepArray[z+1][5][1] + deepArray[z+1][5][2] + deepArray[z+1][5][3] + deepArray[z+1][5][4] + deepArray[z+1][5][5];
			case 11:
				return deepArray[z][3][1] + deepArray[z][2][2] + deepArray[z][4][2]
						+ deepArray[z+1][1][1] + deepArray[z+1][2][1] + deepArray[z+1][3][1] + deepArray[z+1][4][1] + deepArray[z+1][5][1];
			case 13:
				return deepArray[z][3][5] + deepArray[z][2][4] + deepArray[z][4][4]
						+ deepArray[z+1][1][5] + deepArray[z+1][2][5] + deepArray[z+1][3][5] + deepArray[z+1][4][5] + deepArray[z+1][5][5];
			case 6:
			case 8:
			case 16:
			case 18:
				return deepArray[z][y][x+1] + deepArray[z][y][x-1] +deepArray[z][y+1][x] +deepArray[z][y-1][x];
			default:
				return 0;
		}
	}
}
