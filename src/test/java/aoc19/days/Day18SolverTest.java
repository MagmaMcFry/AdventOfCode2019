package aoc19.days;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class Day18SolverTest {

	@Test
	void testSmallMaze() {
		List<String> smallMaze = Arrays.asList(
				"#########",
				"#b.A.@.a#",
				"#########"
		);
		Day18Solver.Maze maze = new Day18Solver.Maze(smallMaze, 'b');
		maze.checkPaths();
		assertEquals(2, maze.getPathLength('@', 'a'));
		assertEquals(0b0, maze.getPathMask('@', 'a'));
		assertEquals(6, maze.getPathLength('a', 'b'));
		assertEquals(0b1, maze.getPathMask('a', 'b'));
		assertEquals(8, maze.getTotalCollectionDistance());
	}

	private int getCollectionDistance(char maxKey, String... mazeStrings) {
		Day18Solver.Maze maze = new Day18Solver.Maze(Arrays.asList(mazeStrings), maxKey);
		return maze.getTotalCollectionDistance();
	}

	@Test
	void testMediumMaze1() {
		assertEquals(86, getCollectionDistance('f',
				"########################",
				"#f.D.E.e.C.b.A.@.a.B.c.#",
				"######################.#",
				"#d.....................#",
				"########################")
		);
	}

	@Test
	void testMediumMaze2() {
		assertEquals(132, getCollectionDistance('g',
				"########################",
				"#...............b.C.D.f#",
				"#.######################",
				"#.....@.a.B.c.d.A.e.F.g#",
				"########################"
		));
	}

	@Test
	void testMediumMaze3() {
		assertEquals(136, getCollectionDistance('p',
				"#################",
				"#i.G..c...e..H.p#",
				"########.########",
				"#j.A..b...f..D.o#",
				"########@########",
				"#k.E..a...g..B.n#",
				"########.########",
				"#l.F..d...h..C.m#",
				"#################"
		));
	}

	@Test
	void testMediumMaze4() {
		assertEquals(81, getCollectionDistance('i',
				"########################",
				"#@..............ac.GI.b#",
				"###d#e#f################",
				"###A#B#C################",
				"###g#h#i################",
				"########################"
		));
	}


	private int getMultiCollectionDistance(char maxKey, String... mazeStrings) {
		Day18SolverPart2.Maze maze = new Day18SolverPart2.Maze(Arrays.asList(mazeStrings), maxKey);
		return maze.getTotalCollectionDistance();
	}

	@Test
	void testMultiMaze1() {
		assertEquals(8, getMultiCollectionDistance('d',
				"#######",
				"#a.#Cd#",
				"##@#%##",
				"#######",
				"##$#&##",
				"#cB#Ab#",
				"#######"
		));
	}

	@Test
	void testMultiMaze2() {
		assertEquals(24, getMultiCollectionDistance('d',
				"###############",
				"#d.ABC.#.....a#",
				"######@#%######",
				"###############",
				"######$#&######",
				"#b.....#.....c#",
				"###############"
		));
	}

	@Test
	void testMultiMaze3() {
		assertEquals(32, getMultiCollectionDistance('l',
				"#############",
				"#DcBa.#.GhKl#",
				"#.###@#%#I###",
				"#e#d#####j#k#",
				"###C#$#&###J#",
				"#fEbA.#.FgHi#",
				"#############"
		));
	}

	@Test
	void testMultiMaze4() {
		assertEquals(72, getMultiCollectionDistance('o',
				"#############",
				"#g#f.D#..h#l#",
				"#F###e#E###.#",
				"#dCba@#%BcIJ#",
				"#############",
				"#nK.L$#&G...#",
				"#M###N#H###.#",
				"#o#m..#i#jk.#",
				"#############"
		));
	}

}
