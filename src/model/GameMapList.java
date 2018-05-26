package model;

import java.util.HashMap;

/**
 * Equivalent to ArrayList<List<Type>> with some custom functions
 * 
 * @param <Type>
 *            what this game map holds - typically Integer to represent images
 *            from a tile set, or Tile to represent the obstacles on the map
 */
public class GameMapList<Type> extends HashMap<String, Type> {

	private static final long serialVersionUID = 5325017932787074716L;

	/**
	 * Sets the specified coordinate
	 * 
	 * @param c
	 *            - the coordinate to set
	 * @param value
	 *            the value to place at that coordinate
	 */
	public void set(Coordinate c, Type value) {
		this.put(c.toString(), value);
	}
}
