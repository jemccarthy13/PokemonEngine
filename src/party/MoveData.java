package party;

import java.io.Serializable;

public class MoveData implements Serializable {

	private static final long serialVersionUID = 9003426637910342523L;

	public enum MOVETYPE {
		NORMAL, FIGHTING, POISON, BUG, DARK, GHOST, GROUND, ELECTRIC, FLYING
	}

	public enum MOVECATEGORY {
		PHYSICAL, SPECIAL, STAT
	}

	public String name = null;
	public int power = -1;

	public MOVECATEGORY category;
	public MOVETYPE type;
	public int movePP = -1;
	public int accuracy = -1;

	public String toString() {
		String retStr = name + "\n";
		retStr += power + "\n";
		retStr += "Category: " + category + "\n";
		retStr += "Type: " + type;
		return retStr;
	}

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
			type = MOVETYPE.FLYING;
		} else {
			type = MOVETYPE.NORMAL;
		}
		movePP = Integer.parseInt(x[4]);
		accuracy = Integer.parseInt(x[5]);
	}

	public boolean isValidData() {
		return (name != null && type != null && power != -1 && category != null && type != null && movePP != -1 && accuracy != -1);
	}
}
