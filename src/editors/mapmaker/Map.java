package editors.mapmaker;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

/**
 * A representation of a tiled map
 */
public class Map {
	/**
	 * Default width of the tiles
	 */
	int tileWidth = 32;
	/**
	 * Default height of the tiles
	 */
	int tileHeight = 32;
	/**
	 * Default zoom width
	 */
	int zoomWidth = 32;
	/**
	 * Default zoom height
	 */
	int zoomHeight = 32;
	/**
	 * The view starts at 400 tiles wide
	 */
	int viewWidth = 400;
	/**
	 * The view starts at 400 tiles high
	 */
	int viewHeight = 400;
	/**
	 * The graphics bank holds tile images
	 */
	GraphicsBank gfx;
	/**
	 * All of the change listeners
	 */
	ArrayList<MapChangeListener> changeListeners;
	/**
	 * The map holds 3 layers
	 */
	static final int LAYERS = 3;
	/**
	 * The three layers of tiles are stored in this array
	 */
	MapTile[][][] tiles;

	/**
	 * Constructs the tile map and initializes change listeners
	 * 
	 * @param paramInt1
	 *            - size of first tiles []
	 * @param paramInt2
	 *            - size of second tiles []
	 */
	public Map(int paramInt1, int paramInt2) {
		this.tiles = new MapTile[paramInt1][paramInt2][3];
		this.changeListeners = new ArrayList<>();
	}

	/**
	 * A constructor that takes in a width and height
	 * 
	 * @param paramInt1
	 *            - parameter for default constructor
	 * @param paramInt2
	 *            - parameter for default constructor
	 * @param width
	 *            - used for tile width and zoom width
	 * @param height
	 *            - used for tile height and zoom height
	 */
	public Map(int paramInt1, int paramInt2, int width, int height) {
		this(paramInt1, paramInt2);
		this.tileWidth = width;
		this.tileHeight = height;
		this.zoomWidth = width;
		this.zoomHeight = height;
	}

	/**
	 * Set a tile value at a speicied location
	 * 
	 * @param paramInt1
	 *            - first location parameter
	 * @param paramInt2
	 *            - second location parameter
	 * @param paramInt3
	 *            - third location parameter
	 * @param paramTile
	 *            - the value to set at the specified location
	 */
	public void setTile(int paramInt1, int paramInt2, int paramInt3, MapTile paramTile) {
		this.tiles[paramInt1][paramInt2][paramInt3] = paramTile;
	}

	void setZoom(float paramFloat) {
		this.zoomWidth = ((int) (this.tileWidth * paramFloat));
		this.zoomHeight = ((int) (this.tileHeight * paramFloat));
	}

	/**
	 * Set the viewport size
	 * 
	 * @param width
	 *            - the new width
	 * @param height
	 *            - the new height
	 */
	public void setViewSize(int width, int height) {
		this.viewWidth = width;
		this.viewHeight = height;
	}

	/**
	 * Render graphics for a specific area
	 * 
	 * @param paramGraphics
	 *            - the graphics to paint with
	 * @param paramInt1
	 *            - the x distance to render
	 * @param paramInt2
	 *            - the y distance to render
	 */
	public void render(Graphics paramGraphics, int paramInt1, int paramInt2) {
		int i = Math.max(paramInt1 / this.zoomWidth, 0);
		int j = Math.min((paramInt1 + this.viewWidth) / this.zoomWidth, this.tiles.length);

		int k = Math.max(paramInt2 / this.zoomHeight, 0);
		int m = Math.min((paramInt2 + this.viewHeight) / this.zoomHeight, this.tiles[0].length);
		for (int n = 0; n < 3; n++) {
			for (int i1 = i; i1 < j; i1++) {
				for (int i2 = k; i2 < m; i2++) {
					if (this.tiles[i1][i2][n] != null) {
						this.tiles[i1][i2][n].render(paramGraphics, i1 * this.zoomWidth - paramInt1,
								i2 * this.zoomHeight - paramInt2);
					}
				}
			}
		}
	}

	/**
	 * Render graphics with a given camera position
	 * 
	 * @param paramGraphics
	 *            - the graphics to paint
	 * @param paramCamera
	 *            - the camera position
	 */
	public void render(Graphics paramGraphics, Camera paramCamera) {
		setViewSize(paramCamera.viewWidth, paramCamera.viewHeight);
		render(paramGraphics, (int) (paramCamera.viewx - this.viewWidth / 2),
				(int) (paramCamera.viewy - this.viewHeight / 2));
	}

	/**
	 * Render graphics with a given point and given boundary
	 * 
	 * @param paramGraphics
	 *            - the graphics to paint
	 * @param paramPoint
	 *            - the point
	 * @param renderBoundary
	 *            - dimension
	 */
	public void render(Graphics paramGraphics, Point paramPoint, Dimension renderBoundary) {
		double d1 = Math.max(paramPoint.getX() / this.zoomWidth, 0.0D);
		double d2 = Math.min((paramPoint.getX() + renderBoundary.getWidth()) / this.zoomWidth, this.tiles.length);

		double d3 = Math.max(paramPoint.getY() / this.zoomHeight, 0.0D);
		double d4 = Math.min((paramPoint.getY() + renderBoundary.getHeight()) / this.zoomHeight, this.tiles[0].length);
		for (int i = 0; i < 3; i++) {
			for (int j = (int) d3; j < d4; j++) {
				for (int k = (int) d1; k < d2; k++) {
					if (this.tiles[k][j][i] != null) {
						this.tiles[k][j][i].render(paramGraphics, k * this.zoomWidth + this.zoomWidth,
								j * this.zoomHeight + this.zoomHeight);
					}
				}
			}
		}
	}

	/**
	 * Render graphics of a given point w/ a given boundary, for a layer
	 * 
	 * @param paramGraphics
	 *            - the graphics to use
	 * @param paramPoint
	 *            - the point to start at
	 * @param boundary
	 *            - the boundary of the render
	 * @param layer
	 *            - the layer to look at
	 */
	public void render(Graphics paramGraphics, Point paramPoint, Dimension boundary, int layer) {
		double d1 = Math.max(paramPoint.getX() / this.zoomWidth, 0.0D);
		double d2 = Math.min((paramPoint.getX() + boundary.getWidth()) / this.zoomWidth, this.tiles.length);

		double d3 = Math.max(paramPoint.getY() / this.zoomHeight, 0.0D);
		double d4 = Math.min((paramPoint.getY() + boundary.getHeight()) / this.zoomHeight, this.tiles[0].length);
		int i = layer;
		for (int j = (int) d3; j < d4; j++) {
			for (int k = (int) d1; k < d2; k++) {
				if (this.tiles[k][j][i] != null) {
					this.tiles[k][j][i].render(paramGraphics, k * this.zoomWidth + this.zoomWidth,
							j * this.zoomHeight + this.zoomHeight);
				}
			}
		}
	}

	/**
	 * Get the width of the map
	 * 
	 * @return int width
	 */
	public int getWidth() {
		return this.tiles.length;
	}

	/**
	 * Get the height of the map
	 * 
	 * @return int height
	 */
	public int getHeight() {
		return this.tiles[0].length;
	}

	/**
	 * Get the width of a map tile
	 * 
	 * @return int tile width
	 */
	public int getTileWidth() {
		return this.tileWidth;
	}

	/**
	 * Get the height of a map tile
	 * 
	 * @return int tile height
	 */
	public int getTileHeight() {
		return this.tileHeight;
	}

	/**
	 * Get the zoom width of the map
	 * 
	 * @return int zoom width
	 */
	public int getZoomWidth() {
		return this.zoomWidth;
	}

	/**
	 * Get the zoom height of the map
	 * 
	 * @return int zoom height
	 */
	public int getZoomHeight() {
		return this.zoomHeight;
	}

	/**
	 * Get a tile from a given location in the tile map
	 * 
	 * @param paramInt1
	 *            - first location point
	 * @param paramInt2
	 *            - second location point
	 * @param paramInt3
	 *            - third location point
	 * @return a MapTile from the given location
	 */
	public MapTile getTile(int paramInt1, int paramInt2, int paramInt3) {
		return this.tiles[paramInt1][paramInt2][paramInt3];
	}

	void resize(int paramInt1, int paramInt2) {
		resize(paramInt1, paramInt2, 3);
	}

	void resize(int paramInt1, int paramInt2, int paramInt3) {
		int int1 = Math.max(1, paramInt1);
		int int2 = Math.max(1, paramInt2);
		MapTile[][][] arrayOfTile = new MapTile[int1][int2][paramInt3];

		int i = Math.min(int1, this.tiles.length);
		int j = Math.min(int2, this.tiles[0].length);
		int k = Math.min(paramInt3, this.tiles[0][0].length);
		for (int m = 0; m < i; m++) {
			for (int n = 0; n < j; n++) {
				for (int i1 = 0; i1 < k; i1++) {
					arrayOfTile[m][n][i1] = this.tiles[m][n][i1];
				}
			}
		}
		this.tiles = arrayOfTile;
	}

	void shift(int paramInt1, int paramInt2) {
		MapTile[][][] arrayOfTile = new MapTile[this.tiles.length][this.tiles[0].length][3];

		int i = Math.max(0, -paramInt1);
		int j = Math.max(0, -paramInt2);
		int k = Math.min(this.tiles.length, this.tiles.length - paramInt1);
		int m = Math.min(this.tiles[0].length, this.tiles[0].length - paramInt2);
		for (int n = i; n < k; n++) {
			for (int i1 = j; i1 < m; i1++) {
				for (int i2 = 0; i2 < 3; i2++) {
					arrayOfTile[(n + paramInt1)][(i1 + paramInt2)][i2] = this.tiles[n][i1][i2];
				}
			}
		}
		this.tiles = arrayOfTile;
	}

	void clear() {
		for (int i = 0; i < this.tiles.length; i++) {
			for (int j = 0; j < this.tiles[0].length; j++) {
				for (int k = 0; k < 3; k++) {
					this.tiles[i][j][k] = null;
				}
			}
		}
	}

	/**
	 * Convert the map to an array of int for processing
	 * 
	 * @return int[][][] representing the map as numbered tiles for rendering by
	 *         the game engine
	 */
	public int[][][] toIntArray() {
		int[][][] arrayOfInt = new int[this.tiles.length][this.tiles[0].length][this.tiles[0][0].length];
		for (int i = 0; i < this.tiles.length; i++) {
			for (int j = 0; j < this.tiles[0].length; j++) {
				for (int k = 0; k < 3; k++) {
					if (this.tiles[i][j][k] != null) {
						arrayOfInt[i][j][k] = this.tiles[i][j][k].number;
					} else {
						arrayOfInt[i][j][k] = 0;
					}
				}
			}
		}
		return arrayOfInt;
	}

	/**
	 * Set all the current map's tiles to be the argument 'map's tiles
	 * 
	 * @param map
	 *            - the int[][][] to copy data from
	 * @param bank
	 *            - the graphics bank to use for painting the map
	 */
	public void setAllTiles(int[][][] map, GraphicsBank bank) {
		this.gfx = bank;
		resize(this.tiles.length, this.tiles[0].length, this.tiles[0][0].length);
		for (int i = 0; i < this.tiles.length; i++) {
			for (int j = 0; j < this.tiles[0].length; j++) {
				for (int k = 0; k < 3; k++) {
					this.tiles[i][j][k] = bank.getMapTile(map[i][j][k]);
				}
			}
		}
	}

	/**
	 * Create a new tileset from a graphics bank
	 * 
	 * @param bank
	 *            - the graphics bank to uses
	 */
	public void setTileset(GraphicsBank bank) {
		int[][][] arrayOfInt = toIntArray();
		setAllTiles(arrayOfInt, bank);
	}

	/**
	 * Add a change listener to the map.
	 * 
	 * @param listenerToAdd
	 *            - the change listener to add
	 */
	public void addChangeListener(MapChangeListener listenerToAdd) {
		this.changeListeners.add(listenerToAdd);
	}

	/**
	 * Remove a change listener from the Map.
	 * 
	 * @param listenerToRemove
	 *            - the listener to remove
	 */
	public void removeChangeListener(MapChangeListener listenerToRemove) {
		this.changeListeners.remove(listenerToRemove);
	}
}