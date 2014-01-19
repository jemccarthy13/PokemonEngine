package graphics;

import trainers.NPC;
import utilities.EnumsAndConstants;
import utilities.Utils;

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
			for (NPC other : EnumsAndConstants.npc_lib.npcs) {
				if ((other != null) && (!other.isStationary())) {
					other.setSpriteFacing(Utils.randomDirection());
				}
			}
			try {
				sleep(1000);
			} catch (InterruptedException e) {}
		}
	}
}