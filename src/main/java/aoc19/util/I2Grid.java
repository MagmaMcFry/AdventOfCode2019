package aoc19.util;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class I2Grid<T> {
	private final Map<I2, T> i2Grid = new HashMap<>();

	public T get(int x, int y, T defaultValue) {
		return i2Grid.getOrDefault(new I2(x,y), defaultValue);
	}

	public void put(int x, int y, T value) {
		i2Grid.put(new I2(x,y), value);
	}

	public void remove(int x, int y) {
		i2Grid.remove(new I2(x,y));
	}

	public int size() {
		return i2Grid.size();
	}

	public boolean isEmpty() {
		return i2Grid.isEmpty();
	}

	public void print(PrintStream stream, T defaultValue, Function<T, String> representation) {
		int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
		for (I2 i : i2Grid.keySet()) {
			if (i.x < minX) minX = i.x;
			if (i.x > maxX) maxX = i.x;
			if (i.y < minY) minY = i.y;
			if (i.y > maxY) maxY = i.y;
		}
		for (int y = minY; y <= maxY; ++y) {
			for (int x = minX; x <= maxX; ++x) {
				stream.print(representation.apply(get(x,y,defaultValue)));
			}
			stream.println();
		}
	}
}
