package com.dre.itemsxl;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.dre.itemsxl.Events;
import com.dre.itemsxl.yaml.BlockYaml;
import com.dre.itemsxl.yaml.InventoryYaml;
import com.dre.itemsxl.yaml.ItemYaml;
import com.dre.itemsxl.yaml.SchematicYaml;

public class P extends JavaPlugin {

	public static String path;
	public static Config config = null;	
	
	public static Map<String, ItemYaml> items = new HashMap<String, ItemYaml>();
	public static Map<String, SchematicYaml> schematics = new HashMap<String, SchematicYaml>();
	public static Map<String, BlockYaml> blocks = new HashMap<String, BlockYaml>();
	public static Map<String, InventoryYaml> inventory = new HashMap<String, InventoryYaml>();

	public static File file;
	public static Plugin plugin;

	
	public void onEnable() {
		plugin = this;
		path = getDataFolder().getAbsolutePath();
		initConfig();
		loadItems();
		loadSchematics();
		loadBlocks();
		getServer().getPluginManager().registerEvents(new Events(), this);

	}


	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,String[] args) {
		
		return new OnCommand().getBoolean(sender, cmd, label, args);
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
	
	
	public void reloadPConfig() {
		reloadConfig();
		config.setConfig(getConfig().getValues(true));
		this.getServer().resetRecipes();
		loadItems();
		loadSchematics();
	}
	
	/* >>P.Class<< */
	
	public static Plugin getPlugin() {
		return plugin;
	}
	
	/* >>Path<< */
	
	public static String getPath() {
		return path;
	}
	
	/* >>Config<< */

	public static Config getPluginConfig() {
		return config;
	}
	
	/* >>Schematic<< */
	
	/**
	 * Lädt die Schematics aus dem Ordner. Und fügt diese in die "schematics" Map ein. 
	 */
	/*
	 *  Nicht die schematics mit schematics.clear leeren! 
	 *  Die temporeren schematics werden mit der UUID zwischen gespeichert und würden sonst verloren gehen.
	 */
	public void loadSchematics() {
		File file = new File(getPath() + "/schematics");
		for (int i = 0; i < file.list().length; i++) {
			String[] parts = file.list()[i].split(".yml");
			SchematicYaml schematic = new SchematicYaml(parts[0]);
			schematic.load();
			if(schematics.containsKey(parts[0])){
				schematics.replace(parts[0], schematic);
			}else{
				schematics.put(parts[0], schematic);
			}			
		}
	}

	public static SchematicYaml getSchematic(String name) {
		return schematics.get(name);
	}
	
	public static void addSchematic(String key, SchematicYaml schematic){
		schematics.put(key, schematic);
	}
	
	public static boolean containSchematic(SchematicYaml schematic){
		return schematics.containsValue(schematic);
	}
	
	public static boolean containSchematic(String key){
		return schematics.containsKey(key);
	}
	
	/* >>Items<< */
	
	/**
	 * Lädt die Items aus dem Ordner. Und fügt diese in der "Items" Map ein.
	 */
	public void loadItems() {
		items.clear();
		File file = new File(getPath() + "/items");
		for (int i = 0; i < file.list().length; i++) {
			String[] parts = file.list()[i].split(".yml");
			ItemYaml itemYaml = new ItemYaml(parts[0]);
			itemYaml.load();
		}
	}
	
	public static ItemYaml getItemYaml(String key) {
		return items.get(key);
	}
	
	public static void addItemYaml(ItemYaml itemYaml){
		items.put(itemYaml.getName(), itemYaml);
	}
	
	public static Boolean containsItem(ItemYaml item) {
		return items.containsValue(item);
	}
	
	public static Boolean containsItem(String key) {
		return items.containsKey(key);
	}
	
	/* >>Blocks<< */
	
	/**
	 * Lädt die Blöcke aus dem Ordenr. Und fügt diese in der "Blocks" Map ein.
	 */
	public void loadBlocks(){
		blocks.clear();
		File file = new File(getPath() + "/blocks");
		for (int i = 0; i < file.list().length; i++) {
			String[] fileName = file.list()[i].split(".yml");
			loadBlockYaml(fileName[0]);
		}
	}
	
	public void loadBlockYaml(String fileName){
		if(blocks.containsKey(fileName)){
			blocks.remove(getBlockYaml(fileName));
		}
		BlockYaml blockYaml = new BlockYaml(fileName);
		blockYaml.load();
		
		addBlockYaml(blockYaml);
		
	}
	
	public static BlockYaml getBlockYaml(String fileName){
		return blocks.get(fileName);
	}
	
	public static void addBlockYaml(BlockYaml blockYaml){
		if(blocks.containsValue(blockYaml)){
			blocks.replace(blockYaml.getName(), blockYaml);
		}else{
			blocks.put(blockYaml.getName(), blockYaml);
		}
	}
	
	public static Boolean containsBlockYaml(BlockYaml blockYaml){
		return blocks.containsValue(blockYaml);
	}
	
	public static Boolean containsBlockYaml(String StrctureName){
		return blocks.containsKey(StrctureName);
	}
	
	/* >>Rezept<< */
	
	public void addRecipe(ShapedRecipe recipe){
		this.getServer().addRecipe(recipe);
	}
}
