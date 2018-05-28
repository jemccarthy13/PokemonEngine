package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.RepaintManager;

import audio.AudioLibrary;
import controller.BattleEngine;
import controller.GameController;
import controller.GameKeyListener;
import model.Coordinate;
import model.GameTime;
import scenes.WorldScene;
import tiles.TileSet;
import trainers.Actor;
import trainers.Actor.DIR;
import trainers.NPCLibrary;
import trainers.Player;
import utilities.DebugUtility;

/**
 * Holds the actionlistener. This is where the graphics are pained
 */
public class GamePanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 5951510422984321057L;

	private static GamePanel instance = new GamePanel();

	public static GamePanel getInstance() {
		RepaintManager.currentManager(instance).setDoubleBufferingEnabled(true);
		return instance;
	}

	// ================== Display control variables =========================//
	/**
	 * The current message for display
	 */
	public String displayMessage;

	// ===================== Game logic controller ==========================//
	/**
	 * Single point of access for control of the game - abstraction between the view
	 * and the data (model)
	 */
	public GameController gameController = new GameController();

	/**
	 * Default constructor for Main panel. This is the panel that all aspects of the
	 * game are painted to.
	 * 
	 * Constructor loads the map, registeres to listen for key events, and starts
	 * the title music (sets up the start of gameplay)
	 */
	public GamePanel() {

		try {
			GameMap.getInstance().loadMap(gameController);
		} catch (IOException | InterruptedException e) {
			DebugUtility.printError("Unable to load map!");
		}
		// setup key press listening
		GameKeyListener.getInstance().setGameController(gameController);
		addKeyListener(GameKeyListener.getInstance());

		DebugUtility.printHeader("Event Registration");
		DebugUtility.printMessage("Added event handler.");
		DebugUtility.printMessage("Registered for events.");

		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(465, 305));

		DebugUtility.printMessage("Playing title music...");
		AudioLibrary.playBackgroundMusic("Title");
	}

	/**
	 * Any time an action is performed in the frame, this method updates the time
	 * and handles world actions.
	 */
	public void actionPerformed(ActionEvent e) {
		GameTime.getInstance().updateTime();
		if (GameGraphicsData.getInstance().getScene() == WorldScene.instance) {
			// get all comparison variables up front
			Player player = gameController.getPlayer();
			Coordinate playerPos = player.getPosition();

			// check for teleport at location
			if (GameMap.getInstance().isTeleportAt(playerPos)) {
				gameController.doTeleport(playerPos);
			} else {
				// Party playerPokemon = player.getParty();
				player.doAnimation(gameController);
				if (player.hasMoved()) {
					gameController.postMovementChecks();
				}
			}
		}
		checkForNPCEncounter();
	}

	/**
	 * Checks if the player is in sight range of any NPC and the NPC is looking at
	 * the player and the player's walking animation is finished
	 */
	private void checkForNPCEncounter() {
		// check for trainer encounter with any NPC
		for (Actor curNPC : NPCLibrary.getInstance().values()) {
			if (gameController.validEncounterConditions(curNPC)) {
				NPCThread.getInstance().stopMoving();
				enemyTrainerAnimation(curNPC);
				AudioLibrary.playBackgroundMusic("TrainerBattle");
				BattleEngine.getInstance().fight(curNPC.getParty(), gameController, curNPC.getName());
			} else {
				if (gameController.getPlayer() != null) {
					gameController.getPlayer().canMove = true;
				}
			}
		}
	}

	/**
	 * When an NPC sees the player, move to confront them
	 * 
	 * @param curNPC
	 *            - the enemy NPC
	 */
	private void enemyTrainerAnimation(Actor curNPC) {

		AudioLibrary.getInstance().pickTrainerMusic();

		try {
			// ! painting on initial eyesight
			Painter.paintTrainerSighted(getGraphics(), gameController, curNPC.getPosition());
			Thread.sleep(1500);
		} catch (InterruptedException e) {}

		DIR NPC_DIR = curNPC.getDirection();
		int distToTravel = 0;

		Player player = gameController.getPlayer();

		if (NPC_DIR == DIR.NORTH) {
			player.setDirection(DIR.SOUTH);
			distToTravel = (curNPC.getCurrentY() - (player.getCurrentY() + 1));
		} else if (NPC_DIR == DIR.SOUTH) {
			player.setDirection(DIR.NORTH);
			distToTravel = ((player.getCurrentY() - 1) - curNPC.getCurrentY());
		} else if (NPC_DIR == DIR.EAST) {
			player.setDirection(DIR.WEST);
			distToTravel = ((player.getCurrentX() - 1) - curNPC.getCurrentX());
		} else if (NPC_DIR == DIR.WEST) {
			player.setDirection(DIR.EAST);
			distToTravel = (curNPC.getCurrentX() - (player.getCurrentX() + 1));
		}

		// until NPC reaches player, place a normal tile, move NPC, repaint
		for (int x = 0; x < distToTravel; x++) {
			GameMap.getInstance().setMapTileAt(curNPC.tData.position, TileSet.NORMAL);

			curNPC.setDirection(NPC_DIR);
			curNPC.moveDir(NPC_DIR);
		}
	}

	/**
	 * Calls the painter to paint it's graphics, refreshes the screen
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Painter.paintComponent(g, this);
		repaint();
		validate();
	}
}