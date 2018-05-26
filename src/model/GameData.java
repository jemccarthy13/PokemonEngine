package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import controller.TeleportLibrary;
import graphics.BaseScene;
import graphics.Scene;
import graphics.TitleScene;
import model.Configuration.PLAYER_SPEED;
import tiles.BattleTile;
import tiles.Tile;
import tiles.TileSet;
import utilities.RandomNumUtils;

/**
 * Holds data related to gameplay logic control
 */
public class GameData implements Serializable {

	// ======================= Serialization ================================//

	private static final long serialVersionUID = 4753670767642154788L;

	//
	// Converting to use game controller to convert to MVC design pattern
	//
	// ==================== Game Logic Control ==============================//

	/**
	 * Player animation value - is the player walking?
	 */
	public boolean isPlayerWalking = false;
	/**
	 * Can the player move
	 */
	public boolean playerCanMove = false;
	/**
	 * Current message of the game
	 */
	public String currentMessage;
	/**
	 * Current message stage
	 */
	public int messageStage;

	/**
	 * The current screen, defaults to main credits
	 */
	public BaseScene scene = TitleScene.instance; // start at the title screen
	/**
	 * The stage of the introduction
	 */
	public int introStage = 1;

	// ============================ Game Data =============================//

	/**
	 * This session's 'unique' ID
	 */
	public int gameSessionID = RandomNumUtils.createTrainerID();

	// ======================== Map Data ===================================//

	/**
	 * The width of the map
	 */
	public int map_width;
	/**
	 * The height of the map
	 */
	public int map_height;

	// ====================== Graphics control variables ===================//
	/**
	 * Painting variable, map offset in the x direction
	 */
	public int offsetX = 0;
	/**
	 * Painting variable, map offset in the y direction
	 */
	public int offsetY = 0;

	/**
	 * Teleportation graphics x variable
	 */
	public int start_coorX;
	/**
	 * Teleportation graphics y variable
	 */
	public int start_coorY; // teleportation graphics variables

	/**
	 * Stored as a map so each SCREEN can store it's own current selection
	 */
	public HashMap<Scene, Coordinate> currentSelection = new HashMap<Scene, Coordinate>();

	/**
	 * Representation of the map images
	 */
	private GameMap<Integer> imageMap = new GameMap<Integer>();

	/**
	 * Representation of the map tiles (characteristics such as obstacle or not)
	 */
	private GameMap<Tile> tileMap = new GameMap<Tile>();

	/**
	 * Is the game currently in a battle scene?
	 */
	public boolean inBattle = false;

	/**
	 * Did the player win the battle yet?
	 */
	public boolean playerWin = false;

	// ======================== User Data ==================================//

	/**
	 * The player's current speed
	 */
	public PLAYER_SPEED currentSpeed = PLAYER_SPEED.WALK;

	/**
	 * Create a string describing the current game state
	 * 
	 * @return string representing game state
	 */
	public String toString() {
		String retStr = "Sound on: " + Configuration.getInstance().isSoundOn() + "\n";
		retStr += "* Game ID: " + gameSessionID + "\n";
		retStr += "* Current msg: " + currentMessage + "\n";
		retStr += Configuration.getInstance().getConfig() + "\n";
		retStr += "* Current speed: " + currentSpeed + "\n";
		retStr += "* Current scene: " + scene;
		return retStr;
	}

	/**
	 * Retrieve the tile image number at a given (layer, y) position
	 * 
	 * @param layer
	 *            - the layer to look in
	 * @param y
	 *            - the index of the image
	 * @return int representing which tile of the tileset should be painted
	 */
	public int getMapImageAt(int layer, int y) {
		Integer i = imageMap.get(new Coordinate(y, layer));
		if (i == null) {
			return 0;
		} else {
			return imageMap.get(new Coordinate(y, layer));
		}
	}

	/**
	 * Set the tile image number at a given (layer, y) position
	 * 
	 * @param layer
	 *            - the layer to look in
	 * @param y
	 *            - the index of the image
	 * @param value
	 *            - the new image number value
	 */
	public void setMapImageAt(int layer, int y, int value) {
		imageMap.set(new Coordinate(y, layer), value);
	}

	/**
	 * If the map image location doesn't exit, add it
	 * 
	 * @param x
	 *            - layer index
	 * @param y
	 *            - element index within the layer
	 * @param value
	 *            - the value to add
	 */
	public void addMapImageAt(int x, int y, int value) {
		List<Integer> layer = null;
		// set up a new layer if it doesn't exist
		try {
			layer = imageMap.get(x);
		} catch (IndexOutOfBoundsException e) {
			imageMap.add(new ArrayList<Integer>());
		}
		if (layer != null) {
			if (layer.size() <= y) {
				for (int i = 0; i < y + 1; i++) {
					layer.add(0);
				}
			}
			layer.set(y, value);
		}
	}

	/**
	 * Retrieve a tile given a coordinate
	 * 
	 * @param c
	 *            - the coordinate to look at
	 * @return a Tile of the map
	 */
	public Tile getMapTileAt(Coordinate c) {
		return tileMap.get(c);
	}

	/**
	 * Set a tile at a given coordinate
	 * 
	 * @param position
	 *            - the coordinate to set
	 * @param tile
	 *            - the tile to set that index to
	 */
	public void setMapTileAt(Coordinate position, Tile tile) {
		tileMap.set(position, tile);
	}

	/**
	 * Set the tileMap to contain null tiles
	 * 
	 * @param mapwidth
	 *            - the width of the map
	 * @param mapheight
	 *            - the height of the map
	 */
	public void initializeTileMapToNull(int mapwidth, int mapheight) {
		for (int r = 0; r < mapheight; r++) {
			Tile[] numbers = new Tile[mapwidth];
			Arrays.fill(numbers, null);
			List<Tile> row = Arrays.asList(numbers);
			tileMap.add(row);
		}
	}

	public boolean isObstacleAt(Coordinate loc) {
		// TODO water tiles
		return (TileSet.compareTiles(getMapTileAt(loc), TileSet.OBSTACLE));
	}

	public boolean isBattleAt(Coordinate loc) {
		// TODO water tiles
		return getMapTileAt(loc).getClass().equals(BattleTile.class);
	}

	public boolean isTeleportAt(Coordinate loc) {
		return TeleportLibrary.getList().containsKey(loc);
	}
}
