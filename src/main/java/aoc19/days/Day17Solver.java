package aoc19.days;

import aoc19.Longcode;
import aoc19.util.FileReader;

import java.util.ArrayList;
import java.util.List;

public class Day17Solver {
	public static void main(String[] args) {
		solvePart1();
		solvePart2();
	}

	private static long[] getCode() {
		return FileReader.readLongcode("/day17/code.txt");
	}

	private static void solvePart1() {
		Longcode longcode = new Longcode(getCode());
		longcode.setInputArray(new long[0]);
		List<Long> list = longcode.setOutputList();
		longcode.runCode();
		for (long i : list) {
			System.out.print((char)i);
		}
		List<int[]> rows = new ArrayList<>();
		int rowStart = 0;
		for (int i = 0; i < list.size() - 1; ++i) { // Why does the intcode return double newline at the end :(
			if (list.get(i) == '\n') {
				//System.out.println("Line break at position " + i);
				rows.add(list.subList(rowStart, i).stream().mapToInt(Long::intValue).toArray());
				rowStart = i+1;
			}
		}
		int[][] grid = rows.toArray(new int[0][]);
		int intersectionSum = 0;
		for (int y = 1; y < grid.length-1; ++y) {
			for (int x = 1; x < grid[0].length-1; ++x) {
				if (grid[y][x] == '#'
				&& grid[y][x-1] == '#'
				&& grid[y][x+1] == '#'
				&& grid[y+1][x] == '#'
				&& grid[y-1][x] == '#'
				) {
					intersectionSum += x*y;
				}
			}
		}
		System.out.println("Intersection sum: " + intersectionSum);
	}

	private static void solvePart2() {
		long[] code = getCode();
		code[0] = 2;
		Longcode longcode = new Longcode(code);
		longcode.setInputArray(makeInstructionSet(
				// Solved it by hand
				"A,B,B,C,A,B,C,A,B,C",
				"L,6,R,12,L,4,L,6",
				"R,6,L,6,R,12",
				"L,6,L,10,L,10,R,6",
				"n"
		));
		long[] out = longcode.setOutputCollector();
		longcode.runCode();
		System.out.println("Dust collected: " + out[0]);
	}

	private static long[] makeInstructionSet(String... strings) {
		List<Long> ans = new ArrayList<>();
		for (String s : strings) {
			for (char c : s.toCharArray()) {
				ans.add((long) c);
			}
			ans.add((long) '\n');
		}
		return ans.stream().mapToLong(l->l).toArray();
	}

}
