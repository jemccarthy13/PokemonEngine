package locations;

import java.io.Serializable;
import java.util.ArrayList;

public class Town extends Location implements Serializable {
	private static final long serialVersionUID = 1L;
	ArrayList<Place> places = new ArrayList<Place>();

	public Town(String n) {
		super(n);
	}
}
