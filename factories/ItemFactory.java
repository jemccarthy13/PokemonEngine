package factories;

import java.util.HashMap;

import list_types.ItemList;
import data_structures.Item;

public class ItemFactory {
	public Item PokeBall = new Item("Poke Ball", 200);
	public Item Potion = new Item("Potion", 300);
	public Item Antidote = new Item("Antidote", 100);
	public Item ParalyzeHeal = new Item("Paralyze Heal", 200);
	public Item Awaking = new Item("Awaking", 250);
	public Item BurnCream = new Item("Burn Cream", 250);
	public Item IceHeal = new Item("Ice Heal", 250);
	public static HashMap<String, Item> itemDict = new HashMap<String, Item>();

	public ItemFactory() {
		itemDict.put("Poke Ball", this.PokeBall);
		itemDict.put("Potion", this.Potion);
		itemDict.put("Antidote", this.Antidote);
		itemDict.put("Paralyze Heal", this.ParalyzeHeal);
		itemDict.put("Awaking", this.Awaking);
		itemDict.put("Burn Cream", this.BurnCream);
		itemDict.put("Ice Heal", this.IceHeal);
	}

	public ItemList generateInventory(String[] strings) {
		ItemList inv = new ItemList();
		for (int x = 0; x < strings.length; x++) {
			inv.add((Item) itemDict.get(strings[x]));
		}
		return inv;
	}
}