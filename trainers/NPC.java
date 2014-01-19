package trainers;

import java.awt.Image;
import java.util.ArrayList;

import pokedex.PokemonList;
import utilities.EnumsAndConstants;
import utilities.EnumsAndConstants.DIR;

public class NPC {
	private static final long serialVersionUID = 1L;
	public TrainerData nData;

	public NPC(TrainerData npcData) {
		nData = npcData;
		setSpriteFacing(DIR.SOUTH);
	}

	public void setDirection(EnumsAndConstants.DIR dir) {
		nData.dir = dir;
	}

	public DIR getDirection() {
		return nData.dir;
	}

	public PokemonList getPokemon() {
		return nData.pokemon;
	}

	public int getCurrentX() {
		return nData.position.getX();
	}

	public int getCurrentY() {
		return nData.position.getY();
	}

	public String getName() {
		return this.nData.name;
	}

	public Image getSprite() {
		return nData.sprite;
	}

	public void setSprite(Image i) {
		nData.sprite = i;
	}

	public boolean getTalkable(Player other) {
		if (other.getCurrentY() + 1 == nData.position.getY()) {
			if (other.getCurrentX() == nData.position.getX()) {
				return true;
			}
		}
		if (other.getCurrentY() - 1 == nData.position.getY()) {
			if (other.getCurrentX() == nData.position.getX()) {
				return true;
			}
		}
		if (other.getCurrentX() + 1 == nData.position.getX()) {
			if (other.getCurrentY() == nData.position.getY()) {
				return true;
			}
		}
		if (other.getCurrentX() - 1 == nData.position.getX()) {
			if (other.getCurrentY() == nData.position.getY()) {
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
		if (dir.equals(DIR.NORTH)) {
			setSprite(nData.sprites.get(9));
		}
		if (dir.equals(DIR.SOUTH)) {
			setSprite(nData.sprites.get(0));
		}
		if (dir.equals(DIR.EAST)) {
			setSprite(nData.sprites.get(6));
		}
		if (dir.equals(DIR.WEST)) {
			setSprite(nData.sprites.get(3));
		}
	}

	public void setStationary(boolean b) {
		this.nData.stationary = b;
	}

	public boolean isStationary() {
		return this.nData.stationary;
	}

	public void move(DIR dir) {
		nData.position = nData.position.move(dir);
	}

	public String getText(int stage) {
		return this.nData.conversationText.get(stage);
	}

	public boolean isTrainer() {
		return nData.trainer;
	}

	public void setName(String n) {
		nData.name = n;
	}
}