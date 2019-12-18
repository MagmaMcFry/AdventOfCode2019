package aoc19.util;

public enum Direction {
	NORTH(0,-1,1), SOUTH(0,1,2), WEST(-1,0,3), EAST(1,0,4);
	public final int dx, dy;
	public final int symbol;
	Direction(int dx, int dy, int symbol) {
		this.dx = dx; this.dy = dy; this.symbol = symbol;
	}
	public static Direction get(int symbol) {
		switch(symbol) {
			case 1: return NORTH;
			case 2: return SOUTH;
			case 3: return WEST;
			case 4: return EAST;
			default: throw new IllegalArgumentException();
		}
	}

	public Direction opposite() {
		switch(this) {
			case NORTH: return SOUTH;
			case SOUTH: return NORTH;
			case WEST: return EAST;
			case EAST: return WEST;
			default: throw new IllegalStateException();
		}
	}
}
