package editors.mapmaker;

/**
 * An interface describing a change listener for the map
 */
abstract interface MapChangeListener {
	/**
	 * Event fired when the map is changing
	 * 
	 * @param isChanging
	 */
	public abstract void mapChanging(boolean isChanging);

	/**
	 * Event fired when the map has changed
	 * 
	 * @param hasChanged
	 */
	public abstract void mapChanged(boolean hasChanged);
}