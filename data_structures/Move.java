package data_structures;

import java.io.Serializable;

public class Move implements Serializable {
	private static final long serialVersionUID = 1L;

	MoveData moveData;

	public Move(MoveData mData) {
		moveData = mData;
	}

	public int getStrength() {
		return moveData.power;
	}

	public String getName() {
		return moveData.name;
	}

	public String toString() {
		return moveData.name;
	}

	public String getType() {
		return moveData.type;
	}
}