package aoc19.days;

import aoc19.util.FileReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Day6Solver {
	public static void main(String[] args) {
		solvePart1();
		solvePart2();
	}

	public static List<String> getInput() {
		return FileReader.readLines("/day6/orbits.txt");
	}


	public static Map<String, String> computeParentMap(List<String> directOrbitStrings) {
		Map<String, String> parentMap = new HashMap<>();
		for (String s : directOrbitStrings) {
			String[] parts = s.split("\\)");
			parentMap.put(parts[1], parts[0]);
		}
		return parentMap;
	}

	public static int getHeight(Map<String, String> parentMap, String pos) {
		int height = 0;
		String currentPlanet = pos;
		while (parentMap.containsKey(currentPlanet)) {
			++height;
			currentPlanet = parentMap.get(currentPlanet);
		}
		return height;
	}

	public static int countOrbits(Map<String, String> parentMap) {
		int count = 0;
		for (String s : parentMap.keySet()) {
			count += getHeight(parentMap, s);
		}
		return count;
	}

	public static int getDistance(Map<String, String> parentMap, String pos1, String pos2) {
		int h1 = getHeight(parentMap, pos1);
		int h2 = getHeight(parentMap, pos2);
		int dist = Math.abs(h1-h2);
		while (h1 > h2) {
			pos1 = parentMap.get(pos1);
			--h1;
		}
		while (h1 < h2) {
			pos2 = parentMap.get(pos2);
			--h2;
		}
		while (!Objects.equals(pos1, pos2)) {
			pos1 = parentMap.get(pos1);
			pos2 = parentMap.get(pos2);
			dist += 2;
		}
		return dist;
	}

	private static void solvePart1() {
		Map<String, String> parentMap = computeParentMap(getInput());
		int count = countOrbits(parentMap);
		System.out.println("Orbit count: " + count);
	}

	private static void solvePart2() {
		Map<String, String> parentMap = computeParentMap(getInput());
		String pos1 = parentMap.get("YOU");
		String pos2 = parentMap.get("SAN");
		int dist = getDistance(parentMap, pos1, pos2);
		System.out.println("Distance between parent bodies of YOU and SAN: " + dist);
	}
}
