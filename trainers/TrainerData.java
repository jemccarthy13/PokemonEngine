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

public class TrainerData {

	public String name = "";
	public Coordinate position = new Coordinate();
	public ArrayList<Image> sprites = new ArrayList<Image>();
	public PokemonList pokemon = new PokemonList();
	public int money = 0;
	public ArrayList<String> conversationText = new ArrayList<String>();
	public boolean stationary = false;
	public boolean trainer = false;
	public DIR dir = DIR.SOUTH;
	public Image sprite;

	public TrainerData(String path) {
		FileInputStream fs = null;
		try {
			fs = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Scanner s = new Scanner(fs);
		name = s.nextLine();
		String[] nextLine = s.nextLine().split(" ");
		position.setX(Integer.parseInt(nextLine[0]));
		position.setY(Integer.parseInt(nextLine[1]));

		String text = s.nextLine();
		stationary = text.replace(" ", "").equals("true");
		sprites = EnumsAndConstants.sprite_lib.getSprites(s.nextLine().trim());
		String[] conversation = s.nextLine().split("%");

		for (String x : conversation) {
			conversationText.add(x.trim());
		}
		if (s.hasNext()) {
			trainer = true;

			String[] pokemonData = s.nextLine().split(",");
			for (String x : pokemonData) {
				String[] datasplit = x.split(" ");
				pokemon.add(EnumsAndConstants.pokemon_generator.createPokemon(datasplit[0],
						Integer.parseInt(datasplit[1])));
			}
			money = Integer.parseInt(s.nextLine());

		}
	}

	public TrainerData() {}
}
