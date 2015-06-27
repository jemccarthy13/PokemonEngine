package trainers;

import java.io.Serializable;
import java.util.ArrayList;

import libraries.LocationLibrary;
import location.Location;
import utilities.RandomNumUtils;

//////////////////////////////////////////////////////////////////////////
//
// Player - holds data for the current user
//
// ////////////////////////////////////////////////////////////////////////
public class Player extends Actor implements Serializable {

	private static final long serialVersionUID = 1L;

	NPC rival;
	public ArrayList<String> beatenTrainers = new ArrayList<String>();
	private int badges = 0;
	private int id;
	Location curLoc;

	// ////////////////////////////////////////////////////////////////////////
	//
	// Player constructor given location and a name
	//
	// ////////////////////////////////////////////////////////////////////////
	public Player(int x, int y, String n) {
		super(x, y, n);
		curLoc = new Location(LocationLibrary.getInstance().get("New Bark Town"));
		id = RandomNumUtils.createTrainerID();
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Player constructor given a name - typically for unit tests
	//
	// ////////////////////////////////////////////////////////////////////////
	public Player(String n) {
		super(0, 0, n);
		curLoc = new Location(LocationLibrary.getInstance().get("New Bark Town"));
		id = RandomNumUtils.createTrainerID();
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// beatTrainer - adds the beaten NPC to the list of beaten trainers
	//
	// ////////////////////////////////////////////////////////////////////////
	public void beatTrainer(NPC t) {
		this.beatenTrainers.add(t.getName());
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// accessors and mutators for member variables
	//
	// ////////////////////////////////////////////////////////////////////////
	public Location getCurLoc() {
		return curLoc;
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

	public void setID(int input) {
		this.id = input;
	}

	public int getID() {
		return this.id;
	}
}