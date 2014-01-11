package locations;

import list_types.ItemList;

public class Store extends Place {
	private static final long serialVersionUID = 1L;
	ItemList items = new ItemList();

	public Store(String n, String[] strings) {
		super(n);
	}
}