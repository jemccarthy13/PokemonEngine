package party;

import java.util.ArrayList;
import java.util.Scanner;

public class MoveList extends ArrayList<MoveData> {

	private static final long serialVersionUID = 5121990509346726333L;

	private String name = "";
	private int capacity = 4;

	public MoveList(String memberName) {
		name = memberName;
	}

	public boolean add(MoveData move, boolean overwrite) {
		if (capacity < 4) {
			this.add(move);
		} else {
			if (overwrite) {
				System.out.println(name + " already knows 4 moves.  Make room for another?");
				Scanner s = new Scanner(System.in);
				String str = s.nextLine();
				if (str.contains("y")) {
					System.out.println("Which move to delete?");
					Integer delete = Integer.parseInt(s.nextLine());
					this.remove(delete);
					add(move);
				}
				s.close();
			} else {
				if (this.size() >= capacity) {
					this.remove(0);
				}
				add(move);
			}
		}
		return true;
	}
}
