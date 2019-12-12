package aoc19.days;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Day12SolverTest {
	@Test
	void testSimulation() {
		Day12Solver.Moon[] moons = {
				new Day12Solver.Moon(-1, 0, 2),
				new Day12Solver.Moon(2, -10, -7),
				new Day12Solver.Moon(4, -8, 8),
				new Day12Solver.Moon(3, 5, -1),
		};

		Day12Solver.tick(moons);
		Day12Solver.Moon[] expectedMoons = {
				new Day12Solver.Moon(2, -1, 1, 3, -1, -1),
				new Day12Solver.Moon(3, -7, -4, 1, 3, 3),
				new Day12Solver.Moon(1, -7, 5, -3, 1, -3),
				new Day12Solver.Moon(2, 2, 0, -1, -3, 1)
		};
		assertArrayEquals(expectedMoons, moons);

		Day12Solver.tick(moons);
		expectedMoons = new Day12Solver.Moon[]{
				new Day12Solver.Moon(5, -3, -1, 3, -2, -2),
				new Day12Solver.Moon(1, -2, 2, -2, 5, 6),
				new Day12Solver.Moon(1, -4, -1, 0, 3, -6),
				new Day12Solver.Moon(1, -4, 2, -1, -6, 2),
		};
		assertArrayEquals(expectedMoons, moons);

		Day12Solver.run(moons, 8);
		expectedMoons = new Day12Solver.Moon[]{
				new Day12Solver.Moon(2, 1, -3, -3, -2, 1),
				new Day12Solver.Moon(1, -8, 0, -1, 1, 3),
				new Day12Solver.Moon(3, -6, 1, 3, 2, -3),
				new Day12Solver.Moon(2, 0, 4, 1, -1, -1),
		};
		assertArrayEquals(expectedMoons, moons);

		assertEquals(179, Day12Solver.getEnergy(moons));
	}

	@Test
	void testRepetition() {
		Day12Solver.Moon[] moons = {
				new Day12Solver.Moon(-1, 0, 2),
				new Day12Solver.Moon(2, -10, -7),
				new Day12Solver.Moon(4, -8, 8),
				new Day12Solver.Moon(3, 5, -1),
		};

		assertEquals(2772L, Day12Solver.getPeriod(moons));

		assertEquals(2772L, Day12Solver.getPeriodEfficiently(moons));
	}
}
