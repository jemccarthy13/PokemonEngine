package tiles;

import utilities.EnumsAndConstants.DIR;

public class Coordinate {
	int x;
	int y;

	public Coordinate(int X, int Y) {
		this.x = X;
		this.y = Y;
	}

	public Coordinate() {
		this.x = 0;
		this.y = 0;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public boolean equals(Coordinate another) {
		if (another.getX() == getX() && another.getY() == getY()) {
			return true;
		} else {
			return false;
		}
	}

	public void setX(int locX) {
		this.x = locX;
	}

	public void setY(int locY) {
		this.y = locY;
	}

	public Coordinate move(DIR dir) {
		Coordinate nCoor = new Coordinate(x, y);
		if (dir == DIR.NORTH) {
			nCoor.y -= 1;
		} else if (dir == DIR.SOUTH) {
			nCoor.y += 1;
		} else if (dir == DIR.WEST) {
			nCoor.x -= 1;
		} else if (dir == DIR.EAST) {
			nCoor.x += 1;
		}
		return nCoor;
	}
}
