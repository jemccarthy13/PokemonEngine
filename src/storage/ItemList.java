package storage;

import java.util.HashMap;
import java.util.Iterator;

public class ItemList extends HashMap<Item, Integer> implements Iterable<Item> {
	private static final long serialVersionUID = -2316537837654587420L;

	@Override
	public Iterator<Item> iterator() {
		return this.keySet().iterator();
	}
}