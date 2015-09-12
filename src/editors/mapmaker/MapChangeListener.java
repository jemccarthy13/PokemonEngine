package editors.mapmaker;

abstract interface MapChangeListener {
	public abstract void mapChanging(boolean paramBoolean);

	public abstract void mapChanged(boolean paramBoolean);
}