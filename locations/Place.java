package locations;

public class Place extends Location {
	private static final long serialVersionUID = 1L;
	String name;
	Location Town;

	public Place(String n) {
		super(n);
	}
}