package pokedex;

import factories.MoveFactory;

public class Squirtle extends Pokemon {
	private static final long serialVersionUID = 1L;

	public Squirtle(int level) {
		super(level);
		this.evolution_stages[0] = "Squirtle";
		this.evolution_stages[1] = "Wartortle";
		this.evolution_stages[2] = "Blastoise";
		this.evolution_levels[1] = Integer.valueOf(16);
		this.evolution_levels[2] = Integer.valueOf(36);
		this.moves[0] = MoveFactory.TACKLE;
		this.baseExp = 63;
		this.pokedexNumber = "007";
		setName();
		setStage();
	}
}