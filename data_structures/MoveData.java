package data_structures;


public class MoveData {

	public String name;
	public int power;
	public String type;

	public MoveData(String moveData) {
		String[] x = moveData.split(",");
		name = x[0];
		power = Integer.parseInt(x[1]);
		type = x[2];
	}
}
