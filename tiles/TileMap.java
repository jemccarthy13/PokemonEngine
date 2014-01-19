package tiles;

import java.util.ArrayList;

import utilities.EnumsAndConstants.DIR;
import utilities.GameData;

public class TileMap extends ArrayList<ArrayList<Tile>> {

	public void set(Coordinate c, Tile tile) {
		int curRow = c.getY();
		int curCol = c.getX();
		this.get(curRow).set(curCol, tile);
	}

	public Tile getTileAt(Coordinate c) {
		return this.get(c.getY()).get(c.getX());
	}

	public Tile getTileFromDir(Coordinate c, DIR dir) {
		Coordinate y = c.move(dir);
		return getTileAt(y);
	}

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
		return !(getTileAt(y) instanceof ObstacleTile);
	}
}
