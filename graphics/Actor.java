package graphics;

public class Actor {
	public int loc_x;
	public int loc_y;
	public int o_loc_x;
	public int o_loc_y;

	public Actor(int x, int y) {
		this.loc_x = x;
		this.loc_y = y;
		this.o_loc_x = x;
		this.o_loc_y = y;
	}

	public int getX() {
		return this.loc_x;
	}

	public int getY() {
		return this.loc_y;
	}

	public void act() {}

	public void move() {}

	public String[] getText() {
		return new String[] { "" };
	}
}