package driver;

import graphics.NameScene;
import graphics.Painter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import pokedex.Pokemon;
import pokedex.Pokemon.STATS;
import pokedex.PokemonList;
import tiles.TileSet;
import trainers.Actor;
import trainers.Actor.DIR;
import trainers.NPCLibrary;
import trainers.Player;
import utilities.BattleEngine;
import utilities.Coordinate;
import utilities.DebugUtility;
import utilities.RandomNumUtils;
import driver.GameData.SCREEN;

// ////////////////////////////////////////////////////////////////////////
//
// Main panel - holds the key listener, event handler, and graphics painter
// essentially controls all game flow logic and holds game data.
//
// ////////////////////////////////////////////////////////////////////////
public class GamePanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 5951510422984321057L;

	// ================== Display control variables =========================//
	private int animationStep = 0; // movement (animation) counter
	private boolean isRightFoot = false; // animation flag
	public String messageString;

	// ===================== Graphics Logic Controllers =====================//
	public NameScene nameScreen;
	public EventHandler eventHandler;

	// ===================== Game logic controller ==========================//

	public GameController game = new GameController();

	// ////////////////////////////////////////////////////////////////////////
	//
	// Default constructor for Main panel. This is the panel that all
	// aspects of the game are painted to / from.
	//
	// Constructor initializes event handler, all the scenes that get
	// painted during gameplay, and the timer that handles sprite movement
	// speeds. Also sets up input controller (key listener).
	//
	// ////////////////////////////////////////////////////////////////////////
	public GamePanel() {
		eventHandler = new EventHandler(this);
		nameScreen = new NameScene();

		try {
			game.loadMap();
		} catch (Exception e) {
			DebugUtility.error("Unable to load map.");
		}
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(480, 320));

		DebugUtility.printMessage("Playing title music...");
		game.playBackgroundMusic("Title");
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Any time an action is performed in the frame, this method is called.
	// In Pokemon game, this method is constantly listening for actions
	//
	// ////////////////////////////////////////////////////////////////////////
	public void actionPerformed(ActionEvent e) {

		game.updateTime();

		if (game.isInBattle() && game.getScreen() != SCREEN.MESSAGE) {
			if (BattleEngine.getInstance().playerCurrentPokemon.getStat(STATS.HP) <= 0) {
				// TODO - player switch pokemon
				BattleEngine.getInstance().playerSwitchPokemon();
			}
			if (BattleEngine.getInstance().enemyPokemon.get(0).getStat(STATS.HP) <= 0) {
				// TODO - needs to check for all enemy pokemon health <= 0
				BattleEngine.getInstance().Win();
			}
			if (!BattleEngine.getInstance().playerTurn) {
				BattleEngine.getInstance().enemyTurn();
			}
		} else if (game.getScreen() == SCREEN.WORLD) {
			handleMovement();
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Handles player movement on keyboard input
	//
	// ////////////////////////////////////////////////////////////////////////
	private void handleMovement() {
		Player player = game.getPlayer();
		// get all comparison variables up front
		DIR playerDir = player.getDirection();
		PokemonList playerPokemon = player.getPokemon();
		Coordinate playerPos = player.tData.position;

		if (game.isPlayerWalking()) { // take care of walking animation
			// as the gameTimer increments,
			animationStep += 1;
			game.setOffsetY(playerDir);
			game.setOffsetX(playerDir);

			player.changeSprite(animationStep, isRightFoot);
		}

		// check for teleport at location
		// if no teleport, check for animation completion
		// when walking animation is done, handle poison damage
		if (game.isTeleportTile(playerPos)) {
			game.doTeleport(playerPos);
		} else if (animationStep >= 16) {
			animationStep = 0; // reset animation counter
			game.setPlayerWalking(false); // player is no longer in animation
			isRightFoot = (!isRightFoot);
			if (game.canMoveInDir(playerDir)) {
				player.move(playerDir);
				// check for wild encounter
				if (game.isBattleTile(player.getPosition())) {
					if (RandomNumUtils.isWildEncounter()) {
						game.doWildEncounter();
					}
				}
			}
			for (Pokemon p : playerPokemon) { // deal PZN/BRN damage
				if ((p.statusEffect == 2) || (p.statusEffect == 3))
					p.doDamage(1);
			}
		}

		// check for trainer encounter with any NPC
		for (Actor curNPC : NPCLibrary.getInstance().values()) {
			if (game.validEncounterConditions(curNPC)) {
				game.stopNPCMovement();
				enemyTrainerAnimation(curNPC);
				game.playBackgroundMusic("TrainerBattle");
				game.doEncounter(curNPC.getPokemon(), curNPC.getName());
			} else {
				game.setMovable(true);
			}
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Upon NPC seeing player, move to confront player.
	//
	// TODO - NPC "!" upon beeing seen - right before Thread.sleep
	//
	// ////////////////////////////////////////////////////////////////////////
	private void enemyTrainerAnimation(Actor curNPC) {

		game.playTrainerMusic();

		try { // wait for ! before moving
			Thread.sleep(2000);
		} catch (InterruptedException e) {}

		// overhead preparation / calculations before moving NPC
		DIR NPC_DIR = curNPC.getDirection();
		int distToTravel = 0;

		Player player = game.getPlayer();

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
			game.setMapTileAt(curNPC.tData.position, TileSet.NORMAL_TILE);
			curNPC.move(NPC_DIR);
			paintComponent(getGraphics());
			try {
				Thread.sleep(500);
			} catch (InterruptedException localInterruptedException) {}
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Paint component - calls PAINTER and refershes screen
	//
	// ////////////////////////////////////////////////////////////////////////
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Painter.paintComponent(g, this);
		repaint();
		validate();
	}
}