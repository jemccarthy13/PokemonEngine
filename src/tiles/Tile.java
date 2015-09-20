package tiles;

import java.io.Serializable;

/**
 * A type of a Tile that causes wild encounters
 */
public abstract class Tile implements Serializable {
	/**
	 * Serialization information
	 */
	private static final long serialVersionUID = 6990722468924905718L;
	/**
	 * Default map tile size
	 */
	public static final int TILESIZE = 32;
}
