package party;

import java.io.Serializable;

/**
 * The data behind battle moves
 */
public class MoveData implements Serializable {

	private static final long serialVersionUID = 9003426637910342523L;

	/**
	 * todo analyze move categories for abstraction
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
	 * How powerful the move is (1-100), 100+ are super moves that require recharge
	 * todo implement recharge moves
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
	 * The number of times a move can be used before the character needs to heal or
	 * recover
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
	@Override
	public String toString() {
		String retStr = this.name + "\n";
		retStr += this.power + "\n";
		retStr += "Category: " + this.category + "\n";
		retStr += "Type: " + this.type;
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
		this.name = x[0];
		this.power = Integer.parseInt(x[1]);
		if (x[2].equals("PHYSICAL")) {
			this.category = MOVECATEGORY.PHYSICAL;
		} else if (x[2].equals("SPECIAL")) {
			this.category = MOVECATEGORY.SPECIAL;
		} else if (x[2].equals("STAT")) {
			this.category = MOVECATEGORY.STAT;
		} else {
			this.category = MOVECATEGORY.PHYSICAL;
		}

		if (x[3].equals("FLYING")) {
			this.type = "FLYING";
		} else {
			this.type = "NORMAL";
		}
		this.movePP = Integer.parseInt(x[4]);
		this.accuracy = Integer.parseInt(x[5]);
	}

	/**
	 * Check if the move data is valid
	 * 
	 * @return whether or not the move data is valid
	 */
	public boolean isValidData() {
		return (this.name != null && this.type != null && this.power != -1 && this.category != null && this.type != null
				&& this.movePP != -1 && this.accuracy != -1);
	}
}
