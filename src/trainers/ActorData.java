package trainers;

import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import graphics.SpriteLibrary;
import model.Coordinate;
import party.Party;
import trainers.Actor.DIR;

/**
 * Holds data variables common to all trainers
 */
public class ActorData implements Serializable {

	/**
	 * Serialization information
	 */
	private static final long serialVersionUID = 9089885833065857943L;
	/**
	 * Trainer's name
	 */
	public String name = null;
	/**
	 * Trainer's x,y position
	 */
	public Coordinate position = null;
	/**
	 * Trainer's current battle party
	 */
	public Party party = null;
	/**
	 * Trainer's current money
	 */
	public int money = 0;
	/**
	 * Trainer's conversation text
	 */
	public ArrayList<String> conversationText = null;
	/**
	 * Whether or not the Actor moves
	 */
	public boolean stationary = false;
	/**
	 * Whether or not the Actor is a trainer
	 */
	public boolean trainer = false;
	/**
	 * The current direction this Actor is facing
	 */
	public DIR dir = DIR.SOUTH;
	/**
	 * The sprite name of this Actor
	 */
	public String sprite_name = "";
	/**
	 * The current sprite of this Actor
	 */
	public ImageIcon sprite = null;

	// ////////////////////////////////////////////////////////////////////////
	//
	// Initializes data based on a given file
	//
	// ////////////////////////////////////////////////////////////////////////
	/**
	 * Default constructor
	 */
	public ActorData() {}

	/**
	 * Is this structure valid data?
	 * 
	 * @return true if member values aren't null
	 */
	public boolean isValidData() {
		return (this.name != null && this.position != null);
	}

	/**
	 * Get the sprite list for this Actor
	 * 
	 * @return ArrayList<ImageIcon> sprites
	 */
	public ArrayList<ImageIcon> getSprites() {
		return SpriteLibrary.getSprites(this.sprite_name);
	}

	/**
	 * A user-friendly representation of the Actor
	 * 
	 * @return a string representation of this Actor
	 */
	@Override
	public String toString() {
		String retStr = this.name;
		retStr += "\n- " + this.position.getX() + ", " + this.position.getY();
		retStr += "\n- " + this.money;
		retStr += "\n- " + this.dir;
		return retStr;
	}
}