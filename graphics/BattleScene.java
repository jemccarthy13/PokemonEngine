package graphics;

import java.util.Random;

import pokedex.Move;
import pokedex.Pokemon;
import pokedex.PokemonList;
import trainers.NPC;
import utilities.EnumsAndConstants;
import utilities.EnumsAndConstants.STATS;
import utilities.RandomNumUtils;
import audio.AudioLibrary;
import driver.Game;

//////////////////////////////////////////////////////////////////////////
//
// BattleScene - holds all logic for a Pokemon battle - w/wild or Trainer
//
// TODO - implement player switching Pokemon
// TODO - implement enemy switching Pokemon
// TODO - change to message boxes for messages
//
//////////////////////////////////////////////////////////////////////////
public class BattleScene {
	private Game game;
	public boolean playerTurn;
	public boolean inMain = true;
	public boolean inFight = false;
	public boolean inItem = false;
	public boolean inPokemon = false;
	public boolean inRun = false;
	public boolean playerWon = false;
	public int currentSelectionMainX;
	public int currentSelectionMainY;
	public int currentSelectionFightX;
	public int currentSelectionFightY;
	public Pokemon playerPokemon;
	public PokemonList enemyPokemon;
	public NPC enemy = null;

	// ////////////////////////////////////////////////////////////////////////
	//
	// Constructor - keeps a local copy of the game and the current npc
	//
	// ////////////////////////////////////////////////////////////////////////
	public BattleScene(Game pkmnGame, NPC curNPC) {
		game = pkmnGame;
		playerPokemon = pkmnGame.gData.player.getPokemon().get(0);
		playerPokemon.setParticipated();
		enemyPokemon = curNPC.getPokemon();
		enemy = curNPC;
		playerTurn = true;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Start - set all variables for the start of a battle
	//
	// ////////////////////////////////////////////////////////////////////////

	public void Start() {
		this.currentSelectionMainX = 0;
		this.currentSelectionFightX = 0;
		this.currentSelectionMainY = 0;
		this.currentSelectionFightY = 0;
		this.inMain = true;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Fight - set variables to be in the fight menu
	//
	// ////////////////////////////////////////////////////////////////////////
	public void Fight() {
		this.inMain = false;
		this.inFight = true;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Item - set variables for the item menu
	//
	// ////////////////////////////////////////////////////////////////////////
	public void Item() {
		this.inItem = true;
		System.out.println("Item");
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Pokemon - set variables for the party menu
	//
	// ////////////////////////////////////////////////////////////////////////
	public void Pokemon() {
		this.inPokemon = true;
		System.out.println("Pokemon");
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// playerSwitchPokemon - if players Pokemon are out of HP, white out -
	// otherwise, switch pokemon
	//
	// ////////////////////////////////////////////////////////////////////////
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

	// ////////////////////////////////////////////////////////////////////////
	//
	// giveExp - give experience to all Pokemon that have participated
	//
	// Experience is calculated in the Pokemon class- based on the number
	// of player's Pokemon that have participated in battle.
	//
	// ////////////////////////////////////////////////////////////////////////
	public void giveEXP() {
		int s = 0;
		for (int x = 0; x < this.game.gData.player.getPokemon().size(); x++) {
			if (((Pokemon) this.game.gData.player.getPokemon().get(x)).hasParticipated()) {
				s++;
			}
		}
		this.playerPokemon.gainExp(((Pokemon) this.enemyPokemon.get(0)).getExpGain(false, s));
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Run - set variables for running away from battle (or exiting the fight)
	//
	// ////////////////////////////////////////////////////////////////////////
	public void Run() {
		if (enemy == null) {
			this.inMain = false;
			this.inRun = true;
			((Pokemon) this.enemyPokemon.get(0)).statusEffect = 0;

			this.game.gData.inBattle = false;
			System.out.println("Got away safely!");
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Win - set the variables for a player win in a battle
	//
	// ////////////////////////////////////////////////////////////////////////
	public void Win() {
		giveEXP();
		this.inMain = false;
		this.inRun = true;
		((Pokemon) this.enemyPokemon.get(0)).statusEffect = 0;

		this.game.gData.inBattle = false;

		// reset the music, add to beaten trainers
		AudioLibrary.getInstance().pauseBackgrondMusic();
		AudioLibrary.getInstance().playBackgroundMusic("NewBarkTown");
		game.gData.player.beatenTrainers.add(enemy.getName());
		game.gData.playerWin = false;
		game.movable = true;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Lose - set the variables for a player loss (white out) in a battle
	//
	// ////////////////////////////////////////////////////////////////////////
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

	// ////////////////////////////////////////////////////////////////////////
	//
	// enemyTurn - enemy chooses move and deals damage to player
	//
	// TODO - change to message boxes
	//
	// ////////////////////////////////////////////////////////////////////////
	public void enemyTurn() {
		if (!this.playerWon) {
			if ((((Pokemon) this.enemyPokemon.get(0)).statusEffect == 4)
					|| (((Pokemon) this.enemyPokemon.get(0)).statusEffect == 5)) {
				// Do frozen or sleep logic
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
				// do paralyzed logic
				Random r = new Random();
				int rand = r.nextInt(2);
				if (rand <= 0) {
					int choice = RandomNumUtils.generateRandom(0,
							((Pokemon) this.enemyPokemon.get(0)).getNumMoves() - 1);
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
								* RandomNumUtils.generateRandom(85, 100) / 100.0);
					}
					this.playerPokemon.doDamage(damage);
					AudioLibrary.getInstance().playClip(AudioLibrary.getInstance().SE_DAMAGE,
							this.game.gData.option_sound);
					System.out.println("Enemy's turn is over");
				} else {
					System.out
							.println(((Pokemon) this.enemyPokemon.get(0)).getName() + " is paralyzed. It can't move.");
				}
			} else {
				// otherwise do nomral battle order of events
				int choice = RandomNumUtils.generateRandom(0, ((Pokemon) this.enemyPokemon.get(0)).getNumMoves() - 1);
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
							* RandomNumUtils.generateRandom(85, 100) / 100.0D);
				}
				AudioLibrary.getInstance().playClip(AudioLibrary.getInstance().SE_SELECT, this.game.gData.option_sound);
			}
			if (((Pokemon) this.enemyPokemon.get(0)).statusEffect == 2) {
				AudioLibrary.getInstance().playClip(AudioLibrary.getInstance().SE_DAMAGE, this.game.gData.option_sound);
				System.out.println(((Pokemon) this.enemyPokemon.get(0)).getName() + " has been hurt by its burn");
			}
			if (((Pokemon) this.enemyPokemon.get(0)).statusEffect == 3) {
				AudioLibrary.getInstance().playClip(AudioLibrary.getInstance().SE_DAMAGE, this.game.gData.option_sound);
				System.out.println(((Pokemon) this.enemyPokemon.get(0)).getName() + " has been hurt by its poison");
			}
			this.playerTurn = true;
		}
	}

}