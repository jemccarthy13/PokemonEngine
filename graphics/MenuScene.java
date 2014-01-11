package graphics;

import utilities.EnumsAndConstants;
import driver.Main;
import factories.SpriteFactory;

public class MenuScene {
	private Main game;
	public boolean inMain = false;
	public boolean inPokeDex = false;
	public boolean inPokemon = false;
	public boolean inBag = false;
	public boolean inPokeGear = false;
	public boolean inTrainerCard = false;
	public boolean inSave = false;
	public boolean inOption = false;
	public boolean inExit = false;
	public boolean inConversation = false;
	public int currentSelectionMain;
	public int currentSelectionItemX;
	public int currentSelectionItemY;
	public int currentSelectionPokeGear;
	public int currentSelectionSave;
	public int currentSelectionOption;
	public boolean cancelbutton = false;
	SpriteFactory sprite_lib = EnumsAndConstants.sprite_lib;
	public NPC conversation;
	public int stage = 0;

	public MenuScene(Main theGame) {
		this.game = theGame;
		Start();
	}

	public void Start() {
		this.currentSelectionMain = 2;
		this.currentSelectionItemX = 0;
		this.currentSelectionItemY = 0;
		this.currentSelectionPokeGear = 0;
		this.currentSelectionSave = 0;
	}

	public void PokeDex() {
		this.inMain = false;
		this.inPokeDex = true;
	}

	public void Pokemon() {
		this.inMain = false;
		this.inPokemon = true;
		System.out.println("Pokemon");
	}

	public void Bag() {
		this.inMain = false;
		this.inBag = true;
		System.out.println("Bag");
	}

	public void PokeGear() {
		this.inMain = false;
		this.inPokeGear = true;
		System.out.println("PokeGear");
	}

	public void TrainerCard() {
		this.inMain = false;
		this.inTrainerCard = true;
		System.out.println("Trainer Card");
	}

	public void Save() {
		this.inMain = false;
		this.inSave = true;
		System.out.println("Save");
	}

	public void Option() {
		this.inMain = false;
		this.inOption = true;
		System.out.println("Option");
	}

	public void Exit() {
		this.currentSelectionMain = 2;
		this.currentSelectionItemX = 0;
		this.currentSelectionItemY = 0;
		this.currentSelectionSave = 0;
		this.inMain = false;
		this.inConversation = false;
		this.game.inMenu = false;
		if (this.conversation != null) {
			this.conversation.setStationary(false);
		}
		System.out.println("Exit");
	}

	public void Message(NPC borderNPC) {
		this.inMain = false;
		this.inConversation = true;
		this.conversation = borderNPC;
		this.conversation.setStationary(true);
	}
}