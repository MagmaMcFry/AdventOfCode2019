package aoc19;

public class Intcode {
	public static int runCode(int[] code) {
		int pos = 0;
		while (true) {
			switch (code[pos]) {
				case 1:
					code[code[pos+3]] = code[code[pos+1]] + code[code[pos+2]]; pos += 4; break;
				case 2:
					code[code[pos+3]] = code[code[pos+1]] * code[code[pos+2]]; pos += 4; break;
				case 99: return code[0];
				default: return -999;
			}
		}
	}
}
