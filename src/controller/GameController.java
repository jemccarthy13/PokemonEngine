package controller;

import graphics.NPCThread;
import graphics.SpriteLibrary;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
import javax.swing.Timer;

import location.LocationLibrary;
import model.Configuration;
import model.Coordinate;
import model.GameData;
import model.GameData.SCREEN;
import model.GameTime;
import model.NameBuilder;
import party.Party;
import party.PartyMember;
import party.PartyMemberFactory;
import tiles.BattleTile;
import tiles.Tile;
import tiles.TileSet;
import trainers.Actor;
import trainers.Actor.DIR;
import trainers.Player;
import utilities.BattleEngine;
import utilities.DebugUtility;
import audio.AudioLibrary;
import audio.AudioLibrary.SOUND_EFFECT;

/**
 * Controls game logic flow by providing an interface between view and data
 */
public class GameController implements Serializable {

	private static final long serialVersionUID = 968834933407220662L;

	// main game logic
	private GameData gData;

	// configuration details
	private Configuration config = new Configuration();

	// handles any audio
	private AudioLibrary audio = new AudioLibrary();

	// builds names using the name screen - used to name PartyMembers
	private NameBuilder nameBuilder = new NameBuilder();

	// current NPC - in conversation or in battle
	private Actor currentNPC;

	// the player Actor
	private Player player;

	// the current battle enemy party
	private Party currentEnemy = new Party();

	// controls the speed game events are handled and the current game time
	private GameTime gameTimeStruct = new GameTime(0, 0, 0);
	private Timer gameSpeed;

	// NPC random movement controller
	private NPCThread npcs = new NPCThread();

	// ////////////////////////////////////////////////////////////////////////
	//
	// Create a new controller to wrap around the game data
	//
	// ////////////////////////////////////////////////////////////////////////
	public GameController() {
		gData = new GameData();

	}

	/***************************************************************************
	 * 
	 * NAME CONTROL LOGIC
	 * 
	 * Helps control the name builder.
	 * 
	 **************************************************************************/

	// ////////////////////////////////////////////////////////////////////////
	//
	// Sets the toBeNamed object. Used for graphics painting
	// to get the sprite of the thing to be named.
	//
	// ////////////////////////////////////////////////////////////////////////
	public void setToBeNamed(String tbn) {
		nameBuilder.setToBeNamed(tbn);
	}

	public String getToBeNamed() {
		return nameBuilder.getToBeNamed();
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Increment and decrement for the row / col of character selection screen
	//
	// ////////////////////////////////////////////////////////////////////////
	public void decrNameRowSelection() {
		int row = nameBuilder.getRowSelection();
		if (row > 0) {
			nameBuilder.setRowSelection(row - 1);
		}
	}

	public void incrNameRowSelection() {
		int row = nameBuilder.getRowSelection();
		if (row < nameBuilder.maxRows()) {
			nameBuilder.setRowSelection(row + 1);
		}
	}

	public void incrNameColSelection() {
		int col = nameBuilder.getColSelection();
		if (col < nameBuilder.maxCols()) {
			nameBuilder.setColSelection(col + 1);
		}
	}

	public void decrNameColSelection() {
		int col = nameBuilder.getColSelection();
		if (col > 0) {
			nameBuilder.setColSelection(col - 1);
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Set and get the row / col selected for character selection
	//
	// ////////////////////////////////////////////////////////////////////////
	public void setNameColSelection(int i) {
		nameBuilder.setColSelection(i);
	}

	public void setNameRowSelection(int i) {
		nameBuilder.setRowSelection(i);
	}

	public int getNameRowSelection() {
		return nameBuilder.getRowSelection();
	}

	public int getNameColSelection() {
		return nameBuilder.getColSelection();
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Add the selected character to the name builder
	//
	// ////////////////////////////////////////////////////////////////////////
	public void addSelectedChar() {
		nameBuilder.addSelectedChar();
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Removes a char from the string in the process of being built
	//
	// ////////////////////////////////////////////////////////////////////////
	public void removeChar() {
		nameBuilder.removeChar();
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Retrieve the name built by the user in the current session
	//
	// ////////////////////////////////////////////////////////////////////////
	public String getChosenName() {
		return nameBuilder.toString();
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Resets the name builder (clear all chars)
	//
	// ////////////////////////////////////////////////////////////////////////
	public void resetNameBuilder() {
		nameBuilder.reset();
	}

	/***************************************************************************
	 * 
	 * GRAPHICS CONTROL LOGIC
	 * 
	 * Helps control graphical display elements.
	 * 
	 **************************************************************************/

	// ////////////////////////////////////////////////////////////////////////
	//
	// Retrieve the tile image number at a given (layer, y) position
	//
	// ////////////////////////////////////////////////////////////////////////
	public int getMapImageAt(int layer, int y) {
		Integer i = gData.imageMap.get(new Coordinate(y, layer));
		if (i == null) {
			return 0;
		} else {
			return gData.imageMap.get(new Coordinate(y, layer));
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Set the tile image number at a given (layer, y) position
	//
	// ////////////////////////////////////////////////////////////////////////
	public void setMapImageAt(int layer, int y, int parseInt) {
		gData.imageMap.set(new Coordinate(y, layer), parseInt);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// If the map image location doesn't exist, add it
	//
	// ////////////////////////////////////////////////////////////////////////
	public void addMapImageAt(int x, int y, int parseInt) {
		List<Integer> layer = null;
		// set up a new layer if it doesn't exist
		try {
			layer = gData.imageMap.get(x);
		} catch (IndexOutOfBoundsException e) {
			gData.imageMap.add(new ArrayList<Integer>());
		}
		if (layer != null) {
			if (layer.size() <= y) {
				for (int i = 0; i < y + 1; i++) {
					layer.add(0);
				}
			}
			layer.set(y, parseInt);
		}
	}

	public Tile getMapTileAt(Coordinate c) {
		return gData.tileMap.get(c);
	}

	public void setMapTileAt(Coordinate position, Tile tile) {
		gData.tileMap.set(position, tile);
	}

	public int getOffsetX() {
		return gData.offsetX;
	}

	public int getOffsetY() {
		return gData.offsetY;
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
					} else if (Arrays.binarySearch(TileSet.BATTLE_TILES, Integer.parseInt(code)) >= 0) {
						if (layer == 1 || (layer == 2 && Integer.parseInt(code) > 0)) {
							data.setMapTileAt(c, TileSet.BATTLE);
						}
					} else {
						if ((layer == 1 || (layer == 2 && Integer.parseInt(code) > 0)) && data.getMapTileAt(c) == null) {
							data.setMapTileAt(c, TileSet.NORMAL);
						}
					}
					data.addMapImageAt(layer, y, Integer.parseInt(code));
				}
			}
		}

		DebugUtility.printHeader("Loading Map");

		// intialize file reader
		BufferedReader bReader = new BufferedReader(new InputStreamReader(
				GameController.class.getResourceAsStream(Configuration.MAP_TO_LOAD)));
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

	// ////////////////////////////////////////////////////////////////////////
	//
	// Pause all NPC movement
	//
	// ////////////////////////////////////////////////////////////////////////
	public void pauseNPCMovement() {
		npcs.stop = true;
	}

	// ////////////////////////// MOVEMENT CONTROL LOGIC ///////////////////////

	public boolean isMovable() {
		return gData.movable;
	}

	public void setMovable(boolean b) {
		gData.movable = b;
	}

	public boolean isPlayerWalking() {
		return gData.isPlayerWalking;
	}

	public void setPlayerWalking(boolean isPlayerWalking) {
		gData.isPlayerWalking = isPlayerWalking;
	}

	public void startGameTimer(ActionListener theGame) {
		gameSpeed = new Timer(100 - gData.currentSpeed.getValue(), theGame);
		gameSpeed.start();
	}

	// //////////////////////////// TIME CONTROL //////////////////////////////

	public void updateTime() {
		gameTimeStruct.updateTime();
	}

	public String formatTime() {
		return gameTimeStruct.formatTime();
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// start the game, based off a save file or as a new game
	//
	// ////////////////////////////////////////////////////////////////////////
	public GameData startGame(boolean continued) {

		DebugUtility.printHeader("Starting game:");
		if (continued) {
			loadGame();
			if (!player.tData.isValidData()) {
				DebugUtility.error("Unable to continue game from save file. Corrupt data:\n" + player.tData.toString());
			}
		} else {
			String name = "GOLD";
			player = new Player(4, 2, name);
			PartyMember charmander = PartyMemberFactory.createPokemon("Charmander", 7);
			PartyMember sentret = PartyMemberFactory.createPokemon("Sentret", 3);
			player.caughtPokemon(charmander);
			player.caughtPokemon(sentret);
			player.setMoney(1000000);
			player.setCurLocation(LocationLibrary.getLocation("Route 27"));
			playBackgroundMusic("NewBarkTown");
			gData.start_coorX = (Tile.TILESIZE * (8 - player.getCurrentX()));
			gData.start_coorY = (Tile.TILESIZE * (6 - player.getCurrentY()));
			if (Configuration.SHOWINTRO) {
				gData.introStage = 1;
				gData.screen = SCREEN.INTRO;
			}
			DebugUtility.printMessage("Started new game.");
		}

		// initialize the player sprite
		player.tData.sprite_name = "PLAYER";
		player.tData.sprite = SpriteLibrary.getSprites("PLAYER").get(player.getDirection().ordinal() * 3);

		// get out of any menus
		gData.screen = SCREEN.WORLD;

		npcs.start();

		// start clock for current session
		gameTimeStruct.timeStarted = System.currentTimeMillis();

		DebugUtility.printMessage("- " + player.tData.toString());
		DebugUtility.printMessage("Rendered session id: " + gData.gameSessionID);
		return gData;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// canMoveInDir - makes a temporary coordinate, and checks to see if the
	// actor can move in the specified direction
	//
	// ////////////////////////////////////////////////////////////////////////
	public boolean canMoveInDir(DIR dir) {
		boolean canMove = false;

		if (Configuration.NOCLIP) {
			// if noclip is turned on, player can always move
			canMove = true;
		} else {
			// temporarily store the location the player would move to
			Coordinate loc = getPlayer().getPosition().move(dir);

			// if the potential location is in bounds, can only move if tile is
			// not obstacle
			// TODO water tiles
			if (loc.getY() > 0 && loc.getY() <= getMapHeight() && loc.getX() > 0 && loc.getX() <= getMapWidth()) {
				canMove = !(TileSet.compareTiles(getMapTileAt(loc), TileSet.OBSTACLE));
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

	public int getStartX() {
		return gData.start_coorX;
	}

	public int getStartY() {
		return gData.start_coorY;
	}

	public Player getPlayer() {
		return player;
	}

	public int getId() {
		return gData.gameSessionID;
	}

	public void setPlayerDirection(DIR dir) {
		setPlayerSprite(SpriteLibrary.getSpriteForDir("PLAYER", dir));
		getPlayer().setDirection(dir);
	}

	public void setPlayerSprite(ImageIcon imageIcon) {
		getPlayer().tData.sprite = imageIcon;
	}

	public void toggleSound() {
		gData.option_sound = !gData.option_sound;
		if (gData.option_sound) {
			playBackgroundMusic();
		} else {
			AudioLibrary.pauseBackgroundMusic();
		}
	}

	public void playClip(SOUND_EFFECT select) {
		if (gData.option_sound) {
			AudioLibrary.playClip(select);
		}
	}

	public void playTrainerMusic() {
		if (gData.option_sound) {
			audio.pickTrainerMusic();
		}
	}

	public void playBackgroundMusic(String string) {
		if (gData.option_sound) {
			audio.playBackgroundMusic(string);
		}
	}

	public void playBackgroundMusic() {
		if (gData.option_sound) {
			audio.playBackgroundMusic(getPlayer().getCurLoc().getName());
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
		return getMapTileAt(position).getClass().equals(BattleTile.class);
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
			isValid = npc.isTrainer() && !isPlayerWalking() && Configuration.DOBATTLES
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
				&& (NPC_Y - playerCurY <= Configuration.NPC_SIGHT_DISTANCE) && (NPC_DIR == DIR.NORTH)) || ((playerCurY > NPC_Y)
				&& (playerCurY - NPC_Y <= Configuration.NPC_SIGHT_DISTANCE) && (NPC_DIR == DIR.SOUTH)))) || ((playerCurY == NPC_Y) && (((playerCurX < NPC_X)
				&& (NPC_X - playerCurX <= Configuration.NPC_SIGHT_DISTANCE) && (NPC_DIR == DIR.WEST)) || ((playerCurX > NPC_X)
				&& (playerCurX - NPC_X <= Configuration.NPC_SIGHT_DISTANCE) && (NPC_DIR == DIR.EAST)))));
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
			if (config.numSelections.containsKey(getScreen())) {
				if (getCurrentSelection() < config.numSelections.get(getScreen()) - 1) {
					setCurrentSelection(curSel + 1);
				}
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

	public void setPlayerWin(boolean b) {
		gData.playerWin = false;
	}

	// TODO - message queue instead
	public void setCurrentMessage(String string) {
		gData.currentMessage = string;
	}

	public String getCurrentMessage(boolean npc) {
		if (npc)
			return getCurNPC().getText(getMessageStage());
		else
			return gData.currentMessage;
	}

	public void initialize() {
		gData = new GameData();
	}

	public Party getCurrentEnemy() {
		return currentEnemy;
	}

	public void setWildPokemon() {
		currentEnemy.clear();
		currentEnemy.add(PartyMemberFactory.getInstance().randomPokemon(getPlayer().getCurLoc()));
	}

	public void doEncounter(Party pokemon, String name) {
		BattleEngine.getInstance().fight(pokemon, this, name);
	}

	public int getMessageStage() {
		return gData.messageStage;
	}

	public Actor getCurNPC() {
		return currentNPC;
	}

	public void setCurNPC(Actor npc) {
		currentNPC = npc;
	}

	public void exitMenu() {
		getCurNPC().setStationary(false);
		setScreen(SCREEN.WORLD);
	}

	public void saveGame() {
		gameTimeStruct.saveTime();

		FileOutputStream fout = null;
		ObjectOutputStream oos = null;
		try {
			File dir = new File("resources/data");
			DebugUtility.printMessage(dir.isDirectory() + "");
			DebugUtility.printMessage(getPlayer().tData.toString());
			fout = new FileOutputStream("resources/data/PokemonOrange.sav");
			oos = new ObjectOutputStream(fout);
			oos.writeObject(gData);
			oos.close();
			fout.close();
		} catch (Exception e1) {
			e1.printStackTrace();
			DebugUtility.printMessage("Unable to save game...");
			return;
		}
		DebugUtility.printMessage("** Saved game.");
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// loadGame - loads game object from a default .SAV file
	//
	// ////////////////////////////////////////////////////////////////////////
	public void loadGame() {
		GameData data = null;
		FileInputStream fout = null;
		ObjectInputStream oos = null;
		try {
			fout = new FileInputStream("resources/data/PokemonOrange.sav");
			oos = new ObjectInputStream(fout);
			data = (GameData) oos.readObject();
			oos.close();
			fout.close();
			DebugUtility.printMessage("** Loaded game from save.");
		} catch (Exception e1) {
			e1.printStackTrace();
			DebugUtility.printMessage("Unable to load game...");
		}
		gData = data;
	}

	public void printData() {
		DebugUtility.printHeader("Game Data");
		DebugUtility.printMessage(gData.toString());
	}

}
