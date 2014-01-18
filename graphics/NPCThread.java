package graphics;

import utilities.EnumsAndConstants;
import utilities.Utils;

public class NPCThread extends Thread {
	public boolean stop;

	public NPCThread() {}

	public void run() {
		while (true) {
			if (stop) {
				break;
			}
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