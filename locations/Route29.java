package locations;


public class Route29 extends Location {
	private static final long serialVersionUID = 1L;
	static String[] found = { "Pidgey", "Rattatta", "Sentret" };

	public Route29() {
		super("Route 29", 2, 4, found);
		this.trainers = null;
		this.probabilities[0] = 55.0D;
		this.probabilities[1] = 65.0D;
		this.probabilities[2] = 100.0D;
	}
}