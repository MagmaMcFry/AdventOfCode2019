package aoc19.days;

import aoc19.Longcode;
import aoc19.util.FileReader;
import aoc19.util.I2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day23Solver {
	public static void main(String[] args) {
		solvePart1();
		solvePart2();
	}

	private static long[] getCode() {
		return FileReader.readLongcode("/day23/code.txt");
	}

	private static final int MAX_STEPS = 1_000_000;
	private static final int NUM_MACHINES = 50;
	private static final int NAT_ADDRESS = 255;

	private static void solvePart1() {
		System.out.println("Starting part 1");
		List<Longcode> machines = new ArrayList<>();
		List<List<Long>> inputQueues = new ArrayList<>();
		boolean[] finished = new boolean[1];
		for (int i = 0; i < NUM_MACHINES; ++i) {
			final int id = i;
			List<Long> inputQueue = new ArrayList<>();
			inputQueues.add(inputQueue);
			inputQueue.add((long) i);
			Longcode machine = new Longcode(getCode());
			machine.setInputMethod(new Longcode.LongcodeInput() {
				@Override
				public boolean hasNextInput() {
					return true;
				}

				@Override
				public long getNextInput() {
					List<Long> queue = inputQueues.get(id);
					return queue.isEmpty() ? -1 : queue.remove(0);
				}
			});

			machine.setOutputFixedLongBuffer(3, array -> {
				int sendIndex = (int) array[0];
				if (sendIndex == NAT_ADDRESS) {
					System.out.println("Message: " + array[1] + ", " + array[2]);
					finished[0] = true;
				} else if (sendIndex >= 0 && sendIndex < NUM_MACHINES) {
					List<Long> otherQueue = inputQueues.get((int) array[0]);
					otherQueue.add(array[1]);
					otherQueue.add(array[2]);
				} else {
					System.out.println("Broken! Tried to send to " + sendIndex);
					finished[0] = true;
				}
			});

			machines.add(machine);
		}

		int numSteps = 0;
		while (!finished[0]) {
			for (Longcode machine : machines) {
				machine.step();
			}
			if (++numSteps >= MAX_STEPS) {
				System.out.println("Time ran out!");
				break;
			}
		}
		System.out.println("Runtime: " + machines.get(0).getRuntime());
	}

	private static void solvePart2() {
		System.out.println("Starting part 2");
		List<Longcode> machines = new ArrayList<>();
		List<List<Long>> inputQueues = new ArrayList<>();
		boolean[] finished = new boolean[1];
		long[] natPacket = new long[2];
		Set<I2> usedNatPackets = new HashSet<>();
		boolean[] waiting = new boolean[NUM_MACHINES];
		for (int i = 0; i < NUM_MACHINES; ++i) {
			final int id = i;
			List<Long> inputQueue = new ArrayList<>();
			inputQueues.add(inputQueue);
			inputQueue.add((long) i);
			Longcode machine = new Longcode(getCode());
			machine.setInputMethod(new Longcode.LongcodeInput() {
				@Override
				public boolean hasNextInput() {
					return true;
				}

				@Override
				public long getNextInput() {
					A: if (inputQueue.isEmpty()) {
						waiting[id] = true;
						for (boolean waits : waiting) {
							if (!waits) break A;
						}
						// Everything is waiting now
						I2 usedNatPacket = new I2((int)natPacket[0], (int)natPacket[1]);
						if (usedNatPackets.contains(usedNatPacket)) {
							System.out.println("Repeated message: " + natPacket[0] + ", " + natPacket[1]);
							finished[0] = true;
						}
						//System.out.println("Everything is waiting, resending packet " + usedNatPacket);
						usedNatPackets.add(usedNatPacket);
						inputQueues.get(0).add(natPacket[0]);
						inputQueues.get(0).add(natPacket[1]);
						waiting[0] = false;
					}
					return inputQueue.isEmpty() ? -1 : inputQueue.remove(0);
				}
			});

			machine.setOutputFixedLongBuffer(3, array -> {
				int sendIndex = (int) array[0];
				if (sendIndex == NAT_ADDRESS) {
					natPacket[0] = array[1];
					natPacket[1] = array[2];
				} else if (sendIndex >= 0 && sendIndex < NUM_MACHINES) {
					List<Long> otherQueue = inputQueues.get((int) array[0]);
					otherQueue.add(array[1]);
					otherQueue.add(array[2]);
					waiting[sendIndex] = false;
				} else {
					System.out.println("Broken! Tried to send to " + sendIndex);
					finished[0] = true;
				}
			});

			machines.add(machine);
		}

		int numSteps = 0;
		while (!finished[0]) {
			for (Longcode machine : machines) {
				machine.step();
			}
			if (++numSteps >= MAX_STEPS) {
				System.out.println("Time ran out!");
				break;
			}
		}
		System.out.println("Runtime: " + machines.get(0).getRuntime());
	}
}
