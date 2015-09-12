package model;

import java.io.Serializable;

import trainers.Actor.DIR;

//////////////////////////////////////////////////////////////////////////
//
// Coordinate class - holds x,y pair data for graphics and location data
//
//////////////////////////////////////////////////////////////////////////
public class Coordinate implements Serializable {

	private static final long serialVersionUID = 2989130617545732382L;
	int x;
	int y;

	// ////////////////////////////////////////////////////////////////////////
	//
	// Constructors - initializes or creates an empty coordinate at (0,0)
	//
	// ////////////////////////////////////////////////////////////////////////
	public Coordinate(int X, int Y) {
		this.x = X;
		this.y = Y;
	}

	public Coordinate() {
		this.x = 0;
		this.y = 0;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// accessors and mutators for x and y
	//
	// ////////////////////////////////////////////////////////////////////////
	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public void setX(int locX) {
		this.x = locX;
	}

	public void setY(int locY) {
		this.y = locY;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// equals - checks if one coordinate's x,y pair is equal to another's
	//
	// ////////////////////////////////////////////////////////////////////////
	public boolean equals(Coordinate another) {
		if (another.getX() == getX() && another.getY() == getY()) {
			return true;
		} else {
			return false;
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// move - moves the coordinate in a specified direction
	//
	// ////////////////////////////////////////////////////////////////////////
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
