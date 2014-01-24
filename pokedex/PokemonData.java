package pokedex;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

// ////////////////////////////////////////////////////////////////////////
//
// PokemonData - holds all the data needed to construct a Pokmon:
// name, number, evolution stages + levels, baseExp, moves + levels learned
//
// ////////////////////////////////////////////////////////////////////////
public class PokemonData {

	public String type = null;
	public String pokedexNumber = null;

	public ArrayList<String> evolution_stages = null;
	public ArrayList<Integer> evolution_levels = null;

	public int baseExp = 0;

	public ArrayList<String> moves = null;
	public ArrayList<Integer> levelsLearned = null;

	// ////////////////////////////////////////////////////////////////////////
	//
	// Constructor - generates PokemonData from a given file + validates input
	//
	// ////////////////////////////////////////////////////////////////////////
	public PokemonData(String filePath) {
		FileInputStream fs = null;
		try {
			fs = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Scanner s = new Scanner(fs);
		if (s.hasNext())
			pokedexNumber = s.nextLine();
		if (s.hasNext()) {
			evolution_stages = new ArrayList<String>();
			for (String x : s.nextLine().split(","))
				evolution_stages.add(x);
		}
		if (s.hasNext()) {
			evolution_levels = new ArrayList<Integer>();
			for (String x : s.nextLine().split(","))
				evolution_levels.add(Integer.parseInt(x));
		}
		if (s.hasNext())
			type = s.nextLine();
		if (s.hasNext())
			baseExp = Integer.parseInt(s.nextLine());

		if (s.hasNext()) {
			moves = new ArrayList<String>();
			levelsLearned = new ArrayList<Integer>();
			for (String x : s.nextLine().split(",")) {
				String[] y = x.split(" ");
				String moveName = "";
				for (int z = 0; z < y.length - 1; z++) {
					moveName += " " + y[z];
				}
				moves.add(moveName.trim().toUpperCase());
				levelsLearned.add(Integer.parseInt(y[y.length - 1]));
			}
		}
		s.close();
	}

	public boolean isValidData() {
		return (type != null && pokedexNumber != null && evolution_stages != null && evolution_levels != null
				&& baseExp != 0 && moves != null && levelsLearned != null);
	}
}
