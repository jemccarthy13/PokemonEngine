package trainers;

import java.awt.Image;
import java.io.Serializable;
import java.util.ArrayList;

import location.Location;
import pokedex.Pokemon;
import pokedex.PokemonList;
import tiles.Coordinate;
import utilities.EnumsAndConstants;
import utilities.EnumsAndConstants.DIR;

public class Player implements Serializable {

	private static final long serialVersionUID = 1L;
	public TrainerData pData = new TrainerData();

	NPC rival;
	public ArrayList<String> beatenTrainers = new ArrayList<String>();
	private int badges = 0;
	private int id;
	Location curLoc;

	public Player(int x, int y, String n) {
		pData.name = n;
		pData.position.setX(x);
		pData.position.setY(y);
		pData.sprites = EnumsAndConstants.sprite_lib.getSprites("PLAYER");
		curLoc = new Location("New Bark Town");
		setSpriteFacing(DIR.SOUTH);
	}

	public Player(String n) {
		pData.name = n;
		pData.position = new Coordinate(0, 0);
		pData.money = 2000;
		pData.sprites = EnumsAndConstants.sprite_lib.getSprites("PLAYER");
		this.setSpriteFacing(DIR.SOUTH);
	}

	public void move(DIR dir) {
		pData.position = pData.position.move(dir);
	}

	public Location getCurLoc() {
		return curLoc;
	}

	public void setDirection(DIR direction) {
		pData.dir = direction;
	}

	public void caughtPokemon(Pokemon p) {
		pData.pokemon.add(p);
	}

	public PokemonList getPokemon() {
		return pData.pokemon;
	}

	public void setSpriteFacing(DIR dir) {
		setDirection(dir);
		if (dir.equals(DIR.NORTH)) {
			setSprite(pData.sprites.get(9));
		}
		if (dir.equals(DIR.SOUTH)) {
			setSprite(pData.sprites.get(0));
		}
		if (dir.equals(DIR.EAST)) {
			setSprite(pData.sprites.get(6));
		}
		if (dir.equals(DIR.WEST)) {
			setSprite(pData.sprites.get(3));
		}
	}

	public void beatTrainer(NPC t) {
		this.beatenTrainers.add(t.getName());
	}

	public void setRivalName(String n) {
		this.rival.setName(n);
	}

	public String getRivalName() {
		return this.rival.getName();
	}

	public int getBadges() {
		return this.badges;
	}

	public void setBadges(int badges) {
		this.badges = badges;
	}

	public void setMoney(int m) {
		pData.money = m;
	}

	public int getMoney() {
		return pData.money;
	}

	public void setDir(DIR dir) {
		pData.dir = dir;
	}

	public DIR getDir() {
		return pData.dir;
	}

	public int getCurrentX() {
		return pData.position.getX();
	}

	public int getCurrentY() {
		return pData.position.getY();
	}

	public void setLoc(Coordinate c) {
		pData.position = c;
	}

	public void setSprite(Image m) {
		pData.sprite = m;
	}

	public Image getSprite() {
		return pData.sprite;
	}

	public void changeSprite(int pixels, boolean rightFoot) {

		int direction = 3 * getDir().ordinal();

		if ((pixels >= 0) && (pixels < 4)) {
			setSprite(pData.sprites.get(direction));
		} else if ((pixels > 4) && (pixels < 8)) {
			setSprite(pData.sprites.get(direction));
		} else if ((pixels > 8) && (pixels < 12)) {
			if (!rightFoot) {
				setSprite(pData.sprites.get(direction + 1));
			} else {
				setSprite(pData.sprites.get(direction + 2));
			}
		} else if ((pixels >= 12) && (pixels < 15)) {
			if (!rightFoot) {
				setSprite(pData.sprites.get(direction + 1));
			} else {
				setSprite(pData.sprites.get(direction + 2));
			}
		} else {
			setSprite(pData.sprites.get(direction));
		}
	}

	public String getName() {
		return pData.name;
	}

	public void setName(String nameSelected) {
		pData.name = nameSelected;
	}

	public void setID(int input) {
		this.id = input;
	}

	public int getID() {
		return this.id;
	}

	public String getPokemonOwned() {
		return String.valueOf(pData.pokemon.size());
	}
}