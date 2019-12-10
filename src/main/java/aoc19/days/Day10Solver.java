package aoc19.days;

import aoc19.util.FileReader;
import aoc19.util.I2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day10Solver {
	public static void main(String[] args) {
		solvePart1();
		solvePart2();
	}

	private static String[] getInput() {
		return FileReader.readLines("/day10/grid.txt").toArray(new String[0]);
	}

	public static boolean[][] getGrid(String[] lines) {
		boolean[][] grid = new boolean[lines.length][lines[0].length()];
		for (int y = 0; y < lines.length; ++y) {
			String line = lines[y];
			for (int x = 0; x < line.length(); ++x) {
				grid[y][x] = line.charAt(x) == '#';
			}
		}
		return grid;
	}

	public static List<I2> getPositions(boolean[][] grid) {
		List<I2> positions = new ArrayList<>();
		for (int y = 0; y < grid.length; ++y) {
			for (int x = 0; x < grid[0].length; ++x) {
				if (grid[y][x]) {
					positions.add(new I2(x, y));
				}
			}
		}
		return positions;
	}

	public static Map<I2, Map<I2, I2>> getDirMap(List<I2> positions) {
		Map<I2, Map<I2, I2>> dirMap = new HashMap<>();
		for (I2 p1 : positions) {
			Map<I2, I2> p1Map = new HashMap<>();
			dirMap.put(p1, p1Map);
			for (I2 p2 : positions) {
				if (p1.equals(p2)) continue;
				I2 d = I2.dir(p1, p2);
				I2 p2b = p1Map.get(d);
				if (p2b == null || p1.d1(p2) < p1.d1(p2b)) {
					p1Map.put(d, p2);
				}
			}
		}
		return dirMap;
	}

	public static I2 getPosWithMaxDirs(List<I2> positions, Map<I2, Map<I2, I2>> dirMap) {
		I2 result = positions.get(0);
		int maxDirs = 0;
		for (I2 position : positions) {
			int dirs = dirMap.get(position).size();
			if (dirs > maxDirs) {
				result = position;
				maxDirs = dirs;
			}
		}
		return result;
	}

	private static void solvePart1() {
		boolean[][] grid = getGrid(getInput());
		List<I2> positions = getPositions(grid);
		Map<I2, Map<I2, I2>> dirMap = getDirMap(positions);
		I2 stationPos = getPosWithMaxDirs(positions, dirMap);
		System.out.println("Best pos: " + stationPos + ", max dirs: " + dirMap.get(stationPos).size());
	}

	private static void solvePart2() {
		boolean[][] grid = getGrid(getInput());
		List<I2> positions = getPositions(grid);
		Map<I2, Map<I2, I2>> dirMap = getDirMap(positions);
		I2 stationPos = getPosWithMaxDirs(positions, dirMap);
		List<I2> dirsToVaporize = new ArrayList<>(dirMap.get(stationPos).keySet());
		dirsToVaporize.sort(Comparator.comparingDouble(dir -> -Math.atan2(dir.x, dir.y)));
		System.out.println("Asteroid to destroy: " + dirMap.get(stationPos).get(dirsToVaporize.get(199)));
	}
}
