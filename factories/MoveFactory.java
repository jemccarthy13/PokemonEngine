package factories;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

import data_structures.Move;
import data_structures.MoveData;

public class MoveFactory {
	class MoveDataMap extends HashMap<String, MoveData> {

		private static final long serialVersionUID = 1L;

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