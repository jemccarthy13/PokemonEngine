package pokedex;

import factories.MoveFactory;

public class Pidgey extends Pokemon {
	private static final long serialVersionUID = 1L;

	public Pidgey(int level) {
		super(level);
		this.evolution_stages[0] = "Pidgey";
		this.evolution_stages[1] = "Pidgeotto";
		this.evolution_stages[2] = "Pidgeot";
		this.evolution_levels[1] = Integer.valueOf(18);
		this.evolution_levels[2] = Integer.valueOf(36);
		this.moves[0] = MoveFactory.TACKLE;
		this.moves[1] = MoveFactory.SANDATTACK;
		this.baseExp = 50;
		this.pokedexNumber = "016";
		setName();
		setStage();
	}
}