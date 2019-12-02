package aoc19.day1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day1SolverTest {

	@Test
	void getFuelTest() {
		assertEquals(0, Day1Solver.getFuel(0));
		assertEquals(1, Day1Solver.getFuel(11));
		assertEquals(2, Day1Solver.getFuel(12));
		assertEquals(2, Day1Solver.getFuel(13));
		assertEquals(2, Day1Solver.getFuel(14));
		assertEquals(3, Day1Solver.getFuel(15));
		assertEquals(654, Day1Solver.getFuel(1969));
		assertEquals(33583, Day1Solver.getFuel(100756));
	}

	@Test
	void getFuelRecursiveTest() {
		assertEquals(966, Day1Solver.getFuelRecursive(1969));
		assertEquals(50346, Day1Solver.getFuelRecursive(100756));
	}
}
