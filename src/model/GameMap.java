package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Equivalent to ArrayList<List<Type>> with some custom functions
 * 
 * @param <Type>
 *            what this game map holds - typically Integer to represent images
 *            from a tile set, or Tile to represent the obstacles on the map
 */
public class GameMap<Type> extends ArrayList<List<Type>> {

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
		this.get(c.getY()).set(c.getX(), value);
	}

	/**
	 * Retrieve the value at a specified coordinate
	 * 
	 * @param c
	 *            - the coordinate to retrieve
	 * @return a value of Type at the given coordinate
	 */
	public Type get(Coordinate c) {
		return this.get(c.getY()).get(c.getX());
	}
}
