package party;

import java.io.Serializable;

/**
 * The data behind battle moves
 */
public class MoveData implements Serializable {

	private static final long serialVersionUID = 9003426637910342523L;

	/**
	 * TODO analyze move categories for abstraction
	 */
	public enum MOVECATEGORY {
		/**
		 * Physically damaging moves use att/def stats
		 */
		PHYSICAL,
		/**
		 * Special moves use sp att/def stats
		 */
		SPECIAL,
		/**
		 * Stat moves damage accuracy/speed stats
		 */
		STAT
	}

	/**
	 * The name of the move
	 */
	public String name = null;
	/**
	 * How powerful the move is (1-100), 100+ are super moves that require
	 * recharge TODO implement recharge moves
	 */
	public int power = -1;

	/**
	 * Which category of stats to use
	 */
	public MOVECATEGORY category;
	/**
	 * The type of the move (GROUND, EARTH, WATER, MAGIC, etc)
	 */
	public String type;
	/**
	 * The number of times a move can be used before the character needs to heal
	 * or recover
	 */
	public int movePP = -1;
	/**
	 * How accurate the move is (1-100) in percent
	 */
	public int accuracy = -1;

	/**
	 * Retrieve a string representation of the move
	 * 
	 * @return string representing move data
	 */
	public String toString() {
		String retStr = name + "\n";
		retStr += power + "\n";
		retStr += "Category: " + category + "\n";
		retStr += "Type: " + type;
		return retStr;
	}

	/**
	 * Create a move data entry given a string that describes the move
	 * 
	 * @param moveData
	 *            - the raw move string
	 */
	public MoveData(String moveData) {
		String[] x = moveData.split(",");
		name = x[0];
		power = Integer.parseInt(x[1]);
		if (x[2].equals("PHYSICAL")) {
			category = MOVECATEGORY.PHYSICAL;
		} else if (x[2].equals("SPECIAL")) {
			category = MOVECATEGORY.SPECIAL;
		} else if (x[2].equals("STAT")) {
			category = MOVECATEGORY.STAT;
		} else {
			category = MOVECATEGORY.PHYSICAL;
		}

		if (x[3].equals("FLYING")) {
			type = "FLYING";
		} else {
			type = "NORMAL";
		}
		movePP = Integer.parseInt(x[4]);
		accuracy = Integer.parseInt(x[5]);
	}

	/**
	 * Check if the move data is valid
	 * 
	 * @return whether or not the move data is valid
	 */
	public boolean isValidData() {
		return (name != null && type != null && power != -1 && category != null && type != null && movePP != -1 && accuracy != -1);
	}
}
