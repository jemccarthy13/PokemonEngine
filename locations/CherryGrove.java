package locations;

public class CherryGrove extends Town {
	private static final long serialVersionUID = 1L;
	Store pokeMart = new Store("PokeMart", new String[] { "Poke Ball", "Potion", "Antidote", "Paralyze Heal",
			"Awaking", "Burn Cream", "Ice Heal" });

	public CherryGrove() {
		super("Cherrygrove");
	}
}