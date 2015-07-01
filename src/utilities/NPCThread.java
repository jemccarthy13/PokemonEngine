package utilities;

import trainers.Actor;
import trainers.NPCLibrary;

// ////////////////////////////////////////////////////////////////////////////
//
// NPCThread handles random movement for NPCs every second
//
// ////////////////////////////////////////////////////////////////////////////
public class NPCThread extends Thread {
	public boolean stop = false;

	public NPCThread() {}

	public void run() {
		while (true) {
			if (stop) {
				// stop the npc thread to pause logic during a battle
				break;
			}
			// otherwise move each NPC
			for (Actor other : NPCLibrary.getInstance().values()) {
				if ((other != null) && (!other.isStationary())) {
					other.setSpriteFacing(RandomNumUtils.randomDirection());
				}
			}
			try {
				sleep(1000);
			} catch (InterruptedException e) {}
		}
	}
}