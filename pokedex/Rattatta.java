package pokedex;

import utilities.EnumsAndConstants;
import factories.MoveFactory;

public class Rattatta extends Pokemon {
	private static final long serialVersionUID = 1L;

	public Rattatta(int level) {
		super(level);
		this.evolution_stages[0] = "Rattatta";
		this.evolution_stages[1] = "Raticate";
		this.evolution_stages[2] = null;
		this.evolution_levels[1] = Integer.valueOf(20);
		this.evolution_levels[2] = null;
		this.moves[0] = MoveFactory.TACKLE;
		this.baseExp = 57;
		this.pokedexNumber = "019";
		this.back_sprite = EnumsAndConstants.tk.createImage(getClass().getResource(
				"../graphics_lib/Battlers/" + this.pokedexNumber + "b.png"));
		this.front_sprite = EnumsAndConstants.tk.createImage(getClass().getResource(
				"../graphics_lib/Battlers/" + this.pokedexNumber + ".png"));
		setName();
		setStage();
	}
}