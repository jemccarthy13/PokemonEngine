package trainers;

import java.awt.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import pokedex.PokemonList;
import tiles.Coordinate;
import utilities.EnumsAndConstants;
import utilities.EnumsAndConstants.DIR;

// ////////////////////////////////////////////////////////////////////////
//
// Holds data variables common to all trainers
//
// ////////////////////////////////////////////////////////////////////////
public class TrainerData {

	public String name = null;
	public Coordinate position = null;
	public ArrayList<Image> sprites = null;
	public PokemonList pokemon = null;
	public int money = 0;
	public ArrayList<String> conversationText = null;
	public boolean stationary = false;
	public boolean trainer = false;
	public DIR dir = DIR.SOUTH;
	public Image sprite = null;

	// ////////////////////////////////////////////////////////////////////////
	//
	// Initializes data based on a given file
	//
	// ////////////////////////////////////////////////////////////////////////
	public TrainerData(String path) {
		FileInputStream fs = null;
		try {
			fs = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Scanner s = new Scanner(fs);
		if (s.hasNext())
			name = s.nextLine();

		if (s.hasNext()) {
			String[] nextLine = s.nextLine().split(" ");
			position = new Coordinate(Integer.parseInt(nextLine[0]), Integer.parseInt(nextLine[1]));
		}
		if (s.hasNext()) {
			String text = s.nextLine();
			stationary = text.replace(" ", "").equals("true");
		}
		if (s.hasNext()) {
			sprites = EnumsAndConstants.sprite_lib.getSprites(s.nextLine().trim());
			sprite = sprites.get(0);
		}
		String[] conversation = null;
		if (s.hasNext()) {
			conversationText = new ArrayList<String>();
			conversation = s.nextLine().split("%");
			for (String x : conversation)
				conversationText.add(x.trim());
		}

		if (s.hasNext()) {
			trainer = true;
			pokemon = new PokemonList();
			String[] pokemonData = s.nextLine().split(",");
			for (String x : pokemonData) {
				String[] datasplit = x.split(" ");
				pokemon.add(EnumsAndConstants.pokemon_generator.createPokemon(datasplit[0],
						Integer.parseInt(datasplit[1])));
			}
			money = Integer.parseInt(s.nextLine());

		}
	}

	public boolean isValidData() {
		return (name != null && position != null && sprites != null);
	}

	public TrainerData() {}
}
