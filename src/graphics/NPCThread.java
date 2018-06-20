package graphics;

import java.io.Serializable;

import trainers.Actor;
import trainers.NPCLibrary;
import utilities.RandomNumUtils;

/**
 * Handles random movement for NPCs once every second
 */
public class NPCThread extends Thread implements Serializable {

	private static final long serialVersionUID = 1650659865966804001L;
	/**
	 * Thread stop signal
	 */
	private boolean stop = false;

	private static NPCThread instance = new NPCThread();

	public static NPCThread getInstance() {
		return instance;
	}

	/**
	 * Extension of Thread, must implement "run" - this performs the random movement
	 * of the NPCs
	 */
	@Override
	public void run() {
		while (true) {
			if (this.stop) {
				// stop the npc thread to pause logic during a battle
				break;
			}
			// otherwise move each NPC
			for (Actor other : NPCLibrary.getInstance().values()) {
				if ((other != null) && (!other.isStationary())) {
					other.setDirection(RandomNumUtils.randomDirection());
				}
			}
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void stopMoving() {
		this.stop = true;
	}
}