package aoc19;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntConsumer;

public class Intcode {
	private final int[] code;
	private int pos;
	private IntcodeInput input;
	private IntConsumer output;
	private boolean done = false;
	private boolean broken = false;

	public Intcode(int[] code) {
		this.code = code;
		this.pos = 0;
	}

	public void runCode() {
		LOOP: while (true) {
			switch (code[pos]) {
				// ADD
				case 1:
					code[code[pos+3]] = code[code[pos+1]] + code[code[pos+2]]; pos += 4; break;
				case 101:
					code[code[pos+3]] = code[pos+1] + code[code[pos+2]]; pos += 4; break;
				case 1001:
					code[code[pos+3]] = code[code[pos+1]] + code[pos+2]; pos += 4; break;
				case 1101:
					code[code[pos+3]] = code[pos+1] + code[pos+2]; pos += 4; break;

				// MUL
				case 2:
					code[code[pos+3]] = code[code[pos+1]] * code[code[pos+2]]; pos += 4; break;
				case 102:
					code[code[pos+3]] = code[pos+1] * code[code[pos+2]]; pos += 4; break;
				case 1002:
					code[code[pos+3]] = code[code[pos+1]] * code[pos+2]; pos += 4; break;
				case 1102:
					code[code[pos+3]] = code[pos+1] * code[pos+2]; pos += 4; break;

				// IN
				case 3:
					if (input.hasNextInput()) {
						code[code[pos+1]] = input.getNextInput();
						pos += 2;
						break;
					} else {
						// Waiting on input
						break LOOP;
					}

				// OUT
				case 4:
					nextOutput(code[code[pos+1]]); pos += 2; break;
				case 104:
					nextOutput(code[pos+1]); pos += 2; break;

				// JEZ
				case 5:
					if (code[code[pos+1]] != 0) {
						pos = code[code[pos+2]];
					} else {
						pos += 3;
					}
					break;
				case 105:
					if (code[pos+1] != 0) {
						pos = code[code[pos+2]];
					} else {
						pos += 3;
					}
					break;
				case 1005:
					if (code[code[pos+1]] != 0) {
						pos = code[pos+2];
					} else {
						pos += 3;
					}
					break;
				case 1105:
					if (code[pos+1] != 0) {
						pos = code[pos+2];
					} else {
						pos += 3;
					}
					break;

				// JNZ
				case 6:
					if (code[code[pos+1]] == 0) {
						pos = code[code[pos+2]];
					} else {
						pos += 3;
					}
					break;
				case 106:
					if (code[pos+1] == 0) {
						pos = code[code[pos+2]];
					} else {
						pos += 3;
					}
					break;
				case 1006:
					if (code[code[pos+1]] == 0) {
						pos = code[pos+2];
					} else {
						pos += 3;
					}
					break;
				case 1106:
					if (code[pos+1] == 0) {
						pos = code[pos+2];
					} else {
						pos += 3;
					}
					break;

				// TLT
				case 7: code[code[pos+3]] = (code[code[pos+1]] < code[code[pos+2]]) ? 1 : 0; pos += 4; break;
				case 107: code[code[pos+3]] = (code[pos+1] < code[code[pos+2]]) ? 1 : 0; pos += 4;  break;
				case 1007: code[code[pos+3]] = (code[code[pos+1]] < code[pos+2]) ? 1 : 0; pos += 4;  break;
				case 1107: code[code[pos+3]] = (code[pos+1] < code[pos+2]) ? 1 : 0; pos += 4;  break;

				// TEQ
				case 8: code[code[pos+3]] = (code[code[pos+1]] == code[code[pos+2]]) ? 1 : 0; pos += 4;  break;
				case 108: code[code[pos+3]] = (code[pos+1] == code[code[pos+2]]) ? 1 : 0; pos += 4;  break;
				case 1008: code[code[pos+3]] = (code[code[pos+1]] == code[pos+2]) ? 1 : 0; pos += 4;  break;
				case 1108: code[code[pos+3]] = (code[pos+1] == code[pos+2]) ? 1 : 0; pos += 4;  break;

				// HALT
				case 99: done = true; break LOOP;

				// HCF
				default: nextOutput(-999); broken = true; done = true; break LOOP;
			}
		}
	}

	public void setInputMethod(IntcodeInput input) {
		this.input = input;
	}

	public void setInputConstant(int constant) {
		this.input = new IntcodeInput() {
			@Override
			public boolean hasNextInput() {
				return true;
			}
			@Override
			public int getNextInput() {
				return constant;
			}
		};
	}

	public void setInputArray(int[] list) {
		this.input = new IntcodeInput() {
			int inputPos = 0;
			@Override
			public boolean hasNextInput() {
				return inputPos < list.length;
			}
			@Override
			public int getNextInput() {
				return list[inputPos++];
			}
		};
	}

	public void setOutputMethod(IntConsumer output) {
		this.output = output;
	}

	public List<Integer> setOutputList() {
		List<Integer> list = new ArrayList<>();
		this.output = list::add;
		return list;
	}

	public int[] setOutputCollector() {
		int[] out = new int[1];
		this.output = (i) -> out[0] = i;
		return out;
	}

	public boolean isDone() {
		return done;
	}

	public boolean isBroken() {
		return broken;
	}

	public int getPos() {
		return pos;
	}

	private void nextOutput(int val) {
		output.accept(val);
	}

	public static int runAndReturnFirstElement(int[] code) {
		Intcode intcode = new Intcode(code);
		intcode.runCode();
		return intcode.code[0];
	}

	public static int runAndReturnLastOutput(int[] code, int[] inputs) {
		Intcode intcode = new Intcode(code);
		intcode.setInputArray(inputs);
		int[] output = intcode.setOutputCollector();
		intcode.runCode();
		return output[0];
	}

	public interface IntcodeInput {
		boolean hasNextInput();
		int getNextInput();
	}
}
