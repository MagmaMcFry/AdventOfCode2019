package aoc19;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LongcodeTest {

	@Test
	void runCodeTest() {
		assertEquals(2, Longcode.runAndReturnFirstElement(new long[]{1,0,0,0,99}));
		assertEquals(30, Longcode.runAndReturnFirstElement(new long[]{1,1,1,4,99,5,6,0,99}));
		assertEquals(3500, Longcode.runAndReturnFirstElement(new long[]{1,9,10,3,2,3,11,0,99,30,40,50}));
	}

	@Test
	void testIO() {
		for (long i : new long[]{353,234,1,-4}) {
			Longcode longcode = new Longcode(new long[]{3, 0, 4, 0, 99});
			longcode.setInputArray(new long[]{i});
			List<Long> list = longcode.setOutputList();

			longcode.runCode();

			assertEquals(1, list.size());
			assertEquals(i, list.get(0));
		}
	}

	@Test
	void testJT() {
		assertEquals(0, Longcode.runAndReturnLastOutput(new long[]{3,9,8,9,10,9,4,9,99,-1,8}, new long[]{5}));
		assertEquals(1, Longcode.runAndReturnLastOutput(new long[]{3,9,8,9,10,9,4,9,99,-1,8}, new long[]{8}));
		assertEquals(0, Longcode.runAndReturnLastOutput(new long[]{3,9,8,9,10,9,4,9,99,-1,8}, new long[]{13}));
		assertEquals(1, Longcode.runAndReturnLastOutput(new long[]{3,9,7,9,10,9,4,9,99,-1,8}, new long[]{5}));
		assertEquals(0, Longcode.runAndReturnLastOutput(new long[]{3,9,7,9,10,9,4,9,99,-1,8}, new long[]{8}));
		assertEquals(0, Longcode.runAndReturnLastOutput(new long[]{3,9,7,9,10,9,4,9,99,-1,8}, new long[]{13}));

		assertEquals(0, Longcode.runAndReturnLastOutput(new long[]{3,3,1108,-1,8,3,4,3,99}, new long[]{5}));
		assertEquals(1, Longcode.runAndReturnLastOutput(new long[]{3,3,1108,-1,8,3,4,3,99}, new long[]{8}));
		assertEquals(0, Longcode.runAndReturnLastOutput(new long[]{3,3,1108,-1,8,3,4,3,99}, new long[]{13}));
		assertEquals(1, Longcode.runAndReturnLastOutput(new long[]{3,3,1107,-1,8,3,4,3,99}, new long[]{5}));
		assertEquals(0, Longcode.runAndReturnLastOutput(new long[]{3,3,1107,-1,8,3,4,3,99}, new long[]{8}));
		assertEquals(0, Longcode.runAndReturnLastOutput(new long[]{3,3,1107,-1,8,3,4,3,99}, new long[]{13}));
	}

	@Test
	void testQuine() {
		long[] quine = new long[]{109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99};
		long[] quineCopy = Arrays.copyOf(quine, quine.length);
		Longcode longcode = new Longcode(quineCopy);
		longcode.setInputArray(new long[0]);
		List<Long> outputList = longcode.setOutputList();
		longcode.runCode();
		long[] outputArray = outputList.stream().mapToLong(i->i).toArray();
		assertFalse(longcode.isBroken());
		assertTrue(longcode.isDone());
		assertArrayEquals(quine, outputArray);
	}

	@Test
	void testBigNum() {
		assertEquals(16, Long.toString(Longcode.runAndReturnLastOutput(
				new long[]{1102,34915192,34915192,7,4,7,99,0}, new long[0]
		)).length());
	}

	@Test
	void testBigNum2() {
		assertEquals(1125899906842624L, Longcode.runAndReturnLastOutput(new long[]{104,1125899906842624L,99}, new long[0]));
	}
}
