package libraries;

import java.io.File;
import java.util.HashMap;

import location.LocationData;

// ////////////////////////////////////////////////////////////////////////
//
// Look through the Location data files and map Name->Data for each
//
// ////////////////////////////////////////////////////////////////////////
public class LocationLibrary {

	LocationDataMap locationData = new LocationDataMap();

	// ////////////////////////////////////////////////////////////////////////
	//
	// Location Map maps Name->Data
	//
	// ////////////////////////////////////////////////////////////////////////
	class LocationDataMap extends HashMap<String, LocationData> {

		private static final long serialVersionUID = 1L;

		public LocationDataMap() {
			String path = "Src/Data/Locations";
			File folder = new File(path);
			File[] listOfFiles = folder.listFiles();
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					LocationData ld = new LocationData(listOfFiles[i].getPath());
					if (ld.isValidData())
						put(ld.name, ld);
				}
			}
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// getLocation - retrieve a location from the Map
	//
	// ////////////////////////////////////////////////////////////////////////
	public LocationData getLocation(String locKey) {
		return locationData.get(locKey);
	}
}