package graphics;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.Random;

import pokedex.Move;
import pokedex.Pokemon;
import pokedex.PokemonList;
import trainers.NPC;
import utilities.EnumsAndConstants;
import utilities.EnumsAndConstants.STATS;
import utilities.Utils;
import driver.Game;

public class BattleScene {
	private Game game;
	public boolean playerTurn;
	public int elapsedTurns;
	public boolean inMain = true;
	public boolean inFight = false;
	public boolean inItem = false;
	public boolean inPokemon = false;
	public boolean inRun = false;
	public boolean playerWon = false;
	public boolean pokemonfainted = false;
	public boolean confirmBattleEnd = false;
	public int currentSelectionMainX;
	public int currentSelectionMainY;
	public int currentSelectionFightX;
	public int currentSelectionFightY;
	public Pokemon playerPokemon;
	public PokemonList enemyPokemon;
	public NPC enemy = null;
	Image BG = Toolkit.getDefaultToolkit().getImage(BattleScene.class.getResource("/graphics_lib/Pictures/BG.png"));
	Image battleMainBG = Toolkit.getDefaultToolkit().getImage(
			BattleScene.class.getResource("/graphics_lib/Pictures/Battle.png"));
	Image battleFightBG = Toolkit.getDefaultToolkit().getImage(
			BattleScene.class.getResource("/graphics_lib/Pictures/Battle2.png"));
	Image statusPAR = Toolkit.getDefaultToolkit().getImage(
			BattleScene.class.getResource("/graphics_lib/Pictures/StatusPAR.png"));
	Image statusBRN = Toolkit.getDefaultToolkit().getImage(
			BattleScene.class.getResource("/graphics_lib/Pictures/StatusBRN.png"));
	Image statusPSN = Toolkit.getDefaultToolkit().getImage(
			BattleScene.class.getResource("/graphics_lib/Pictures/StatusPSN.png"));
	Image statusSLP = Toolkit.getDefaultToolkit().getImage(
			BattleScene.class.getResource("/graphics_lib/Pictures/StatusSLP.png"));
	Image statusFRZ = Toolkit.getDefaultToolkit().getImage(
			BattleScene.class.getResource("/graphics_lib/Pictures/StatusFRZ.png"));

	public BattleScene(Game pkmnGame, NPC curNPC) {
		game = pkmnGame;
		playerPokemon = pkmnGame.gData.player.getPokemon().get(0);
		playerPokemon.setParticipated();
		enemyPokemon = curNPC.getPokemon();
		enemy = curNPC;
		playerTurn = true;
	}

	public void Start() {
		this.currentSelectionMainX = 0;
		this.currentSelectionFightX = 0;
		this.currentSelectionMainY = 0;
		this.currentSelectionFightY = 0;
		this.inMain = true;
	}

	public void Fight() {
		this.inMain = false;
		this.inFight = true;
	}

	public void Item() {
		this.inItem = true;
		System.out.println("Item");
	}

	public void Pokemon() {
		this.inPokemon = true;
		System.out.println("Pokemon");
	}

	public void playerSwitchPokemon() {
		boolean loss = true;
		for (Pokemon p : game.gData.player.getPokemon()) {
			if (p.getStat(STATS.HP) > 0) {
				loss = false;
			}
		}
		if (loss)
			Lose();

		// TODO - else switch pokemon
	}

	public void enemySwitchPokemon() {}

	public void giveEXP() {
		int s = 0;
		for (int x = 0; x < this.game.gData.player.getPokemon().size(); x++) {
			if (((Pokemon) this.game.gData.player.getPokemon().get(x)).hasParticipated()) {
				s++;
			}
		}
		this.playerPokemon.gainExp(((Pokemon) this.enemyPokemon.get(0)).getExpGain(false, s));
	}

	public void Run() {
		if (enemy == null) {
			this.inMain = false;
			this.inRun = true;
			((Pokemon) this.enemyPokemon.get(0)).statusEffect = 0;

			this.game.gData.inBattle = false;
			System.out.println("Got away safely!");
		}
	}

	public void Win() {
		giveEXP();
		this.inMain = false;
		this.inRun = true;
		((Pokemon) this.enemyPokemon.get(0)).statusEffect = 0;

		this.game.gData.inBattle = false;

		Utils.pauseBackgrondMusic();
		Utils.playBackgroundMusic(game.gData.player.getCurLoc());
		game.gData.player.beatenTrainers.add(enemy.getName());
		game.gData.playerWin = false;
		game.movable = true;
	}

	public void Lose() {
		this.inMain = false;
		this.inRun = true;
		((Pokemon) this.enemyPokemon.get(0)).statusEffect = 0;
		System.out.println("Player Pokemon has fainted");
		System.out.println(game.gData.player.getName() + " is all out of usable Pokemon!");
		System.out.println(game.gData.player.getName() + " whited out.");
		game.gData.player.setSprite(EnumsAndConstants.sprite_lib.getSprites("PLAYER").get(9));
		game.gData.player.getPokemon().get(0).heal(-1);
		this.game.gData.inBattle = false;
	}

	public void enemyTurn() {
		if (!this.playerWon) {
			if ((((Pokemon) this.enemyPokemon.get(0)).statusEffect == 4)
					|| (((Pokemon) this.enemyPokemon.get(0)).statusEffect == 5)) {
				Random rr = new Random();
				int wakeupthaw = rr.nextInt(5);
				if (wakeupthaw <= 1) {
					if (((Pokemon) this.enemyPokemon.get(0)).statusEffect == 4) {
						System.out.println(((Pokemon) this.enemyPokemon.get(0)).getName() + " has woken up.");
					}
					if (((Pokemon) this.enemyPokemon.get(0)).statusEffect == 5) {
						System.out.println(((Pokemon) this.enemyPokemon.get(0)).getName()
								+ " has broken free from the ice.");
					}
					((Pokemon) this.enemyPokemon.get(0)).statusEffect = 0;
				} else {
					if (((Pokemon) this.enemyPokemon.get(0)).statusEffect == 4) {
						System.out.println(((Pokemon) this.enemyPokemon.get(0)).getName() + " is still asleep.");
					}
					if (((Pokemon) this.enemyPokemon.get(0)).statusEffect == 5) {
						System.out.println(((Pokemon) this.enemyPokemon.get(0)).getName() + " is frozen solid.");
					}
				}
			}
			if (((Pokemon) this.enemyPokemon.get(0)).statusEffect == 1) {
				Random r = new Random();
				int rand = r.nextInt(2);
				if (rand <= 0) {
					int choice = Utils.generateRandom(0, ((Pokemon) this.enemyPokemon.get(0)).getNumMoves() - 1);
					Move chosen = ((Pokemon) this.enemyPokemon.get(0)).getMove(choice);
					int attackStat = 0;
					int defStat = 0;
					if (chosen.getType().equals("PHYSICAL")) {
						attackStat = ((Pokemon) this.enemyPokemon.get(0)).getStat(EnumsAndConstants.STATS.ATTACK);
						defStat = this.playerPokemon.getStat(EnumsAndConstants.STATS.DEFENSE);
					}
					if (chosen.getType().equals("SPECIAL")) {
						attackStat = ((Pokemon) this.enemyPokemon.get(0)).getStat(EnumsAndConstants.STATS.SP_ATTACK);
						defStat = this.playerPokemon.getStat(EnumsAndConstants.STATS.SP_DEFENSE);
					}
					int damage = 0;
					if (!chosen.getType().equals("STAT")) {
						damage = (int) (((2 * ((Pokemon) this.enemyPokemon.get(0)).getLevel() / 5 + 2)
								* chosen.getStrength() * attackStat / 50 / defStat + 2)
								* Utils.generateRandom(85, 100) / 100.0);
					}
					this.playerPokemon.doDamage(damage);
					System.out.println("Enemy's turn is over");
				} else {
					System.out
							.println(((Pokemon) this.enemyPokemon.get(0)).getName() + " is paralyzed. It can't move.");
				}
			} else {
				int choice = Utils.generateRandom(0, ((Pokemon) this.enemyPokemon.get(0)).getNumMoves() - 1);
				Move chosen = ((Pokemon) this.enemyPokemon.get(0)).getMove(choice);

				int attackStat = 0;
				int defStat = 1;
				if (chosen.getType().equals("PHYSICAL")) {
					attackStat = ((Pokemon) this.enemyPokemon.get(0)).getStat(EnumsAndConstants.STATS.ATTACK);
					defStat = this.playerPokemon.getStat(EnumsAndConstants.STATS.DEFENSE);
				}
				if (chosen.getType().equals("SPECIAL")) {
					attackStat = ((Pokemon) this.enemyPokemon.get(0)).getStat(EnumsAndConstants.STATS.SP_ATTACK);
					defStat = this.playerPokemon.getStat(EnumsAndConstants.STATS.SP_DEFENSE);
				}
				int damage = 0;
				if (!chosen.getType().equals("STAT")) {
					damage = (int) (((2 * ((Pokemon) this.enemyPokemon.get(0)).getLevel() / 5 + 2)
							* chosen.getStrength() * attackStat / defStat / 50 + 2)
							* Utils.generateRandom(85, 100) / 100.0D);
				}
				this.playerPokemon.doDamage(damage);
			}
			if (((Pokemon) this.enemyPokemon.get(0)).statusEffect == 2) {
				((Pokemon) this.enemyPokemon.get(0)).doDamage(2);
				System.out.println(((Pokemon) this.enemyPokemon.get(0)).getName() + " has been hurt by its burn");
			}
			if (((Pokemon) this.enemyPokemon.get(0)).statusEffect == 3) {
				((Pokemon) this.enemyPokemon.get(0)).doDamage(2);
				System.out.println(((Pokemon) this.enemyPokemon.get(0)).getName() + " has been hurt by its poison");
			}
			this.playerTurn = true;
		}
	}

}