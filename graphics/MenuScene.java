package graphics;

import trainers.NPC;

public class MenuScene {

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

	public NPC MENU_conversation;

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

		if (this.MENU_conversation != null) {
			this.MENU_conversation.setStationary(false);
		}
	}

	public void Message(NPC borderNPC) {
		this.MENU_inMain = false;
		this.MENU_inConversation = true;
		this.MENU_conversation = borderNPC;
		this.MENU_conversation.setStationary(true);
	}
}