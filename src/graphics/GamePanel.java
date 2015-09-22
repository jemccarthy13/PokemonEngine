package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import model.Coordinate;
import model.GameData.SCREEN;
import party.Battler;
import party.Battler.STATUS;
import party.Party;
import tiles.TileSet;
import trainers.Actor;
import trainers.Actor.DIR;
import trainers.NPCLibrary;
import trainers.Player;
import utilities.DebugUtility;
import utilities.RandomNumUtils;
import controller.GameController;
import controller.GameKeyListener;

/**
 * Holds the actionlistener. This is where the graphics are pained
 */
public class GamePanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 5951510422984321057L;

	// ================== Display control variables =========================//
	private int animationStep = 0; // movement (animation) counter
	private boolean isRightFoot = false; // animation flag
	/**
	 * The current message for display
	 */
	public String displayMessage;

	// ===================== Game logic controller ==========================//
	/**
	 * Single point of access for control of the game - abstraction between the
	 * view and the data (model)
	 */
	public GameController gameController = new GameController();

	/**
	 * Default constructor for Main panel. This is the panel that all aspects of
	 * the game are painted to.
	 * 
	 * Constructor loads the map, registeres to listen for key events, and
	 * starts the title music (sets up the start of gameplay)
	 */
	public GamePanel() {
		try {
			gameController.loadMap();
		} catch (Exception e) {
			DebugUtility.error("Unable to load map.");
		}

		addKeyListener(new GameKeyListener(gameController));

		DebugUtility.printHeader("Event Registration");
		DebugUtility.printMessage("Added event handler.");
		DebugUtility.printMessage("Registered for events.");

		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(480, 320));

		DebugUtility.printMessage("Playing title music...");
		gameController.playBackgroundMusic("Title");
	}

	/**
	 * Any time an action is performed in the frame, this method updates the
	 * time and handles world actions.
	 */
	public void actionPerformed(ActionEvent e) {
		gameController.updateTime();
		if (gameController.getScreen() == SCREEN.WORLD) {
			handleMovement();
		}
	}

	/**
	 * Handles player movement & animation on keyboard input
	 */
	private void handleMovement() {
		// get all comparison variables up front
		Player player = gameController.getPlayer();
		DIR playerDir = player.getDirection();
		Coordinate playerPos = player.getPosition();

		// check for teleport at location
		if (gameController.isTeleportTile(playerPos)) {
			gameController.doTeleport(playerPos);
		} else {
			Party playerPokemon = player.getParty();

			if (gameController.isPlayerWalking()) { // take care of walking
													// animation
				// as the gameTimer increments,
				animationStep += 1;
				gameController.setOffsetY(playerDir);
				gameController.setOffsetX(playerDir);

				player.changeSprite(animationStep, isRightFoot);
			}

			// check for animation completion
			// when walking animation is done, handle poison damage
			if (animationStep >= 16) {
				animationStep = 0; // reset animation counter
				gameController.setPlayerWalking(false); // player is no longer
														// in
				// animation
				isRightFoot = !isRightFoot;
				if (gameController.canMoveInDir(playerDir)) {
					player.move(playerDir);
					checkForWildEncounter(player.getPosition());
				}
				checkForStatusDamage(playerPokemon);
			}

		}
		checkForNPCEncounter();
	}

	/**
	 * On movement, if any party member has a harmful status effect, do damage
	 * 
	 * @param party
	 */
	private void checkForStatusDamage(Party party) {
		for (Battler p : party) {
			STATUS partyStatus = p.getStatusEffect();
			if (partyStatus == STATUS.BRN || partyStatus == STATUS.PZN)
				p.doDamage(1);
		}
	}

	/**
	 * Checks the current tile for a wild encounter
	 * 
	 * @param position
	 *            - the map position to check
	 */
	private void checkForWildEncounter(Coordinate position) {
		if (gameController.isBattleTile(position)) {
			if (RandomNumUtils.isWildEncounter()) {
				gameController.setWildPokemon();
				Battler wildP = gameController.getCurrentEnemy().get(0);
				gameController.setCurrentMessage("Wild " + wildP.getName() + " appeared.");
				gameController.doEncounter(gameController.getCurrentEnemy(), null);
			}
		}
	}

	/**
	 * Checks if the player is in sight range of any NPC and the NPC is looking
	 * at the player and the player's walking animation is finished
	 */
	private void checkForNPCEncounter() {
		// check for trainer encounter with any NPC
		for (Actor curNPC : NPCLibrary.getInstance().values()) {
			if (gameController.validEncounterConditions(curNPC)) {
				gameController.pauseNPCMovement();
				enemyTrainerAnimation(curNPC);
				gameController.playBackgroundMusic("TrainerBattle");
				gameController.doEncounter(curNPC.getParty(), curNPC.getName());
			} else {
				gameController.setMovable(true);
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

		gameController.playTrainerMusic();

		try {
			// TODO - ! needs to be pained here
			Thread.sleep(2000);
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
			gameController.setMapTileAt(curNPC.tData.position, TileSet.NORMAL);
			curNPC.move(NPC_DIR);
			paintComponent(getGraphics());
			try {
				Thread.sleep(500);
			} catch (InterruptedException localInterruptedException) {}
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