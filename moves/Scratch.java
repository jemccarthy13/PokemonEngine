package moves;

import utilities.EnumsAndConstants;
import data_structures.Move;

public class Scratch extends Move {
	private static final long serialVersionUID = 1L;

	public Scratch() {
		super("SCRATCH", 40);
		this.type = EnumsAndConstants.MOVETYPE.PHYSICAL;
	}
}