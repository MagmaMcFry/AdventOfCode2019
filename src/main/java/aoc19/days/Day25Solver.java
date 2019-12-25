package aoc19.days;

import aoc19.Longcode;
import aoc19.util.FileReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day25Solver {
	public static void main(String[] args) {
		solvePart1();
		solvePart2();
	}

	private static long[] getCode() {
		return FileReader.readLongcode("/day25/code.txt");
	}

	private static void solvePart1() {
		runCodeInteractively(getCode());
		// Correct mass: mutex, festive hat, whirled peas, coin
	}

	private static void solvePart2() {

	}

	private static void runCodeInteractively(long[] code) {
		Longcode longcode = new Longcode(code);
		longcode.setOutputMethod(i -> System.out.print((char)i));
		Scanner s = new Scanner(System.in);
		String inputString;
		List<Longcode> backups = new ArrayList<>();
		do {
			longcode.runCode();
			System.out.flush();
			inputString = s.nextLine();
			if (inputString.equals("!size")) {
				System.out.println("Intcode memory size: " + longcode.getMemSize());
			} else if (inputString.equals("!save")) {
				backups.add(longcode.backup());
				System.out.println("Saved to slot " + backups.size());
			} else if (inputString.equals("!load")) {
				if (backups.isEmpty()) {
					System.out.println("No saves available");
				} else {
					System.out.println("Loading slot " + backups.size());
					longcode = backups.get(backups.size() - 1);
				}
			} else if (inputString.equals("!pop")) {
				if (backups.isEmpty()) {
					System.out.println("No saves available");
				} else {
					System.out.println("Deleting slot " + backups.size());
					backups.remove(backups.size()-1);
				}
			} else if (inputString.startsWith("!")) {
				System.out.println("Unrecognized macro!");
			} else {
				longcode.setInputArray((inputString + "\n").chars().mapToLong(c -> c).toArray());
			}
		} while (!(longcode.isDone() || inputString.startsWith("!q")));
	}
}
