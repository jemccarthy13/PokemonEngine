package graphics;

import java.io.Serializable;

import trainers.Actor;

// ////////////////////////////////////////////////////////////////////////
//
// MenuScene holds variables related to menu printing. On each key press,
// the variables are adjusted accordingly
//
// ////////////////////////////////////////////////////////////////////////
public class MenuScene implements Serializable {

	private static final long serialVersionUID = 1302432872413157945L;
	public boolean MENU_inMain = false;
	public boolean MENU_inPokeDex = false;
	public boolean MENU_inPokemon = false;
	public boolean MENU_inBag = false;
	public boolean MENU_inPokeGear = false;
	public boolean MENU_inTrainerCard = false;
	public boolean MENU_inSave = false;
	public boolean MENU_inOption = false;
	public boolean MENU_inExit = false;
	public boolean MENU_inConversation = false;

	public int MENU_currentSelectionMain;
	public int MENU_currentSelectionItemX;
	public int MENU_currentSelectionItemY;
	public int MENU_currentSelectionPokeGear;
	public int MENU_currentSelectionSave;
	public int MENU_currentSelectionOption;

	public int MENU_stage = 0;

	public Actor MENU_NPC;

	public MenuScene() {
		this.MENU_currentSelectionMain = 2;
		this.MENU_currentSelectionItemX = 0;
		this.MENU_currentSelectionItemY = 0;
		this.MENU_currentSelectionPokeGear = 0;
		this.MENU_currentSelectionSave = 0;
		this.MENU_currentSelectionOption = 0;
	}

	public void PokeDex() {
		this.MENU_inMain = false;
		this.MENU_inPokeDex = true;
	}

	public void Pokemon() {
		this.MENU_inMain = false;
		this.MENU_inPokemon = true;
	}

	public void Bag() {
		this.MENU_inMain = false;
		this.MENU_inBag = true;
	}

	public void PokeGear() {
		this.MENU_inMain = false;
		this.MENU_inPokeGear = true;
	}

	public void TrainerCard() {
		this.MENU_inMain = false;
		this.MENU_inTrainerCard = true;
	}

	public void Save() {
		this.MENU_inMain = false;
		this.MENU_inSave = true;
	}

	public void Option() {
		this.MENU_inMain = false;
		this.MENU_inOption = true;
	}

	public void Exit() {
		this.MENU_currentSelectionMain = 2;
		this.MENU_currentSelectionItemX = 0;
		this.MENU_currentSelectionItemY = 0;
		this.MENU_currentSelectionSave = 0;
		this.MENU_inMain = false;
		this.MENU_inConversation = false;

		if (this.MENU_NPC != null) {
			this.MENU_NPC.setStationary(false);
		}
	}

	public void Message(Actor borderNPC) {
		this.MENU_inMain = false;
		this.MENU_inConversation = true;
		this.MENU_NPC = borderNPC;
		this.MENU_NPC.setStationary(true);
	}
}