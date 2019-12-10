package aoc19.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class I2 {
	public final int x, y;
	public I2(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public static I2[] array(Integer... args) {
		if (args.length % 2 != 0) throw new IllegalArgumentException("Length of arg list must be divisible by 2");
		I2[] result = new I2[args.length/2];
		for (int i = 0; i < result.length; ++i) {
			result[i] = new I2(args[2*i], args[2*i+1]);
		}
		return result;
	}

	public static List<I2> l(Integer... args) {
		if (args.length % 2 != 0) throw new IllegalArgumentException("Length of arg list must be divisible by 2");
		List<I2> result = new ArrayList<>();
		for (int i = 0; i < args.length; i+=2) {
			result.add(new I2(args[i], args[i+1]));
		}
		return result;
	}

	/**
	 * Direction from p1 to p2, represented as an I2 with coprime entries
	 */
	public static I2 dir(I2 p1, I2 p2) {
		int gcd = gcd(p2.x-p1.x, p2.y-p1.y);
		return new I2((p2.x-p1.x)/gcd, (p2.y-p1.y)/gcd);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		I2 i2 = (I2) o;
		return x == i2.x &&
				y == i2.y;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public String toString() {
		return "(" + x +
				"," + y +
				")";
	}

	public int l1() {
		return Math.abs(x) + Math.abs(y);
	}

	public int d1(I2 o) {
		return Math.abs(x-o.x) + Math.abs(y-o.y);
	}

	private static int gcd(int x, int y) {
		x = Math.abs(x);
		y = Math.abs(y);
		if (x == 0 && y == 0) return 1; // Let's not divide by 0
		if (y == 0) return x;
		if (x == 0) return y;
		return gcd(y, x%y);
	}
}
