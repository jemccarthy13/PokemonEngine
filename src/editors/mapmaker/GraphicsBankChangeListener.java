package editors.mapmaker;

/**
 * A change listener interface
 */
abstract interface GraphicsBankChangeListener {

	/**
	 * Function called when the tileset is updated
	 * 
	 * @param bank
	 *            - the graphics bank
	 */
	public abstract void tilesetUpdated(GraphicsBank bank);

	/**
	 * Function called when a map tile is removed from the graphics bank
	 * 
	 * @param graphicsBank
	 *            - the graphics bank
	 * @param mapTile
	 *            - the map tile that was removed
	 */
	public abstract void MapTileRemoved(GraphicsBank graphicsBank, MapTile mapTile);

	/**
	 * Function called when a map tile is removed from the graphics bank
	 * 
	 * @param graphicsBank
	 *            - the graphics bank
	 * @param mapTile
	 *            - the map tile that was removed
	 */
	public abstract void MapTileAdded(GraphicsBank graphicsBank, MapTile mapTile);
}