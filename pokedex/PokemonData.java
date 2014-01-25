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

	public String toString() {
		String retStr = "Name: " + this.evolution_stages.get(0) + "\n";
		retStr += "PokedexNumber: " + this.pokedexNumber + "\n";
		retStr += "Base exp: " + this.baseExp + "\n";
		return retStr;
	}

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
			this.pokedexNumber = s.nextLine();
		if (s.hasNext()) {
			this.evolution_stages = new ArrayList<String>();
			for (String x : s.nextLine().split(","))
				this.evolution_stages.add(x);
		}
		if (s.hasNext()) {
			this.evolution_levels = new ArrayList<Integer>();
			for (String x : s.nextLine().split(","))
				this.evolution_levels.add(Integer.parseInt(x));
		}
		if (s.hasNext())
			this.type = s.nextLine();
		if (s.hasNext())
			this.baseExp = Integer.parseInt(s.nextLine());

		if (s.hasNext()) {
			this.moves = new ArrayList<String>();
			this.levelsLearned = new ArrayList<Integer>();
			for (String x : s.nextLine().split(",")) {
				String[] y = x.split(" ");
				String moveName = "";
				for (int z = 0; z < y.length - 1; z++) {
					moveName += " " + y[z];
				}
				this.moves.add(moveName.trim().toUpperCase());
				this.levelsLearned.add(Integer.parseInt(y[y.length - 1]));
			}
		}
		s.close();
	}

	public boolean isValidData() {
		return (this.type != null && this.pokedexNumber != null && this.evolution_stages != null
				&& this.evolution_levels != null && this.baseExp != 0 && this.moves != null && this.levelsLearned != null);
	}
}
