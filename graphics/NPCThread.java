package graphics;

import utilities.EnumsAndConstants;
import utilities.Utils;
import data_structures.Player;

public class NPCThread extends Thread {
	NPC[] NPCs;
	public Player user;
	public boolean stop;

	public NPCThread(Player gold) {
		this.NPCs = EnumsAndConstants.npc_lib.getAll();
		this.user = gold;
	}

	public void run() {
		while (true) {
			if (stop) {
				break;
			}
			for (NPC other : this.NPCs) {
				if ((other != null) && (!other.isStationary())) {
					other.setSpriteFacing(Utils.randomDirection());
				}
			}
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}