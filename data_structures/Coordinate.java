package data_structures;

public class Coordinate {
	int x;
	int y;

	// TODO use this class in place of x,y pairs?

	public Coordinate(int X, int Y) {
		x = X;
		y = Y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean equals(Coordinate another) {
		if (another.getX() == getX() && another.getY() == getY()) {
			return true;
		} else {
			return false;
		}
	}
}
