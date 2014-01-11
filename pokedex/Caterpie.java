package pokedex;

import factories.MoveFactory;

public class Caterpie extends Pokemon {
	private static final long serialVersionUID = 1L;

	public Caterpie(int level) {
		super(level);
		this.evolution_stages[0] = "Caterpie";
		this.evolution_stages[1] = "Metapod";
		this.evolution_stages[2] = "Butterfree";
		this.evolution_levels[1] = Integer.valueOf(7);
		this.evolution_levels[2] = Integer.valueOf(10);
		this.moves[0] = MoveFactory.TACKLE;
		this.baseExp = 39;
		this.pokedexNumber = "010";
		setName();
		setStage();
	}
}