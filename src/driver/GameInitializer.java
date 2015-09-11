package driver;


//////////////////////////////////////////////////////////////////////////
//
// theGameInitializer loads a .SAV file or starts a new theGame.  Starts all 
// necessary threads and utilities.
//
// ////////////////////////////////////////////////////////////////////////
public class GameInitializer {

	static GameInitializer m_instance;
	public GameController controller;

	public GameInitializer getInstance(GameController c) {
		controller = c;
		return m_instance;
	}

	private GameInitializer() {}

}
