package controller;

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
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.Timer;

import audio.AudioLibrary;
import audio.AudioLibrary.SOUND_EFFECT;
import client.GameClient;
import graphics.BaseScene;
import graphics.IntroScene;
import graphics.NPCThread;
import graphics.SpriteLibrary;
import graphics.WorldScene;
import location.LocationLibrary;
import model.Configuration;
import model.Coordinate;
import model.GameData;
import model.GameTime;
import model.MessageQueue;
import model.NameBuilder;
import party.Battler;
import party.Battler.STATUS;
import party.BattlerFactory;
import party.Party;
import tiles.BattleTile;
import tiles.Tile;
import tiles.TileSet;
import trainers.Actor;
import trainers.Actor.DIR;
import trainers.NPCLibrary;
import trainers.Player;
import utilities.BattleEngine;
import utilities.DebugUtility;
import utilities.RandomNumUtils;

/**
 * Controls game logic flow by providing an interface between view and data
 * 
 * TODO add column to Configuration selections so name screen and battle scenes
 * can use the same selection map
 */
public class GameController implements Serializable {

	private static final long serialVersionUID = 968834933407220662L;

	// handles any audio
	private AudioLibrary audio = AudioLibrary.getInstance();

	// controls the speed game events are handled and the current game time
	private GameTime gameTimeStruct = new GameTime(0, 0, 0);
	private Timer gameSpeed;

	// main game logic
	private GameData gData;

	// add messages to be painted
	private MessageQueue messages = new MessageQueue();

	// builds names using the name screen - used to name PartyMembers
	private NameBuilder nameBuilder = new NameBuilder();

	// the player Actor
	private Player player;

	// the current battle enemy party
	private Party currentEnemyParty = new Party();

	// NPC random movement controller
	private NPCThread npcs = new NPCThread();

	// multiplayer client
	private GameClient gameClient;

	/**
	 * Create a new controller to wrap around the game data
	 */
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

	/**
	 * Sets the toBeNamed object. Used for graphics painting to get the sprite of
	 * the thing to be named.
	 * 
	 * @param tbn
	 *            - new to be named object
	 */
	public void setToBeNamed(String tbn) {
		nameBuilder.setToBeNamed(tbn);
	}

	/**
	 * Retrieve the thing currently being named
	 * 
	 * @return string name of being named object
	 */
	public String getToBeNamed() {
		return nameBuilder.getToBeNamed();
	}

	/**
	 * Add the selected character to the name builder
	 */
	public void addSelectedChar() {
		nameBuilder.addSelectedChar(getCurrentSelection());
	}

	/**
	 * Removes a char from the string in the process of being built
	 */
	public void removeChar() {
		nameBuilder.removeChar();
	}

	/**
	 * Retrieve the name built by the user in the current session
	 * 
	 * @return built name
	 */
	public String getChosenName() {
		return nameBuilder.toString();
	}

	/**
	 * Clears all characters from the name builder
	 */
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
		Integer i = gData.imageMap.get(new Coordinate(y, layer));
		if (i == null) {
			return 0;
		} else {
			return gData.imageMap.get(new Coordinate(y, layer));
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
		gData.imageMap.set(new Coordinate(y, layer), value);
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
		return gData.tileMap.get(c);
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
		gData.tileMap.set(position, tile);
	}

	/**
	 * Get the current graphical offset (x direction)
	 * 
	 * @return int x offset
	 */
	public int getOffsetX() {
		return gData.offsetX;
	}

	/**
	 * Get the current graphical offset (y direction)
	 * 
	 * @return int y offset
	 */
	public int getOffsetY() {
		return gData.offsetY;
	}

	/**
	 * From the Configuration map file path, populate the image / tile data
	 * 
	 * @throws IOException
	 *             - if the file cannot be read
	 * @throws InterruptedException
	 *             - if the reader threads are interrupted
	 */
	public void loadMap() throws IOException, InterruptedException {

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
						if ((layer == 1 || (layer == 2 && Integer.parseInt(code) > 0))
								&& data.getMapTileAt(c) == null) {
							data.setMapTileAt(c, TileSet.NORMAL);
						}
					}
					data.addMapImageAt(layer, y, Integer.parseInt(code));
				}
			}
		}

		DebugUtility.printHeader("Loading Map");

		// intialize file reader
		BufferedReader bReader = new BufferedReader(
				new InputStreamReader(GameController.class.getResourceAsStream(Configuration.MAP_TO_LOAD)));
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

	/**
	 * Pause all NPC movement
	 */
	public void pauseNPCMovement() {
		npcs.stop = true;
	}

	/**
	 * Get the height of the map
	 * 
	 * @return int height of map
	 */
	public int getMapHeight() {
		return gData.map_height;
	}

	/**
	 * Get the width of the map
	 * 
	 * @return int width of map
	 */
	public int getMapWidth() {
		return gData.map_width;
	}

	/**
	 * Set the height of the map
	 * 
	 * @param hgt
	 *            - the new height of the map
	 */
	private void setMapHeight(int hgt) {
		gData.map_height = hgt;
	}

	/**
	 * Set the width of the map
	 * 
	 * @param wdt
	 *            - the new width of the map
	 */
	private void setMapWidth(int wdt) {
		gData.map_width = wdt;
	}

	/**
	 * Get the original start X (before offsets where added)
	 * 
	 * @return x start coordinate
	 */
	public int getStartX() {
		return gData.start_coorX;
	}

	/**
	 * Get the original start Y (before offsets where added)
	 * 
	 * @return y start coordinate
	 */
	public int getStartY() {
		return gData.start_coorY;
	}

	/***************************************************************************
	 * 
	 * MOVEMENT CONTROL LOGIC
	 * 
	 * Helps control movement on screen
	 * 
	 **************************************************************************/

	/**
	 * Can the player move?
	 * 
	 * @return whether or not the player can move
	 */
	public boolean isMovable() {
		return gData.playerCanMove;
	}

	/**
	 * Set whether the player can move
	 * 
	 * @param b
	 *            - new value for can the player move
	 */
	public void setMovable(boolean b) {
		gData.playerCanMove = b;
	}

	/**
	 * Is the player currently walking?
	 * 
	 * @return whether or not player is in walking animation
	 */
	public boolean isPlayerWalking() {
		return gData.isPlayerWalking;
	}

	/**
	 * Set whether or not the player is walking
	 * 
	 * @param isPlayerWalking
	 *            - new value for player in walking animation
	 */
	public void setPlayerWalking(boolean isPlayerWalking) {
		gData.isPlayerWalking = isPlayerWalking;
	}

	/**
	 * Start the game timer to fire game events
	 * 
	 * @param theGame
	 *            - the ActionListner to base this timer on
	 */
	public void startGameTimer(ActionListener theGame) {
		gameSpeed = new Timer(100 - gData.currentSpeed.getValue(), theGame);
		gameSpeed.start();
	}

	/***************************************************************************
	 * 
	 * TIME CONTROL LOGIC
	 * 
	 * Helps control time and gameplay speed
	 * 
	 **************************************************************************/

	/**
	 * Update the current game time
	 */
	public void updateTime() {
		gameTimeStruct.updateTime();
	}

	/**
	 * Format the time into a readable string
	 * 
	 * @return HH:MM:SS game time
	 */
	public String formatTime() {
		return gameTimeStruct.formatTime();
	}

	/**
	 * Makes a temporary coordinate, and checks to see if the actor can move in the
	 * specified direction
	 * 
	 * @param dir
	 *            - the direction to check
	 * @return - whether or not the player can move there
	 */
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

	/**
	 * Retrieve the current player instance
	 * 
	 * @return player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Retrieve the current session (player) ID
	 * 
	 * @return a new ID
	 */
	public int getId() {
		return gData.gameSessionID;
	}

	/**
	 * Set the player sprite in a specified direction
	 * 
	 * @param dir
	 *            - player's new direction
	 */
	public void setPlayerDirection(DIR dir) {

		getPlayer().tData.sprite = SpriteLibrary.getSpriteForDir("PLAYER", dir);
		getPlayer().setDirection(dir);
	}

	/***************************************************************************
	 * 
	 * MULTIPLAYER LOGIC
	 * 
	 * Connection to the game server, handling updates and such.
	 * 
	 **************************************************************************/
	/**
	 * Start a multiplayer session for this client.
	 */
	public void establishMultiplayerSession() {
		DebugUtility.printMessage("Connecting to game server...");
		gameClient = new GameClient(player.getID());
		new Thread(gameClient).start();
		DebugUtility.printMessage("Connected to game server.");
	}

	/**
	 * Close the multiplayer session for this client.
	 */
	public void endMultiplayerSession() {
		gameClient.sendMessage("end");
		DebugUtility.printMessage("Disconnected from game server.");
	}

	/***************************************************************************
	 * 
	 * TIME CONTROL LOGIC
	 * 
	 * Helps control time and gameplay speed
	 * 
	 **************************************************************************/

	/**
	 * New game
	 */
	private void newGame() {
		String name = "GOLD";
		player = new Player(4, 2, name);
		Battler charmander = BattlerFactory.createPokemon("Charmander", 7);
		Battler sentret = BattlerFactory.createPokemon("Sentret", 3);
		Battler charizard = BattlerFactory.createPokemon("Charmander", 99);
		Battler pidgey = BattlerFactory.createPokemon("Pidgey", 20);
		Battler mewtwo = BattlerFactory.createPokemon("Mewtwo", 50);
		Battler squirtle = BattlerFactory.createPokemon("Squirtle", 50);
		player.caughtWild(charmander);
		player.caughtWild(sentret);
		player.caughtWild(charizard);
		player.caughtWild(pidgey);
		player.caughtWild(mewtwo);
		player.caughtWild(squirtle);
		player.setMoney(1000000);
		player.setCurLocation(LocationLibrary.getLocation("Route 27"));
		playBackgroundMusic();
		gData.start_coorX = (Tile.TILESIZE * (8 - player.getCurrentX()));
		gData.start_coorY = (Tile.TILESIZE * (6 - player.getCurrentY()));
		gData.introStage = 1;
	}

	/**
	 * Start the game, based off a save file or as a new game
	 * 
	 * @param continued
	 *            - whether or not to continue the default save game
	 * @return a new GameData instance representing the game state
	 */
	public GameData startGame(boolean continued) {

		DebugUtility.printHeader("Starting game:");
		if (continued) {
			loadGame();

			if (!player.tData.isValidData()) {
				DebugUtility.error("Unable to continue game from save file. Corrupt data:\n" + player.tData.toString());
			}
			// get out of continue menu
			gData.scene = WorldScene.instance;
		} else {
			newGame();

			if (Configuration.SHOWINTRO) {
				gData.scene = IntroScene.instance;
				addAllMessages(NPCLibrary.getInstance().get("Professor Oak").getConversationText().toArray());
			} else {
				gData.scene = WorldScene.instance;
			}
			DebugUtility.printMessage("Started new game.");
		}

		// TODO connect to the game server
		establishMultiplayerSession();

		// initialize the player sprite
		player.tData.sprite_name = "PLAYER";
		player.tData.sprite = SpriteLibrary.getSprites("PLAYER").get(player.getDirection().ordinal() * 3);

		npcs.start();

		// start clock for current session
		gameTimeStruct.timeStarted = System.currentTimeMillis();

		DebugUtility.printMessage("- " + player.tData.toString());
		DebugUtility.printMessage("Rendered session id: " + gData.gameSessionID);
		return gData;
	}

	/***************************************************************************
	 * 
	 * SOUND CONTROL LOGIC
	 * 
	 * Helps control playing of sounds.
	 * 
	 **************************************************************************/
	/**
	 * Toggle the master volume control
	 */
	public void toggleSound() {
		gData.option_sound = !gData.option_sound;
		if (gData.option_sound) {
			playBackgroundMusic();
		} else {
			AudioLibrary.pauseBackgroundMusic();
		}
	}

	/**
	 * Play the given sound effect clip
	 * 
	 * @param select
	 *            - sound effect selected
	 */
	public void playClip(SOUND_EFFECT select) {
		if (gData.option_sound) {
			AudioLibrary.playClip(select);
		}
	}

	/**
	 * Choose a random trainer music and play it
	 */
	public void playTrainerMusic() {
		if (gData.option_sound) {
			audio.pickTrainerMusic();
		}
	}

	/**
	 * Play the given background music (by track title)
	 * 
	 * @param songTitle
	 *            - sound effect selected
	 */
	public void playBackgroundMusic(String songTitle) {
		if (gData.option_sound) {
			audio.playBackgroundMusic(songTitle);
		}
	}

	/**
	 * Play the background music for the player's current location
	 */
	public void playBackgroundMusic() {
		if (gData.option_sound) {
			audio.playBackgroundMusic(getPlayer().getCurLoc().getName());
		}
	}

	/**
	 * Add to the offset (y direction) given a direction the player is moving
	 * 
	 * @param playerDir
	 *            - Direction player is facing
	 */
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

	/**
	 * Add to the offset (x direction) given a direction the player is moving
	 * 
	 * @param playerDir
	 *            - Direction player is facing
	 */
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

	/**
	 * Set the start X coordinate for a teleport
	 * 
	 * @param i
	 *            - start X coordinate
	 */
	public void setStartCoordX(int i) {
		gData.start_coorX = i;
	}

	/**
	 * Set the start Y coordinate for a teleport
	 * 
	 * @param i
	 *            - start y coordinate
	 */
	public void setStartCoordY(int i) {
		gData.start_coorY = i;
	}

	/**
	 * Is the player standing on a battle tile?
	 * 
	 * @param position
	 *            - current player position
	 * @return whether or not the postion is a battle position
	 */
	public boolean isBattleTile(Coordinate position) {
		return getMapTileAt(position).getClass().equals(BattleTile.class);
	}

	/**
	 * Is the map tile at the given position on a teleport tile?
	 * 
	 * @param position
	 *            - map position
	 * @return whether or not the postion is a teleport position
	 */
	public boolean isTeleportTile(Coordinate position) {
		return TeleportLibrary.getList().containsKey(position);
	}

	/**
	 * Perform a teleport given the player's current position
	 * 
	 * @param playerPos
	 *            - current player position
	 */
	public void doTeleport(Coordinate playerPos) {
		Player player = getPlayer();

		player.setLoc(TeleportLibrary.getList().get(playerPos));

		setStartCoordX((player.getCurrentX() - playerPos.getX()) * -1 * Tile.TILESIZE);
		setStartCoordY((player.getCurrentY() - playerPos.getY()) * -1 * Tile.TILESIZE);

		// face the opposite direction of the way the player entered the
		// teleport square
		player.turnAround();
	}

	/**
	 * Are we at the world screen, does the NPC see the player, is the player not
	 * walking, is the master control for battles turned on, and the player is not
	 * beaten?
	 * 
	 * @param npc
	 *            - npc to check
	 * @return whether or not the above conditions are met
	 */
	public boolean validEncounterConditions(Actor npc) {
		boolean isValid = false;
		if (getScene() == WorldScene.instance) {
			isValid = npc.isTrainer() && !isPlayerWalking() && Configuration.DOBATTLES
					&& !getPlayer().beatenTrainers.contains(npc.getName()) && npcSeesPlayer(npc);
		}
		return isValid;
	}

	/**
	 * Given an NPC, check if that NPC sees the player
	 * 
	 * Sight rules:
	 * 
	 * @NORTH - columns match, playerY < NPC_Y, within sight distance
	 * @SOUTH - columns match, playerY > NPC_Y, within sight distance
	 * @EAST - rows match, playerX > NPC_X, within sight distance
	 * @WEST - rows match, playerX < NPC_X, within sight distance
	 * 
	 * @param curNPC
	 *            - the npc to check against
	 * @return whether or not the NPC sees the player
	 */
	private boolean npcSeesPlayer(Actor curNPC) {
		Player player = getPlayer();
		int playerCurY = player.getCurrentY();
		int playerCurX = player.getCurrentX();
		int NPC_X = curNPC.getCurrentX();
		int NPC_Y = curNPC.getCurrentY();
		DIR NPC_DIR = curNPC.getDirection();

		return (((playerCurX == curNPC.getCurrentX()) && (((playerCurY < NPC_Y)
				&& (NPC_Y - playerCurY <= Configuration.NPC_SIGHT_DISTANCE) && (NPC_DIR == DIR.NORTH))
				|| ((playerCurY > NPC_Y) && (playerCurY - NPC_Y <= Configuration.NPC_SIGHT_DISTANCE)
						&& (NPC_DIR == DIR.SOUTH))))
				|| ((playerCurY == NPC_Y) && (((playerCurX < NPC_X)
						&& (NPC_X - playerCurX <= Configuration.NPC_SIGHT_DISTANCE) && (NPC_DIR == DIR.WEST))
						|| ((playerCurX > NPC_X) && (playerCurX - NPC_X <= Configuration.NPC_SIGHT_DISTANCE)
								&& (NPC_DIR == DIR.EAST)))));
	}

	/**
	 * Perform these sets of checks after player movement
	 */
	public void postMovementChecks() {
		// check for wild encounter
		if (isBattleTile(player.getPosition())) {
			if (RandomNumUtils.isWildEncounter()) {
				currentEnemyParty.clear();
				currentEnemyParty.add(BattlerFactory.getInstance().randomPokemon(getPlayer().getCurLoc()));
				addMessage("Wild " + currentEnemyParty.get(0).getName() + " appeared");
				doEncounter(currentEnemyParty, null);
			}
		}
		for (Battler p : getPlayer().getParty()) {
			STATUS partyStatus = p.getStatusEffect();
			if (partyStatus == STATUS.BRN || partyStatus == STATUS.PZN)
				p.doDamage(1);
		}
	}

	/**
	 * Start an encounter with the given party and opponent name
	 * 
	 * @param pokemon
	 *            - the party to use
	 * @param name
	 *            - opponent's name
	 */
	public void doEncounter(Party pokemon, String name) {
		BattleEngine.getInstance().fight(pokemon, this, name);
	}

	/**
	 * Is master volume on?
	 * 
	 * @return whether or not music/sound should be played
	 */
	public boolean isSoundOn() {
		return gData.option_sound;
	}

	/**
	 * Get the current row selection for the current screen
	 * 
	 * @return int current selection for this screen
	 */
	public int getCurrentRowSelection() {
		if (!gData.currentSelection.containsKey(getScene())) {
			setCurrentSelection(new Coordinate(0, 0));
		}
		return gData.currentSelection.get(getScene()).getX();
	}

	/**
	 * Get the selection for the current screen
	 * 
	 * @return int current selection for this screen
	 */
	public int getCurrentColSelection() {
		if (!gData.currentSelection.containsKey(getScene())) {
			setCurrentSelection(new Coordinate(0, 0));
		}
		return gData.currentSelection.get(getScene()).getY();
	}

	/**
	 * Check cur NPC coordinate w/ an adjusted coordinate & player direction.
	 * 
	 * @param curNpc
	 *            - the NPC to check against
	 * @param c
	 *            - the coordinate of the player
	 * @param playerDir
	 *            - the direction the player is facing
	 * @param keyCode
	 *            - the key code of the key pressed
	 */
	public void tryBorderNPC(Actor curNpc, Coordinate c, DIR playerDir) {
		Coordinate c1 = curNpc.getPosition();
		if (c1.equals(c)) {
			curNpc.setDirectionOpposite(playerDir);
			curNpc.setStationary(true);
			addMessage(curNpc.getConversationText().get(0));
		}
	}

	/**
	 * Set the selection for the current screen
	 * 
	 * @param i
	 *            - the new selection index
	 */
	public void setCurrentSelection(Coordinate i) {
		if (gData.currentSelection.containsKey(getScene())) {
			gData.currentSelection.replace(getScene(), i);
		} else {
			gData.currentSelection.put(getScene(), new Coordinate(0, 0));
		}
	}

	/**
	 * Decrement the row selection at the current screen
	 */
	public void decrementRowSelection() {
		setCurrentSelection(getCurrentSelection().move(DIR.WEST));
	}

	/**
	 * Increment the row selection at the current screen
	 */
	public void incrementRowSelection() {
		setCurrentSelection(getCurrentSelection().move(DIR.EAST));
	}

	/**
	 * Decrement the column selection at the current screen
	 */
	public void decrementColSelection() {
		setCurrentSelection(getCurrentSelection().move(DIR.NORTH));
	}

	/**
	 * Increment the column selection at the current screen
	 */
	public void incrementColSelection() {
		setCurrentSelection(getCurrentSelection().move(DIR.SOUTH));
	}

	/**
	 * Retrieve the current selection
	 * 
	 * @return the current selection
	 */
	public Coordinate getCurrentSelection() {
		if (!gData.currentSelection.containsKey(getScene())) {
			setCurrentSelection(new Coordinate(0, 0));
		}
		return gData.currentSelection.get(getScene());
	}

	/**
	 * Control which screen is printed and which logic is handled
	 * 
	 * @return current screen to be painted
	 */
	public BaseScene getScene() {
		return gData.scene;
	}

	/**
	 * Control which screen is printed and which logic is handled
	 * 
	 * @param curScreen
	 *            - the new current screen
	 */
	public void setScene(BaseScene curScreen) {
		gData.scene = curScreen;
	}

	/**
	 * Add a message to the message queue
	 * 
	 * @param string
	 *            - the message to add
	 */
	public void addMessage(String string) {
		messages.add(string);
	}

	/**
	 * Add a series of messages to the message queue
	 * 
	 * @param string
	 *            - the message to add
	 */
	public void addMessages(Collection<String> string) {
		messages.addAll(string);
	}

	/**
	 * Add all lines of text to the current conversation / message
	 * 
	 * @param strings
	 *            - a list of messages
	 */
	public void addAllMessages(Object[] strings) {
		for (Object message : strings) {
			messages.add(message.toString());
		}
	}

	/**
	 * Get the next message in the queue
	 * 
	 * @return String next message
	 */
	public String[] getCurrentMessage() {
		return messages.getMessages();
	}

	/**
	 * Get the current stage of the intro scene
	 * 
	 * TODO consolidate all these message control functions into a message queue
	 * 
	 * @return int intro scene stage
	 */
	public int getIntroStage() {
		return gData.introStage;
	}

	/**
	 * Increment to get the next 2 messages for the intro scene
	 */
	public void incrIntroStage() {
		gData.introStage += 2;
	}

	/**
	 * Retrieve the current message stage
	 * 
	 * @return int stage of conversation
	 */
	public int getMessageStage() {
		return gData.messageStage;
	}

	/**
	 * Does the message queue have another message?
	 * 
	 * @return whether or not there is another message in the queue
	 */
	public boolean hasNextMessage() {
		return messages.peek() != null;
	}

	/**
	 * Move to the next two messages in the queue
	 */
	public void nextMessage() {
		messages.poll();
		messages.poll();
	}

	/**
	 * Save game object to a default .SAV file
	 */
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

	/**
	 * loads game object from a default .SAV file
	 */
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

	/**
	 * Print the current game state
	 */
	public void printData() {
		DebugUtility.printHeader("Game Data");
		DebugUtility.printMessage(gData.toString());
	}
}
