package data_structures;

public class Item {
	String name;
	int price;
	private int itemnumber;
	private int itemEffect;
	private int itemType;
	private String itemName;
	private String itemDescription;
	private boolean inBattle;

	public Item(String string, int i) {
		this.name = string;
		this.price = i;
	}

	public Item(int i) {
		this.itemnumber = i;
	}

	public String getItemName() {
		if (this.itemnumber == 0) {
			this.itemName = "";
		} else if (this.itemnumber == 1) {
			this.itemName = "Potion";
		} else if (this.itemnumber == 2) {
			this.itemName = "PokeBall";
		} else if (this.itemnumber == 3) {
			this.itemName = "Berry";
		} else if (this.itemnumber == 4) {
			this.itemName = "Bicycle";
		}
		return this.itemName;
	}

	public String getItemDescription() {
		if (this.itemnumber == 0) {
			this.itemDescription = "";
		} else if (this.itemnumber == 1) {
			this.itemDescription = "Heal a Pokemon for 20HP.";
		} else if (this.itemnumber == 2) {
			this.itemDescription = "Throw at a Wild Pokemon to capture it!";
		} else if (this.itemnumber == 3) {
			this.itemDescription = "A Pokemon may Hold this item. Restores 10HP.";
		} else if (this.itemnumber == 4) {
			this.itemDescription = "A super-fast Bike that you can ride!";
		}
		return this.itemDescription;
	}

	public int getItemEffect() {
		if (this.itemnumber == 0) {
			this.itemEffect = 0;
		} else if (this.itemnumber == 1) {
			this.itemEffect = 1;
		} else if (this.itemnumber == 2) {
			this.itemEffect = 2;
		} else if (this.itemnumber == 3) {
			this.itemEffect = 1;
		} else if (this.itemnumber == 4) {
			this.itemEffect = 3;
		}
		return this.itemEffect;
	}

	public int getItemType() {
		if (this.itemnumber == 0) {
			this.itemType = 0;
		} else if (this.itemnumber == 1) {
			this.itemType = 1;
		} else if (this.itemnumber == 2) {
			this.itemType = 2;
		} else if (this.itemnumber == 3) {
			this.itemType = 1;
		} else if (this.itemnumber == 4) {
			this.itemType = 3;
		}
		return this.itemType;
	}

	public boolean isItemUsableInBattle() {
		if (this.itemnumber == 0) {
			this.inBattle = false;
		} else if (this.itemnumber == 1) {
			this.inBattle = true;
		} else if (this.itemnumber == 2) {
			this.inBattle = true;
		} else if (this.itemnumber == 3) {
			this.inBattle = true;
		} else if (this.itemnumber == 4) {
			this.inBattle = false;
		}
		return this.inBattle;
	}
}

/*
 * Location: C:\eclipse\workspace\PokemonOrange.jar
 * 
 * Qualified Name: data_structures.Item
 * 
 * JD-Core Version: 0.7.0.1
 */