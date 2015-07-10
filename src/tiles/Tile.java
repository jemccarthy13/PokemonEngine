package tiles;

import java.io.Serializable;

// ////////////////////////////////////////////////////////////////////////
//
// Tile - the abstract parent class for all Tiles
//
// ////////////////////////////////////////////////////////////////////////
public abstract class Tile implements Serializable {
	private static final long serialVersionUID = 6990722468924905718L;
	public static final int TILESIZE = 32;
	final String name = "BASE TILE";

	public String getName() {
		return this.name;
	}
}
