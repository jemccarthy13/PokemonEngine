package moves;

import utilities.EnumsAndConstants;
import data_structures.Move;

public class Tackle extends Move {
	private static final long serialVersionUID = 1L;

	public Tackle() {
		super("Tackle", 35);
		this.type = EnumsAndConstants.MOVETYPE.PHYSICAL;
	}
}