package trainers;

import graphics.SpriteLibrary;

import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import model.Coordinate;
import party.Party;
import trainers.Actor.DIR;

// ////////////////////////////////////////////////////////////////////////
//
// Holds data variables common to all trainers
//
// ////////////////////////////////////////////////////////////////////////
public class TrainerData implements Serializable {

	private static final long serialVersionUID = 9089885833065857943L;
	public String name = null;
	public Coordinate position = null;
	public Party pokemon = null;
	public int money = 0;
	public ArrayList<String> conversationText = null;
	public boolean stationary = false;
	public boolean trainer = false;
	public DIR dir = DIR.SOUTH;
	public String sprite_name = "";
	public ImageIcon sprite = null;

	// ////////////////////////////////////////////////////////////////////////
	//
	// Initializes data based on a given file
	//
	// ////////////////////////////////////////////////////////////////////////
	public TrainerData() {}

	public boolean isValidData() {
		return (this.name != null && this.position != null);
	}

	public ArrayList<ImageIcon> getSprites() {
		return SpriteLibrary.getSprites(sprite_name);
	}

	public String toString() {
		String retStr = name;
		retStr += "\n- " + position.getX() + ", " + position.getY();
		retStr += "\n- " + money;
		retStr += "\n- " + dir;
		return retStr;
	}
}