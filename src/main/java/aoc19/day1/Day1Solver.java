package aoc19.day1;

import aoc19.util.FileReader;

import java.util.List;

public class Day1Solver {
	public static void main(String[] args) {
		solvePart1();
		solvePart2();
	}

	static List<Integer> getMasses() {
		return FileReader.readListOfInts("/day1/modules.txt");
	}

	static void solvePart1() {
		List<Integer> masses = getMasses();
		int fuel = 0;
		for (int mass : masses) {
			fuel += getFuel(mass);
		}
		System.out.println("Fuel mass: " + fuel);
	}

	static int getFuel(int mass) {
		if (mass <= 8) return 0;
		return (mass/3)-2;
	}

	static void solvePart2() {
		List<Integer> masses = getMasses();
		int fuel = 0;
		for (int mass : masses) {
			fuel += getFuelRecursive(mass);
		}
		System.out.println("Recursive fuel mass: " + fuel);
	}

	static int getFuelRecursive(int mass) {
		int fuel = getFuel(mass);
		if (fuel <= 8) return fuel;
		return fuel + getFuelRecursive(fuel);
	}

}
