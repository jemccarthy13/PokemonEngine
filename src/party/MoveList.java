package party;

import java.util.ArrayList;
import java.util.Scanner;

import utilities.DebugUtility;

/**
 * Represents the list of moves a Party Member knows
 */
public class MoveList extends ArrayList<MoveData> {

	private static final long serialVersionUID = 5121990509346726333L;

	private String name = "";
	private int capacity = 4;

	/**
	 * 
	 * @param memberName
	 */
	public MoveList(String memberName) {
		this.name = memberName;
	}

	/**
	 * Add a move to the move list. If Battler knows 'capacity' number of moves,
	 * ask if the Player wants to replace a move.
	 * 
	 * @param move
	 *            - the move to add
	 * @param askForOverwrite
	 *            - whether or not to ask for an overwrite
	 * @return whether or not the move was added
	 */
	public boolean add(MoveData move, boolean askForOverwrite) {
		boolean success = false;
		if (size() < this.capacity || size() == 0) {
			this.add(move);
			success = true;
		} else {
			/** @todo replace this with a ReplaceMoveScene **/
			if (askForOverwrite) {
				DebugUtility.printMessage(this.name + " already knows 4 moves.  Make room for another?");

				try (Scanner s = new Scanner(System.in)) {
					String str = s.nextLine();
					if (str.contains("y")) {
						DebugUtility.printMessage("Which move to delete?");
						int delete = Integer.parseInt(s.nextLine());
						this.remove(this.get(delete));
						add(move);
						success = true;
					}
				}
			} else {
				if (this.size() >= this.capacity) {
					this.remove(0);
				}
				add(move);
				success = true;
			}
		}
		return success;
	}
}
