package com.dre.Itemsxl;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import com.dre.Itemsxl.Events;


public class P extends JavaPlugin {
	
	public static Config config = null;
	public static List<Item> items = new ArrayList<Item>();
	
	
	public void onEnable(){
		initConfig();
		addItems();
		getServer().getPluginManager().registerEvents(new Events(), this);
	}
	
	


	private void initConfig() {
		config = new Config();
		reloadConfig();
		getConfig().addDefaults(config.getConfig());
		getConfig().options().copyDefaults(true);
		saveConfig();
		reloadConfig();
		config.setConfig(getConfig().getValues(true));
		
	}

	public void onDisable() {
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("ixl")){
			if (sender instanceof Player){
				((Player) sender).getInventory().addItem(items.get(0));
			}
		}
		return false;
	}
	
	public void addItems(){
		items.removeAll(items);
		for(int i = 0; config.contains("" + i); i++){
			Item item = new Item(config.getString(i + Config.MATERIAL), config.getString(i + Config.DISPLAYNAME), config.getStringList(i + Config.LORE), 1);
			for(String enchantment : config.getStringList(i + Config.ENCHANTMENTS)){
				item.addEnchantment(enchantment, config.getInt(i + Config.ENCHANTMENTS_LEVEL + "." + enchantment));
			}
			items.add(item);
			addRecipe(item, config.getStringList(i + Config.RECIPE));
		}
	}
	

	private void addRecipe(Item item, List<String> recipeList) {
		ShapedRecipe recipe = new ShapedRecipe(item);
		recipe.shape("ABC","DEF","GHI");
		char[] chars = {'A','B','C','D','E','F','G','H','I'};
		for(int i=0; i<=8; i++){
			recipe.setIngredient(chars[i], Material.getMaterial(recipeList.get(i)));
		}
		this.getServer().addRecipe(recipe);
	}
	
	public static Config getPConfig(){
		return config;
	}
	
}
