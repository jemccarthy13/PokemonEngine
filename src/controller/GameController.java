package controller;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.swing.Timer;

import audio.AudioLibrary;
import client.GameClient;
import graphics.GameGraphicsData;
import graphics.GameMap;
import graphics.NPCThread;
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
import scenes.IntroScene;
import scenes.Scene;
import scenes.WorldScene;
import storage.Bag.POCKETS;
import storage.ItemLibrary;
import tiles.Tile;
import trainers.Actor;
import trainers.Actor.DIR;
import trainers.NPCLibrary;
import trainers.Player;
import utilities.DebugUtility;
import utilities.RandomNumUtils;

/**
 * Controls game logic flow by providing an interface between view and data *
 */
public class GameController implements Serializable {

	private static final long serialVersionUID = 968834933407220662L;

	// controls the speed game events are handled and the current game time
	private Timer gameSpeed;

	// the player Actor
	private Player player;

	// the current battle enemy party
	private Party currentEnemyParty = new Party();

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
		this.gameSpeed = new Timer(100 - GameData.getInstance().currentSpeed.getValue(), theGame);
		this.gameSpeed.start();
	}

	/**
	 * Retrieve the current player instance
	 * 
	 * @return player
	 */
	public Player getPlayer() {
		return this.player;
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
		this.player = new Player(4, 2, name);
		Battler charmander = BattlerFactory.createPokemon("Charmander", 7);
		Battler sentret = BattlerFactory.createPokemon("Sentret", 3);
		Battler charizard = BattlerFactory.createPokemon("Charmander", 99);
		Battler pidgey = BattlerFactory.createPokemon("Pidgey", 20);
		Battler mewtwo = BattlerFactory.createPokemon("Mewtwo", 50);
		Battler squirtle = BattlerFactory.createPokemon("Squirtle", 50);
		this.player.caughtWild(charmander);
		this.player.caughtWild(sentret);
		this.player.caughtWild(charizard);
		this.player.caughtWild(pidgey);
		this.player.caughtWild(mewtwo);
		this.player.caughtWild(squirtle);
		this.player.setMoney(1000000);
		this.player.setCurLocation(LocationLibrary.getLocation("Route 27"));
		AudioLibrary.playBackgroundMusic(getPlayer().getCurLoc().getName());

		this.player.getBag().addToPocket(POCKETS.ITEMS, ItemLibrary.getInstance().get("POTION"));
		this.player.getBag().addToPocket(POCKETS.ITEMS, ItemLibrary.getInstance().get("POTION"));
		this.player.getBag().addToPocket(POCKETS.ITEMS, ItemLibrary.getInstance().get("HYPER POTION"));

		int i = (Tile.TILESIZE * (8 - this.player.getCurrentX()));
		GameGraphicsData.getInstance().setStartCoordX(i);
		i = (Tile.TILESIZE * (6 - this.player.getCurrentY()));
		GameGraphicsData.getInstance().setStartCoordY(i);
		GameData.getInstance().introStage = 1;
	}

	/**
	 * Start the game, based off a save file or as a new game
	 * 
	 * @param continued
	 *            - whether or not to continue the default save game
	 * @throws IOException
	 */
	public void startGame(boolean continued) throws IOException {

		DebugUtility.printHeader("Starting game:");
		Scene nextScene = WorldScene.instance;
		boolean continueFound = false;

		if (continued) {
			loadGame();
			if (this.player == null || this.player.tData == null || !this.player.tData.isValidData()) {
				DebugUtility.printError("Unable to continue game from save file.");
			} else {
				continueFound = true;
			}
		}

		if (!continueFound) {
			DebugUtility.printMessage("Starting new game.");
			newGame();

			if (Configuration.SHOWINTRO) {
				nextScene = IntroScene.instance;
				MessageQueue.getInstance()
						.addAllMessages(NPCLibrary.getInstance().get("Professor Oak").getConversationText().toArray());
			}
		}

		// get out of continue menu
		GameGraphicsData.getInstance().setScene(nextScene);

		DebugUtility.printMessage("Started game.");

		// Connect to the server
		GameClient.getInstance().establishMultiplayerSession(this.player.getID());

		// Start NPC movement
		NPCThread.getInstance().start();

		// start clock for current session
		GameTime.getInstance().start();

		DebugUtility.printMessage("- " + this.player.tData.toString());
		DebugUtility.printMessage("Rendered session id: " + GameData.getInstance().gameSessionID);
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
		if (this.player.canMoveInDir(playerDir)) {
			switch (playerDir) {
			case NORTH:
				GameGraphicsData.getInstance().addOffsetY(2);
				break;
			case SOUTH:
				GameGraphicsData.getInstance().addOffsetY(-2);
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
		if (this.player.canMoveInDir(playerDir)) {
			switch (playerDir) {
			case EAST:
				GameGraphicsData.getInstance().addOffsetX(-2);
				break;
			case WEST:
				GameGraphicsData.getInstance().addOffsetX(2);
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

		this.player.setLoc(TeleportLibrary.getList().get(playerPos));

		int i = (this.player.getCurrentX() - playerPos.getX()) * -1 * Tile.TILESIZE;
		GameGraphicsData.getInstance().setStartCoordX(i);
		i = (this.player.getCurrentY() - playerPos.getY()) * -1 * Tile.TILESIZE;
		GameGraphicsData.getInstance().setStartCoordY(i);

		// face the opposite direction of the way the player entered the
		// teleport square
		this.player.turnAround();
	}

	/**
	 * Are we at the world screen, does the NPC see the player, is the player
	 * not walking, is the master control for battles turned on, and the trainer
	 * is not beaten?
	 * 
	 * @param npc
	 *            - npc to check
	 * @return whether or not the above conditions are met
	 */
	public boolean validEncounterConditions(Actor npc) {
		boolean isValid = false;
		if (GameGraphicsData.getInstance().getScene() == WorldScene.instance) {
			isValid = npc.isTrainer() && !this.player.isWalking() && Configuration.DOBATTLES
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
		int playerCurY = this.player.getCurrentY();
		int playerCurX = this.player.getCurrentX();
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
		if (GameMap.getInstance().isBattleAt(this.player.getPosition())) {
			if (RandomNumUtils.isWildEncounter()) {
				this.currentEnemyParty.clear();
				this.currentEnemyParty.add(BattlerFactory.randomPokemon(getPlayer().getCurLoc()));
				MessageQueue.getInstance().add("Wild " + this.currentEnemyParty.get(0).getName() + " appeared");
				BattleEngine.getInstance().fight(this.currentEnemyParty, this, null);
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
	public static void tryBorderNPC(Actor curNpc, Coordinate c, DIR playerDir) {
		Coordinate c1 = curNpc.getPosition();
		if (c1.equals(c)) {
			curNpc.setDirectionOpposite(playerDir);
			curNpc.setStationary(true);
			MessageQueue.getInstance().add(curNpc.getConversationText().get(0));
		}
	}

	/**
	 * Increment to get the next 2 messages for the intro scene
	 */
	public static void incrIntroStage() {
		GameData.getInstance().introStage += 2;
	}

	/**
	 * Save game object to a default .SAV file
	 * 
	 * @throws IOException
	 */
	public void saveGame() throws IOException {
		GameTime.getInstance().saveTime();

		try (FileOutputStream fout = new FileOutputStream("resources/data/PokemonOrange.sav");
				ObjectOutputStream oos = new ObjectOutputStream(fout);) {
			File dir = new File("resources/data");
			DebugUtility.printMessage(dir.isDirectory() + "");
			DebugUtility.printMessage(getPlayer().tData.toString());
			oos.writeObject(GameData.getInstance());
		} catch (

		Exception e1) {
			e1.printStackTrace();
			DebugUtility.printMessage("Unable to save game...");
		}
		DebugUtility.printMessage("** Saved game.");
	}

	/**
	 * loads game object from a default .SAV file
	 * 
	 * @throws IOException
	 */
	public static void loadGame() throws IOException {
		try (FileInputStream fout = new FileInputStream("resources/data/PokemonOrange.sav");
				ObjectInputStream oos = new ObjectInputStream(fout);) {
			GameData.setInstance((GameData) oos.readObject());
			DebugUtility.printMessage("** Loaded game from save.");
		} catch (Exception e1) {
			DebugUtility.printMessage("Unable to load game...\n" + e1.getLocalizedMessage());
		}
	}
}
