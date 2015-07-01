package trainers;

import java.awt.Image;
import java.util.ArrayList;

import pokedex.PokemonList;
import tiles.Coordinate;
import trainers.Actor.DIR;

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
	public TrainerData() {}

	public boolean isValidData() {
		return (this.name != null && this.position != null && this.sprites != null);
	}
}