package aoc19.days;

import aoc19.util.I2;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class Day10SolverTest {

	@Test
	void testI2Dir() {
		assertEquals(new I2(1, 1), I2.dir(new I2(3, 4), new I2(6, 7)));
		assertEquals(new I2(-1, -1), I2.dir(new I2(6, 7), new I2(3, 4)));
	}

	private final String[][] detectionTestCases = {
			{
					"......#.#.",
					"#..#.#....",
					"..#######.",
					".#.#.###..",
					".#..#.....",
					"..#....#.#",
					"#..#....#.",
					".##.#..###",
					"##...#..#.",
					".#....####",
			},
			{
					"#.#...#.#.",
					".###....#.",
					".#....#...",
					"##.#.#.#.#",
					"....#.#.#.",
					".##..###.#",
					"..#...##..",
					"..##....##",
					"......#...",
					".####.###.",
			},
			{
					".#..#..###",
					"####.###.#",
					"....###.#.",
					"..###.##.#",
					"##.##.#.#.",
					"....###..#",
					"..#.#..#.#",
					"#..#.#.###",
					".##...##.#",
					".....#.#..",
			},
			{
					".#..##.###...#######",
					"##.############..##.",
					".#.######.########.#",
					".###.#######.####.#.",
					"#####.##.#.##.###.##",
					"..#####..#.#########",
					"####################",
					"#.####....###.#.#.##",
					"##.#################",
					"#####.##.###..####..",
					"..######..##.#######",
					"####.##.####...##..#",
					".#####..#.######.###",
					"##...#.##########...",
					"#.##########.#######",
					".####.#.###.###.#.##",
					"....##.##.###..#####",
					".#.#.###########.###",
					"#.#.#.#####.####.###",
					"###.##.####.##.#..##",
			},
	};

	private final static int[] detectionTestExpectedResults = {33, 35, 41, 210};

	@Test
	void testMaxDirs() {
		assertEquals(detectionTestCases.length, detectionTestExpectedResults.length);
		for (int i = 0; i < detectionTestCases.length; ++i) {
			boolean[][] grid = Day10Solver.getGrid(detectionTestCases[i]);
			List<I2> positions = Day10Solver.getPositions(grid);
			Map<I2, Map<I2, I2>> dirMap = Day10Solver.getDirMap(positions);
			I2 posWithMaxDirs = Day10Solver.getPosWithMaxDirs(positions, dirMap);
			assertEquals(detectionTestExpectedResults[i], dirMap.get(posWithMaxDirs).size());
		}
	}
}
