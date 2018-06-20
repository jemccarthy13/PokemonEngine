package graphics;

import scenes.Scene;
import scenes.TitleScene;

public class GameGraphicsData {

	private static GameGraphicsData instance = new GameGraphicsData();

	private GameGraphicsData() {}

	public static GameGraphicsData getInstance() {
		return instance;
	}

	private Scene currentScene = TitleScene.instance;

	public void setScene(Scene toScene) {
		this.currentScene = toScene;
	}

	public Scene getScene() {
		return this.currentScene;
	}

	// ====================== Graphics control variables ===================//
	/**
	 * Painting variable, map offset in the x direction
	 */
	private int offsetX = 0;
	/**
	 * Painting variable, map offset in the y direction
	 */
	private int offsetY = 0;

	public int getOffsetX() {
		return this.offsetX;
	}

	public int getOffsetY() {
		return this.offsetY;
	}

	public void addOffsetY(int toAdd) {
		this.offsetY = this.offsetY + toAdd;
	}

	public void addOffsetX(int toAdd) {
		this.offsetX = this.offsetX + toAdd;
	}

	/**
	 * Teleportation graphics x variable
	 */
	private int start_coorX;
	/**
	 * Teleportation graphics y variable
	 */
	private int start_coorY;

	public void setStartCoordX(int i) {
		this.start_coorX = i;
	}

	public void setStartCoordY(int i) {
		this.start_coorY = i;
	}

	public int getStartCoordX() {
		return this.start_coorX;
	}

	public int getStartCoordY() {
		return this.start_coorY;
	}

}
