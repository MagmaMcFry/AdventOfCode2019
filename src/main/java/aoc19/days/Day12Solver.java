package aoc19.days;

import java.util.Arrays;
import java.util.Objects;

public class Day12Solver {
	public static void main(String[] args) {
		solvePart1();
		solvePart2();
	}

	private static Moon[] getInput() {
		return new Moon[]{
				new Moon(-9, 10, -1),
				new Moon(-14, -8, 14),
				new Moon(1, 5, 6),
				new Moon(-19, 7, 8),
		};
	}

	public static void tick(Moon[] moons) {
		for (Moon moon : moons) {
			for (Moon otherMoon : moons) {
				moon.accelerateTo(otherMoon);
			}
		}
		for (Moon moon : moons) {
			moon.tick();
		}
	}

	public static void run(Moon[] moons, int steps) {
		for (int i = 0; i < steps; ++i) {
			tick(moons);
		}
	}

	public static long getPeriod(Moon[] moons) {
		Moon[] simMoons = new Moon[moons.length];
		for (int i = 0; i < moons.length; ++i) {
			simMoons[i] = moons[i].copy();
		}
		long steps = 0;
		do {
			tick(simMoons);
			++steps;
			if ((steps & 0xfffffL) == 0) {
				System.out.println("Current steps: " + steps);
			}
		} while (!Arrays.equals(moons, simMoons));
		return steps;
	}

	public static long getPeriodEfficiently(Moon[] moons) {
		long xPeriod = getPeriod(Arrays.stream(moons).map(moon -> new Moon(moon.px, 0, 0, moon.vx, 0, 0)).toArray(Moon[]::new));
		long yPeriod = getPeriod(Arrays.stream(moons).map(moon -> new Moon(moon.py, 0, 0, moon.vy, 0, 0)).toArray(Moon[]::new));
		long zPeriod = getPeriod(Arrays.stream(moons).map(moon -> new Moon(moon.pz, 0, 0, moon.vz, 0, 0)).toArray(Moon[]::new));
		return lcm(xPeriod, lcm(yPeriod,  zPeriod));
	}

	private static long lcm(long a, long b) {
		return (a/gcd(a,b))*b;
	}

	private static long gcd(long x, long y) {
		x = Math.abs(x);
		y = Math.abs(y);
		if (x == 0 && y == 0) return 1; // Let's not divide by 0
		if (y == 0) return x;
		if (x == 0) return y;
		return gcd(y, x%y);
	}

	public static int getEnergy(Moon[] moons) {
		int energy = 0;
		for (Moon moon : moons) {
			energy += moon.getEnergy();
		}
		return energy;
	}

	private static void solvePart1() {
		Moon[] moons = getInput();
		run(moons, 1000);
		System.out.println("Total energy after 1000 steps: " + getEnergy(moons));
	}

	private static void solvePart2() {
		Moon[] moons = getInput();
		long stepsTaken = getPeriodEfficiently(moons);
		System.out.println("System period: " + stepsTaken);
	}

	static class Moon {
		int px, py, pz;
		int vx, vy, vz;

		public Moon(int px, int py, int pz) {
			this(px, py, pz, 0, 0, 0);
		}

		public Moon(int px, int py, int pz, int vx, int vy, int vz) {
			this.px = px;
			this.py = py;
			this.pz = pz;
			this.vx = vx;
			this.vy = vy;
			this.vz = vz;
		}

		@Override
		public String toString() {
			return "Moon(" + px +
					"," + py +
					"," + pz +
					") + t*(" + vx +
					"," + vy +
					"," + vz +
					")";
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Moon moon = (Moon) o;
			return px == moon.px &&
					py == moon.py &&
					pz == moon.pz &&
					vx == moon.vx &&
					vy == moon.vy &&
					vz == moon.vz;
		}

		@Override
		public int hashCode() {
			return Objects.hash(px, py, pz, vx, vy, vz);
		}

		public void accelerateTo(Moon otherMoon) {
			vx += Integer.compare(otherMoon.px, px);
			vy += Integer.compare(otherMoon.py, py);
			vz += Integer.compare(otherMoon.pz, pz);
		}

		public void tick() {
			px += vx;
			py += vy;
			pz += vz;
		}

		public int getEnergy() {
			return (Math.abs(px) + Math.abs(py) + Math.abs(pz))
					* (Math.abs(vx) + Math.abs(vy) + Math.abs(vz));
		}


		public Moon copy() {
			return new Moon(px, py, pz, vx, vy, vz);
		}
	}
}
