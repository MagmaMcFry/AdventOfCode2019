package aoc19;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class PermutationEngineTest {
	@Test
	public void testPermutationEngine() {
		PermutationEngine pe = new PermutationEngine(10);
		final int[] count = {0};
		int expectedCount = 3628800;
		pe.run(perm->++count[0]);
		assertEquals(expectedCount, count[0]);
	}
}
