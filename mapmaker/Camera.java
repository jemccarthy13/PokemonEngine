package mapmaker;

public class Camera {
	static final float floatiness = 0.002F;
	int viewWidth;
	int viewHeight;
	float viewx;
	float viewy;
	float speedx;
	float speedy;
	float trackingx;
	float trackingy;
	Sprite trackSprite = null;

	void trackSprite(Sprite paramSprite) {
		this.trackSprite = paramSprite;
	}

	void setViewSize(int paramInt1, int paramInt2) {
		this.viewWidth = paramInt1;
		this.viewHeight = paramInt2;
	}

	public void logic() {
		if (this.trackSprite != null) {
			this.trackingx = this.trackSprite.getX();
			this.trackingy = this.trackSprite.getY();
		}
		this.speedx += (this.trackingx - this.viewx) * 0.002F;
		this.speedy += (this.trackingy - this.viewy) * 0.002F;

		this.speedx *= 0.94F;
		this.speedy *= 0.94F;

		this.viewx += this.speedx;
		this.viewy += this.speedy;
	}
}

/*
 * Location: C:\eclipse\workspace\PokemonOrange.jar
 * 
 * Qualified Name: mapmaker.Camera
 * 
 * JD-Core Version: 0.7.0.1
 */