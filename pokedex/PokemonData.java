package pokedex;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class PokemonData {

	public String type;
	public String pokedexNumber;

	public ArrayList<String> evolution_stages;
	public ArrayList<Integer> evolution_levels;

	public int baseExp;

	public ArrayList<String> moves;
	public ArrayList<Integer> levelsLearned;

	public String toString() {
		String retStr = "";
		retStr += pokedexNumber + "\n";
		for (String x : evolution_stages) {
			retStr += x + " ";
		}
		retStr += "\n" + baseExp + "\n";
		for (Integer x : evolution_levels) {
			retStr += x + " ";
		}
		retStr += "\n";
		for (String x : moves) {
			retStr += x + " ";
		}
		retStr += "\n";
		for (Integer x : levelsLearned) {
			retStr += x + " ";
		}
		return retStr;
	}

	public PokemonData(String filePath) {
		FileInputStream fs = null;
		try {
			fs = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Scanner s = new Scanner(fs);
		pokedexNumber = s.nextLine();
		evolution_stages = new ArrayList<String>();
		for (String x : s.nextLine().split(","))
			evolution_stages.add(x);
		evolution_levels = new ArrayList<Integer>();
		for (String x : s.nextLine().split(","))
			evolution_levels.add(Integer.parseInt(x));
		type = s.nextLine();
		baseExp = Integer.parseInt(s.nextLine());
		moves = new ArrayList<String>();
		levelsLearned = new ArrayList<Integer>();
		for (String x : s.nextLine().split(",")) {
			String[] y = x.split(" ");
			String moveName = "";
			for (int z = 0; z < y.length - 1; z++) {
				moveName += " " + y[z];
			}
			moves.add(moveName.trim());
			levelsLearned.add(Integer.parseInt(y[y.length - 1]));
		}
		s.close();
	}
}
