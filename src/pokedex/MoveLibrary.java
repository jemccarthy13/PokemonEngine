package pokedex;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Scanner;

// ////////////////////////////////////////////////////////////////////////
//
// Look through the Move data file, map move Name->Data for each move
// in that file
//
// ////////////////////////////////////////////////////////////////////////
public class MoveLibrary extends HashMap<String, Move> {

	static MoveLibrary m_instance = new MoveLibrary();
	private static final long serialVersionUID = 1L;

	// ////////////////////////////////////////////////////////////////////////
	//
	// Maps location Name->Data
	//
	// ////////////////////////////////////////////////////////////////////////
	private MoveLibrary() {
		String filePath = "resources/data/MOVES.MDAT";
		FileInputStream fs = null;
		try {
			fs = new FileInputStream(filePath);
			Scanner s = new Scanner(fs);
			while (s.hasNext()) {
				Move m = new Move(new MoveData(s.nextLine()));
				put(m.getName(), m);
			}
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// get the single instance of the MoveLibrary
	//
	// ////////////////////////////////////////////////////////////////////////
	public static MoveLibrary getInstance() {
		return m_instance;
	}
}