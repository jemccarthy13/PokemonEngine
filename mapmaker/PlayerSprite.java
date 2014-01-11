package mapmaker;

import java.awt.Color;
import java.awt.Graphics;

public class PlayerSprite implements Sprite {
	float facingAngle;
	float animDist;
	float x;
	float y;

	public void render(Graphics paramGraphics, int paramInt1, int paramInt2) {
		paramGraphics.setColor(Color.BLACK);
		paramGraphics.fillRect((int) (this.x - 5.0F) - paramInt1,
				(int) (this.y - 5.0F) - paramInt2, 100, 100);
	}

	public void render(Graphics paramGraphics, Camera paramCamera) {
		render(paramGraphics, (int) paramCamera.viewx - paramCamera.viewWidth
				/ 2, (int) paramCamera.viewy - paramCamera.viewWidth / 2);
	}

	public float getX() {
		return this.x;
	}

	public float getY() {
		return this.y;
	}

	public void move(float paramFloat1, float paramFloat2) {
		this.x = ((float) (this.x + Math.cos(paramFloat1) * paramFloat2));
		this.y = ((float) (this.y + Math.sin(paramFloat1) * paramFloat2));
		this.facingAngle = paramFloat1;
		this.animDist += paramFloat2;
	}

	public void logic() {
	}
}

/*
 * Location: C:\eclipse\workspace\PokemonOrange.jar
 * 
 * Qualified Name: mapmaker.PlayerSprite
 * 
 * JD-Core Version: 0.7.0.1
 */