package driver;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;

import audio.AudioLibrary;
import graphics.MenuScene;
import graphics.NameScene;
import graphics.Painter;
import pokedex.Pokemon;
import pokedex.Pokemon.STATS;
import pokedex.PokemonFactory;
import pokedex.PokemonList;
import tiles.Tile;
import tiles.TileSet;
import trainers.Actor;
import trainers.Actor.DIR;
import trainers.NPCLibrary;
import utilities.BattleEngine;
import utilities.Coordinate;
import utilities.NPCThread;
import utilities.RandomNumUtils;

// ////////////////////////////////////////////////////////////////////////
//
// Main panel - holds the key listener, event handler, and graphics painter
// essentially controls all game flow logic and holds game data.
//
// ////////////////////////////////////////////////////////////////////////
public class Game extends JPanel implements ActionListener {

	private static final long serialVersionUID = 5951510422984321057L;

	// ================= Movement control variables =========================//
	boolean walking = false; // player animation counter
	private int movespritepixels = 0; // movement (animation) counter
	public boolean movable = true;
	boolean[] moveable_dir = new boolean[4]; // can move, each direction
	boolean rightFoot = false; // animation flag
	// ====================== NPC Random movement controller ================//
	public NPCThread NPCTHREAD = new NPCThread();
	// ===================== Graphics Logic Controllers =====================//
	public MenuScene menuScreen;
	public NameScene nameScreen;
	public EventHandler eventHandler;
	// ======================== Game logic Data =============================//
	public GameData gData = new GameData();
	public PokemonList enemyPkmn = new PokemonList();

	public String messageString;

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
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Any time an action is performed in the frame, this method is called.
	// In Pokemon game, this method is constantly listening for actions
	//
	// ////////////////////////////////////////////////////////////////////////
	public void actionPerformed(ActionEvent e) {

		gData.gameTimeStruct.updateTime(); // update the time played

		if (gData.inBattle && !gData.inMessage) {
			if (BattleEngine.getInstance().playerCurrentPokemon.getStat(STATS.HP) <= 0) {
				// TODO - check for other todo switch pokemon
				BattleEngine.getInstance().playerSwitchPokemon();
			}
			if (BattleEngine.getInstance().enemyPokemon.get(0).getStat(STATS.HP) <= 0) {
				// TODO - needs to check for all enemy pokemon health <= 0
				BattleEngine.getInstance().Win();
			}
			if (!BattleEngine.getInstance().playerTurn) {
				BattleEngine.getInstance().enemyTurn();
			}
		} else
			if (!gData.atTitle && !gData.inIntro && !gData.inNameScreen && !gData.inMenu && !gData.atContinueScreen) {
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
		DIR playerDir = gData.player.getDirection();
		PokemonList playerPokemon = gData.player.getPokemon();
		Coordinate playerPos = gData.player.tData.position;

		// check for collisions in each direction
		moveable_dir[DIR.NORTH.ordinal()] = gData.tm.canMoveInDir(playerPos, DIR.NORTH, gData) || gData.NOCLIP;
		moveable_dir[DIR.WEST.ordinal()] = gData.tm.canMoveInDir(playerPos, DIR.WEST, gData) || gData.NOCLIP;
		moveable_dir[DIR.SOUTH.ordinal()] = gData.tm.canMoveInDir(playerPos, DIR.SOUTH, gData) || gData.NOCLIP;
		moveable_dir[DIR.EAST.ordinal()] = gData.tm.canMoveInDir(playerPos, DIR.EAST, gData) || gData.NOCLIP;

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
		Map<Coordinate, Coordinate> dict = TeleportLibrary.getListofTeleports();
		Set<Coordinate> k = dict.keySet();
		boolean teleported = false;

		for (Coordinate x : k) {
			if (x.equals(playerPos)) {
				gData.player.setLoc(dict.get(x));

				gData.start_coorX = (gData.player.getCurrentX() - x.getX()) * -1 * Tile.TILESIZE;
				gData.start_coorY = (gData.player.getCurrentY() - x.getY()) * -1 * Tile.TILESIZE;
				teleported = true;
				gData.player.setDirection(DIR.NORTH);
			}
		}

		// finally, the animation is done, handle the movement logic
		if (movespritepixels >= 16 && !teleported) {
			movespritepixels = 0; // reset animation counter
			walking = false; // player is no longer in animation
			rightFoot = (!rightFoot);
			if (moveable_dir[playerDir.ordinal()]) {
				gData.player.move(playerDir);
				// check for wild encounter
				if (gData.tm.getTileAt(gData.player.getPosition()) == TileSet.WILD_TILE) {
					if (RandomNumUtils.isWildEncounter()) {
						enemyPkmn.clear();
						enemyPkmn.add(PokemonFactory.getInstance().randomPokemon(gData.player.getCurLoc()));
						BattleEngine.getInstance().fight(enemyPkmn, this, null);
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
			boolean beaten = gData.player.beatenTrainers.contains(curNPC.getName());
			if (curNPC.isTrainer() && !walking && !gData.NOBATTLE && !beaten && npcSeesPlayer(curNPC)
					&& !gData.inMenu) {
				NPCTHREAD.stop = true;
				enemyTrainerAnimation(curNPC);
				AudioLibrary.getInstance().playBackgroundMusic("TrainerBattle", gData.option_sound);
				BattleEngine.getInstance().fight(curNPC.getPokemon(), this, curNPC.getName());
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
	private boolean npcSeesPlayer(Actor curNPC) {
		int playerCurY = gData.player.getCurrentY();
		int playerCurX = gData.player.getCurrentX();
		int NPC_X = curNPC.getCurrentX();
		int NPC_Y = curNPC.getCurrentY();
		DIR NPC_DIR = curNPC.getDirection();

		return (((playerCurX == curNPC.getCurrentX()) && (((playerCurY < NPC_Y)
				&& (NPC_Y - playerCurY <= GameData.NPC_SIGHT_DISTANCE) && (NPC_DIR == DIR.NORTH))
				|| ((playerCurY > NPC_Y) && (playerCurY - NPC_Y <= GameData.NPC_SIGHT_DISTANCE)
						&& (NPC_DIR == DIR.SOUTH))))
				|| ((playerCurY == NPC_Y) && (((playerCurX < NPC_X)
						&& (NPC_X - playerCurX <= GameData.NPC_SIGHT_DISTANCE) && (NPC_DIR == DIR.WEST))
						|| ((playerCurX > NPC_X) && (playerCurX - NPC_X <= GameData.NPC_SIGHT_DISTANCE)
								&& (NPC_DIR == DIR.EAST)))));
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Upon NPC seeing player, move to confront player.
	//
	// TODO - NPC "!" upon beeing seen - right before Thread.sleep
	//
	// ////////////////////////////////////////////////////////////////////////
	private void enemyTrainerAnimation(Actor curNPC) {

		AudioLibrary.getInstance().pickTrainerMusic(gData.option_sound);

		try { // wait for ! before moving
			Thread.sleep(2000);
		} catch (InterruptedException e) {}

		// overhead preparation / calculations before moving NPC
		DIR NPC_DIR = curNPC.getDirection();
		int distToTravel = 0;

		if (NPC_DIR == DIR.NORTH) {
			gData.player.setDirection(DIR.SOUTH);
			distToTravel = (curNPC.getCurrentY() - (gData.player.getCurrentY() + 1));
		} else if (NPC_DIR == DIR.SOUTH) {
			gData.player.setDirection(DIR.NORTH);
			distToTravel = ((gData.player.getCurrentY() - 1) - curNPC.getCurrentY());
		} else if (NPC_DIR == DIR.EAST) {
			gData.player.setDirection(DIR.WEST);
			distToTravel = ((gData.player.getCurrentX() - 1) - curNPC.getCurrentX());
		} else if (NPC_DIR == DIR.WEST) {
			gData.player.setDirection(DIR.EAST);
			distToTravel = (curNPC.getCurrentX() - (gData.player.getCurrentX() + 1));
		}

		// until NPC reaches player, place tile, move NPC, repaint
		for (int x = 0; x < distToTravel; x++) {
			gData.tm.set(curNPC.tData.position, TileSet.NORMAL_TILE);
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

	public void reset() {
		gData.atContinueScreen = false;
		gData.atTitle = false;
		gData.inBattle = false;
		gData.inIntro = false;
		gData.inMenu = false;
		gData.inMessage = false;
		gData.inNameScreen = false;
		gData.introStage = 1;
		this.movable = true;
		this.walking = false;
	}
}