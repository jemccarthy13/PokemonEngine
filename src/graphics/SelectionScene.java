package graphics;

import controller.GameController;
import model.GameData;

public class SelectionScene extends BaseScene {

	private static final long serialVersionUID = 1L;
	int maxColSelection = 0;
	int maxRowSelection = 0;

	/**
	 * Handle left arrow press at scene
	 */
	@Override
	public void doLeft(GameController gameControl) {
		GameData.getInstance().decrementColSelection(gameControl.getScene());
	}

	/**
	 * Handle right arrow press at scene
	 */
	@Override
	public void doRight(GameController gameControl) {
		GameData.getInstance().incrementColSelection(gameControl.getScene(), this.maxColSelection);
	}

	/**
	 * Up arrow press at scene
	 */
	@Override
	public void doUp(GameController gameControl) {
		GameData.getInstance().decrementRowSelection(gameControl.getScene());
	}

	/**
	 * Down arrow press at scene
	 */
	@Override
	public void doDown(GameController gameControl) {
		GameData.getInstance().incrementRowSelection(gameControl.getScene(), this.maxRowSelection);
	}
}
