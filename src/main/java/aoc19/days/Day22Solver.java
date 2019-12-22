package aoc19.days;

import aoc19.util.FileReader;

import java.math.BigInteger;
import java.util.Objects;

public class Day22Solver {
	public static void main(String[] args) {
		solvePart1();
		solvePart2();
	}

	private static String[] getInput() {
		return FileReader.readLines("/day22/shuffle.txt").toArray(new String[0]);
	}

	private static void solvePart1() {
		Shuffle shuffle = Shuffle.get(10007, getInput());
		System.out.println("Position of card 2019: " + shuffle.apply(2019));
	}

	private static void solvePart2() {
		Shuffle shuffle = Shuffle.get(119315717514047L, getInput());
		Shuffle repeatedShuffle = shuffle.repeat(101741582076661L);
		System.out.println("Number of card in position 2020: " + repeatedShuffle.invert().apply(2020));
	}

	/**
	 * Shuffle applies the following function: card -> (card * factor + offset) % numCards
	 */
	static class Shuffle {
		long numCards;
		long factor;
		long offset;

		public Shuffle(long numCards, long factor, long offset) {
			this.numCards = numCards;
			this.factor = ((factor % numCards) + numCards) % numCards;
			this.offset = ((offset % numCards) + numCards) % numCards;
		}

		public static Shuffle cut(long numCards, long offset) {
			return new Shuffle(numCards, 1, -offset);
		}

		public static Shuffle dealIncrement(long numCards, long factor) {
			return new Shuffle(numCards, factor, 0);
		}

		public static Shuffle newStack(long numCards) {
			return new Shuffle(numCards, -1, -1);
		}

		private static Shuffle identity(long numCards) {
			return new Shuffle(numCards, 1, 0);
		}

		public static Shuffle get(long numCards, String... spec) {
			Shuffle result = Shuffle.identity(numCards);
			for (String s : spec) {
				if (s.startsWith("deal with increment ")) {
					result = result.then(dealIncrement(numCards, Long.parseLong(s.substring(20))));
				} else if (s.startsWith("cut ")) {
					result = result.then(cut(numCards, Long.parseLong(s.substring(4))));
				} else if (s.equals("deal into new stack")) {
					result = result.then(newStack(numCards));
				}
			}
			return result;
		}

		public Shuffle then(Shuffle after) {
			if (this.numCards != after.numCards)
				throw new IllegalArgumentException("Can't compose shuffles with different numbers of cards");
			return new Shuffle(
					numCards,
					multMod(this.factor, after.factor, numCards),
					multMod(after.factor, this.offset, numCards) + after.offset
			);
		}

		public long apply(long card) {
			return (multMod(card, factor, numCards) + offset) % numCards;
		}

		public Shuffle invert() {
			long invFactor = invertMod(factor, numCards);
			return new Shuffle(numCards, invFactor, -multMod(invFactor, offset, numCards));
		}

		public Shuffle repeat(long times) {
			Shuffle ans = Shuffle.identity(numCards);
			long n = 1;
			Shuffle thisToN = this;
			while (n <= times) {
				if ((n & times) != 0) {
					ans = ans.then(thisToN);
				}
				n *= 2;
				thisToN = thisToN.then(thisToN);
			}
			return ans;
		}

		public boolean isIdentity() {
			return factor == 1 && offset == 0;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Shuffle shuffle = (Shuffle) o;
			return numCards == shuffle.numCards &&
					offset == shuffle.offset &&
					factor == shuffle.factor;
		}

		@Override
		public int hashCode() {
			return Objects.hash(numCards, offset, factor);
		}

		public static long multMod(long a, long b, long mod) {
			BigInteger bigA = BigInteger.valueOf(a);
			BigInteger bigB = BigInteger.valueOf(b);
			BigInteger bigMod = BigInteger.valueOf(mod);
			return bigA.multiply(bigB).mod(bigMod).longValueExact();
		}

		public static long invertMod(long val, long mod) {
			BigInteger bigVal = BigInteger.valueOf(val);
			BigInteger bigMod = BigInteger.valueOf(mod);
			return bigVal.modInverse(bigMod).longValueExact();
		}
	}
}
