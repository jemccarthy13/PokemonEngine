package party;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Storage for unused party and stored items
 */
public class Storage implements Serializable {
	private static final long serialVersionUID = 8519420241334625610L;

	// the max number of battlers or items in each box
	private static final int BOX_CAPACITY = 20;
	// the max number of boxes available
	private static final int max_num_boxes = 10;

	// since the above two parameters can change, the boxes themselves must be
	// flexible enough to grow or shrink as necessary
	private ArrayList<ArrayList<PartyMember>> boxes = new ArrayList<ArrayList<PartyMember>>();

	// TODO - Implement items & item storage
	// public ArrayList<ItemList> items = new ArrayList<ItemList>();

	/**
	 * Default constructor
	 */
	public Storage() {
		// initialize all of the boxes up front
		for (int x = 0; x < max_num_boxes; x++) {
			ArrayList<PartyMember> box = new ArrayList<PartyMember>();
			boxes.add(box);
		}
	}

	/**
	 * Add a Party Member to the first box with room
	 * 
	 * @param p
	 *            - the party member to add
	 * @return whether or not the party member was successfully added
	 */
	public boolean depositPokemon(PartyMember p) {
		boolean added = false;
		for (ArrayList<PartyMember> box : boxes) {
			if (box.size() < BOX_CAPACITY) {
				box.add(p);
				added = true;
			}
		}
		return added;
	}

	/**
	 * Retrieve a party member from the box it is located in
	 * 
	 * @param p
	 *            - the party member to withdraw
	 * @return whether or not the party member was withdrawn
	 */
	public boolean withdrawPokemon(PartyMember p) {
		for (ArrayList<PartyMember> box : boxes) {
			if (box.contains(p)) {
				return box.remove(p);
			}
		}
		return false;
	}
}