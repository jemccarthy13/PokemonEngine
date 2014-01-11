package data_structures;

import java.awt.Image;
import java.io.Serializable;

import list_types.BeatenTrainerList;
import locations.Town;
import utilities.EnumsAndConstants;
import utilities.EnumsAndConstants.DIR;

public class Player extends Trainer implements Serializable {
	private int gameState = 0;
	Trainer rival = new Trainer("???");
	Town saveLoc;
	public BeatenTrainerList beatenTrainers = new BeatenTrainerList();
	private static final long serialVersionUID = 1L;
	private int pokemonowned = 1;
	private int badges = 0;
	DIR dir;
	
	public Player(int x, int y, String n, EnumsAndConstants.SPRITENAMES s) {
		super(x, y, n, new String[] { "" }, s, null, 2000);
		this.sprite = EnumsAndConstants.sprite_lib.PLAYER_DOWN;
		this.sprites[0] = EnumsAndConstants.sprite_lib.PLAYER_DOWN;
		this.sprites[1] = EnumsAndConstants.sprite_lib.PLAYER_DOWN1;
		this.sprites[2] = EnumsAndConstants.sprite_lib.PLAYER_DOWN2;
		this.sprites[3] = EnumsAndConstants.sprite_lib.PLAYER_RIGHT;
		this.sprites[4] = EnumsAndConstants.sprite_lib.PLAYER_RIGHT1;
		this.sprites[5] = EnumsAndConstants.sprite_lib.PLAYER_RIGHT2;
		this.sprites[6] = EnumsAndConstants.sprite_lib.PLAYER_UP;
		this.sprites[7] = EnumsAndConstants.sprite_lib.PLAYER_UP1;
		this.sprites[8] = EnumsAndConstants.sprite_lib.PLAYER_UP2;
		this.sprites[9] = EnumsAndConstants.sprite_lib.PLAYER_LEFT;
		this.sprites[10] = EnumsAndConstants.sprite_lib.PLAYER_LEFT1;
		this.sprites[11] = EnumsAndConstants.sprite_lib.PLAYER_LEFT2;
	}

	public Player(String n, EnumsAndConstants.SPRITENAMES s) {
		super(0, 0, n, new String[] { "" }, s, null, 2000);
			this.sprite = EnumsAndConstants.sprite_lib.PLAYER_DOWN;
	}

	public int getPokemonOwned() {
		return this.pokemonowned;
	}

	public void beatTrainer(Trainer t) {
		this.beatenTrainers.add(t.getName());
	}

	public void setRivalName(String n) {
		this.rival.setName(n);
	}

	public String getRivalName() {
		return this.rival.getName();
	}

	public void setGameState(int i) {
		this.gameState = i;
	}

	public int getGameState() {
		return this.gameState;
	}

	public void setSaveLoc(Town town) {
		this.saveLoc = town;
	}

	public int getBadges() {
		return this.badges;
	}

	public void setBadges(int badges) {
		this.badges = badges;
	}

	public void setMoney(int m) {
		this.money = m;
	}

	public int getMoney() {
		return this.money;
	}

	public void setDir(DIR dir) {
		this.dir = dir;
	}

	public DIR getDir() {
		return this.dir;
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

	public void setCurrentX(int X) {
		this.loc_x = X;
	}

	public void setCurrentY(int Y) {
		this.loc_y = Y;
	}

	public void setLoc(int X, int Y) {
		setCurrentX(X);
		setCurrentY(Y);
	}

	public void changeSprite(int pixels, boolean rightFoot) {
		int direction = 3 * getDir().ordinal();
		if ((pixels >= 0) && (pixels < 4)) {
			setSprite(sprites[direction]);
		} else if ((pixels > 4) && (pixels < 8)) {
			setSprite(sprites[direction]);
		} else if ((pixels > 8) && (pixels < 12)) {
			if (!rightFoot) {
				setSprite(sprites[(direction + 1)]);
			} else {
				setSprite(sprites[(direction + 2)]);
			}
		} else if ((pixels >= 12) && (pixels < 15)) {
			if (!rightFoot) {
				setSprite(sprites[(direction + 1)]);
			} else {
				setSprite(sprites[(direction + 2)]);
			}
		} else {
			setSprite(sprites[direction]);
		}
	}
}