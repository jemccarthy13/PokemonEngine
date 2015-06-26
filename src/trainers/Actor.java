package trainers;

import java.awt.Image;
import java.util.ArrayList;

import pokedex.Pokemon;
import pokedex.PokemonList;
import tiles.Coordinate;
import utilities.EnumsAndConstants;
import utilities.EnumsAndConstants.DIR;

//////////////////////////////////////////////////////////////////////////
//
// Actor - the base class for any movable character or sprite
//
// TODO - implement box storage for players
//
//////////////////////////////////////////////////////////////////////////
public class Actor {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	public TrainerData tData = new TrainerData();

	// ////////////////////////////////////////////////////////////////////////
	//
	// Player constructor - given location and name, create a player
	//
	// ////////////////////////////////////////////////////////////////////////
	public Actor(int x, int y, String n) {
		this.tData.name = n;
		this.tData.position = new Coordinate(x, y);
		this.tData.money = 2000;
		this.tData.sprites = EnumsAndConstants.sprite_lib.getSprites("PLAYER");
		this.tData.pokemon = new PokemonList();
		setSpriteFacing(DIR.SOUTH);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// NPC constructor - given some data, create a new NPC actor
	//
	// ////////////////////////////////////////////////////////////////////////
	public Actor(TrainerData npcData) {
		this.tData = npcData;
		setSpriteFacing(DIR.SOUTH);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// caughtPokemon - add the caught pokemon to the list of pokemon
	//
	// ////////////////////////////////////////////////////////////////////////
	public void caughtPokemon(Pokemon p) {
		this.tData.pokemon.add(p);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// getPokemon - returns the lsit of owned pokemon in party
	//
	// ////////////////////////////////////////////////////////////////////////

	public PokemonList getPokemon() {
		return this.tData.pokemon;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// move - move the character in the given direction
	//
	// ////////////////////////////////////////////////////////////////////////
	public void move(DIR dir) {
		this.tData.position = this.tData.position.move(dir);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// setDirection - sets the characters direction
	//
	// ////////////////////////////////////////////////////////////////////////
	public void setDirection(DIR direction) {
		this.tData.dir = direction;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// setSpriteFacing - sets the sprites direction
	//
	// ////////////////////////////////////////////////////////////////////////
	public void setSpriteFacing(DIR dir) {
		setDirection(dir);
		if (dir.equals(DIR.NORTH)) {
			setSprite(this.tData.sprites.get(9));
		}
		if (dir.equals(DIR.SOUTH)) {
			setSprite(this.tData.sprites.get(0));
		}
		if (dir.equals(DIR.EAST)) {
			setSprite(this.tData.sprites.get(6));
		}
		if (dir.equals(DIR.WEST)) {
			setSprite(this.tData.sprites.get(3));
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// changeSprite - changes sprite based on animation
	//
	// ////////////////////////////////////////////////////////////////////////
	public void changeSprite(int pixels, boolean rightFoot) {

		int direction = 3 * getDir().ordinal();

		if ((pixels >= 0) && (pixels < 4)) {
			setSprite(this.tData.sprites.get(direction));
		} else if ((pixels > 4) && (pixels < 8)) {
			setSprite(this.tData.sprites.get(direction));
		} else if ((pixels > 8) && (pixels < 12)) {
			if (!rightFoot) {
				setSprite(this.tData.sprites.get(direction + 1));
			} else {
				setSprite(this.tData.sprites.get(direction + 2));
			}
		} else if ((pixels >= 12) && (pixels < 15)) {
			if (!rightFoot) {
				setSprite(this.tData.sprites.get(direction + 1));
			} else {
				setSprite(this.tData.sprites.get(direction + 2));
			}
		} else {
			setSprite(this.tData.sprites.get(direction));
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// getTalkable - returns whether or not Actor can talk to another Actor
	//
	// ////////////////////////////////////////////////////////////////////////
	public boolean getTalkable(Actor other) {
		if (other.getCurrentY() + 1 == this.tData.position.getY()) {
			if (other.getCurrentX() == this.tData.position.getX()) {
				return true;
			}
		}
		if (other.getCurrentY() - 1 == this.tData.position.getY()) {
			if (other.getCurrentX() == this.tData.position.getX()) {
				return true;
			}
		}
		if (other.getCurrentX() + 1 == this.tData.position.getX()) {
			if (other.getCurrentY() == this.tData.position.getY()) {
				return true;
			}
		}
		if (other.getCurrentX() - 1 == this.tData.position.getX()) {
			if (other.getCurrentY() == this.tData.position.getY()) {
				return true;
			}
		}
		return false;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// accessors and mutators for member variables
	//
	// ////////////////////////////////////////////////////////////////////////
	public void setMoney(int m) {
		this.tData.money = m;
	}

	public int getMoney() {
		return this.tData.money;
	}

	public void setDir(DIR dir) {
		this.tData.dir = dir;
	}

	public DIR getDir() {
		return this.tData.dir;
	}

	public int getCurrentX() {
		return this.tData.position.getX();
	}

	public int getCurrentY() {
		return this.tData.position.getY();
	}

	public void setLoc(Coordinate c) {
		this.tData.position = c;
	}

	public void setSprite(Image i) {
		this.tData.sprite = i;
	}

	public Image getSprite() {
		return this.tData.sprite;
	}

	public void setName(String nameSelected) {
		this.tData.name = nameSelected;
	}

	public String getPokemonOwned() {
		return String.valueOf(this.tData.pokemon.size());
	}

	public DIR getDirection() {
		return this.tData.dir;
	}

	public String getName() {
		return this.tData.name;
	}

	public ArrayList<String> getText() {
		return this.tData.conversationText;
	}

	public int getTextLength() {
		return this.tData.conversationText.size();
	}

	public void setStationary(boolean b) {
		this.tData.stationary = b;
	}

	public boolean isStationary() {
		return this.tData.stationary;
	}

	public String getText(int stage) {
		return this.tData.conversationText.get(stage);
	}

	public boolean isTrainer() {
		return this.tData.trainer;
	}

}
