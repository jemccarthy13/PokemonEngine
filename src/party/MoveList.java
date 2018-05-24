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
		name = memberName;
	}

	/**
	 * Try to add a move to the move list. If the party member knows 'capacity'
	 * moves, check if overwrite or not. If not overwrite, remove the first move
	 * and add the newest.
	 * 
	 * If the capacity is -1, always add the move.
	 * 
	 * @param move
	 *            - the move to add
	 * @param askForOverwrite
	 *            - whether or not to ask for an overwrite
	 * @return whether or not the move was added
	 */
	public boolean add(MoveData move, boolean askForOverwrite) {
		boolean success = false;
		if (size() < capacity || size() == 0) {
			this.add(move);
			success = true;
		} else {
			if (askForOverwrite) {
				DebugUtility.printMessage(name + " already knows 4 moves.  Make room for another?");
				Scanner s = new Scanner(System.in);
				String str = s.nextLine();
				if (str.contains("y")) {
					DebugUtility.printMessage("Which move to delete?");
					Integer delete = Integer.parseInt(s.nextLine());
					this.remove(delete);
					add(move);
					success = true;
				}
				s.close();
			} else {
				if (this.size() >= capacity) {
					this.remove(0);
				}
				add(move);
				success = true;
			}
		}
		return success;
	}
}
