package data_structures;

import java.awt.Image;
import java.io.Serializable;

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

	public Trainer(int x, int y, String n, int i) {
		this.name = n;
		this.loc_x = x;
		this.loc_y = y;
		this.money = i;
		curLoc = new Location("New Bark Town");
	}

	public Trainer(String string, int i) {
		name = string;
		money = i;
	}

	public Trainer() {}

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