package driver;

import graphics.SpriteLibrary;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
import javax.swing.Timer;

import location.LocationLibrary;
import pokedex.Pokemon;
import pokedex.PokemonFactory;
import pokedex.PokemonList;
import tiles.ObstacleTile;
import tiles.Tile;
import tiles.TileSet;
import tiles.WildTile;
import trainers.Actor;
import trainers.Actor.DIR;
import trainers.Player;
import utilities.BattleEngine;
import utilities.Configuration;
import utilities.Coordinate;
import utilities.DebugUtility;
import utilities.NPCThread;
import utilities.Utils;
import audio.AudioLibrary;
import driver.GameData.SCREEN;

public class GameController implements Serializable {

	private static final long serialVersionUID = 968834933407220662L;

	// main game logic
	private GameData gData;
	private Configuration config = new Configuration();

	// NPC random movement controller
	public NPCThread npcs = new NPCThread();

	GameController() {
		gData = new GameData();

	}

	boolean isNoClip() {
		return gData.NOCLIP;
	}

	public void setNoClip(boolean isNoClip) {
		gData.NOCLIP = isNoClip;
	}

	public boolean isMovable() {
		return gData.movable;
	}

	public void setMovable(boolean b) {
		gData.movable = b;
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

	public void startNewTimer(ActionListener theGame) {
		gData.gameSpeed = new Timer(100 - gData.currentSpeed, theGame);
		gData.gameSpeed.start();
	}

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
	// ////////////////////////////////////////////////////////////////////////
	public GameData startGame(boolean continued) {
		DebugUtility.printHeader("Starting game:");
		if (continued) {
			gData = Utils.loadGame();
			if (!gData.player.tData.isValidData()) {
				DebugUtility.error(gData.player.tData.toString());
			}
		} else {
			String name = "GOLD";
			gData.player = new Player(4, 2, name);
			Pokemon charmander = PokemonFactory.createPokemon("Charmander", 7);
			gData.player.caughtPokemon(charmander);
			gData.player.setMoney(1000000);
			gData.player.setCurLocation(LocationLibrary.getLocation("Route 27"));
			playBackgroundMusic("NewBarkTown");
			gData.start_coorX = (Tile.TILESIZE * (8 - gData.player.getCurrentX()));
			gData.start_coorY = (Tile.TILESIZE * (6 - gData.player.getCurrentY()));
			if (isShowIntro()) {
				gData.introStage = 1;
				gData.screen = SCREEN.INTRO;
			}
		}

		// initialize the player sprite
		gData.player.tData.sprite_name = "PLAYER";
		gData.player.tData.sprite = SpriteLibrary.getSprites("PLAYER").get(gData.player.getDirection().ordinal() * 3);

		// get out of any menus
		gData.screen = SCREEN.WORLD;

		npcs.start();

		// start clock for current session
		gData.gameTimeStruct.timeStarted = System.currentTimeMillis();

		DebugUtility.printMessage("- " + gData.player.tData.toString());
		DebugUtility.printMessage("* Rendered session id: " + gData.id);
		return gData;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// loadMap - given a path to a map file, populate the Image and Tile data
	//
	// ////////////////////////////////////////////////////////////////////////
	public void loadMap() throws Exception {

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

		DebugUtility.printHeader("Loading Map");

		// intialize file reader
		BufferedReader bReader = new BufferedReader(new InputStreamReader(
				GameController.class.getResourceAsStream(config.MAP_TO_LOAD)));
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

	public Tile getMapTileAt(Coordinate c) {
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
			AudioLibrary.getInstance().playClip(clipToPlay);
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

	public void incrIntroStage() {
		gData.introStage += 2;
	}

	public boolean isInBattle() {
		return gData.inBattle;
	}

	public void setOffsetY(DIR playerDir) {
		if (canMoveInDir(playerDir)) {
			switch (playerDir) {
			case NORTH:
				gData.offsetY += 2;
				break;
			case SOUTH:
				gData.offsetY -= 2;
				break;
			default:
				break;
			}
		}
	}

	public void setOffsetX(DIR playerDir) {
		if (canMoveInDir(playerDir)) {
			switch (playerDir) {
			case EAST:
				gData.offsetX -= 2;
				break;
			case WEST:
				gData.offsetX += 2;
				break;
			default:
				break;
			}
		}
	}

	public void setStartCoordX(int i) {
		gData.start_coorX = i;
	}

	public void setStartCoordY(int i) {
		gData.start_coorY = i;
	}

	public boolean isBattleTile(Coordinate position) {
		return getMapTileAt(position).getClass().equals(WildTile.class);
	}

	public boolean isTeleportTile(Coordinate playerPos) {
		return TeleportLibrary.getListofTeleports().containsKey(playerPos);
	}

	public void doTeleport(Coordinate playerPos) {
		Player player = getPlayer();

		player.setLoc(TeleportLibrary.getListofTeleports().get(playerPos));

		setStartCoordX((player.getCurrentX() - playerPos.getX()) * -1 * Tile.TILESIZE);
		setStartCoordY((player.getCurrentY() - playerPos.getY()) * -1 * Tile.TILESIZE);

		// face the opposite direction of the way the player entered the
		// teleport square
		player.turnAround();
	}

	public boolean validEncounterConditions(Actor npc) {
		boolean isValid = false;
		if (getScreen() == SCREEN.WORLD) {
			isValid = npc.isTrainer() && !isPlayerWalking() && doBattles()
					&& !getPlayer().beatenTrainers.contains(npc.getName()) && npcSeesPlayer(npc);
		}
		return isValid;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Given an NPC, check if that NPC sees the player
	//
	// Sight rules:
	// NORTH - columns match, playerY < NPC_Y, within sight distance
	// SOUTH - columns match, playerY > NPC_Y, within sight distance
	// EAST - rows match, playerX > NPC_X, within sight distance
	// WEST - rows match, playerX < NPC_X, within sight distance
	//
	// ////////////////////////////////////////////////////////////////////////
	private boolean npcSeesPlayer(Actor curNPC) {
		Player player = getPlayer();
		int playerCurY = player.getCurrentY();
		int playerCurX = player.getCurrentX();
		int NPC_X = curNPC.getCurrentX();
		int NPC_Y = curNPC.getCurrentY();
		DIR NPC_DIR = curNPC.getDirection();

		return (((playerCurX == curNPC.getCurrentX()) && (((playerCurY < NPC_Y)
				&& (NPC_Y - playerCurY <= config.NPC_SIGHT_DISTANCE) && (NPC_DIR == DIR.NORTH)) || ((playerCurY > NPC_Y)
				&& (playerCurY - NPC_Y <= config.NPC_SIGHT_DISTANCE) && (NPC_DIR == DIR.SOUTH)))) || ((playerCurY == NPC_Y) && (((playerCurX < NPC_X)
				&& (NPC_X - playerCurX <= config.NPC_SIGHT_DISTANCE) && (NPC_DIR == DIR.WEST)) || ((playerCurX > NPC_X)
				&& (playerCurX - NPC_X <= config.NPC_SIGHT_DISTANCE) && (NPC_DIR == DIR.EAST)))));
	}

	public void resetMenuLogic() {
		gData.screen = SCREEN.WORLD;
		gData.inBattle = false;
		gData.introStage = 1;
	}

	public boolean isSoundOn() {
		return gData.option_sound;
	}

	public int getCurrentSelection() {
		SCREEN curScreen = getScreen();
		if (!gData.currentSelection.containsKey(curScreen)) {
			setCurrentSelection(0);
		}
		return gData.currentSelection.get(curScreen);
	}

	public void setCurrentSelection(int i) {
		if (gData.currentSelection.containsKey(getScreen())) {
			gData.currentSelection.replace(getScreen(), i);
		} else {
			gData.currentSelection.put(getScreen(), 0);
		}
	}

	public void decrementSelection() {
		if (gData.currentSelection.containsKey(getScreen())) {
			int curSel = gData.currentSelection.get(getScreen());
			if (getCurrentSelection() > 0 && getScreen() != SCREEN.WORLD) {
				setCurrentSelection(curSel - 1);
			}
		} else {
			setCurrentSelection(0);
		}
	}

	public void incrementSelection() {
		if (gData.currentSelection.containsKey(getScreen())) {
			int curSel = gData.currentSelection.get(getScreen());
			if (getCurrentSelection() < config.numSelections.get(getScreen()) - 1) {
				setCurrentSelection(curSel + 1);
			}
		} else {
			setCurrentSelection(1);
		}
	}

	public boolean isInNameScreen() {
		return gData.screen == SCREEN.NAME;
	}

	public SCREEN getScreen() {
		return gData.screen;
	}

	public void setScreen(SCREEN curScreen) {
		gData.screen = curScreen;
	}

	public void toggleSound() {
		gData.option_sound = !gData.option_sound;
		if (gData.option_sound) {
			playBackgroundMusic(getPlayer().getCurLoc().getName());
		} else {
			AudioLibrary.getInstance().pauseBackgrondMusic();
		}
	}

	public void setPlayerWin(boolean b) {
		gData.playerWin = false;
	}

	public void setMessage(String string) {
		gData.messageString = string;
	}

	public Object getGameData() {
		// TODO Auto-generated method stub
		return gData;
	}

	public void initialize() {
		gData = new GameData();
	}

	public void doWildEncounter() {
		gData.wildPokemon.clear();
		gData.wildPokemon.add(PokemonFactory.getInstance().randomPokemon(getPlayer().getCurLoc()));
		BattleEngine.getInstance().fight(gData.wildPokemon, this, null);
	}

	public void doEncounter(PokemonList pokemon, String name) {
		BattleEngine.getInstance().fight(pokemon, this, name);
	}

	public int getMessageStage() {
		return gData.messageStage;
	}

	public Actor getCurNPC() {
		return gData.curNPC;
	}

	public void setCurNPC(Actor npc) {
		gData.curNPC = npc;
	}

	public void exitMenu() {
		getCurNPC().setStationary(false);
		setScreen(SCREEN.WORLD);
	}

	public String getCurrentMessage(boolean npc) {
		if (npc)
			return getCurNPC().getText(getMessageStage());
		else
			return "";
	}

}
