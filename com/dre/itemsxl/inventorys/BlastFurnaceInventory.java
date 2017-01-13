package com.dre.itemsxl.inventorys;



import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

public class BlastFurnaceInventory {

	Player player = null;
	Inventory inv = null;
	int[] i = {	1,0,0,0,0,0,1,1,1,
				1,0,0,0,0,0,1,1,1,
				1,0,0,0,0,0,1,0,1,
				1,0,0,0,0,0,1,1,1,
				1,1,1,1,1,1,1,1,1,
				1,0,0,0,0,0,0,0,1};
	
	@SuppressWarnings("deprecation")
	public BlastFurnaceInventory(Player player){
		this.player = player;
		
		inv = player.getServer().createInventory(null, 54, "§6Hochofen");
		ItemStack glassPanel = new ItemStack(Material.STAINED_GLASS_PANE);
		glassPanel.setAmount(1);
		MaterialData data = glassPanel.getData();
		data.setData((byte) 7);
		glassPanel.setData(data);
		
		ItemMeta meta = glassPanel.getItemMeta();
		meta.setDisplayName("§1");
		glassPanel.setItemMeta(meta);
		
		setSlot(i, glassPanel);
		player.openInventory(inv);
	}
	
	
	private void setSlot(int[] iArray, ItemStack item){
		for(int i = 0; i < iArray.length; i++){
			if(iArray[i] == 1){
				inv.setItem(i , item);
			}
		}
		
	}
}
