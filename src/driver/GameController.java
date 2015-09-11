package driver;

import graphics.SpriteLibrary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
import javax.swing.Timer;

import location.LocationLibrary;
import pokedex.Pokemon;
import pokedex.PokemonFactory;
import tiles.ObstacleTile;
import tiles.Tile;
import tiles.TileSet;
import trainers.Actor.DIR;
import trainers.Player;
import utilities.Coordinate;
import utilities.DebugUtility;
import utilities.NPCThread;
import utilities.Utils;
import audio.AudioLibrary;

public class GameController {
	// main game logic
	private GameData gData;

	// NPC random movement controller
	public NPCThread npcs = new NPCThread();

	GameController(GameData game) {
		gData = game;
	}

	boolean isNoClip() {
		return gData.NOCLIP;
	}

	public void setNoClip(boolean isNoClip) {
		gData.NOCLIP = isNoClip;
	}

	boolean doBattles() {
		return gData.DOBATTLES;
	}

	public void setDoBattles(boolean areThereBattles) {
		gData.DOBATTLES = areThereBattles;
	}

	boolean isShowIntro() {
		return gData.SHOWINTRO;
	}

	public void setShowIntro(boolean showIntro) {
		gData.SHOWINTRO = showIntro;
	}

	boolean isPlayerWalking() {
		return gData.isPlayerWalking;
	}

	public void setPlayerWalking(boolean isPlayerWalking) {
		gData.isPlayerWalking = isPlayerWalking;
	}

	public void updateTime() {
		gData.gameTimeStruct.updateTime();
	}

	public String formatTime() {
		return gData.gameTimeStruct.formatTime();
	}

	public void saveTime() {
		gData.gameTimeStruct.saveTime();
	}

	public void startNewTimer(GamePanel theGame) {
		gData.gameSpeed = new Timer(100 - gData.currentSpeed, theGame);
		gData.gameSpeed.start();
	}

	public boolean isMovable() {
		return gData.movable;
	}

	public void setMovable(boolean b) {
		gData.movable = b;
	}

	// public void startNPCMovement() {
	// //
	// }

	public void stopNPCMovement() {
		npcs.stop = true;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// starttheGame method - given whether or not the theGame is a continue,
	// start
	// the theGame based off a save file, or start a new theGame.
	// Prepares all the necessary utilities, such as
	//
	// TODO - map to be used is scriptable in data file with other "EVENTS"
	//
	// ////////////////////////////////////////////////////////////////////////
	public GameData startGame(boolean continued, GamePanel p) {
		Player player = getPlayer();

		if (continued) {
			gData = Utils.loadGame();
			if (!player.tData.isValidData()) {
				System.err.println(player.tData.toString());
			}
		} else {
			String name = "GOLD";
			player = new Player(4, 2, name);
			Pokemon charmander = PokemonFactory.createPokemon("Charmander", 7);
			player.caughtPokemon(charmander);
			player.setMoney(1000000);
			player.setCurLocation(LocationLibrary.getLocation("Route 27"));
			playBackgroundMusic("NewBarkTown");
			gData.start_coorX = (Tile.TILESIZE * (8 - player.getCurrentX()));
			gData.start_coorY = (Tile.TILESIZE * (6 - player.getCurrentY()));
			if (isShowIntro()) {
				gData.introStage = 1;
				gData.inIntro = true;
			}
		}

		// initialize the player sprite
		player.tData.sprite_name = "PLAYER";
		player.tData.sprite = SpriteLibrary.getSprites("PLAYER").get(player.getDirection().ordinal() * 3);

		// get out of any menus
		gData.atTitle = false;
		gData.atContinueScreen = false;
		gData.inMenu = false;

		npcs.start();

		// start clock for current session
		gData.gameTimeStruct.timeStarted = System.currentTimeMillis();

		// start the timer that handles events (gameplay speed)
		startNewTimer(p);

		DebugUtility.printMessage("Starting game:");
		DebugUtility.printMessage("- " + player.tData.toString());
		DebugUtility.printMessage("* Rendered session id: " + gData.id);
		return gData;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// loadMap - given a path to a map file, populate the Image and Tile data
	//
	// ////////////////////////////////////////////////////////////////////////
	public void loadMap(String loadedMap) throws Exception {

		class SynchronizedReader {
			BufferedReader reader;

			SynchronizedReader(BufferedReader r) {
				reader = r;
			}

			public synchronized String readLine() throws IOException {
				return reader.readLine();
			}
		}

		class ProcessLayerThread extends Thread {

			SynchronizedReader reader;
			GameController data;
			int layer;

			ProcessLayerThread(SynchronizedReader r, GameController gameData, int lyr) {
				reader = r;
				data = gameData;
				layer = lyr;
			}

			@Override
			public void run() {
				String line = null;
				try {
					line = reader.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				int curRow = 0;
				int curCol = 0;
				int mapwidth = data.getMapWidth();
				int mapheight = data.getMapHeight();

				StringTokenizer tokens = new StringTokenizer(line);
				for (int y = 0; y < mapwidth * mapheight; y++) {

					String code = tokens.nextToken();

					curCol = y % mapwidth;
					if ((curCol == 0) && (layer == 1 || layer == 2) && (y != 0)) {
						curRow++;
					}

					// add special tiles to the map
					Coordinate c = new Coordinate(curCol, curRow);

					if (Arrays.binarySearch(TileSet.IMPASSIBLE_TILES, Integer.parseInt(code)) >= 0) {
						if (layer == 1 || (layer == 2 && Integer.parseInt(code) > 0)) {
							data.setMapTileAt(c, TileSet.OBSTACLE);
						}
					} else if (Arrays.binarySearch(TileSet.WILD_TILES, Integer.parseInt(code)) >= 0) {
						if (layer == 1 || (layer == 2 && Integer.parseInt(code) > 0)) {
							data.setMapTileAt(c, TileSet.WILD_TILE);
						}
					} else {
						if ((layer == 1 || (layer == 2 && Integer.parseInt(code) > 0)) && data.getMapTileAt(c) == null) {
							data.setMapTileAt(c, TileSet.NORMAL_TILE);
						}
					}
					data.setMapImageAt(layer, y, Integer.parseInt(code));
				}
			}
		}

		DebugUtility.debugHeader("Loading Map");

		// intialize file reader
		BufferedReader bReader = new BufferedReader(new InputStreamReader(
				GameInitializer.class.getResourceAsStream("/maps/" + loadedMap + ".map")));
		SynchronizedReader reader = new SynchronizedReader(bReader);
		String line = reader.readLine();
		StringTokenizer tokens = new StringTokenizer(line);

		// read the dimensions, and skip additional data until map data
		setMapWidth(Integer.parseInt(tokens.nextToken()));
		setMapHeight(Integer.parseInt(tokens.nextToken()));

		int mapheight = getMapHeight();
		int mapwidth = getMapWidth();

		line = reader.readLine();
		tokens = new StringTokenizer(line);
		while (!line.equals(".")) {
			line = reader.readLine();
		}

		DebugUtility.printMessage("Initializing map tiles to null...");
		// initialize the Tile map
		for (int r = 0; r < mapheight; r++) {

			Tile[] numbers = new Tile[mapwidth];
			Arrays.fill(numbers, null);
			List<Tile> row = Arrays.asList(numbers);
			gData.tileMap.add(row);
		}

		DebugUtility.printMessage(" - Map tiles: " + mapheight * mapwidth + " tiles initialized.");

		// initialize the Image map and add obstacles to the Tile map
		ArrayList<Thread> threads = new ArrayList<Thread>();
		for (int layers = 0; layers < 3; layers++) {
			Thread t = new ProcessLayerThread(reader, this, layers);
			t.start();
			threads.add(t);
		}

		for (Thread t : threads) {
			t.join();
		}
		DebugUtility.printMessage("Loaded map.");
	}

	public void setMapImageAt(int layer, int y, int parseInt) {
		gData.imageMap[layer][y] = parseInt;
	}

	public int getMapImageAt(int layer, int tile_number) {
		return gData.imageMap[layer][tile_number];
	}

	public int getOffsetX() {
		return gData.offsetX;
	}

	public int getOffsetY() {
		return gData.offsetY;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// canMoveInDir - makes a copy of the coordinate, and checks directional
	// data to see if the actor can move in the specified direction
	//
	// ////////////////////////////////////////////////////////////////////////
	public boolean canMoveInDir(DIR dir) {
		boolean canMove = false;

		if (isNoClip()) {
			// if noclip is turned on, player can always move
			canMove = true;
		} else {
			// a temporary location after moving
			Coordinate loc = getPlayer().getPosition().move(dir);
			boolean outOfBounds = false;
			if (dir == DIR.NORTH) {
				if (loc.getY() < 0) {
					outOfBounds = true;
				}
			} else if (dir == DIR.SOUTH) {
				if (loc.getY() >= getMapHeight()) {
					outOfBounds = true;
				}
			} else if (dir == DIR.EAST) {
				if (loc.getX() >= getMapWidth()) {
					outOfBounds = true;
				}
			} else if (dir == DIR.WEST) {
				if (loc.getX() < 0) {
					outOfBounds = true;
				}
			}

			if (!outOfBounds) {
				canMove = !(getMapTileAt(loc).equals(ObstacleTile.name));
			}
		}
		return canMove;
	}

	public int getMapHeight() {
		return gData.map_height;
	}

	public int getMapWidth() {
		return gData.map_width;
	}

	public void setMapHeight(int hgt) {
		gData.map_height = hgt;
	}

	public void setMapWidth(int wdt) {
		gData.map_width = wdt;
	}

	public Object getMapTileAt(Coordinate c) {
		return gData.tileMap.getTileAt(c);
	}

	public void setMapTileAt(Coordinate position, Tile tile) {
		gData.tileMap.set(position, tile);
	}

	public int getStartX() {
		return gData.start_coorX;
	}

	public int getStartY() {
		return gData.start_coorY;
	}

	public Player getPlayer() {
		return gData.player;
	}

	public int getId() {
		return gData.id;
	}

	public void setPlayerDirection(DIR dir) {
		setPlayerSprite(SpriteLibrary.getSpriteForDir("PLAYER", dir));
		getPlayer().setDirection(dir);
	}

	public void setPlayerSprite(ImageIcon imageIcon) {
		getPlayer().tData.sprite = imageIcon;
	}

	public void playClip(String clipToPlay) {
		if (gData.option_sound) {
			AudioLibrary.getInstance().playClip(AudioLibrary.SE_COLLISION);
		}
	}

	public void playTrainerMusic() {
		if (gData.option_sound) {
			AudioLibrary.getInstance().pickTrainerMusic();
		}
	}

	public void playBackgroundMusic(String string) {
		if (gData.option_sound) {
			AudioLibrary.getInstance().playBackgroundMusic(string);
		}
	}

	public int getIntroStage() {
		return gData.introStage;
	}
}
