package tiles;

import java.io.Serializable;

// ////////////////////////////////////////////////////////////////////////
//
// ObstacleTile - an extension of a Tile that prohibits movement
//
// ////////////////////////////////////////////////////////////////////////
public class ObstacleTile extends Tile implements Serializable {
	private static final long serialVersionUID = 8939607965131857483L;
	public static final String name = "OBSTACLE";
}