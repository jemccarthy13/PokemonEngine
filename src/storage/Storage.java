package storage;

import java.io.Serializable;
import java.util.ArrayList;

import party.Battler;

/**
 * Storage for unused party and stored items
 * 
 * "Bill's PC"
 */
public class Storage implements Serializable {
	private static final long serialVersionUID = 8519420241334625610L;

	// the max number of battlers or items in each box
	private static final int BOX_CAPACITY = 20;
	// the max number of boxes available
	private static final int max_num_boxes = 10;

	// since the above two parameters can change, the boxes themselves must be
	// flexible enough to grow or shrink as necessary
	private ArrayList<ArrayList<Battler>> boxes = new ArrayList<ArrayList<Battler>>();

	// behind the scenes, the PC stores items exactly how the Bag does
	public Bag items = new Bag();

	/**
	 * Default constructor
	 */
	public Storage() {
		// initialize all of the boxes up front
		for (int x = 0; x < max_num_boxes; x++) {
			ArrayList<Battler> box = new ArrayList<Battler>();
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
	public boolean depositPokemon(Battler p) {
		boolean added = false;
		for (ArrayList<Battler> box : boxes) {
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
	public boolean withdrawPokemon(Battler p) {
		for (ArrayList<Battler> box : boxes) {
			if (box.contains(p)) {
				return box.remove(p);
			}
		}
		return false;
	}
}