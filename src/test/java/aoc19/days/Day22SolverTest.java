package aoc19.days;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Day22SolverTest {

	void testSimpleNontrivialShuffle(long numCards, long val0, long val1, long val2, long val3, String... shuffleDef) {
		Day22Solver.Shuffle shuffle = Day22Solver.Shuffle.get(numCards, shuffleDef);
		Day22Solver.Shuffle iShuffle = shuffle.invert();
		assertEquals(iShuffle.apply(0), val0);
		assertEquals(iShuffle.apply(1), val1);
		assertEquals(iShuffle.apply(2), val2);
		assertEquals(iShuffle.apply(3), val3);

		assertFalse(shuffle.isIdentity());
		assertFalse(iShuffle.isIdentity());
		assertTrue(iShuffle.then(shuffle).isIdentity());
		assertTrue(shuffle.then(iShuffle).isIdentity());
	}

	@Test
	void testShortShuffles() {
		testSimpleNontrivialShuffle(10, 0, 3, 6, 9,
				"deal with increment 7",
				"deal into new stack",
				"deal into new stack"
		);
		testSimpleNontrivialShuffle(10, 3, 0, 7, 4,
				"cut 6",
				"deal with increment 7",
				"deal into new stack"
		);
		testSimpleNontrivialShuffle(10, 6, 3, 0, 7,
				"deal with increment 7",
				"deal with increment 9",
				"cut -2"
		);
		testSimpleNontrivialShuffle(10, 9, 2, 5, 8,
				"deal into new stack",
				"cut -2",
				"deal with increment 7",
				"cut 8",
				"cut -4",
				"deal with increment 7",
				"cut 3",
				"deal with increment 9",
				"deal with increment 3",
				"cut -1"
		);
	}

	@Test
	void testShuffleRepeat() {
		Day22Solver.Shuffle s1 = Day22Solver.Shuffle.get(119315717514047L,
				"deal into new stack",
				"cut -2",
				"deal with increment 119315717514",
				"cut 931571751404",
				"cut -4",
				"deal with increment 119315617514",
				"cut 3",
				"deal with increment 9",
				"deal with increment 3",
				"cut -1"
		);

		Day22Solver.Shuffle s2 = s1.then(s1);
		assertEquals(s2.then(s1), s1.then(s2));
		Day22Solver.Shuffle s3 = s1.then(s2);
		assertEquals(s3.then(s1), s2.then(s2));
		assertEquals(s1.then(s3), s2.then(s2));

		assertFalse(s1.isIdentity());
		Day22Solver.Shuffle si = s1.invert();
		assertFalse(si.isIdentity());
		assertTrue(si.then(s1).isIdentity());
		assertTrue(s1.then(si).isIdentity());

		assertEquals(s1.repeat(2), s2);
		assertEquals(s1.repeat(3), s3);
		assertEquals(s1.repeat(4), s2.then(s2));
		assertEquals(s1.repeat(32493857 + 13432542), s1.repeat(32493857).then(s1.repeat(13432542)));
		assertTrue(s1.repeat(1234567).then(si.repeat(1234567)).isIdentity());
	}
}
