package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

import model.Coordinate;
import model.GameData.SCREEN;
import party.Party;
import party.PartyMember;
import party.PartyMember.STATS;
import tiles.TileSet;
import trainers.Actor;
import trainers.Actor.DIR;
import trainers.NPCLibrary;
import trainers.Player;
import utilities.BattleEngine;
import utilities.DebugUtility;
import utilities.RandomNumUtils;
import audio.AudioLibrary.SOUND_EFFECT;
import controller.EventController;
import controller.GameController;
import controller.GameKeyListener;

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
	public EventController eventHandler;

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
		try {
			game.loadMap();
		} catch (Exception e) {
			DebugUtility.error("Unable to load map.");
		}

		addKeyListener(kListener);
		eventHandler = new EventController(this);

		DebugUtility.printHeader("Event Registration");
		DebugUtility.printMessage("Added event handler.");
		DebugUtility.printMessage("Registered for events.");

		nameScreen = new NameScene();

		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(480, 320));

		DebugUtility.printMessage("Playing title music...");
		game.playBackgroundMusic("Title");
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// KeyListener override - the heart of the game driver
	//
	// Each menu / screen has buttons mapped to events
	//
	// ////////////////////////////////////////////////////////////////////////
	GameKeyListener kListener = new GameKeyListener() {
		private static final long serialVersionUID = 433796777156267003L;

		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();
			switch (game.getScreen()) {
			case TITLE: // title screen "press enter"
				if (keyCode == KeyEvent.VK_ENTER) {
					game.playBackgroundMusic("Continue");
					game.setScreen(SCREEN.CONTINUE);
				}
				break;
			case CONTINUE: // continue screen choice select
				if (keyCode == KeyEvent.VK_UP) {
					if (game.getCurrentSelection() > 0)
						game.decrementSelection();
				} else if (keyCode == KeyEvent.VK_DOWN) {
					if (game.getCurrentSelection() < 2)
						game.incrementSelection();
				}
				if (keyCode == KeyEvent.VK_Z) {
					if (game.getCurrentSelection() == 0) {
						game.startGame(true);
					} else if (game.getCurrentSelection() == 1) {
						game.startGame(false);
					}
					repaint();
					validate();
				}
				game.playClip(SOUND_EFFECT.SELECT);
				break;
			case INTRO: // intro screen, advance oak's text
				if (keyCode == KeyEvent.VK_Z) {
					game.incrIntroStage();
					if (game.getIntroStage() > NPCLibrary.getInstance().get("Professor Oak").getTextLength() - 1) {
						game.playBackgroundMusic("NewBarkTown");
						game.setScreen(SCREEN.WORLD);
					} else if (game.getIntroStage() == 15) {
						game.setScreen(SCREEN.NAME);
						nameScreen.setToBeNamed("PLAYER");
					}
				}
				break;
			case NAME:// name screen, add or remove chars
				if (keyCode == KeyEvent.VK_X)
					nameScreen.removeChar();
				if ((keyCode == KeyEvent.VK_Z)) {
					if (nameScreen.rowSelection == 5) {
						// check for end or del
						if (nameScreen.colSelection == 1 && nameScreen.getChosenName().length() > 0) {
							game.getPlayer().setName(nameScreen.getChosenName());
							nameScreen.reset();
							game.setScreen(SCREEN.INTRO);
						} else if (nameScreen.colSelection == 0)
							nameScreen.removeChar();
					} else {
						nameScreen.addSelectedChar();
					}
				}
				if (keyCode == KeyEvent.VK_DOWN && nameScreen.rowSelection < 5) {
					nameScreen.rowSelection++;
					if (nameScreen.rowSelection == 5 && nameScreen.colSelection < 3)
						nameScreen.colSelection = 0;
					if (nameScreen.rowSelection == 5 && nameScreen.colSelection >= 3)
						nameScreen.colSelection = 1;
				} else if (keyCode == KeyEvent.VK_UP && nameScreen.rowSelection > 0) {
					nameScreen.rowSelection--;
				} else if (keyCode == KeyEvent.VK_LEFT && nameScreen.colSelection > 0) {
					nameScreen.colSelection--;
				} else if (keyCode == KeyEvent.VK_RIGHT && nameScreen.rowSelection == 5 && nameScreen.colSelection < 1) {
					nameScreen.colSelection++;
				} else if (keyCode == KeyEvent.VK_RIGHT && nameScreen.rowSelection < 5 && nameScreen.colSelection < 5) {
					nameScreen.colSelection++;
				}
				break;
			case POKEMON:
			case BAG:
			case SAVE:
			case OPTION:
			case POKEDEX:
			case POKEGEAR:
			case TRAINERCARD:
			case MENU:
				eventHandler.handleMenuEvent(keyCode);
				break;
			case BATTLE:
			case BATTLE_FIGHT:
			case BATTLE_MESSAGE:
				eventHandler.handleBattleEvent(keyCode);
				break;
			case MESSAGE:
				eventHandler.handleMessageEvent(keyCode);
				break;
			default:
				// otherwise, fire the eventHandler
				if (game.isMovable() && !game.isInBattle() && !game.isPlayerWalking()) {
					eventHandler.handleWorldEvent(keyCode);
				}
			}
			repaint();
			validate();
		}

		public void keyReleased(KeyEvent e) {}

		public void keyTyped(KeyEvent e) {}
	};

	// ////////////////////////////////////////////////////////////////////////
	//
	// Any time an action is performed in the frame, this method is called.
	// In Pokemon game, this method is constantly listening for actions
	//
	// ////////////////////////////////////////////////////////////////////////
	public void actionPerformed(ActionEvent e) {

		game.updateTime();

		if (game.getScreen() == SCREEN.BATTLE || game.getScreen() == SCREEN.BATTLE_FIGHT) {
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
		Party playerPokemon = player.getPokemon();
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
						game.setWildPokemon();
						PartyMember wildP = game.getCurrentEnemy().get(0);
						game.setCurrentMessage("Wild " + wildP.getName() + " appeared.");
						game.doEncounter(game.getCurrentEnemy(), "");
					}
				}
			}
			for (PartyMember p : playerPokemon) { // deal PZN/BRN damage
				if ((p.getStatusEffect() == 2) || (p.getStatusEffect() == 3))
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
			game.setMapTileAt(curNPC.tData.position, TileSet.NORMAL);
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