package aoc19.days;

import aoc19.Intcode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day2SolverTest {

	@Test
	void runCodeTest() {
		assertEquals(2, Intcode.runAndReturnFirstElement(new int[]{1,0,0,0,99}));
		assertEquals(30, Intcode.runAndReturnFirstElement(new int[]{1,1,1,4,99,5,6,0,99}));
		assertEquals(3500, Intcode.runAndReturnFirstElement(new int[]{1,9,10,3,2,3,11,0,99,30,40,50}));
	}

}
