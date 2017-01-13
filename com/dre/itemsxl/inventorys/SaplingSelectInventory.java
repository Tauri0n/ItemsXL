package com.dre.itemsxl.inventorys;



import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SaplingSelectInventory {

	Player player = null;
	Inventory inv = null;
	
	public SaplingSelectInventory(Player player){
		this.player = player;
		
		inv = player.getServer().createInventory(null, 9, "§6Wähle den Setzling");
		ItemStack sapling = new ItemStack(Material.SAPLING);
		for(int i = 0; i < 6; i++ ){
			sapling.setDurability((short) i);
			ItemMeta meta = sapling.getItemMeta();
			meta.setDisplayName("§rUndefinierter Setzling");
			inv.setItem(i, sapling);
		}
		player.openInventory(inv);
	}
}
