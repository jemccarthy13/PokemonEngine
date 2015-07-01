package pokedex;

public class MoveData {

	public String name = null;
	public int power = -1;
	public boolean isPhysical;
	public boolean isStat;
	public String type = null;
	public int movePP = -1;
	public int accuracy = -1;

	public MoveData(String moveData) {
		String[] x = moveData.split(",");
		name = x[0];
		power = Integer.parseInt(x[1]);
		if (x[2].equals("PHYSICAL")) {
			isPhysical = true;
			isStat = false;
		} else {
			isPhysical = false;
			isStat = true;
		}
		type = x[3];
		movePP = Integer.parseInt(x[4]);
		accuracy = Integer.parseInt(x[5]);
	}

	public boolean isValidData() {
		return (name != null && type != null && power != -1 && (isPhysical || isStat) && type != null && movePP != -1
				&& accuracy != -1);
	}
}
