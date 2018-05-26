package controller;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.swing.Timer;

import audio.AudioLibrary;
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
import party.Battler;
import party.Battler.STATUS;
import party.BattlerFactory;
import party.Party;
import tiles.NormalTile;
import tiles.Tile;
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

	// controls the speed game events are handled and the current game time
	private Timer gameSpeed;

	// main game logic
	public GameData gData = GameData.getInstance();

	// the player Actor
	private Player player;

	// the current battle enemy party
	private Party currentEnemyParty = new Party();

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
	 * MOVEMENT LOGIC
	 * 
	 **************************************************************************/

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

		if (Configuration.getInstance().isNoClip()) {
			// if noclip is turned on, player can always move
			canMove = true;
		} else {
			// temporarily store the location the player would move to
			Coordinate loc = getPlayer().getPosition().move(dir);

			// if the potential location is in bounds, can only move if tile is
			// not obstacle
			if (GameData.getInstance().isInBounds(loc)) {
				canMove = !GameData.getInstance().isObstacleAt(loc);
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
		AudioLibrary.playBackgroundMusic(getPlayer().getCurLoc().getName());

		int i = (Tile.TILESIZE * (8 - player.getCurrentX()));
		GameData.getInstance().setStartCoordX(i);
		i = (Tile.TILESIZE * (6 - player.getCurrentY()));
		GameData.getInstance().setStartCoordY(i);
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
				MessageQueue.getInstance()
						.addAllMessages(NPCLibrary.getInstance().get("Professor Oak").getConversationText().toArray());
			} else {
				gData.scene = WorldScene.instance;
			}
			DebugUtility.printMessage("Started new game.");
		}

		// Connect to the server
		GameClient.getInstance().establishMultiplayerSession(player.getID());

		// initialize the player sprite
		player.tData.sprite_name = "PLAYER";
		player.tData.sprite = SpriteLibrary.getSprites("PLAYER").get(player.getDirection().ordinal() * 3);

		// Start NPC movement
		NPCThread.getInstance().start();

		// start clock for current session
		GameTime.getInstance().start();

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
		Configuration.getInstance().toggleSound();
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
				GameData.getInstance().addOffsetY(2);
				break;
			case SOUTH:
				GameData.getInstance().addOffsetY(-2);
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
				GameData.getInstance().addOffsetX(-2);
				break;
			case WEST:
				GameData.getInstance().addOffsetY(2);
				break;
			default:
				break;
			}
		}
	}

	/** Check for teleport tile at a given position **/
	public boolean isTeleportTile(Coordinate playerPos) {
		return gData.isTeleportAt(playerPos);
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

		int i = (player.getCurrentX() - playerPos.getX()) * -1 * Tile.TILESIZE;
		GameData.getInstance().setStartCoordX(i);
		i = (player.getCurrentY() - playerPos.getY()) * -1 * Tile.TILESIZE;
		GameData.getInstance().setStartCoordY(i);

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

	public void setMapTileAt(Coordinate position, NormalTile normal) {
		gData.setMapTileAt(position, normal);
	}

	/**
	 * Perform these sets of checks after player movement
	 */
	public void postMovementChecks() {
		// check for wild encounter
		if (gData.isBattleAt(player.getPosition())) {
			if (RandomNumUtils.isWildEncounter()) {
				currentEnemyParty.clear();
				currentEnemyParty.add(BattlerFactory.getInstance().randomPokemon(getPlayer().getCurLoc()));
				MessageQueue.getInstance().add("Wild " + currentEnemyParty.get(0).getName() + " appeared");
				doEncounter(currentEnemyParty, null);
			}
		}
		for (Battler p : getPlayer().getParty()) {
			STATUS partyStatus = p.getStatusEffect();
			if (partyStatus == STATUS.BRN || partyStatus == STATUS.PZN)
				p.takeDamage(1);
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
			MessageQueue.getInstance().add(curNpc.getConversationText().get(0));
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
	 * Save game object to a default .SAV file
	 */
	public void saveGame() {
		GameTime.getInstance().saveTime();

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
