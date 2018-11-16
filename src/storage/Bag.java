package storage;

import java.util.HashMap;

import storage.Bag.POCKETS;

/**
 * The Bag is a Map of keys (i.e. the pocket name) to ItemList.
 */
public class Bag extends HashMap<POCKETS, ItemList> {

	private static final long serialVersionUID = -767337606921097310L;

	private POCKETS currentPocket = POCKETS.ITEMS;

	public Bag() {
		for (POCKETS p : POCKETS.values()) {
			this.put(p, new ItemList());
		}
	}

	/**
	 * Enumeration for each of the POCKETS in the bag.
	 */
	public enum POCKETS {
		ITEMS(0, "ITEMS"), KEYITEMS(1, "KEY ITEMS"), BALLS(2, "BALLS"), MACHINES(3, "HM/TM");

		private int pocketNum;
		private String pocketString;

		POCKETS(int val, String str) {
			this.pocketNum = val;
			this.pocketString = str;
		}

		public int getNumber() {
			return this.pocketNum;
		}

		@Override
		public String toString() {
			return this.pocketString;
		}
	}

	/**
	 * Retrieve the pocket corresponding with a selection.
	 * 
	 * @param num
	 *            - the pocket number
	 * @return - the POCKETS enum of that pocket
	 */
	public static POCKETS getPocket(int num) {
		POCKETS retVal = POCKETS.ITEMS;
		for (POCKETS pocket : POCKETS.values()) {
			if (num == pocket.getNumber()) {
				retVal = pocket;
			}
		}

		return retVal;
	}

	/**
	 * @return - the current pocket
	 */
	public POCKETS getCurrentPocket() {
		return this.currentPocket;
	}

	/**
	 * Sets the current pocket.
	 * 
	 * @param currentPocket
	 *            - pocket to be set
	 */
	public void setCurrentPocket(POCKETS currentPocket) {
		this.currentPocket = currentPocket;
	}

	/**
	 * @return - the list of items in the current pocket
	 */
	public ItemList getCurrentItemList() {
		return this.get(this.currentPocket);
	}

	/**
	 * Add one of an item to a given pocket
	 * 
	 * @param pocket
	 *            - the pocket to add to
	 * @param it
	 *            - the item to add
	 */
	public void addToPocket(POCKETS pocket, Item it) {
		if (this.get(pocket).get(it) == null) {
			this.get(pocket).put(it, Integer.valueOf(0));
		}
		Integer p = Integer.valueOf(this.get(pocket).get(it).intValue() + 1);
		this.get(pocket).put(it, p);
	}
}
