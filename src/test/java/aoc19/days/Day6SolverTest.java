package aoc19.days;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class Day6SolverTest {

	private Map<String, String> getExample() {
		List<String> directOrbitStrings = Arrays.asList("COM)B", "B)C", "C)D", "D)E", "E)F", "B)G", "G)H", "D)I", "E)J", "J)K", "K)L");
		Map<String, String> parentMap = Day6Solver.computeParentMap(directOrbitStrings);
		return parentMap;
	}

	@Test
	void testOrbitCounter() {
		Map<String, String> parentMap = getExample();
		assertEquals(42, Day6Solver.countOrbits(parentMap));
	}

	@Test
	void testHeightFunction() {
		Map<String, String> parentMap = getExample();
		assertEquals(7, Day6Solver.getHeight(parentMap, "L"));
		assertEquals(5, Day6Solver.getHeight(parentMap, "F"));
		assertEquals(0, Day6Solver.getHeight(parentMap, "COM"));
	}

	@Test
	void testDistFunction() {
		Map<String, String> parentMap = getExample();
		assertEquals(0, Day6Solver.getDistance(parentMap, "D", "D"));
		assertEquals(1, Day6Solver.getDistance(parentMap, "D", "E"));
		assertEquals(2, Day6Solver.getDistance(parentMap, "E", "I"));
		assertEquals(3, Day6Solver.getDistance(parentMap, "D", "K"));
		assertEquals(7, Day6Solver.getDistance(parentMap, "COM", "L"));
	}
}
