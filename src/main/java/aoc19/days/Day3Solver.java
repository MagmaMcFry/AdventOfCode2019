package aoc19.days;

import aoc19.util.FileReader;
import aoc19.util.I2;

import java.util.Arrays;
import java.util.List;

public class Day3Solver {
	public static void main(String[] args) {
		solvePart1();
		solvePart2();
	}

	private static List<String> getWireDefs() {
		return FileReader.readLines("/day3/input.txt");
	}

	public static I2[] getWireCoords(String wireString) {
		String[] routes = wireString.split(",");
		I2[] result = new I2[routes.length+1];
		int x = 0, y = 0;
		int pos = 0;
		result[pos++] = I2.p(0,0);
		for (String route : routes) {
			String dir = route.substring(0, 1);
			int len = Integer.valueOf(route.substring(1));
			switch(dir) {
				case "U": y += len; break;
				case "D": y -= len; break;
				case "L": x -= len; break;
				case "R": x += len; break;
			}
			result[pos++] = I2.p(x,y);
		}
		return result;
	}

	static I2 getNearestWireCrossing(String wire1String, String wire2String) {
		I2[] wire1Coords = getWireCoords(wire1String);
		I2[] wire2Coords = getWireCoords(wire2String);
		I2 optimalCrossing = I2.p(0,0);
		for (int i1 = 1; i1 < wire1Coords.length; ++i1) {
			for (int i2 = 1; i2 < wire2Coords.length; ++i2) {
				I2 crossing = getCrossing(wire1Coords[i1-1], wire1Coords[i1], wire2Coords[i2-1], wire2Coords[i2]);
				if (crossing != null) {
					if (optimalCrossing.x == 0 && optimalCrossing.y == 0) {
						optimalCrossing = crossing;
					} else if (crossing.l1() < optimalCrossing.l1()) {
						optimalCrossing = crossing;
					}
				}
			}
		}
		return optimalCrossing;
	}

	public static int getFastestWireCrossingSpeed(String wire1String, String wire2String) {
		I2[] wire1Coords = getWireCoords(wire1String);
		I2[] wire2Coords = getWireCoords(wire2String);
		int optimalSpeed = 0;
		int prevLength1 = 0;
		for (int i1 = 1; i1 < wire1Coords.length; ++i1) {
			int prevLength2 = 0;
			for (int i2 = 1; i2 < wire2Coords.length; ++i2) {
				I2 crossing = getCrossing(wire1Coords[i1-1], wire1Coords[i1], wire2Coords[i2-1], wire2Coords[i2]);
				if (crossing != null) {
					int speed = prevLength1 + prevLength2 + crossing.d1(wire1Coords[i1-1]) + crossing.d1(wire2Coords[i2-1]);
					if (optimalSpeed == 0) {
						optimalSpeed = speed;
					} else if (speed < optimalSpeed) {
						optimalSpeed = speed;
					}
				}
				prevLength2 += wire2Coords[i2-1].d1(wire2Coords[i2]);
			}
			prevLength1 += wire1Coords[i1-1].d1(wire1Coords[i1]);
		}
		return optimalSpeed;
	}

	private static I2 getCrossing(I2 a1, I2 a2, I2 b1, I2 b2) {
		if (a1.x > a2.x || a1.y > a2.y) {
			I2 tmp = a1; a1 = a2; a2 = tmp;
		}
		if (b1.x > b2.x || b1.y > b2.y) {
			I2 tmp = b1; b1 = b2; b2 = tmp;
		}
		if ((b1.x < b2.x) ^ (a1.y < a2.y)) return null;
		if (a1.x <= b2.x && b1.x <= a2.x && a1.y <= b2.y && b1.y <= a2.y) {
			return I2.p(Math.max(a1.x,b1.x), Math.max(a1.y,b1.y));
		} else {
			return null;
		}
	}

	private static void solvePart1() {
		List<String> wireDefs = getWireDefs();
		String wire1String = wireDefs.get(0);
		String wire2String = wireDefs.get(1);
		I2 optimalCrossing = getNearestWireCrossing(wire1String, wire2String);
		System.out.println("Closest crossing: " + optimalCrossing + ". Distance: " + optimalCrossing.l1());
	}

	private static void solvePart2() {
		List<String> wireDefs = getWireDefs();
		String wire1String = wireDefs.get(0);
		String wire2String = wireDefs.get(1);
		int optimalSpeed = getFastestWireCrossingSpeed(wire1String, wire2String);
		System.out.println("Fastest wire speed: " + optimalSpeed + ".");

	}
}
