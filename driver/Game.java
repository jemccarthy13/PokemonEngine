package driver;

import graphics.BattleScene;
import graphics.MenuScene;
import graphics.NPCThread;
import graphics.NameScene;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.Timer;

import pokedex.Pokemon;
import pokedex.PokemonList;
import tiles.Coordinate;
import trainers.NPC;
import utilities.EnumsAndConstants;
import utilities.EnumsAndConstants.DIR;
import utilities.EnumsAndConstants.MUSIC;
import utilities.GameData;
import utilities.Utils;

// ////////////////////////////////////////////////////////////////////////
//
// Main panel - holds the key listener, event handler, and graphics painter
// essentially controls all game flow logic and holds game data.
//
// ////////////////////////////////////////////////////////////////////////
public class Game extends JPanel implements KeyListener, ActionListener {

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// ======================== VARIABLE DECLARATIONS =======================//
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	// ================= Movement control variables =========================//
	boolean walking = false; // player animation counter
	private int movespritepixels = 0; // movement (animation) counter
	public boolean movable = true;
	boolean[] moveable_dir = new boolean[4]; // can move, each direction
	boolean rightFoot = false; // animation flag
	// ====================== NPC Random movement controller ================//
	public static NPCThread NPCTHREAD = new NPCThread();
	// ===================== Graphics Logic Controllers =====================//
	public BattleScene encounter;
	public MenuScene menuScreen;
	public NameScene nameScreen;
	public EventHandler eventHandler;
	// ======================== Game logic Data =============================//
	public GameData gData = new GameData();

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// ===================== END VARIABLE DECLARATIONS ======================//
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

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
	public Game() {
		eventHandler = new EventHandler(this);
		menuScreen = new MenuScene();
		nameScreen = new NameScene();

		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(480, 320));
		addKeyListener(this);

		// gameTimer sets the delay between events, based on PLAYER_SPEED
		gData.gameTimer = new Timer(100 - EnumsAndConstants.PLAYER_SPEED, this);
		gData.gameTimer.start();
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Any time an action is performed in the frame, this method is called.
	// In Pokemon game, this method is constantly listening for actions
	//
	// ////////////////////////////////////////////////////////////////////////
	public void actionPerformed(ActionEvent e) {

		gData.updateTime(); // update the time played

		if (gData.inBattle) {
			if (encounter.playerPokemon.getStat(EnumsAndConstants.STATS.HP) <= 0) {
				encounter.playerSwitchPokemon();
			}
			if (encounter.enemyPokemon.get(0).getStat(EnumsAndConstants.STATS.HP) <= 0) {
				encounter.Win();
			}
			if (!encounter.playerTurn) {
				encounter.enemyTurn();
			}
		} else if (!gData.atTitle && !gData.inIntro && !gData.inNameScreen && !gData.inMenu && !gData.atContinueScreen) {
			handleMovement();
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Handles player movement on keyboard input
	//
	// ////////////////////////////////////////////////////////////////////////
	private void handleMovement() {
		// get all comparison variables up front
		int playerCurX = gData.player.getCurrentX();
		int playerCurY = gData.player.getCurrentY();
		DIR playerDir = gData.player.getDir();
		PokemonList playerPokemon = gData.player.getPokemon();
		Coordinate playerPos = gData.player.tData.position;

		// check for collisions in each direction
		moveable_dir[DIR.NORTH.ordinal()] = gData.tm.canMoveInDir(playerPos, DIR.NORTH, gData) || gData.noClip;
		moveable_dir[DIR.WEST.ordinal()] = gData.tm.canMoveInDir(playerPos, DIR.WEST, gData) || gData.noClip;
		moveable_dir[DIR.SOUTH.ordinal()] = gData.tm.canMoveInDir(playerPos, DIR.SOUTH, gData) || gData.noClip;
		moveable_dir[DIR.EAST.ordinal()] = gData.tm.canMoveInDir(playerPos, DIR.EAST, gData) || gData.noClip;

		if (walking) { // take care of walking animation graphics logic
			movespritepixels += 1;
			gData.offsetY = ((playerDir == DIR.NORTH) && (moveable_dir[DIR.NORTH.ordinal()])) ? gData.offsetY + 2
					: gData.offsetY;
			gData.offsetY = ((playerDir == DIR.SOUTH) && (moveable_dir[DIR.SOUTH.ordinal()])) ? gData.offsetY - 2
					: gData.offsetY;
			gData.offsetX = ((playerDir == DIR.WEST) && (moveable_dir[DIR.WEST.ordinal()])) ? gData.offsetX + 2
					: gData.offsetX;
			gData.offsetX = ((playerDir == DIR.EAST) && (moveable_dir[DIR.EAST.ordinal()])) ? gData.offsetX - 2
					: gData.offsetX;

			gData.player.changeSprite(movespritepixels, rightFoot);
		}

		// prep for door teleportation event check
		Map<Coordinate, Coordinate> dict = EnumsAndConstants.TELEPORTS.getListofTeleports();
		Set<Coordinate> k = dict.keySet();
		boolean teleported = false;

		for (Coordinate x : k) {
			if (x.equals(playerPos)) {
				gData.player.setLoc(dict.get(x));

				gData.start_coorX = (gData.player.getCurrentX() - x.getX()) * -1 * EnumsAndConstants.TILESIZE;
				gData.start_coorY = (gData.player.getCurrentY() - x.getY()) * -1 * EnumsAndConstants.TILESIZE;
				teleported = true;
				gData.player.setDir(EnumsAndConstants.DIR.NORTH);
			}
		}

		// finally, the animation is done, handle the movement logic
		if (movespritepixels >= 16 && !teleported) {
			movespritepixels = 0; // reset animation counter
			walking = false; // player is no longer in animation
			rightFoot = (!rightFoot);
			if (moveable_dir[playerDir.ordinal()])
				gData.player.move(playerDir);
			for (Pokemon p : playerPokemon) { // deal PZN/BRN damage
				if ((p.statusEffect == 2) || (p.statusEffect == 3))
					p.doDamage(1);
			}
		}

		// check for trainer encounter with any NPC
		for (NPC curNPC : EnumsAndConstants.npc_lib.npcs) {
			boolean beaten = gData.player.beatenTrainers.contains(curNPC.getName());
			if (curNPC.isTrainer() && !walking && !gData.noBattle && !beaten && npcSeesPlayer(curNPC) && !gData.inMenu) {
				NPCTHREAD.stop = true;
				enemyTrainerAnimation(curNPC);
				doTrainerBattle(curNPC);
			} else {
				movable = true;
			}
		}
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
	private boolean npcSeesPlayer(NPC curNPC) {
		int playerCurY = gData.player.getCurrentY();
		int playerCurX = gData.player.getCurrentX();
		int NPC_X = curNPC.getCurrentX();
		int NPC_Y = curNPC.getCurrentY();
		DIR NPC_DIR = curNPC.getDirection();

		return (((playerCurX == curNPC.getCurrentX()) && (((playerCurY < NPC_Y)
				&& (NPC_Y - playerCurY <= EnumsAndConstants.NPC_SIGHT_DISTANCE) && (NPC_DIR == DIR.NORTH)) || ((playerCurY > NPC_Y)
				&& (playerCurY - NPC_Y <= EnumsAndConstants.NPC_SIGHT_DISTANCE) && (NPC_DIR == DIR.SOUTH)))) || ((playerCurY == NPC_Y) && (((playerCurX < NPC_X)
				&& (NPC_X - playerCurX <= EnumsAndConstants.NPC_SIGHT_DISTANCE) && (NPC_DIR == DIR.WEST)) || ((playerCurX > NPC_X)
				&& (playerCurX - NPC_X <= EnumsAndConstants.NPC_SIGHT_DISTANCE) && (NPC_DIR == DIR.EAST)))));
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Upon NPC seeing player, move to confront player.
	//
	// TODO - NPC "!" upon beeing seen - right before Thread.sleep
	//
	// ////////////////////////////////////////////////////////////////////////
	private void enemyTrainerAnimation(NPC curNPC) {

		Utils.pickTrainerMusic(); // pick random "I see you" music

		try { // wait for ! before moving
			Thread.sleep(2000);
		} catch (InterruptedException e) {}

		// overhead preparation / calculations before moving NPC
		DIR NPC_DIR = curNPC.getDirection();
		int distToTravel = 0;

		if (NPC_DIR == DIR.NORTH) {
			gData.player.setSpriteFacing(DIR.SOUTH);
			distToTravel = (curNPC.getCurrentY() - (gData.player.getCurrentY() + 1));
		} else if (NPC_DIR == DIR.SOUTH) {
			gData.player.setSpriteFacing(DIR.NORTH);
			distToTravel = ((gData.player.getCurrentY() - 1) - curNPC.getCurrentY());
		} else if (NPC_DIR == DIR.EAST) {
			gData.player.setSpriteFacing(DIR.WEST);
			distToTravel = ((gData.player.getCurrentX() - 1) - curNPC.getCurrentX());
		} else if (NPC_DIR == DIR.WEST) {
			gData.player.setSpriteFacing(DIR.EAST);
			distToTravel = (curNPC.getCurrentX() - (gData.player.getCurrentX() + 1));
		}

		// until NPC reaches player, place tile, move NPC, repaint
		for (int x = 0; x < distToTravel; x++) {
			gData.tm.set(curNPC.tData.position, EnumsAndConstants.TILE);
			curNPC.move(NPC_DIR);
			paintComponent(getGraphics());
			try {
				Thread.sleep(500);
			} catch (InterruptedException localInterruptedException) {}
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// After seeing player, after moving to confront, actually do the battle
	//
	// ////////////////////////////////////////////////////////////////////////
	public void doTrainerBattle(NPC curNPC) {
		movable = false;
		Utils.playBackgroundMusic(MUSIC.TRAINER_BATTLE);
		encounter = new BattleScene(this, curNPC);
		gData.inBattle = true;
		encounter.Start();
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Paint component - calls PAINTER and refershes screen
	//
	// ////////////////////////////////////////////////////////////////////////
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		EnumsAndConstants.PAINTER.paintComponent(g, this);
		repaint();
		validate();
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// KeyListener override - the heart of the game driver
	//
	// Each menu / screen has buttons mapped to events
	// TODO - fix audio errors (SE_SELECT) at continue and title screen
	//
	// ////////////////////////////////////////////////////////////////////////
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (gData.atTitle) {
			// title screen "press enter"
			if (keyCode == KeyEvent.VK_ENTER) {
				gData.atTitle = false;
				Utils.playBackgroundMusic(MUSIC.CONTINUE);
				gData.atContinueScreen = true;
			}
		} else if (gData.atContinueScreen) {
			// continue screen choice select
			if (keyCode == KeyEvent.VK_UP) {
				if (gData.menuSelection > 0)
					gData.menuSelection -= 1;
			} else if (keyCode == KeyEvent.VK_DOWN) {
				if (gData.menuSelection < 2)
					gData.menuSelection += 1;
			}
			if (keyCode == KeyEvent.VK_Z) {
				if (gData.menuSelection == 0) {
					GameInitializer.startGame(true, this);
				} else if (gData.menuSelection == 1) {
					GameInitializer.startGame(false, this);
				}
			}
			EnumsAndConstants.col.playClip("SELECT", gData.option_sound);
		} else if (gData.inIntro) {
			// intro screen, advance oak's text
			if (keyCode == KeyEvent.VK_X)
				nameScreen.removeChar();
			if (keyCode == KeyEvent.VK_Z) {
				gData.introStage += 2;
				if (gData.introStage > EnumsAndConstants.npc_lib.getNPC("Professor Oak").getTextLength() - 1) {
					Utils.playBackgroundMusic(MUSIC.NEWBARKTOWN);
					gData.inIntro = !gData.inIntro;
				} else if (gData.introStage == 15) {
					gData.inNameScreen = true;
					nameScreen.setToBeNamed("PLAYER");
					gData.inIntro = false;
				}
			}
		} else if (gData.inNameScreen) {
			// name screen, add or remove chars
			if (keyCode == KeyEvent.VK_X)
				nameScreen.removeChar();
			if ((keyCode == KeyEvent.VK_Z)) {
				if (nameScreen.rowSelection == 5) {
					// check for end or del
					if (nameScreen.colSelection == 1 && nameScreen.getChosenName().length() > 0) {
						gData.player.setName(nameScreen.getChosenName());
						nameScreen.reset();
						gData.inNameScreen = false;
						gData.inIntro = true;
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
		} else {
			// otherwise, fire the eventHandler
			if ((!gData.inMenu) && (movable) && (!gData.inBattle) && (!walking)) {
				eventHandler.handleWorldEvent(keyCode);
			} else if (gData.inMenu) {
				eventHandler.handleMenuEvent(keyCode);
			} else if (gData.inBattle) {
				eventHandler.handleBattleEvent(keyCode);
			}
		}
		repaint();
		validate();
	}

	public void keyReleased(KeyEvent e) {}

	public void keyTyped(KeyEvent e) {}

}