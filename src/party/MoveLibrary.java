package party;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Look through the Move data file, map move Name->Data for each move in that
 * file
 */
public class MoveLibrary extends HashMap<String, MoveData> {

	private static final long serialVersionUID = 2297312743092782181L;
	private static MoveLibrary m_instance = new MoveLibrary();

	/**
	 * Creates a new move library by mapping move name -> data
	 */
	private MoveLibrary() {
		String filePath = "resources/data/MOVES.MDAT";
		try (FileInputStream fs = new FileInputStream(filePath); Scanner s = new Scanner(fs)) {
			while (s.hasNext()) {
				MoveData m = new MoveData(s.nextLine());
				put(m.name, m);
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
	public static MoveLibrary getInstance() {
		return m_instance;
	}
}