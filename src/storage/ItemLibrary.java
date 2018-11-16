package storage;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Look through the Move data file, map move Name->Data for each move in that
 * file
 */
public class ItemLibrary extends HashMap<String, Item> {

	private static final long serialVersionUID = 2297312743092782181L;
	private static ItemLibrary m_instance = new ItemLibrary();

	/**
	 * Creates a new move library by mapping move name -> data
	 */
	private ItemLibrary() {
		String filePath = "resources/data/ITEMS.MDAT";

		try (FileInputStream fs = new FileInputStream(filePath); Scanner s = new Scanner(fs)) {
			while (s.hasNext()) {
				Item m = new Item(s.nextLine());
				put(m.getName(), m);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Single instance access to the move library
	 * 
	 * @return one instance of the MoveLibrary
	 */
	public static ItemLibrary getInstance() {
		return m_instance;
	}
}