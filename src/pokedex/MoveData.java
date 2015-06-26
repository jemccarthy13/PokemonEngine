package pokedex;

public class MoveData {

	public String name = null;
	public int power = -1;
	public String type = null;

	public MoveData(String moveData) {
		String[] x = moveData.split(",");
		name = x[0];
		power = Integer.parseInt(x[1]);
		type = x[2];
	}

	public boolean isValidData() {
		return (name != null && type != null && power != -1);
	}
}
