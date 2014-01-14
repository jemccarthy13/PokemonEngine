package data_structures;

import java.awt.Image;
import java.io.Serializable;

import locations.Location;
import utilities.EnumsAndConstants;
import utilities.EnumsAndConstants.SPRITENAMES;
import utilities.Utils;

public class Trainer implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	protected Image[] sprites = new Image[12];
	protected Image sprite;
	private int id;
	private String battleText[];
	private Image battleSprite;
	protected int money;
	protected PokemonList pokemon = new PokemonList();
	protected String[] text;
	protected Location curLoc;
	protected int loc_x, loc_y;

	public Trainer(int x, int y, String n, String[] t, SPRITENAMES s, Image bs, int i) {
		this.name = n;
		this.text = t;
		this.loc_x = x;
		this.loc_y = y;
		setBattleText(t);
		this.battleSprite = bs;
		this.money = i;
		if (s.equals(SPRITENAMES.BOY)) {
			this.sprite = EnumsAndConstants.sprite_lib.BOY_DOWN;
			this.sprites[0] = EnumsAndConstants.sprite_lib.BOY_UP;
			this.sprites[1] = EnumsAndConstants.sprite_lib.BOY_UP1;
			this.sprites[2] = EnumsAndConstants.sprite_lib.BOY_UP2;
			this.sprites[3] = EnumsAndConstants.sprite_lib.BOY_DOWN;
			this.sprites[4] = EnumsAndConstants.sprite_lib.BOY_DOWN1;
			this.sprites[5] = EnumsAndConstants.sprite_lib.BOY_DOWN2;
			this.sprites[6] = EnumsAndConstants.sprite_lib.BOY_RIGHT;
			this.sprites[7] = EnumsAndConstants.sprite_lib.BOY_RIGHT1;
			this.sprites[8] = EnumsAndConstants.sprite_lib.BOY_RIGHT2;
			this.sprites[9] = EnumsAndConstants.sprite_lib.BOY_LEFT;
			this.sprites[10] = EnumsAndConstants.sprite_lib.BOY_LEFT1;
			this.sprites[11] = EnumsAndConstants.sprite_lib.BOY_LEFT2;
		} else if (s.equals(SPRITENAMES.PLAYER)) {
			this.sprite = EnumsAndConstants.sprite_lib.PLAYER_DOWN;
			this.sprites[0] = EnumsAndConstants.sprite_lib.PLAYER_UP;
			this.sprites[1] = EnumsAndConstants.sprite_lib.PLAYER_UP1;
			this.sprites[2] = EnumsAndConstants.sprite_lib.PLAYER_UP2;
			this.sprites[3] = EnumsAndConstants.sprite_lib.PLAYER_DOWN;
			this.sprites[4] = EnumsAndConstants.sprite_lib.PLAYER_DOWN1;
			this.sprites[5] = EnumsAndConstants.sprite_lib.PLAYER_DOWN2;
			this.sprites[6] = EnumsAndConstants.sprite_lib.PLAYER_RIGHT;
			this.sprites[7] = EnumsAndConstants.sprite_lib.PLAYER_RIGHT1;
			this.sprites[8] = EnumsAndConstants.sprite_lib.PLAYER_RIGHT2;
			this.sprites[9] = EnumsAndConstants.sprite_lib.PLAYER_LEFT;
			this.sprites[10] = EnumsAndConstants.sprite_lib.PLAYER_LEFT1;
			this.sprites[11] = EnumsAndConstants.sprite_lib.PLAYER_LEFT2;
		}
	}

	public Trainer(String string, int i) {
		name = string;
		money = i;
	}

	public Image getBattleSprite() {
		return this.battleSprite;
	}

	public void caughtPokemon(Pokemon p) {
		this.pokemon.add(p);
	}

	public PokemonList getPokemon() {
		return this.pokemon;
	}

	public Location getCurLoc() {
		return this.curLoc;
	}

	public void setCurLoc(Location curLoc) {
		this.curLoc = curLoc;
	}

	public void createTrainerID() {
		int a = Utils.generateRandom(0, 9);
		int b = Utils.generateRandom(0, 9);
		int c = Utils.generateRandom(0, 9);
		int d = Utils.generateRandom(0, 9);
		int e = Utils.generateRandom(0, 9);
		this.id = (10000 * a + 1000 * b + 100 * c + 10 * d + e);
	}

	public int getCurrentX() {
		return this.loc_x;
	}

	public int getCurrentY() {
		return this.loc_y;
	}

	public void setCurrentX(int x) {
		this.loc_x = x;
	}

	public void setCurrentY(int y) {
		this.loc_y = y;
	}

	public void moveUp() {
		this.loc_y -= 1;
	}

	public void moveDown() {
		this.loc_y += 1;
	}

	public void moveLeft() {
		this.loc_x -= 1;
	}

	public void moveRight() {
		this.loc_x += 1;
	}

	public void setName(String input) {
		this.name = input;
	}

	public String getName() {
		return this.name;
	}

	public void setID(int input) {
		this.id = input;
	}

	public int getID() {
		return this.id;
	}

	public void setSprite(Image m) {
		this.sprite = m;
	}

	public Image getSprite() {
		return this.sprite;
	}

	public String[] getBattleText() {
		return this.battleText;
	}

	public void setBattleText(String[] t) {
		this.battleText = t;
	}
}