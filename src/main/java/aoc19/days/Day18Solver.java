package aoc19.days;

import aoc19.util.Direction;
import aoc19.util.FileReader;
import aoc19.util.I2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day18Solver {
	public static void main(String[] args) {
		solvePart1();
		//solvePart2();
	}

	private static void solvePart1() {
		Maze maze = new Maze(getMazeStrings(), 'z');
		System.out.println("Read maze");
		maze.checkPaths();
		System.out.println("Found and checked paths");
		System.out.println("Collection distance: " +maze.getTotalCollectionDistance());
	}

	private static List<String> getMazeStrings() {
		return FileReader.readLines("/day18/maze.txt");
	}

	static char[][] toCharArray(List<String> mazeStr) {
		return mazeStr.stream().map(String::toCharArray).toArray(char[][]::new);
	}

	static class Maze {
		private final int firstKey, lastKey, firstLock, lastLock;
		private final int numKeys;
		private final Map<Character, I2> objectPositions;
		private final char[][] array;
		private final HashMap<Character, HashMap<Character, Integer>> pathMasks = new HashMap<>();
		private final HashMap<Character, HashMap<Character, Integer>> pathLengths = new HashMap<>();
		private boolean pathsChecked = false;

		public Maze(List<String> mazeStrings, char lastKeyChar) {
			this.lastKey = lastKeyChar;
			firstKey = 'a';
			firstLock = 'A';
			lastLock = firstLock - firstKey + lastKey;
			numKeys = 1 + lastKey - firstKey;
			this.array = toCharArray(mazeStrings);

			objectPositions = new HashMap<>();
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

		public int getTotalCollectionDistance() {
			checkPaths();
			Map<Character, Map<Integer, Integer>> distanceByLastKeyAndMasks = new HashMap<>();
			for (int c = firstKey; c <= lastKey; ++c) {
				distanceByLastKeyAndMasks.computeIfAbsent((char)c, HashMap::new);
			}
			int maskLimit = 1 << numKeys;
			for (int firstEverKeyIndex = 0; firstEverKeyIndex < numKeys; ++firstEverKeyIndex) {

				char firstEverKey = (char) (firstEverKeyIndex + firstKey);
				if (getPathMask('@', firstEverKey) == 0) {
					distanceByLastKeyAndMasks.get(firstEverKey).put(1 << firstEverKeyIndex, getPathLength('@', firstEverKey));
				}
			}

			for (int currentMask = 1; currentMask < maskLimit; ++currentMask) {
				if ((currentMask & 0xfffff) == 0) {
					System.out.println("Current mask: " + Integer.toString(currentMask, 2));
				}
				CURRENT_KEY:
				for (int currentKeyIndex = 0; currentKeyIndex < numKeys; ++currentKeyIndex) {
					if ((currentMask & (1 << currentKeyIndex)) == 0) continue CURRENT_KEY;
					// Current key is being gathered
					int prevMask = currentMask & ~(1 << currentKeyIndex);
					char currentKey = (char) (firstKey + currentKeyIndex);
					PREV_KEY:
					for (int prevKeyIndex = 0; prevKeyIndex < numKeys; ++prevKeyIndex) {
						if ((currentMask & (1 << prevKeyIndex)) == 0) continue PREV_KEY;
						// Previous key is already gathered
						char prevKey = (char) (firstKey + prevKeyIndex);
						if ((prevMask | getPathMask(prevKey, currentKey)) != prevMask) {
							// Previous keys were insufficient to gather current key
							continue PREV_KEY;
						}
						Integer prevLength = distanceByLastKeyAndMasks.get(prevKey).get(prevMask);
						if (prevLength == null) {
							// Previous key was impossible to get last
							continue PREV_KEY;
						}
						int newCurrentLength = prevLength + getPathLength(prevKey, currentKey);
						Integer oldCurrentLength = distanceByLastKeyAndMasks.get(currentKey).get(currentMask);
						if (oldCurrentLength == null || oldCurrentLength > newCurrentLength) {
//							System.out.println("Going from " + prevKey + " to " + currentKey + " using key mask " +
//									Integer.toString(prevMask + maskLimit, 2).substring(1)
//									+ " -> " + Integer.toString(currentMask + maskLimit, 2).substring(1)
//							);
							distanceByLastKeyAndMasks.get(currentKey).put(currentMask, newCurrentLength);
						}
					}
				}
			}
			int bestLength = Integer.MAX_VALUE;
			for (int finalKey = firstKey; finalKey <= lastKey; ++finalKey) {
				Integer distanceGettingThatKeyLast = distanceByLastKeyAndMasks.get((char)finalKey).get(maskLimit-1);
				if (distanceGettingThatKeyLast != null && distanceGettingThatKeyLast < bestLength) {
					bestLength = distanceGettingThatKeyLast;
				}
			}
			return bestLength;
		}

		void checkPaths() {
			if (pathsChecked) return;
			for (int end = 0; end < numKeys; ++end) {
				char endKey = (char) ('a' + end);
				checkPath('@', endKey);
				for (int start = 0; start < numKeys; ++start) {
					char startKey = (char) ('a' + start);
					checkPath(startKey, endKey);
				}
			}
			pathsChecked = true;
		}

		void checkPath(char startItem, char endItem) {
			if (startItem == endItem) {
				pathLengths.computeIfAbsent(startItem, HashMap::new).put(endItem, 0);
				pathMasks.computeIfAbsent(startItem, HashMap::new).put(endItem, 1 << numKeys); // Make self-path impossible to use
				return;
			};
			List<Character> foundPath = findPathBetween(startItem, endItem);
			int mask = 0;
			for (char c : foundPath) {
				if (isLock(c)) {
					mask |= 1 << (c - firstLock);
				} else if (isKey(c)) {
					mask |= 1 << (c - firstKey); // Optimization: Can't go through keys without collecting them
				}
			}
			pathLengths.computeIfAbsent(startItem, HashMap::new).put(endItem, 1+foundPath.size());
			pathMasks.computeIfAbsent(startItem, HashMap::new).put(endItem, mask);
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
				for (I2 currentPos : openSet) {
					TURNING:
					for (Direction dir : Direction.values()) {
						I2 neighborPos = new I2(currentPos.x + dir.dx, currentPos.y + dir.dy);
						if (array[neighborPos.y][neighborPos.x] == '#') {
							continue TURNING;
						}
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
			throw new IllegalStateException("Could not find a path :(");
		}

		private boolean isKey(char val) {
			return val >= firstKey && val <= lastKey;
		}

		private boolean isLock(char val) {
			return val >= firstLock && val <= lastLock;
		}

		private boolean isAgent(char val) {
			return val == '@';
		}

		private char getLock(char key) {
			return (char) (key + 'A' - 'a');
		}

		private char getKey(char lock) {
			return (char) (lock + 'A' - 'a');
		}

		int getPathLength(char start, char end) {
			return pathLengths.get(start).get(end);
		}

		int getPathMask(char start, char end) {
			return pathMasks.get(start).get(end);
		}
	}
}
