package aoc19.days;

import aoc19.util.DijkstraPathfinder;
import aoc19.util.Direction;
import aoc19.util.FileReader;
import aoc19.util.I2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

public class Day20Solver {
	public static void main(String[] args) {
		solvePart1();
		solvePart2();
	}

	private static List<String> getMazeStrings() {
		return FileReader.readLines("/day20/maze.txt");
	}

	private static void solvePart1() {
		TorusMaze maze = new TorusMaze(119, 117, 31, 31, getMazeStrings());
		System.out.println("Maze length: " + maze.findShortestPathLength());
	}

	private static void solvePart2() {
		TorusMaze maze = new TorusMaze(119, 117, 31, 31, getMazeStrings());
		System.out.println("Recursive maze length: " + maze.findShortestRecursivePathLength());
	}

	static char[][] toCharArray(List<String> mazeStrings) {
		return mazeStrings.stream().map(String::toCharArray).toArray(char[][]::new);
	}

	static class TorusMaze {
		private final char[][] grid;
		private final int xSize, ySize;
		private final int xSpan, ySpan;
		private final Map<String, I2> teleportPositions = new HashMap<>();
		private final Map<I2, I2> teleports = new HashMap<>();
		private final Map<I2, Integer> depthChanges = new HashMap<>();
		private I2 start, end;

		public TorusMaze(int xSize, int ySize, int xSpan, int ySpan, List<String> mazeStrings) {
			this.grid = toCharArray(mazeStrings);
			this.xSize = xSize;
			this.ySize = ySize;
			this.xSpan = xSpan;
			this.ySpan = ySpan;
			// Outer horizontal edges
			for (int x = 0; x < xSize; ++x) {
				checkLabel(x, 0, Direction.NORTH, -1);
				checkLabel(x, ySize-1, Direction.SOUTH, -1);
			}
			// Outer vertical edges
			for (int y = 0; y < ySize; ++y) {
				checkLabel(0, y, Direction.WEST, -1);
				checkLabel(xSize-1, y, Direction.EAST, -1);
			}
			// Inner horizontal edges
			for (int x = xSpan; x + xSpan < xSize; ++x) {
				checkLabel(x, ySize-ySpan, Direction.NORTH, 1);
				checkLabel(x, ySpan-1, Direction.SOUTH, 1);
			}
			// Inner vertical edges
			for (int y = ySpan; y + ySpan < ySize; ++y) {
				checkLabel(xSize-xSpan, y, Direction.WEST, 1);
				checkLabel(xSpan-1, y, Direction.EAST, 1);
			}
		}

		public int findShortestPathLength() {
			DijkstraPathfinder.StateAndDistance<TorusState> endState = DijkstraPathfinder.findClosestGoalState(new TorusState(start));
			if (endState == null) return -1;
			return endState.distance;
		}

		public int findShortestRecursivePathLength() {
			DijkstraPathfinder.StateAndDistance<RecursiveTorusState> endState = DijkstraPathfinder.findClosestGoalState(new RecursiveTorusState(start, 0));
			if (endState == null) return -1;
			return endState.distance;
		}

		private void checkLabel(int px, int py, Direction dir, int depthChange) {
			px += 2;
			py += 2;
			String label;
			switch(dir) {
				case NORTH:
				case WEST:
					label = "" + grid[py+2*dir.dy][px+2*dir.dx] + grid[py+dir.dy][px+dir.dx];
					break;
				case EAST:
				case SOUTH:
					label = "" + grid[py+dir.dy][px+dir.dx] + grid[py+2*dir.dy][px+2*dir.dx];
					break;
				default:
					label = "  ";
					break;
			}
			if ("AA".equals(label)) {
				start = new I2(px,py);
			} else if ("ZZ".equals(label)) {
				end = new I2(px,py);
			} else if (label.matches("\\w\\w")) {
				I2 newPos = new I2(px, py);
				depthChanges.put(newPos, depthChange);
				I2 oldPos = teleportPositions.get(label);
				if (oldPos != null) {
					teleports.put(newPos, oldPos);
					teleports.put(oldPos, newPos);
				} else {
					teleportPositions.put(label, new I2(px, py));
				}
			} else if (label.matches("[#.]")) {
				throw new RuntimeException("Improper label position!");
			}
		}

		private class TorusState implements DijkstraPathfinder.State<TorusState> {
			private final I2 pos;

			public TorusState(I2 pos) {
				this.pos = pos;
			}

			@Override
			public void getNextStates(BiConsumer<TorusState, Integer> output) {
				for (Direction dir : Direction.values()) {
					int npx = pos.x + dir.dx;
					int npy = pos.y + dir.dy;
					if (grid[npy][npx] == '.') {
						output.accept(new TorusState(new I2(npx, npy)), 1);
					}
				}
				I2 tpos = teleports.get(pos);
				if (tpos != null) {
					output.accept(new TorusState(tpos), 1);
				}
			}

			@Override
			public boolean isDone() {
				return pos.x == end.x && pos.y == end.y;
			}

			@Override
			public boolean equals(Object o) {
				if (this == o) return true;
				if (o == null || getClass() != o.getClass()) return false;
				TorusState that = (TorusState) o;
				return pos.equals(that.pos);
			}

			@Override
			public int hashCode() {
				return Objects.hash(pos);
			}
		}

		private class RecursiveTorusState implements DijkstraPathfinder.State<RecursiveTorusState> {
			private static final int DEPTH_LIMIT = 100;
			private final I2 pos;
			private final int depth;

			public RecursiveTorusState(I2 pos, int depth) {
				this.pos = pos;
				this.depth = depth;
			}

			@Override
			public void getNextStates(BiConsumer<RecursiveTorusState, Integer> output) {
				for (Direction dir : Direction.values()) {
					int npx = pos.x + dir.dx;
					int npy = pos.y + dir.dy;
					if (grid[npy][npx] == '.') {
						output.accept(new RecursiveTorusState(new I2(npx, npy), depth), 1);
					}
				}
				I2 tpos = teleports.get(pos);
				if (tpos != null) {
					int newDepth = depth + depthChanges.get(pos);
					if (newDepth >= 0 && depth < DEPTH_LIMIT) {
						output.accept(new RecursiveTorusState(tpos, newDepth), 1);
					}
				}
			}

			@Override
			public boolean isDone() {
				return pos.x == end.x && pos.y == end.y && depth == 0;
			}

			@Override
			public boolean equals(Object o) {
				if (this == o) return true;
				if (o == null || getClass() != o.getClass()) return false;
				RecursiveTorusState that = (RecursiveTorusState) o;
				return depth == that.depth &&
						pos.equals(that.pos);
			}

			@Override
			public int hashCode() {
				return Objects.hash(pos, depth);
			}
		}
	}
}
