package storage;

import party.Battler;
import party.Battler.STAT;

public class Item {

	private String name;
	private STAT statEffected;
	private int statBonus;

	public Item(String line) {
		String[] strs = line.split(",");
		this.name = strs[0];

		String[] stats = strs[1].split(" ");
		this.statEffected = Battler.STAT.valueOf(stats[0]);
		this.statBonus = Integer.parseInt(stats[1]);
	}

	public String getName() {
		return this.name;
	}

	public STAT getStatEffected() {
		return this.statEffected;
	}

	public int getStatBonus() {
		return this.statBonus;
	}

	public Item() {}
}
