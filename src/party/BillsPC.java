package party;

import java.io.Serializable;
import java.util.ArrayList;

// ////////////////////////////////////////////////////////////////////////
//
// Logic for PC Pkmn storage
//
// ////////////////////////////////////////////////////////////////////////
public class BillsPC implements Serializable {
	private static final long serialVersionUID = 8519420241334625610L;

	public ArrayList<Party> boxes = new ArrayList<Party>();
	private static final int BOX_CAPACITY = 20;
	private static final int max_num_boxes = 10;

	// TODO - Implement items & item storage
	// public ArrayList<ItemList> items = new ArrayList<ItemList>();

	// ////////////////////////////////////////////////////////////////////////
	//
	// Default constructor
	//
	// ////////////////////////////////////////////////////////////////////////
	public BillsPC() {
		for (int x = 0; x < max_num_boxes; x++) {
			boxes.add(new Party());
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Try to add a pokemon to the first box with room
	//
	// ////////////////////////////////////////////////////////////////////////
	public boolean depositPokemon(PartyMember p) {
		boolean added = false;
		for (Party box : boxes) {
			if (box.size() < BOX_CAPACITY) {
				box.add(p);
				added = true;
			}
		}
		return added;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Default constructor
	//
	// ////////////////////////////////////////////////////////////////////////
	public boolean withdrawPokemon(PartyMember p) {
		for (Party box : boxes) {
			if (box.contains(p)) {
				return box.remove(p);
			}
		}
		return false;
	}
}