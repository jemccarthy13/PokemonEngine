package location;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import tiles.Coordinate;

// ////////////////////////////////////////////////////////////////////////
//
// LocationData - loads all data for a particular location from a file
//
// ////////////////////////////////////////////////////////////////////////
public class LocationData {

	public String name = null;
	public Boolean canFlyOutOf = null;
	public Coordinate topLeft = null, bottomRight = null;
	public ArrayList<String> pokemon = null;
	public ArrayList<Integer> probabilities = null;
	public ArrayList<Integer> minLevels = null, maxLevels = null;

	// ////////////////////////////////////////////////////////////////////////
	//
	// Constructs a LocationData object from a given path
	//
	// ////////////////////////////////////////////////////////////////////////
	public LocationData(String path) {
		FileInputStream fs = null;
		try {
			fs = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Scanner s = new Scanner(fs);
		if (s.hasNext()) {
			name = s.nextLine();
		}
		if (s.hasNext()) {
			canFlyOutOf = s.nextLine().equals("true");
		}
		String[] coordinates = null;
		if (s.hasNext()) {
			coordinates = s.nextLine().split(",");
		}
		if (coordinates != null) {
			topLeft = new Coordinate(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]));
			bottomRight = new Coordinate(Integer.parseInt(coordinates[2]), Integer.parseInt(coordinates[3]));
		}
		if (s.hasNext()) {
			pokemon = new ArrayList<String>();
			probabilities = new ArrayList<Integer>();
			String[] pokemonLine = s.nextLine().split(",");
			for (String x : pokemonLine) {
				String[] pokemonData = x.split(" ");
				pokemon.add(pokemonData[0]);
				probabilities.add(Integer.parseInt(pokemonData[1]));
			}
		}
		if (s.hasNext()) {
			minLevels = new ArrayList<Integer>();
			maxLevels = new ArrayList<Integer>();
			String[] levelInfo = s.nextLine().split(",");
			for (String x : levelInfo) {
				String[] levels = x.split(" ");
				minLevels.add(Integer.parseInt(levels[0]));
				maxLevels.add(Integer.parseInt(levels[1]));
			}
		}
		s.close();
	}

	public boolean isValidData() {
		return (name != null && canFlyOutOf != null && topLeft != null && bottomRight != null && minLevels != null && maxLevels != null);
	}
}
