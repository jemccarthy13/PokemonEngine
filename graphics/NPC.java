package graphics;

import java.awt.Image;

import utilities.EnumsAndConstants;
import data_structures.Player;
import data_structures.Trainer;

public class NPC extends Trainer {
	private static final long serialVersionUID = 1L;
	private String name;
	private EnumsAndConstants.DIR dir;
	private Image sprite;
	private boolean stationary;
	public boolean trainer;

	public NPC(int x, int y, String n, String[] text, EnumsAndConstants.SPRITENAMES s, Image bs, boolean t) {
		super(x, y, n, text, s, null, 0);
		this.name = n;
		this.text = text;
		this.loc_x = x;
		this.loc_y = y;
		this.trainer = t;
	}

	public void setDirection(EnumsAndConstants.DIR dir) {
		this.dir = dir;
	}

	public EnumsAndConstants.DIR getDirection() {
		return this.dir;
	}

	public int getWidth() {
		return this.sprite.getWidth(null) / 4;
	}

	public int getHeight() {
		return this.sprite.getHeight(null) / 4;
	}

	public void moveUp() {
		this.loc_y -= 1;
	}

	public void moveDown() {
		this.loc_y += 1;
	}

	public void moveLeft() {
		this.loc_x -= 1;
	}

	public void moveRight() {
		this.loc_x += 1;
	}

	public String getName() {
		return this.name;
	}

	public Image getSprite() {
		return this.sprite;
	}

	public void setSprite(Image i) {
		this.sprite = i;
	}

	public boolean getTalkable(Player other) {
		if (other.getCurrentY() + 1 == getCurrentY()) {
			if (other.getCurrentX() == getCurrentX()) {
				return true;
			}
		}
		if (other.getCurrentY() - 1 == getCurrentY()) {
			if (other.getCurrentX() == getCurrentX()) {
				return true;
			}
		}
		if (other.getCurrentX() + 1 == getCurrentX()) {
			if (other.getCurrentY() == getCurrentY()) {
				return true;
			}
		}
		if (other.getCurrentX() - 1 == getCurrentX()) {
			if (other.getCurrentY() == getCurrentY()) {
				return true;
			}
		}
		return false;
	}

	public String[] getText() {
		return this.text;
	}

	public String[] getText(Player other) {
		if (other.getCurrentY() + 1 == getCurrentY()) {
			if (other.getCurrentX() == getCurrentX()) {
				return this.text;
			}
		}
		if (other.getCurrentY() - 1 == getCurrentY()) {
			if (other.getCurrentX() == getCurrentX()) {
				return this.text;
			}
		}
		if (other.getCurrentX() + 1 == getCurrentX()) {
			if (other.getCurrentY() == getCurrentY()) {
				return this.text;
			}
		}
		if (other.getCurrentX() - 1 == getCurrentX()) {
			if (other.getCurrentY() == getCurrentY()) {
				return this.text;
			}
		}
		return new String[] {};
	}

	public int getTextLength() {
		return this.text.length;
	}

	public void setSpriteFacing(EnumsAndConstants.DIR dir) {
		setDirection(dir);
		if (dir.equals(EnumsAndConstants.DIR.NORTH)) {
			setSprite(this.sprites[0]);
		}
		if (dir.equals(EnumsAndConstants.DIR.SOUTH)) {
			setSprite(this.sprites[3]);
		}
		if (dir.equals(EnumsAndConstants.DIR.EAST)) {
			setSprite(this.sprites[6]);
		}
		if (dir.equals(EnumsAndConstants.DIR.WEST)) {
			setSprite(this.sprites[9]);
		}
	}

	public void setStationary(boolean b) {
		this.stationary = b;
	}

	public boolean isStationary() {
		return this.stationary;
	}

	public void changeLoc(int dir, int loc) {
		if (dir == 0) {
			if (loc == 0) {
				setCurrentX(getCurrentX() + 1);
			}
			if (loc == 1) {
				setCurrentY(getCurrentY() + 1);
			}
		}
		if (dir == 1) {
			if (loc == 0) {
				setCurrentX(getCurrentX() - 1);
			}
			if (loc == 1) {
				setCurrentY(getCurrentY() - 1);
			}
		}
	}

	public String getText(int stage) {
		return text[stage];
	}
}