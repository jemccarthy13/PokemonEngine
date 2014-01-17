package data_structures;

import utilities.EnumsAndConstants.DIR;

public class Coordinate {
	int x;
	int y;

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

	public void setX(int locX) {
		x = locX;
	}

	public void setY(int locY) {
		y = locY;
	}

	public void move(DIR dir) {
		if (dir == DIR.NORTH) {
			y -= 1;
		} else if (dir == DIR.NORTH) {
			y += 1;
		} else if (dir == DIR.WEST) {
			x -= 1;
		} else if (dir == DIR.EAST) {
			x += 1;
		}
	}
}
