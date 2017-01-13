package com.dre.itemsxl;

import java.util.List;



import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Item extends ItemStack{
	ItemMeta itemMeta = this.getItemMeta();
	
	
	public Item(String mat, String Name, List<String> lore, int amount){
		this.setType(Material.getMaterial(mat));
		this.setAmount(amount);
		itemMeta = this.getItemMeta();
		itemMeta.setDisplayName(Name);
		itemMeta.setLore(lore);
		this.setItemMeta(itemMeta);
	}
	
	public void addEnchantment(String enchantment, Integer level){
		itemMeta.addEnchant(Enchantment.getByName(enchantment), level, true);
		this.setItemMeta(itemMeta);
	}
	
	public void hideEnchants(Boolean hide){
		if(hide){
			itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}else{
			itemMeta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		this.setItemMeta(itemMeta);
	}
	
	public ItemStack getItemStack(){
		return this;
	}

	
}
