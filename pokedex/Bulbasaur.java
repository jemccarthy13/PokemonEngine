package pokedex;

import factories.MoveFactory;

public class Bulbasaur extends Pokemon {
	private static final long serialVersionUID = 1L;

	public Bulbasaur(int level) {
		super(level);
		this.evolution_stages[0] = "Bulbasaur";
		this.evolution_stages[1] = "Ivysaur";
		this.evolution_stages[2] = "Venusaur";
		this.evolution_levels[1] = Integer.valueOf(16);
		this.evolution_levels[2] = Integer.valueOf(32);
		this.moves[0] = MoveFactory.SCRATCH;
		this.baseExp = 64;
		this.pokedexNumber = "001";
		setName();
		setStage();
	}
}