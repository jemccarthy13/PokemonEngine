package tiles;

import java.util.ArrayList;
import java.util.List;

import utilities.Coordinate;

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
}
