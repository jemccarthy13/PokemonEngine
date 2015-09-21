package model;

import java.io.Serializable;

import trainers.Actor.DIR;

/**
 * Holds x,y pair data for graphics and location data
 */
public class Coordinate implements Serializable {

	private static final long serialVersionUID = 2989130617545732382L;
	int x;
	int y;

	/**
	 * Convert this coordinate to a human-readable string
	 * 
	 * @return string representation of coordinate
	 */
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	/**
	 * Initializes a coordinate
	 * 
	 * @param X
	 *            x location
	 * @param Y
	 *            y location
	 */
	public Coordinate(int X, int Y) {
		this.x = X;
		this.y = Y;
	}

	/**
	 * Default constructor, constructs a point (0,0)
	 */
	public Coordinate() {
		this.x = 0;
		this.y = 0;
	}

	/**
	 * Access the x value of the coordinate
	 * 
	 * @return x value
	 */
	public int getX() {
		return this.x;
	}

	/**
	 * Access the y value of the coordinate
	 * 
	 * @return y value
	 */
	public int getY() {
		return this.y;
	}

	/**
	 * Modify the x value of the coordinate
	 * 
	 * @param locX
	 *            the new x value
	 */
	public void setX(int locX) {
		this.x = locX;
	}

	/**
	 * Modify the y value of the coordinate
	 * 
	 * @param locY
	 *            the new y value
	 */
	public void setY(int locY) {
		this.y = locY;
	}

	/**
	 * Checks if one coordinate's x,y pair is equal to another's
	 * 
	 * @param another
	 *            - the coordinate to compare to
	 * @return whether or not the coordinates are the same
	 */
	public boolean equals(Coordinate another) {
		if (another.getX() == getX() && another.getY() == getY()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Get a coordinate 1 'tile' in the specified direction
	 * 
	 * @param dir
	 *            - the direction to move
	 * @return a new coordinate
	 */
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
