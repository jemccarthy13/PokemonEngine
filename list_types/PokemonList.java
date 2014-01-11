package list_types;

import java.io.Serializable;
import java.util.ArrayList;

import pokedex.Pokemon;

public class PokemonList extends ArrayList<Pokemon> implements Serializable {
	private static final long serialVersionUID = 1L;
}