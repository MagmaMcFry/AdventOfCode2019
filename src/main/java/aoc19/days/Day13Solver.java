package aoc19.days;

import aoc19.Longcode;
import aoc19.util.FileReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day13Solver {
	public static void main(String[] args) {
		solvePart1();
		solvePart2();
	}

	private static long[] getCode() {
		return FileReader.readLongcode("/day13/code.txt");
	}

	private static void solvePart1() {
		Longcode longcode = new Longcode(getCode());
		int SW = 100, SH = 100;
		int[][] display = new int[SH][SW];
		longcode.setOutputFixedIntBuffer(3, buffer -> {
			display[buffer[0]][buffer[1]] = buffer[2];
		});
		longcode.runCode();
		int count = 0;
		for (int[] row : display) {
			for (int tile : row) {
				if (tile == 2) {
					// Block tile
					++count;
				}
			}
		}
		System.out.println("Count: " + count);
	}

	private static void solvePart2() {
		long[] code = getCode();
		code[0] = 2;
		Longcode longcode = new Longcode(code);
		//int SW = 50, SH = 27;
		//int[][] display = new int[SH][SW];
		final int[] score = {0};
		final int[] paddlePos = {0};
		final int[] ballPos = {0};
		longcode.setOutputFixedIntBuffer(3, buffer -> {
			if (buffer[0] == -1) {
				score[0] = buffer[2];
			} else {
				if (buffer[2] == 3) {
					paddlePos[0] = buffer[0];
				} else if (buffer[2] == 4) {
					ballPos[0] = buffer[0];
				}
				//display[buffer[1]][buffer[0]] = buffer[2];
			}
		});

		Scanner sc = new Scanner(System.in);

		longcode.runCode();
		while (!longcode.isDone()) {
			//long[] input = getUserInput(display, sc, scanPattern);
			long[] input = {Integer.compare(ballPos[0], paddlePos[0])};
			longcode.setInputArray(input);
			longcode.runCode();
		}
		System.out.println("Done! Score: " + score[0]);
	}

	private static final Pattern USER_INPUT_PATTERN = Pattern.compile("(\\w)(\\d+)");
	private static long[] getUserInput(int[][] display, Scanner sc) {
		String[] displays = {" ", "#", "X", "-", "O"};
		for (int[] row : display) {
			for (int tile : row) {
				System.out.print(displays[tile]);
			}
			System.out.println();
		}
		System.out.println("Input: ");
		String str = sc.nextLine();
		Matcher m = USER_INPUT_PATTERN.matcher(str);
		List<Long> inputList = new ArrayList<>();
		while (m.find()) {
			String op = m.group(1);
			int joystickPos = "a".equals(op) ? -1 : "d".equals(op) ? 1 : 0;
			int count = Integer.parseInt(m.group(2));
			for (int i = 0; i < count; ++i) {
				inputList.add((long) joystickPos);
			}
		}
		return inputList.stream().mapToLong(i -> i).toArray();
	}
}
