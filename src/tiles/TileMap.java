package tiles;

import java.util.ArrayList;
import java.util.List;

import trainers.Actor.DIR;
import utilities.Coordinate;
import driver.GameData;

// ////////////////////////////////////////////////////////////////////////
//
// TileMap - equivalent to ArrayList<Tile> with some custom functions
//
// ////////////////////////////////////////////////////////////////////////
public class TileMap extends ArrayList<List<Tile>> {

	private static final long serialVersionUID = 5325017932787074716L;

	// ////////////////////////////////////////////////////////////////////////
	//
	// set - sets the specified coordinate to the specified Tile type
	//
	// ////////////////////////////////////////////////////////////////////////
	public void set(Coordinate c, Tile tile) {
		int curRow = c.getY();
		int curCol = c.getX();
		this.get(curRow).set(curCol, tile);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// getTile functions - return the Tile at specified coordinate or in a
	// specified direction relative to the coordinate
	//
	// ////////////////////////////////////////////////////////////////////////
	public String getTileAt(Coordinate c) {
		Tile t = this.get(c.getY()).get(c.getX());
		if (t instanceof ObstacleTile) {
			return "OBSTACLE";
		} else if (t instanceof WildTile) {
			return "WILD";
		} else if (t instanceof NormalTile) {
			return "NORMAL";
		} else {
			return "BASE TILE";
		}

	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// canMoveInDir - makes a copy of the coordinate, and checks directional
	// data to see if the actor can move in the specified direction
	//
	// ////////////////////////////////////////////////////////////////////////
	public boolean canMoveInDir(Coordinate c, DIR dir, GameData gData) {
		Coordinate y = c.move(dir);
		if (dir == DIR.NORTH) {
			if (y.getY() < 0) {
				return false;
			}
		} else if (dir == DIR.SOUTH) {
			if (y.getY() >= gData.map_height) {
				return false;
			}
		} else if (dir == DIR.EAST) {
			if (y.getX() >= gData.map_width) {
				return false;
			}
		} else if (dir == DIR.WEST) {
			if (y.getX() < 0) {
				return false;
			}
		}
		return !(getTileAt(y).equals(ObstacleTile.name));
	}
}
