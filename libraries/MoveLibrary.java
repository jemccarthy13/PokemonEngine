package libraries;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
	class MoveDataMap extends HashMap<String, MoveData> {

		private static final long serialVersionUID = 1L;

		// ////////////////////////////////////////////////////////////////////////
		//
		// Maps location Name->Data
		//
		// ////////////////////////////////////////////////////////////////////////
		public MoveDataMap() {
			String filePath = "Data/MOVES.MDAT";
			FileInputStream fs = null;
			try {
				fs = new FileInputStream(filePath);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			Scanner s = new Scanner(fs);
			while (s.hasNext()) {
				MoveData md = new MoveData(s.nextLine());
				this.put(md.name, md);
			}
			s.close();
		}
	}

	MoveDataMap moveData = new MoveDataMap();

	public Move getMove(String name) {

		return new Move(moveData.get(name));
	}
}