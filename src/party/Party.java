package party;

import java.io.Serializable;
import java.util.ArrayList;

// ////////////////////////////////////////////////////////////////////////
//
// PokemonList - a definition of a type equivalent to ArrayList<Pokemon>
//
// ////////////////////////////////////////////////////////////////////////
public class Party extends ArrayList<PartyMember> implements Serializable {
	private static final long serialVersionUID = 8519420241334625610L;
}