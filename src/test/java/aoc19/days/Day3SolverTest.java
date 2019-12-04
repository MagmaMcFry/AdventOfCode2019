package aoc19.days;

import aoc19.days.Day3Solver;
import aoc19.util.I2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day3SolverTest {

	@Test
	void getWireCoordsTest() {
		assertArrayEquals(I2.a(0, 0, 0, 4, 3, 4, 3, 2, -14, 2), Day3Solver.getWireCoords("U4,R3,D2,L17"));

	}

	@Test
	void getNearestWireCrossingTest() {
		assertEquals(159, Day3Solver.getNearestWireCrossing(
				"R75,D30,R83,U83,L12,D49,R71,U7,L72",
				"U62,R66,U55,R34,D71,R55,D58,R83"
		).l1());
		assertEquals(159, Day3Solver.getNearestWireCrossing(
				"U62,R66,U55,R34,D71,R55,D58,R83",
				"R75,D30,R83,U83,L12,D49,R71,U7,L72"
		).l1());
		assertEquals(135, Day3Solver.getNearestWireCrossing(
				"R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51",
				"U98,R91,D20,R16,D67,R40,U7,R15,U6,R7"
		).l1());
	}

	@Test
	void getFastestWireCrossingSpeedTest() {
		assertEquals(30, Day3Solver.getFastestWireCrossingSpeed(
				"R8,U5,L5,D3",
				"U7,R6,D4,L4"
		));
		assertEquals(610, Day3Solver.getFastestWireCrossingSpeed(
				"R75,D30,R83,U83,L12,D49,R71,U7,L72",
				"U62,R66,U55,R34,D71,R55,D58,R83"
		));
		assertEquals(610, Day3Solver.getFastestWireCrossingSpeed(
				"U62,R66,U55,R34,D71,R55,D58,R83",
				"R75,D30,R83,U83,L12,D49,R71,U7,L72"
		));
		assertEquals(410, Day3Solver.getFastestWireCrossingSpeed(
				"R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51",
				"U98,R91,D20,R16,D67,R40,U7,R15,U6,R7"
		));
	}
}
