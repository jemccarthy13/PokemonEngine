package factories;

import locations.CherryGrove;
import locations.Location;
import locations.MomsHouse;
import locations.NewBarkTown;
import locations.ProfElmLab;
import locations.Route29;
import locations.Route30;

public class LocationFactory {
	public Location ELMSLAB = new ProfElmLab();
	public Location NEWBARKTOWN = new NewBarkTown();
	public Location ROUTE29 = new Route29();
	public Location ROUTE30 = new Route30();
	public Location MOMSHOUSE = new MomsHouse();
	public Location CHERRYGROVE = new CherryGrove();

	public void initializeAll() {
		MOMSHOUSE = new MomsHouse();
		ELMSLAB = new ProfElmLab();
		NEWBARKTOWN = new NewBarkTown();
		ROUTE29 = new Route29();
		CHERRYGROVE = new CherryGrove();
		ROUTE30 = new Route30();
	}
}