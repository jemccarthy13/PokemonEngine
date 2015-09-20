package trainers;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Java 'typedef' matches TrainerList to ArrayList<Actor>
 */
public class TrainerList extends ArrayList<Actor> implements Serializable {
	/**
	 * Serialization information
	 */
	private static final long serialVersionUID = 3766075181147117671L;
}