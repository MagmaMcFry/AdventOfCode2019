package aoc19.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class FileReader {
	public static List<String> readLines(String resourcePath) {
		URL url = FileReader.class.getResource(resourcePath);
		try (BufferedReader s = new BufferedReader(new InputStreamReader(url.openStream()))) {
			return s.lines().collect(Collectors.toList());
		} catch (IOException e) {
			throw new RuntimeException("Couldn't open URL " + url);
		}
	}

	public static List<Integer> readListOfInts(String resourcePath) {
		return readLines(resourcePath).stream().map(Integer::valueOf).collect(Collectors.toList());
	}
}
