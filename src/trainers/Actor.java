package trainers;

import java.io.Serializable;
import java.util.ArrayList;

import graphics.SpriteLibrary;
import pokedex.Pokemon;
import pokedex.PokemonList;
import utilities.Coordinate;

//////////////////////////////////////////////////////////////////////////
//
// Actor - the base class for any movable character or sprite
//
// TODO - implement box storage for players
//
//////////////////////////////////////////////////////////////////////////
public class Actor implements Serializable {

	private static final long serialVersionUID = 6292047432930495977L;

	public TrainerData tData = new TrainerData();

	public static enum DIR implements Serializable {
		SOUTH, WEST, EAST, NORTH;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Actor constructor - given location and name, create a player
	//
	// ////////////////////////////////////////////////////////////////////////
	public Actor(int x, int y, String n, String sprite_name) {
		this.tData.name = n;
		this.tData.position = new Coordinate(x, y);
		this.tData.money = 2000;
		this.tData.pokemon = new PokemonList();
		this.tData.sprite_name = sprite_name;
		this.tData.sprite = SpriteLibrary.getInstance().getImage(sprite_name);
		setDirection(DIR.SOUTH);
	}

	public Actor() {}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Actor constructor - given some data, create a new Actor
	//
	// ////////////////////////////////////////////////////////////////////////
	public Actor(TrainerData data) {
		this.tData = data;
		setDirection(DIR.SOUTH);
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
	// changeSprite - changes sprite based on animation
	//
	// ////////////////////////////////////////////////////////////////////////
	public void changeSprite(int pixels, boolean rightFoot) {

		int direction = 3 * getDirection().ordinal();

		if ((pixels >= 0) && (pixels < 4)) {
			tData.sprite = (tData.getSprites().get(direction));
		} else if ((pixels > 4) && (pixels < 8)) {
			tData.sprite = (tData.getSprites().get(direction));
		} else if ((pixels > 8) && (pixels < 12)) {
			if (!rightFoot) {
				tData.sprite = (tData.getSprites().get(direction + 1));
			} else {
				tData.sprite = (tData.getSprites().get(direction + 2));
			}
		} else if ((pixels >= 12) && (pixels < 15)) {
			if (!rightFoot) {
				tData.sprite = (tData.getSprites().get(direction + 1));
			} else {
				tData.sprite = (tData.getSprites().get(direction + 2));
			}
		} else {
			tData.sprite = (tData.getSprites().get(direction));
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

	// ////////////////////////////////////////////////////////////////////////
	//
	// setDirection - sets the characters direction
	//
	// ////////////////////////////////////////////////////////////////////////
	public void setDirection(DIR direction) {
		this.tData.dir = direction;
		this.tData.sprite = this.tData.getSprites().get(direction.ordinal() * 3);
	}

	public DIR getDirection() {
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

	public void setName(String nameSelected) {
		this.tData.name = nameSelected;
	}

	public String getNumPokemonOwned() {
		return String.valueOf(this.tData.pokemon.size());
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

	public Coordinate getPosition() {
		return this.tData.position;
	}

}
