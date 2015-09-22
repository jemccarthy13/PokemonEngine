package editors.mapmaker;

/**
 * A class representing the Camera
 */
public class Camera {
	/**
	 * Accuracy of float to truncate to
	 */
	static final float floatiness = 0.002F;
	/**
	 * Width of the viewport
	 */
	int viewWidth;
	/**
	 * Height of the viewport
	 */
	int viewHeight;
	/**
	 * Graphics helper variable
	 */
	float viewx;
	/**
	 * Graphics helper variable
	 */
	float viewy;
	/**
	 * Graphics helper variable
	 */
	float speedx;
	/**
	 * Graphics helper variable
	 */
	float speedy;
	/**
	 * Graphics helper variable
	 */
	float trackingx;
	/**
	 * Graphics helper variable
	 */
	float trackingy;
	/**
	 * The current sprite to track
	 */
	Sprite trackSprite = null;

	/**
	 * Set the trackSprite
	 * 
	 * @param paramSprite
	 *            - the sprite to track
	 */
	void trackSprite(Sprite paramSprite) {
		this.trackSprite = paramSprite;
	}

	/**
	 * Set the current size of the view
	 * 
	 * @param width
	 *            - the width to view
	 * @param height
	 *            - the height to view
	 */
	void setViewSize(int width, int height) {
		this.viewWidth = width;
		this.viewHeight = height;
	}

	/**
	 * Perform camera logic
	 */
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