package locations;

import list_types.TrainerList;

public class Route30 extends Location {
	private static final long serialVersionUID = 1L;
	static String[] found = { "Weedle", "Kakuna", "Pidgey" };

	public Route30() {
		super("Route 30", 3, 5, found);
		this.trainers = new TrainerList();
		this.probabilities[0] = 35.0D;
		this.probabilities[1] = 15.0D;
		this.probabilities[2] = 50.0D;
		/*
		 * Pokemon JoeyRattatta = new Rattatta(4); Trainer Joey = new
		 * Trainer("Youngster Joey", 64); Joey.caughtPokemon(JoeyRattatta);
		 * 
		 * Pokemon MikeyPidgey = new Pidgey(2); Pokemon MikeyRattatta = new
		 * Rattatta(4); Trainer Mikey = new Trainer("Youngster Mikey", 64);
		 * Mikey.caughtPokemon(MikeyPidgey); Mikey.caughtPokemon(MikeyRattatta);
		 * 
		 * Pokemon DonCaterpie = new Caterpie(3); Pokemon DonCaterpie2 = new
		 * Caterpie(3); Trainer Don = new Trainer("Bug Catcher Don", 48);
		 * Don.caughtPokemon(DonCaterpie); Don.caughtPokemon(DonCaterpie2);
		 * 
		 * this.trainers.add(Joey); this.trainers.add(Mikey);
		 * this.trainers.add(Don);
		 */
	}
}