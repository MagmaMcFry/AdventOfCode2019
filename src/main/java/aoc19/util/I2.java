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

	public static I2 p(int x, int y) {
		return new I2(x,y);
	}

	public static I2[] a(Integer... args) {
		if (args.length % 2 != 0) throw new IllegalArgumentException("Length of arg list must be divisible by 2");
		I2[] result = new I2[args.length/2];
		for (int i = 0; i < result.length; ++i) {
			result[i] = I2.p(args[2*i], args[2*i+1]);
		}
		return result;
	}

	public static List<I2> l(Integer... args) {
		if (args.length % 2 != 0) throw new IllegalArgumentException("Length of arg list must be divisible by 2");
		List<I2> result = new ArrayList<>();
		for (int i = 0; i < args.length; i+=2) {
			result.add(I2.p(args[i], args[i+1]));
		}
		return result;
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
}
