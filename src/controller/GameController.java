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
import graphics.GameMap;
import graphics.GraphicsOrigin;
import graphics.NPCThread;
import graphics.SpriteLibrary;
import location.LocationLibrary;
import model.Configuration;
import model.Coordinate;
import model.GameData;
import model.GameTime;
import model.MessageQueue;
import party.Battler;
import party.Battler.STATUS;
import scenes.BaseScene;
import scenes.IntroScene;
import scenes.Scene;
import scenes.WorldScene;
import party.BattlerFactory;
import party.Party;
import tiles.Tile;
import trainers.Actor;
import trainers.Actor.DIR;
import trainers.NPCLibrary;
import trainers.Player;
import utilities.BattleEngine;
import utilities.DebugUtility;
import utilities.RandomNumUtils;

/**
 * Controls game logic flow by providing an interface between view and data *
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

	private Scene currentScene;

	/***************************************************************************
	 * 
	 * MOVEMENT CONTROL LOGIC
	 * 
	 **************************************************************************/

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

	/**
	 * Retrieve the current player instance
	 * 
	 * @return player
	 */
	public Player getPlayer() {
		return player;
	}

	/***************************************************************************
	 * 
	 * GAME CONTROL LOGIC
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
		GraphicsOrigin.getInstance().setStartCoordX(i);
		i = (Tile.TILESIZE * (6 - player.getCurrentY()));
		GraphicsOrigin.getInstance().setStartCoordY(i);
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
			currentScene = WorldScene.instance;
		} else {
			newGame();

			if (Configuration.SHOWINTRO) {
				currentScene = IntroScene.instance;
				MessageQueue.getInstance()
						.addAllMessages(NPCLibrary.getInstance().get("Professor Oak").getConversationText().toArray());
			} else {
				currentScene = WorldScene.instance;
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
	 * GRAPHICS CONTROL LOGIC
	 * 
	 **************************************************************************/

	/**
	 * Add to the offset (y direction) given a direction the player is moving
	 * 
	 * @param playerDir
	 *            - Direction player is facing
	 */
	public void setOffsetY(DIR playerDir) {
		if (player.canMoveInDir(playerDir)) {
			switch (playerDir) {
			case NORTH:
				GraphicsOrigin.getInstance().addOffsetY(2);
				break;
			case SOUTH:
				GraphicsOrigin.getInstance().addOffsetY(-2);
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
		if (player.canMoveInDir(playerDir)) {
			switch (playerDir) {
			case EAST:
				GraphicsOrigin.getInstance().addOffsetX(-2);
				break;
			case WEST:
				GraphicsOrigin.getInstance().addOffsetX(2);
				break;
			default:
				break;
			}
		}
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
		GraphicsOrigin.getInstance().setStartCoordX(i);
		i = (player.getCurrentY() - playerPos.getY()) * -1 * Tile.TILESIZE;
		GraphicsOrigin.getInstance().setStartCoordY(i);

		// face the opposite direction of the way the player entered the
		// teleport square
		player.turnAround();
	}

	/**
	 * Are we at the world screen, does the NPC see the player, is the player not
	 * walking, is the master control for battles turned on, and the trainer is not
	 * beaten?
	 * 
	 * @param npc
	 *            - npc to check
	 * @return whether or not the above conditions are met
	 */
	public boolean validEncounterConditions(Actor npc) {
		boolean isValid = false;
		if (getScene() == WorldScene.instance) {
			isValid = npc.isTrainer() && !player.isWalking && Configuration.DOBATTLES
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
		if (GameMap.getInstance().isBattleAt(player.getPosition())) {
			if (RandomNumUtils.isWildEncounter()) {
				currentEnemyParty.clear();
				currentEnemyParty.add(BattlerFactory.getInstance().randomPokemon(getPlayer().getCurLoc()));
				MessageQueue.getInstance().add("Wild " + currentEnemyParty.get(0).getName() + " appeared");
				BattleEngine.getInstance().fight(currentEnemyParty, this, null);
			}
		}
		for (Battler p : getPlayer().getParty()) {
			STATUS partyStatus = p.getStatusEffect();
			if (partyStatus == STATUS.BRN || partyStatus == STATUS.PZN)
				p.takeDamage(1);
		}
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
	 * Control which screen is printed and which logic is handled
	 * 
	 * @return current screen to be painted
	 */
	public Scene getScene() {
		return currentScene;
	}

	/**
	 * Control which screen is printed and which logic is handled
	 * 
	 * @param curScreen
	 *            - the new current screen
	 */
	public void setScene(BaseScene curScreen) {
		currentScene = curScreen;
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
