package graphics;

import java.awt.Image;
import java.util.ArrayList;

import pokedex.PokemonList;
import trainers.Player;
import trainers.TrainerData;
import utilities.EnumsAndConstants;
import utilities.EnumsAndConstants.DIR;

public class NPC {
	private static final long serialVersionUID = 1L;
	private EnumsAndConstants.DIR dir;
	public TrainerData nData;
	public Image sprite;

	public NPC(TrainerData npcData) {
		nData = npcData;
		setSpriteFacing(DIR.SOUTH);
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
		return nData.position.getX();
	}

	public int getCurrentY() {
		return nData.position.getY();
	}

	public void moveUp() {
		this.nData.position.move(DIR.NORTH);
	}

	public void moveDown() {
		this.nData.position.move(DIR.SOUTH);
	}

	public void moveLeft() {
		this.nData.position.move(DIR.WEST);
	}

	public void moveRight() {
		this.nData.position.move(DIR.EAST);
	}

	public String getName() {
		return this.nData.name;
	}

	public Image getSprite() {
		return sprite;
	}

	public void setSprite(Image i) {
		sprite = i;
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

	public void changeLoc(DIR dir) {
		if (dir == DIR.EAST)
			nData.position.setX(getCurrentX() + 1);
		else if (dir == DIR.SOUTH)
			nData.position.setY(getCurrentY() + 1);
		else if (dir == DIR.WEST)
			nData.position.setX(getCurrentX() - 1);
		else if (dir == DIR.EAST)
			nData.position.setY(getCurrentY() - 1);
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