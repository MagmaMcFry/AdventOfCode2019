package aoc19.days;

public class Day4Solver {
	public static void main(String[] args) {
		solvePart1();
		solvePart2();
	}

	private static void solvePart1() {
		int count = 0;
		for (int n = 273025; n <= 767253; ++n) {
			count += validCode(n) ? 1 : 0;
		}
		System.out.println("Number of valid codes: " + count);
	}

	private static boolean validCode(int code) {
		char[] c = Integer.toString(code).toCharArray();
		boolean hasDouble = false;
		int currentStreak = 1;
		for (int i = 0; i < c.length - 1; ++i) {
			if (c[i] > c[i+1]) {
				return false;
			}
			if (c[i] == c[i+1]) {
				++currentStreak;
			} else {
				hasDouble |= (currentStreak == 2);
				currentStreak = 1;
			}
		}
		return hasDouble || (currentStreak == 2);
	}
	private static void solvePart2() {
	}
}
