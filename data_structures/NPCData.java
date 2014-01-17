package data_structures;

import java.awt.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import utilities.EnumsAndConstants;

public class NPCData {

	public String name = "";
	public Coordinate location = new Coordinate();
	public ArrayList<Image> sprites = new ArrayList<Image>();
	public PokemonList pokemon = new PokemonList();
	public int moneyWon = 0;
	public ArrayList<String> conversationText = new ArrayList<String>();
	public boolean stationary = false;
	public boolean trainer = false;

	public NPCData(String path) {
		FileInputStream fs = null;
		try {
			fs = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Scanner s = new Scanner(fs);
		System.out.println(path);
		name = s.nextLine();
		String[] nextLine = s.nextLine().split(" ");
		location.setX(Integer.parseInt(nextLine[0]));
		location.setY(Integer.parseInt(nextLine[1]));

		String text = s.nextLine();
		stationary = text.replace(" ", "").equals("true");
		sprites = EnumsAndConstants.sprite_lib.getSpritesForNPC(s.nextLine().trim());
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
			moneyWon = Integer.parseInt(s.nextLine());

		}
	}

	public NPCData() {}
}
