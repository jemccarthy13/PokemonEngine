package graphics;

import java.awt.Image;
import java.util.ArrayList;

import utilities.EnumsAndConstants;
import utilities.EnumsAndConstants.DIR;
import data_structures.NPCData;
import data_structures.Player;
import data_structures.PokemonList;

public class NPC {
	private static final long serialVersionUID = 1L;
	private EnumsAndConstants.DIR dir;
	public NPCData nData;
	public Image sprite;

	public NPC(NPCData npcData) {
		nData = npcData;
	}

	public void setDirection(EnumsAndConstants.DIR dir) {
		this.dir = dir;
	}

	public EnumsAndConstants.DIR getDirection() {
		return this.dir;
	}

	public PokemonList getPokemon() {
		return nData.pokemon;
	}

	public int getCurrentX() {
		return nData.location.getX();
	}

	public int getCurrentY() {
		return nData.location.getY();
	}

	public void moveUp() {
		this.nData.location.move(DIR.NORTH);
	}

	public void moveDown() {
		this.nData.location.move(DIR.SOUTH);
	}

	public void moveLeft() {
		this.nData.location.move(DIR.WEST);
	}

	public void moveRight() {
		this.nData.location.move(DIR.EAST);
	}

	public String getName() {
		return this.nData.name;
	}

	public Image getSprite() {
		return this.sprite;
	}

	public void setSprite(Image i) {
		this.sprite = i;
	}

	public boolean getTalkable(Player other) {
		if (other.getCurrentY() + 1 == nData.location.getY()) {
			if (other.getCurrentX() == nData.location.getX()) {
				return true;
			}
		}
		if (other.getCurrentY() - 1 == nData.location.getY()) {
			if (other.getCurrentX() == nData.location.getX()) {
				return true;
			}
		}
		if (other.getCurrentX() + 1 == nData.location.getX()) {
			if (other.getCurrentY() == nData.location.getY()) {
				return true;
			}
		}
		if (other.getCurrentX() - 1 == nData.location.getX()) {
			if (other.getCurrentY() == nData.location.getY()) {
				return true;
			}
		}
		return false;
	}

	public ArrayList<String> getText() {
		return this.nData.conversationText;
	}

	public int getTextLength() {
		return this.nData.conversationText.size();
	}

	public void setSpriteFacing(EnumsAndConstants.DIR dir) {
		setDirection(dir);
		if (dir.equals(EnumsAndConstants.DIR.NORTH)) {
			setSprite(this.nData.sprites.get(9));
			System.out.println(this.nData.sprites.get(9));
		}
		if (dir.equals(EnumsAndConstants.DIR.SOUTH)) {
			setSprite(this.nData.sprites.get(0));
			System.out.println(this.nData.sprites.get(0));
		}
		if (dir.equals(EnumsAndConstants.DIR.EAST)) {
			setSprite(this.nData.sprites.get(6));
			System.out.println(this.nData.sprites.get(6));
		}
		if (dir.equals(EnumsAndConstants.DIR.WEST)) {
			setSprite(this.nData.sprites.get(3));
			System.out.println(this.nData.sprites.get(3));
		}
	}

	public void setStationary(boolean b) {
		this.nData.stationary = b;
	}

	public boolean isStationary() {
		return this.nData.stationary;
	}

	public void changeLoc(int dir, int loc) {
		if (dir == 0) {
			if (loc == 0) {
				moveRight();
			}
			if (loc == 1) {
				moveDown();
			}
		}
		if (dir == 1) {
			if (loc == 0) {
				moveLeft();
			}
			if (loc == 1) {
				moveRight();
			}
		}
	}

	public String getText(int stage) {
		return this.nData.conversationText.get(stage);
	}

	public boolean isTrainer() {
		return nData.trainer;
	}
}