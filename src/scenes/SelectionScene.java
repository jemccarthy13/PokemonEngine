package scenes;

import controller.GameController;

public class SelectionScene extends BaseScene {

	private static final long serialVersionUID = 1L;
	int maxColSelection = 0;
	int maxRowSelection = 0;

	int colSelection = 0;
	int rowSelection = 0;

	/**
	 * Handle left arrow press at scene
	 */
	@Override
	public void doLeft(GameController gameControl) {
		if (this.colSelection > 0) {
			this.colSelection--;
		}
	}

	/**
	 * Handle right arrow press at scene
	 */
	@Override
	public void doRight(GameController gameControl) {
		if (this.colSelection < this.maxColSelection) {
			this.colSelection++;
		}
	}

	/**
	 * Up arrow press at scene
	 */
	@Override
	public void doUp(GameController gameControl) {
		if (this.rowSelection > 0) {
			this.rowSelection--;
		}
	}

	/**
	 * Down arrow press at scene
	 */
	@Override
	public void doDown(GameController gameControl) {
		if (this.rowSelection < this.maxRowSelection) {
			this.rowSelection++;
		}
	}
}
