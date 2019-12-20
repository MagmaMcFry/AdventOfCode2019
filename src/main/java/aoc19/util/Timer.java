package aoc19.util;

import java.util.Locale;

public class Timer {
	public static void timeThis(Runnable r) {
		timeThis(r, 1);
	}

	public static void timeThis(Runnable r, int tries) {
		long ns_start = System.nanoTime();
		for (int i = 0; i < tries; ++i) {
			r.run();
		}
		long ns_end = System.nanoTime();
		long ns_avg = (ns_end - ns_start)/tries;
		double ms_avg = ns_avg / 1_000_000.0;
		System.out.println(String.format(Locale.ROOT, "Code ran %d times. Average runtime: %.2f ms", tries, ms_avg));
	}
}
