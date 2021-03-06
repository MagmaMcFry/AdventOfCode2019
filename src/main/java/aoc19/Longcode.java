package aoc19;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

public class Longcode {
	private static final int MEM_LIMIT = 10_000_000;
	private long[] code;
	private long pos = 0, base = 0;
	private long runtime = 0;
	private LongcodeInput input = new LongcodeInput() {
		@Override public boolean hasNextInput() { return false; } @Override public long getNextInput() { return 0; }
	};
	private LongcodeOutput output = new LongcodeOutput() {
		@Override public boolean acceptsNextOutput() { return false; } @Override public void setNextOutput(long val) {}
	};
	private boolean done = false;
	private boolean broken = false;

	public Longcode(long[] code) {
		this.code = code;
	}

	public Longcode(Longcode original) {
		this.code = Arrays.copyOf(original.code, original.code.length);
		this.pos = original.pos;
		this.base = original.base;
		this.runtime = original.runtime;
		this.input = original.input;
		this.output = original.output;
		this.done = original.done;
		this.broken = original.broken;
	}

	/** Returns whether the machine should halt after this step */
	public boolean step() {
		int rawOpcode = (int) getCode(pos);
		long op1, op2, op3;
		int pureOpcode = rawOpcode % 100;
		int mode1 = (rawOpcode / 100) % 10;
		int mode2 = (rawOpcode / 1000) % 10;
		int mode3 = (rawOpcode / 10000) % 10;
		switch (mode1) {
			case 0: op1 = getCode(pos+1); break;
			case 1: op1 = pos+1; break;
			case 2: op1 = base+getCode(pos+1); break;
			default: op1 = -1;
		}
		switch (mode2) {
			case 0: op2 = getCode(pos+2); break;
			case 1: op2 = pos+2; break;
			case 2: op2 = base+getCode(pos+2); break;
			default: op2 = -2;
		}
		switch (mode3) {
			case 0: op3 = getCode(pos+3); break;
			case 1: op3 = pos+3; break;
			case 2: op3 = base+getCode(pos+3); break;
			default: op3 = -3;
		}
		switch (pureOpcode) {
			// ADD
			case 1: setCode(op3, getCode(op1) + getCode(op2)); pos += 4; break;

			// MUL
			case 2: setCode(op3, getCode(op1) * getCode(op2)); pos += 4; break;

			// IN
			case 3:
				if (input.hasNextInput()) {
					setCode(op1, input.getNextInput());
					pos += 2;
					break;
				} else {
					// Waiting on input
					return true;
				}

				// OUT
			case 4:
				if (output.acceptsNextOutput()) {
					output.setNextOutput(getCode(op1));
					pos += 2;
					break;
				} else {
					// Waiting on output
					return true;
				}

				// JNZ
			case 5: if (getCode(op1) != 0) { pos = getCode(op2); } else { pos += 3; } break;
			// JEZ
			case 6: if (getCode(op1) == 0) { pos = getCode(op2); } else { pos += 3; } break;

			// TLT
			case 7: setCode(op3, (getCode(op1) < getCode(op2)) ? 1 : 0); pos += 4; break;
			// TEQ
			case 8: setCode(op3, (getCode(op1) == getCode(op2)) ? 1 : 0); pos += 4; break;

			// OFFSET
			case 9: base += getCode(op1); pos += 2; break;

			// HALT
			case 99: done = true; return true;

			// HCF
			default:
				broken = true;
				done = true;
				return true;
		}
		++runtime;
		return false;
	}

	public void runCode() {
		while (!step()) {}
	}

	private long getCode(long addr) {
		int iaddr = (int) addr;
		if (iaddr < 0) throw new ArrayIndexOutOfBoundsException("Code broke, negative read");
		if (iaddr >= MEM_LIMIT) throw new ArrayIndexOutOfBoundsException("Code broke, memory limit reached at " + addr);
		if (code.length <= iaddr) {
			long[] newCode = new long[2*iaddr];
			System.arraycopy(code, 0, newCode, 0, code.length);
			code = newCode;
		}
		return code[iaddr];
	}

	private void setCode(long addr, long val) {
		int iaddr = (int) addr;
		if (iaddr < 0) throw new ArrayIndexOutOfBoundsException("Code broke, negative write");
		if (iaddr >= MEM_LIMIT) throw new ArrayIndexOutOfBoundsException("Code broke, memory limit reached at " + addr);
		if (code.length <= iaddr) {
			long[] newCode = new long[2*iaddr];
			System.arraycopy(code, 0, newCode, 0, code.length);
			code = newCode;
		}
		code[iaddr] = val;
	}

	public void setInputMethod(LongcodeInput input) {
		this.input = input;
	}

	public void setInputConstant(long constant) {
		this.input = new LongcodeInput() {
			@Override
			public boolean hasNextInput() {
				return true;
			}
			@Override
			public long getNextInput() {
				return constant;
			}
		};
	}

	public void setInputArray(long[] list) {
		this.input = new LongcodeInput() {
			int inputPos = 0;
			@Override
			public boolean hasNextInput() {
				return inputPos < list.length;
			}
			@Override
			public long getNextInput() {
				return list[inputPos++];
			}
		};
	}

	public void setOutputMethod(LongcodeOutput outputMethod) {
		this.output = outputMethod;
	}

	public void setOutputMethod(LongConsumer outputMethod) {
		this.output = new LongcodeOutput() {
			@Override
			public boolean acceptsNextOutput() {
				return true;
			}

			@Override
			public void setNextOutput(long val) {
				outputMethod.accept(val);
			}
		};
	}

	public List<Long> setOutputList() {
		List<Long> list = new ArrayList<>();
		setOutputMethod(list::add);
		return list;
	}

	public long[] setOutputCollector() {
		long[] out = new long[1];
		setOutputMethod((i) -> out[0] = i);
		return out;
	}

	public void setOutputFixedLongBuffer(int bufferSize, Consumer<long[]> bufferConsumer) {
		setOutputMethod(new LongConsumer() {
			long[] buffer = new long[bufferSize];
			int bufferPos = 0;
			@Override
			public void accept(long value) {
				buffer[bufferPos++] = value;
				if (bufferPos == bufferSize) {
					bufferPos = 0;
					bufferConsumer.accept(buffer);
				}
			}
		});
	}

	public void setOutputFixedIntBuffer(int bufferSize, Consumer<int[]> bufferConsumer) {
		setOutputMethod(new LongConsumer() {
			int[] buffer = new int[bufferSize];
			int bufferPos = 0;
			@Override
			public void accept(long value) {
				buffer[bufferPos++] = (int)value;
				if (bufferPos == bufferSize) {
					bufferPos = 0;
					bufferConsumer.accept(buffer);
				}
			}
		});
	}

	public long[] setOutputCounter(long limit) {
		long[] counter = new long[1];
		this.output = new LongcodeOutput() {

			@Override
			public boolean acceptsNextOutput() {
				return counter[0] < limit;
			}

			@Override
			public void setNextOutput(long output) {
				++counter[0];
			}
		};
		return counter;
	}

	public boolean isDone() {
		return done;
	}

	public boolean isBroken() {
		return broken;
	}

	public long getPos() {
		return pos;
	}

	public long getRuntime() {
		return runtime;
	}

	public int getMemSize() {
		return code.length;
	}

	public static long runAndReturnFirstElement(long[] code) {
		Longcode longcode = new Longcode(code);
		longcode.runCode();
		return longcode.code[0];
	}

	public static long runAndReturnLastOutput(long[] code, long[] inputs) {
		Longcode longcode = new Longcode(code);
		longcode.setInputArray(inputs);
		long[] output = longcode.setOutputCollector();
		longcode.runCode();
		return output[0];
	}

	public static long[] runAndCollectOutputsToArray(long[] code, long[] inputs) {
		Longcode longcode = new Longcode(code);
		longcode.setInputArray(inputs);
		List<Long> output = longcode.setOutputList();
		longcode.runCode();
		return output.stream().mapToLong(i->i).toArray();
	}

	public Longcode backup() {
		return new Longcode(this);
	}

	public interface LongcodeInput {

		boolean hasNextInput();
		long getNextInput();
	}
	public interface LongcodeOutput {

		boolean acceptsNextOutput();
		void setNextOutput(long output);
	}
}
