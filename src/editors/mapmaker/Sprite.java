package editors.mapmaker;

import java.awt.Graphics;

public abstract interface Sprite {
	public static final float RIGHT = 0.0F;
	public static final float DOWN_RIGHT = 0.7853982F;
	public static final float DOWN = 1.570796F;
	public static final float DOWN_LEFT = 2.356195F;
	public static final float LEFT = 3.141593F;
	public static final float UP_LEFT = 3.926991F;
	public static final float UP = 4.712389F;
	public static final float UP_RIGHT = 5.497787F;

	public abstract void render(Graphics paramGraphics, Camera paramCamera);

	public abstract float getX();

	public abstract float getY();

	public abstract void logic();
}