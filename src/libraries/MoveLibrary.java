package libraries;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Scanner;

import pokedex.Move;
import pokedex.MoveData;

// ////////////////////////////////////////////////////////////////////////
//
// Look through the Move data file, map move Name->Data for each move
// in that file
//
// ////////////////////////////////////////////////////////////////////////
public class MoveLibrary {

	MoveDataMap moveData = new MoveDataMap();

	class MoveDataMap extends HashMap<String, Move> {

		private static final long serialVersionUID = 1L;

		// ////////////////////////////////////////////////////////////////////////
		//
		// Maps location Name->Data
		//
		// ////////////////////////////////////////////////////////////////////////
		public MoveDataMap() {
			String filePath = "resources/data/MOVES.MDAT";
			FileInputStream fs = null;
			try {
				fs = new FileInputStream(filePath);

				Scanner s = new Scanner(fs);
				while (s.hasNext()) {
					Move m = new Move(new MoveData(s.nextLine()));
					this.put(m.getName(), m);
				}
				s.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// make new Move from the data in the dictionary
	//
	// ////////////////////////////////////////////////////////////////////////
	public Move getMove(String name) {

		return moveData.get(name);
	}
}