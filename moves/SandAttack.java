package moves;

import utilities.EnumsAndConstants;
import data_structures.Move;

public class SandAttack extends Move {
	private static final long serialVersionUID = 1L;

	public SandAttack() {
		super("SAND-ATTACK", 0);
		this.type = EnumsAndConstants.MOVETYPE.STAT;
	}
}