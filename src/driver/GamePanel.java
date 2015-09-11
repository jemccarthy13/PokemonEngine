package driver;

import graphics.MenuScene;
import graphics.NameScene;
import graphics.Painter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;

import pokedex.Pokemon;
import pokedex.Pokemon.STATS;
import pokedex.PokemonFactory;
import pokedex.PokemonList;
import tiles.Tile;
import tiles.TileSet;
import tiles.WildTile;
import trainers.Actor;
import trainers.Actor.DIR;
import trainers.NPCLibrary;
import trainers.Player;
import utilities.BattleEngine;
import utilities.Coordinate;
import utilities.RandomNumUtils;

// ////////////////////////////////////////////////////////////////////////
//
// Main panel - holds the key listener, event handler, and graphics painter
// essentially controls all game flow logic and holds game data.
//
// ////////////////////////////////////////////////////////////////////////
public class GamePanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 5951510422984321057L;

	// ================= Movement control variables =========================//
	private int movespritepixels = 0; // movement (animation) counter
	boolean[] moveable_dir = new boolean[4]; // can move, each direction
	boolean rightFoot = false; // animation flag

	// ===================== Graphics Logic Controllers =====================//
	public MenuScene menuScreen;
	public NameScene nameScreen;
	public EventHandler eventHandler;
	// ======================== Game logic Data =============================//
	public GameController game = new GameController(new GameData());
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
	public GamePanel() {
		eventHandler = new EventHandler(this);
		menuScreen = new MenuScene();
		nameScreen = new NameScene();

		try {
			game.loadMap("NewBarkTown");
		} catch (Exception e) {
			System.err.println("Unable to load map.");
		}
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(480, 320));

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
		Player player = game.getPlayer();
		// get all comparison variables up front
		DIR playerDir = player.getDirection();
		PokemonList playerPokemon = player.getPokemon();
		Coordinate playerPos = player.tData.position;

		// check for collisions in each direction
		moveable_dir[DIR.NORTH.ordinal()] = game.canMoveInDir(DIR.NORTH) || game.isNoClip();
		moveable_dir[DIR.WEST.ordinal()] = game.canMoveInDir(DIR.WEST) || game.isNoClip();
		moveable_dir[DIR.SOUTH.ordinal()] = game.canMoveInDir(DIR.SOUTH) || game.isNoClip();
		moveable_dir[DIR.EAST.ordinal()] = game.canMoveInDir(DIR.EAST) || game.isNoClip();

		if (game.isPlayerWalking()) { // take care of walking animation
			// graphics logic
			movespritepixels += 1;
			gData.offsetY = ((playerDir == DIR.NORTH) && (moveable_dir[DIR.NORTH.ordinal()])) ? gData.offsetY + 2
					: gData.offsetY;
			gData.offsetY = ((playerDir == DIR.SOUTH) && (moveable_dir[DIR.SOUTH.ordinal()])) ? gData.offsetY - 2
					: gData.offsetY;
			gData.offsetX = ((playerDir == DIR.WEST) && (moveable_dir[DIR.WEST.ordinal()])) ? gData.offsetX + 2
					: gData.offsetX;
			gData.offsetX = ((playerDir == DIR.EAST) && (moveable_dir[DIR.EAST.ordinal()])) ? gData.offsetX - 2
					: gData.offsetX;

			player.changeSprite(movespritepixels, rightFoot);
		}

		// prep for door teleportation event check
		Map<Coordinate, Coordinate> dict = TeleportLibrary.getListofTeleports();
		Set<Coordinate> k = dict.keySet();
		boolean teleported = false;

		for (Coordinate x : k) {
			if (x.equals(playerPos)) {
				player.setLoc(dict.get(x));

				gData.start_coorX = (player.getCurrentX() - x.getX()) * -1 * Tile.TILESIZE;
				gData.start_coorY = (player.getCurrentY() - x.getY()) * -1 * Tile.TILESIZE;
				teleported = true;
				player.setDirection(DIR.NORTH);
			}
		}

		// finally, the animation is done, handle the movement logic
		if (movespritepixels >= 16 && !teleported) {
			movespritepixels = 0; // reset animation counter
			game.setPlayerWalking(false); // player is no longer in animation
			rightFoot = (!rightFoot);
			if (moveable_dir[playerDir.ordinal()]) {
				player.move(playerDir);
				// check for wild encounter
				if (game.getMapTileAt(player.getPosition()).equals(WildTile.name)) {
					if (RandomNumUtils.isWildEncounter()) {
						enemyPkmn.clear();
						enemyPkmn.add(PokemonFactory.getInstance().randomPokemon(player.getCurLoc()));
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
			boolean beaten = game.getPlayer().beatenTrainers.contains(curNPC.getName());
			if (curNPC.isTrainer() && !game.isPlayerWalking() && game.doBattles() && !beaten && npcSeesPlayer(curNPC)
					&& !gData.inMenu) {
				game.stopNPCMovement();
				enemyTrainerAnimation(curNPC);
				game.playBackgroundMusic("TrainerBattle");
				BattleEngine.getInstance().fight(curNPC.getPokemon(), this, curNPC.getName());
			} else {
				game.setMovable(true);
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
		Player player = game.getPlayer();
		int playerCurY = player.getCurrentY();
		int playerCurX = player.getCurrentX();
		int NPC_X = curNPC.getCurrentX();
		int NPC_Y = curNPC.getCurrentY();
		DIR NPC_DIR = curNPC.getDirection();

		return (((playerCurX == curNPC.getCurrentX()) && (((playerCurY < NPC_Y)
				&& (NPC_Y - playerCurY <= Configuration.NPC_SIGHT_DISTANCE) && (NPC_DIR == DIR.NORTH)) || ((playerCurY > NPC_Y)
				&& (playerCurY - NPC_Y <= Configuration.NPC_SIGHT_DISTANCE) && (NPC_DIR == DIR.SOUTH)))) || ((playerCurY == NPC_Y) && (((playerCurX < NPC_X)
				&& (NPC_X - playerCurX <= Configuration.NPC_SIGHT_DISTANCE) && (NPC_DIR == DIR.WEST)) || ((playerCurX > NPC_X)
				&& (playerCurX - NPC_X <= Configuration.NPC_SIGHT_DISTANCE) && (NPC_DIR == DIR.EAST)))));
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

	public void reset() {
		gData.atContinueScreen = false;
		gData.atTitle = false;
		gData.inBattle = false;
		gData.inIntro = false;
		gData.inMenu = false;
		gData.inMessage = false;
		gData.inNameScreen = false;
		gData.introStage = 1;
		game.setMovable(true);
		game.setPlayerWalking(false);
	}
}