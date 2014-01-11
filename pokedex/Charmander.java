package pokedex;

import utilities.EnumsAndConstants;
import factories.MoveFactory;

public class Charmander extends Pokemon {
	private static final long serialVersionUID = 1L;

	public Charmander(int level) {
		super(level);
		this.evolution_stages[0] = "Charmander";
		this.evolution_stages[1] = "Charmeleon";
		this.evolution_stages[2] = "Charizard";
		this.evolution_levels[1] = Integer.valueOf(16);
		this.evolution_levels[2] = Integer.valueOf(36);
		this.moves[0] = MoveFactory.SCRATCH;
		this.baseExp = 62;
		this.basePokedexNumber = "004";
		if (level < evolution_levels[1]) {
			this.pokedexNumber = "004";
		} else if (level < evolution_levels[2]) {
			this.pokedexNumber = "005";
		} else {
			this.pokedexNumber = "006";
		}
		this.party_icon = EnumsAndConstants.tk.createImage(getClass().getResource(
				"../graphics_lib/Icons/icon" + this.basePokedexNumber + ".png"));
		this.back_sprite = EnumsAndConstants.tk.createImage(getClass().getResource(
				"../graphics_lib/Battlers/" + this.pokedexNumber + "b.png"));
		this.front_sprite = EnumsAndConstants.tk.createImage(getClass().getResource(
				"../graphics_lib/Battlers/" + this.pokedexNumber + ".png"));
		setName();
		setStage();
	}
}