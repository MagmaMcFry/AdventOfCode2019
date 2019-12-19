package aoc19.days;

import aoc19.util.Direction;
import aoc19.util.FileReader;
import aoc19.util.I2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;

public class Day18Solver {
	public static void main(String[] args) {
		solvePart1();
		solvePart2();
	}

	private static void solvePart1() {
		Maze maze = new Maze(getMazeStrings1(), 'z');
		System.out.println("Collection distance in maze 1: " + maze.getTotalCollectionDistance());
	}

	private static void solvePart2() {
		Maze maze = new Maze(getMazeStrings2(), 'z');
		System.out.println("Collection distance in maze 2: " + maze.getTotalCollectionDistance());
	}

	private static List<String> getMazeStrings1() {
		return FileReader.readLines("/day18/maze.txt");
	}

	private static List<String> getMazeStrings2() {
		return FileReader.readLines("/day18/maze2.txt");
	}

	static char[][] toCharArray(List<String> mazeStr) {
		return mazeStr.stream().map(String::toCharArray).toArray(char[][]::new);
	}

	static class Maze {
		private final int firstKey, lastKey, firstLock, lastLock;
		private final int numKeys;
		private final int totalMask;
		private final Map<Character, I2> objectPositions;
		private final char[][] array;
		private final HashMap<Character, HashMap<Character, Integer>> pathMasks = new HashMap<>();
		private final HashMap<Character, HashMap<Character, Integer>> pathLengths = new HashMap<>();
		private boolean pathsChecked = false;
		private final int[] robotPartsByKeyIndex;

		public Maze(List<String> mazeStrings, char lastKeyChar) {
			this.lastKey = lastKeyChar;
			firstKey = 'a';
			firstLock = 'A';
			lastLock = firstLock - firstKey + lastKey;
			numKeys = 1 + lastKey - firstKey;
			robotPartsByKeyIndex = new int[numKeys];
			totalMask = (1 << numKeys) - 1;
			this.array = toCharArray(mazeStrings);

			objectPositions = new HashMap<>();
			for (char c : robotChars) {
				objectPositions.put(c, new I2(0, 0));
			}
			for (int y = 0; y < array.length; ++y) {
				char[] row = array[y];
				for (int x = 0; x < row.length; x++) {
					char thing = row[x];
					if (isKey(thing) || isLock(thing) || isAgent(thing)) {
						objectPositions.put(thing, new I2(x, y));
					}
				}
			}
		}

		void checkPaths() {
			if (pathsChecked) return;
			for (int endKeyIndex = 0; endKeyIndex < numKeys; ++endKeyIndex) {
				char endKey = (char) ('a' + endKeyIndex);
				for (int i = 0; i < robotChars.length; ++i) {
					char r = robotChars[i];
					checkPath(r, endKey);
					if (hasPath(r, endKey)) {
						robotPartsByKeyIndex[endKeyIndex] = i;
					}
				}
				for (int startKeyIndex = 0; startKeyIndex < numKeys; ++startKeyIndex) {
					char startKey = (char) ('a' + startKeyIndex);
					checkPath(startKey, endKey);
				}
			}
			pathsChecked = true;
		}

		void checkPath(char startItem, char endItem) {
			pathLengths.computeIfAbsent(startItem, HashMap::new);
			pathMasks.computeIfAbsent(startItem, HashMap::new);
			if (startItem == endItem) {
				// Ignore self-paths
				return;
			}
			List<Character> foundPath = findPathBetween(startItem, endItem);
			if (foundPath == null) {
				return;
			}
			int mask = 0;
			for (char c : foundPath) {
				if (isLock(c)) {
					mask |= 1 << (c - firstLock);
				} else if (isKey(c)) {
					mask |= 1 << (c - firstKey); // Optimization: Can't go through keys without collecting them
				}
			}
			pathLengths.get(startItem).put(endItem, 1 + foundPath.size());
			pathMasks.get(startItem).put(endItem, mask);
		}

		List<Character> findPathBetween(char startItem, char endItem) {
			return findPath(objectPositions.get(startItem), objectPositions.get(endItem));
		}

		List<Character> findPath(I2 start, I2 end) {
			if (start == null) {
				throw new IllegalArgumentException("Can't find path from null");
			}
			if (end == null) {
				throw new IllegalArgumentException("Can't find path to null");
			}
			Map<I2, I2> prevNeighbors = new HashMap<>();
			List<I2> openSet = new ArrayList<>();
			openSet.add(start);
			prevNeighbors.put(start, start);
			while (!openSet.isEmpty()) {
				List<I2> newOpenSet = new ArrayList<>();
				EXPLORING:
				for (I2 currentPos : openSet) {
					if (array[currentPos.y][currentPos.x] == '#') {
						continue EXPLORING;
					}
					TURNING:
					for (Direction dir : Direction.values()) {
						I2 neighborPos = new I2(currentPos.x + dir.dx, currentPos.y + dir.dy);
						if (prevNeighbors.containsKey(neighborPos)) {
							continue TURNING;
						}
						prevNeighbors.put(neighborPos, currentPos);
						newOpenSet.add(neighborPos);
						if (end.equals(neighborPos)) {
							List<Character> characters = new ArrayList<>();
							while (!prevNeighbors.get(currentPos).equals(currentPos)) {
								characters.add(array[currentPos.y][currentPos.x]);
								currentPos = prevNeighbors.get(currentPos);
							}
							return characters;
						}
					}
				}
				openSet = newOpenSet;
			}
			return null;
		}

		private boolean isKey(char val) {
			return val >= firstKey && val <= lastKey;
		}

		private boolean isLock(char val) {
			return val >= firstLock && val <= lastLock;
		}

		private boolean isAgent(char val) {
			switch (val) {
				case '@':
				case '%':
				case '$':
				case '&':
					return true;
				default:
					return false;
			}
		}

		boolean hasPath(char start, char end) {
			return pathLengths.get(start).containsKey(end);
		}

		int getPathLength(char start, char end) {
			return pathLengths.get(start).get(end);
		}

		int getPathMask(char start, char end) {
			return pathMasks.get(start).get(end);
		}

		static char[] robotChars = {'@', '%', '$', '&'};

		int getTotalCollectionDistance() {
			checkPaths();
			RobotState initialState = new RobotState(0, robotChars, 0);
			RobotState finalState = StatePathfinder.findClosestGoalState(initialState);
			if (finalState == null) return -1; // Final state not reachable
			return finalState.distance;
		}

		class RobotState implements State<RobotState> {
			private final int keyMask;
			private final char[] robotPositions;
			private final int distance;

			RobotState(int keyMask, char[] robotPositions, int distance) {
				this.keyMask = keyMask;
				this.robotPositions = robotPositions;
				this.distance = distance;
			}

			@Override
			public boolean equals(Object o) {
				if (this == o) return true;
				if (o == null || getClass() != o.getClass()) return false;
				RobotState that = (RobotState) o;
				return keyMask == that.keyMask &&
						distance == that.distance &&
						Arrays.equals(robotPositions, that.robotPositions);
			}

			@Override
			public int hashCode() {
				int result = Objects.hash(keyMask, distance);
				result = 31 * result + Arrays.hashCode(robotPositions);
				return result;
			}

			@Override
			public boolean isDone() {
				return keyMask == totalMask;
			}

			@Override
			public List<RobotState> getNextStates() {
				List<RobotState> nextList = new ArrayList<>();
				for (int nextKeyIndex = 0; nextKeyIndex < numKeys; ++nextKeyIndex) {
					int nextKeyMask = (1 << nextKeyIndex);
					char nextKey = (char) (nextKeyIndex + firstKey);
					if ((keyMask & nextKeyMask) != 0) {
						continue; // We got that key already
					}
					int robotToBeMoved = robotPartsByKeyIndex[nextKeyIndex];
					if (!hasPath(robotPositions[robotToBeMoved], nextKey)) {
						System.out.println("Broken");
					}
					if ((keyMask | getPathMask(robotPositions[robotToBeMoved], nextKey)) != keyMask) {
						continue; // We can't get that key yet
					}
					char[] newRobotPositions = Arrays.copyOf(robotPositions, 4);
					newRobotPositions[robotToBeMoved] = nextKey;
					nextList.add(new RobotState(keyMask | nextKeyMask,
							newRobotPositions,
							distance + getPathLength(robotPositions[robotToBeMoved], nextKey)
					));
				}
				return nextList;
			}

			@Override
			public RobotState reduce() {
				return new RobotState(keyMask, robotPositions, 0);
			}

			@Override
			public int compareTo(State o) {
				if (o.getClass() != RobotState.class) return -2;
				RobotState that = (RobotState) o;
				return comparator.compare(this, that);
			}

			@Override
			public String toString() {
				return "RobotState{" +
						"keyMask=" + Integer.toString(keyMask + (1 << numKeys), 2).substring(1) +
						", distance=" + distance +
						'}';
			}
		}

		private static Comparator<RobotState> comparator = Comparator.comparingInt((RobotState r)->r.distance).thenComparing(r -> r.keyMask);
	}

	interface State<T extends State<T>> extends Comparable<State<T>> {
		boolean isDone();

		List<T> getNextStates();
		T reduce();
	}

	static class StatePathfinder {

		private static <T extends State<T>> T findClosestGoalState(T initialState) {
			int stateCount = 0;
			PriorityQueue<T> states = new PriorityQueue<>(); // What kind of dumb priority queue sorts objects by their natural order????
			Set<T> completedStates = new HashSet<>(); // Extra data structure to check duplicates because the PriorityQueue can't do it
			states.add(initialState);
			while (!states.isEmpty()) {
				++stateCount;
				T state = states.poll();
				if ((stateCount & 0xfffff) == 0) {
					System.out.println("States processed so far: " + stateCount);
					System.out.println("Waiting states: " + states.size());
					System.out.println("Current state: " + state.toString());
				}
				T reducedState = state.reduce();
				if (completedStates.contains(reducedState)) {
					continue;
				}
				completedStates.add(reducedState);
				if (state.isDone()) {
					return state;
				}
				List<T> nextStates = state.getNextStates();
				states.addAll(nextStates);
			}
			return null;
		}
	}
}
