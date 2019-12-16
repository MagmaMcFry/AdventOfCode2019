package aoc19.days;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Day16SolverTest {

	@Test
	void testUtils() {
		assertArrayEquals(new int[]{1,3,5,6,2,4,1,9,0,1}, Day16Solver.split("1356241901"));
		assertEquals("1356241901", Day16Solver.join(new int[]{1,3,5,6,2,4,1,9,0,1}, 0, 10));
		assertEquals("6241", Day16Solver.join(new int[]{1,3,5,6,2,4,1,9,0,1}, 3, 4));
		assertArrayEquals(new int[]{1,4,2,3,1,4,2,3,1,4,2,3}, Day16Solver.repeat(new int[]{1,4,2,3}, 3));
	}

	@Test
	void testFFT() {
		int[] input = Day16Solver.split("12345678");
		int[] after1Phase = Day16Solver.split("48226158");
		assertArrayEquals(after1Phase, Day16Solver.fft(input));
		int[] after2Phase = Day16Solver.split("34040438");
		assertArrayEquals(after2Phase, Day16Solver.fft(after1Phase));
		int[] after3Phase = Day16Solver.split("03415518");
		assertArrayEquals(after3Phase, Day16Solver.fft(after2Phase));
	}

	@Test
	void testFastFFT() {
		int[] input = Day16Solver.split("12345678");
		int[] after1Phase = Day16Solver.split("48226158");
		assertArrayEquals(after1Phase, Day16Solver.ffft(input));
		int[] after2Phase = Day16Solver.split("34040438");
		assertArrayEquals(after2Phase, Day16Solver.ffft(after1Phase));
		int[] after3Phase = Day16Solver.split("03415518");
		assertArrayEquals(after3Phase, Day16Solver.ffft(after2Phase));
	}

	@Test
	void testRepeatedFFT() {
		int[] input = Day16Solver.split("80871224585914546619083218645595");
		assertArrayEquals(Day16Solver.split("24176176"), Day16Solver.repeatedFFT(input, 100, 8));

		input = Day16Solver.split("19617804207202209144916044189917");
		assertArrayEquals(Day16Solver.split("73745418"), Day16Solver.repeatedFFT(input, 100, 8));

		input = Day16Solver.split("69317163492948606335995924319873");
		assertArrayEquals(Day16Solver.split("52432133"), Day16Solver.repeatedFFT(input, 100, 8));
	}


	@Test
	void testRepeatedFastFFT() {
		int[] input = Day16Solver.repeat(Day16Solver.split("03036732577212944063491565474664"), 10000);
		int offset = Integer.parseInt(Day16Solver.join(input, 0, 7));
		assertEquals("84462026", Day16Solver.join(Day16Solver.repeatedFFFT(input, 100), offset, 8));

		input = Day16Solver.repeat(Day16Solver.split("02935109699940807407585447034323"), 10000);
		offset = Integer.parseInt(Day16Solver.join(input, 0, 7));
		assertEquals("78725270", Day16Solver.join(Day16Solver.repeatedFFFT(input, 100), offset, 8));

		input = Day16Solver.repeat(Day16Solver.split("03081770884921959731165446850517"), 10000);
		offset = Integer.parseInt(Day16Solver.join(input, 0, 7));
		assertEquals("53553731", Day16Solver.join(Day16Solver.repeatedFFFT(input, 100), offset, 8));

	}
}
