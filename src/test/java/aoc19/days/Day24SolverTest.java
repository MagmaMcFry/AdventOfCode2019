package aoc19.days;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class Day24SolverTest {
	@Test
	void testGetDiversity() {
		assertEquals(2129920, Day24Solver.getDiversity(Day24Solver.toBugArray(Arrays.asList(
				".....",
				".....",
				".....",
				"#....",
				".#..."
		))));
	}

	@Test
	void testSimulateDeeply() {


		assertEquals(Day24Solver.simulateDeeply(Arrays.asList(
				"#....",
				"..#..",
				"#.?##",
				"#..#.",
				"....#"
		), 10), Day24Solver.simulateDeeply(Arrays.asList(
				"....#",
				"#..#.",
				"#.?##",
				"..#..",
				"#...."
		), 10));

		assertEquals(Day24Solver.simulateDeeply(Arrays.asList(
				"....#",
				"..#..",
				"##?.#",
				".#..#",
				"#...."
		), 10), Day24Solver.simulateDeeply(Arrays.asList(
				"....#",
				"#..#.",
				"#.?##",
				"..#..",
				"#...."
		), 10));

		assertEquals(99, Day24Solver.simulateDeeply(Arrays.asList(
				"....#",
				"#..#.",
				"#.?##",
				"..#..",
				"#...."
		), 10));
	}
}
