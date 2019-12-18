package aoc19.days;

import aoc19.Longcode;
import aoc19.util.Direction;
import aoc19.util.FileReader;
import aoc19.util.I2;
import aoc19.util.I2Grid;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day15Solver {
	public static void main(String[] args) {
		solvePart1();
		solvePart2();
	}

	private static long[] getCode() {
		return FileReader.readLongcode("/day15/code.txt");
	}

	private static void solvePart1() {
		RepairDroid droid = new RepairDroid();
		int dist = droid.findOxygenDistance();
		System.out.println("Oxygen distance: " + dist);
	}

	private static void solvePart2() {
		RepairDroid droid = new RepairDroid();
		int dist = droid.findOxygenFloodTime();
		System.out.println("Oxygen flood time: " + dist);
	}

	static class RepairDroid {
		private final long[] output;
		private final I2Grid<Integer> knownArea = new I2Grid<>();
		int currentX = 0, currentY = 0;
		int oxygenX = 0, oxygenY = 0;

		Longcode code;
		RepairDroid() {
			code = new Longcode(getCode());
			output = code.setOutputCollector();
		}

		private int doCodeStep(Direction direction) {
			code.setInputArray(new long[]{direction.symbol});
			code.runCode();
			return (int) output[0];
		}

		void explore() {
			// 0: unexplored
			// 1-4: explored floor, backtrack direction
			// 5: explored floor, already backtracked
			// -1: wall
			knownArea.put(currentX, currentY, 5);
			int steps = 0;
			int MAX_STEPS = 10000;
			EXPLORING: while (++steps < MAX_STEPS) {
				TURNING: for (Direction dir : Direction.values()) {
					if (knownArea.get(currentX + dir.dx, currentY + dir.dy, 0) == 0) {
						// Unknown tile, let's find out about it
						//System.out.println("Checking " + dir.name());
						int stepResult = doCodeStep(dir);
						if (stepResult == 0) {
							// Wall
							knownArea.put(currentX + dir.dx, currentY + dir.dy, -1);
							continue TURNING;
						}
						// Not wall
						//System.out.println("Walking " + dir.name());
						currentX += dir.dx;
						currentY += dir.dy;
						knownArea.put(currentX, currentY, dir.opposite().symbol);
						if (stepResult == 2) {
							// Oxygen system
							oxygenX = currentX;
							oxygenY = currentY;
						}
						continue EXPLORING;
					}
					// Known tile, we don't need to go that way
				}
				// No directions left to explore
				int currentMark = knownArea.get(currentX, currentY, 0);
				if (currentMark == 5) {
					// Done with a tile we are on? Must be the starting location
					break EXPLORING;
				}
				knownArea.put(currentX, currentY, 5); // Done with this tile
				Direction back = Direction.get(currentMark);
				doCodeStep(back);
				currentX += back.dx;
				currentY += back.dy;
			}
		}
		int findDistanceWithinExploredArea(int startX, int startY, int endX, int endY) {
			// Breadth first search
			List<I2> openList = new ArrayList<>();
			Set<I2> checkedSet = new HashSet<>();
			openList.add(new I2(startX, startY));
			checkedSet.add(new I2(startX, startY));
			int openListDist = 0;
			while (!openList.isEmpty()) {
				List<I2> nextOpenList = new ArrayList<>();
				for (I2 openPos : openList) {
					if (openPos.x == endX && openPos.y == endY) {
						return openListDist;
					}
					for (Direction dir : Direction.values()) {
						I2 nextPos = new I2(openPos.x + dir.dx, openPos.y + dir.dy);
						if (knownArea.get(nextPos.x, nextPos.y, 0) > 0 && !checkedSet.contains(nextPos)) {
							nextOpenList.add(nextPos);
							checkedSet.add(nextPos);
						}
					}
				}
				openList = nextOpenList;
				++openListDist;
			}
			return openListDist-1;
		}

		int findOxygenDistance() {
			explore();
			return findDistanceWithinExploredArea(0, 0, oxygenX, oxygenY);
		}

		int findOxygenFloodTime() {
			explore();
			return findDistanceWithinExploredArea(oxygenX, oxygenY, Integer.MAX_VALUE, Integer.MAX_VALUE);
		}

		void printKnownArea() {
			knownArea.print(System.out, 0, i->{
				switch(i) {
					case 0: return " ";
					case -1: return "#";
					case 5: return ".";
					default: return "?";
				}
			});
		}
	}

}
