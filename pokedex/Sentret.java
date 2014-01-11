package pokedex;

import factories.MoveFactory;

public class Sentret extends Pokemon {
	private static final long serialVersionUID = 1L;

	public Sentret(int level) {
		super(level);
		this.evolution_stages[0] = "Sentret";
		this.evolution_stages[1] = "Furret";
		this.evolution_stages[2] = null;
		this.evolution_levels[1] = Integer.valueOf(15);
		this.evolution_levels[2] = null;
		this.moves[0] = MoveFactory.TACKLE;
		this.baseExp = 43;
		setName();
		setStage();
	}
}