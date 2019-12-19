package aoc19.days;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class Day18SolverTest {

	private int getCollectionDistance(char maxKey, String... mazeStrings) {
		Day18Solver.Maze maze = new Day18Solver.Maze(Arrays.asList(mazeStrings), maxKey);
		return maze.getTotalCollectionDistance();
	}

	@Test
	void testSoloMazes() {
		assertEquals(86, getCollectionDistance('f',
				"########################",
				"#f.D.E.e.C.b.A.@.a.B.c.#",
				"######################.#",
				"#d.....................#",
				"########################")
		);
		assertEquals(132, getCollectionDistance('g',
				"########################",
				"#...............b.C.D.f#",
				"#.######################",
				"#.....@.a.B.c.d.A.e.F.g#",
				"########################"
		));
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
		assertEquals(81, getCollectionDistance('i',
				"########################",
				"#@..............ac.GI.b#",
				"###d#e#f################",
				"###A#B#C################",
				"###g#h#i################",
				"########################"
		));
	}

	@Test
	void testMultiMazes() {
		assertEquals(8, getCollectionDistance('d',
				"#######",
				"#a.#Cd#",
				"##@#%##",
				"#######",
				"##$#&##",
				"#cB#Ab#",
				"#######"
		));
		assertEquals(24, getCollectionDistance('d',
				"###############",
				"#d.ABC.#.....a#",
				"######@#%######",
				"###############",
				"######$#&######",
				"#b.....#.....c#",
				"###############"
		));
		assertEquals(32, getCollectionDistance('l',
				"#############",
				"#DcBa.#.GhKl#",
				"#.###@#%#I###",
				"#e#d#####j#k#",
				"###C#$#&###J#",
				"#fEbA.#.FgHi#",
				"#############"
		));
		assertEquals(72, getCollectionDistance('o',
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
