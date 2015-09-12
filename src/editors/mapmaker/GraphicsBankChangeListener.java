package editors.mapmaker;

abstract interface GraphicsBankChangeListener {
	public abstract void tilesetUpdated(GraphicsBank paramGraphicsBank);

	public abstract void MapTileRemoved(GraphicsBank paramGraphicsBank,
			MapTile paramMapTile);

	public abstract void MapTileAdded(GraphicsBank paramGraphicsBank,
			MapTile paramMapTile);
}