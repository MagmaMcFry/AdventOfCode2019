package aoc19;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class IntcodeTest {

	@Test
	void runCodeTest() {
		assertEquals(2, Intcode.runAndReturnFirstElement(new int[]{1,0,0,0,99}));
		assertEquals(30, Intcode.runAndReturnFirstElement(new int[]{1,1,1,4,99,5,6,0,99}));
		assertEquals(3500, Intcode.runAndReturnFirstElement(new int[]{1,9,10,3,2,3,11,0,99,30,40,50}));
	}

	@Test
	void testIO() {
		for (int i : new int[]{353,234,1,-4}) {
			Intcode intcode = new Intcode(new int[]{3, 0, 4, 0, 99});
			intcode.setInputArray(new int[]{i});
			List<Integer> list = intcode.setOutputList();

			intcode.runCode();

			assertEquals(1, list.size());
			assertEquals(i, list.get(0));
		}
	}

	@Test
	void testJT() {
		assertEquals(0, Intcode.runAndReturnLastOutput(new int[]{3,9,8,9,10,9,4,9,99,-1,8}, new int[]{5}));
		assertEquals(1, Intcode.runAndReturnLastOutput(new int[]{3,9,8,9,10,9,4,9,99,-1,8}, new int[]{8}));
		assertEquals(0, Intcode.runAndReturnLastOutput(new int[]{3,9,8,9,10,9,4,9,99,-1,8}, new int[]{13}));
		assertEquals(1, Intcode.runAndReturnLastOutput(new int[]{3,9,7,9,10,9,4,9,99,-1,8}, new int[]{5}));
		assertEquals(0, Intcode.runAndReturnLastOutput(new int[]{3,9,7,9,10,9,4,9,99,-1,8}, new int[]{8}));
		assertEquals(0, Intcode.runAndReturnLastOutput(new int[]{3,9,7,9,10,9,4,9,99,-1,8}, new int[]{13}));

		assertEquals(0, Intcode.runAndReturnLastOutput(new int[]{3,3,1108,-1,8,3,4,3,99}, new int[]{5}));
		assertEquals(1, Intcode.runAndReturnLastOutput(new int[]{3,3,1108,-1,8,3,4,3,99}, new int[]{8}));
		assertEquals(0, Intcode.runAndReturnLastOutput(new int[]{3,3,1108,-1,8,3,4,3,99}, new int[]{13}));
		assertEquals(1, Intcode.runAndReturnLastOutput(new int[]{3,3,1107,-1,8,3,4,3,99}, new int[]{5}));
		assertEquals(0, Intcode.runAndReturnLastOutput(new int[]{3,3,1107,-1,8,3,4,3,99}, new int[]{8}));
		assertEquals(0, Intcode.runAndReturnLastOutput(new int[]{3,3,1107,-1,8,3,4,3,99}, new int[]{13}));

	}
}
