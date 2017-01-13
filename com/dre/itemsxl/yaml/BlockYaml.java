package com.dre.itemsxl.yaml;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.metadata.FixedMetadataValue;

import com.dre.itemsxl.P;

public class BlockYaml {

	
	
	
	/** Definiert den Namen des Blocks **/
	public static final String NAME = "Name";
	
	/** in der Metadata werden sämtliche Informationen über den block gespeichert **/
	public static final String METADATA = "Metadata";
	
	/** Definiert welches Item gedroppt wird. **/
	public static final String DROP_ITEM_NAME = "Metadata.DropItemName";
	
	/** Definiert welche Struktur auf dem Block gebunden ist. **/
	public static final String STRUCTURE_NAME = "Metadata.StructureName";
	
	/** Definiert welches Inventar sich öffnet bei einem rechtsklick. **/
	public static final String INVENTORY_NAME = "Metadata.InventoryName";
	
	/** Definiert an welchen Positionen sich dieser Block befindet.**/
	public static final String LOCATIONS = "Locations";
	
	
	private P plugin = (P) P.getPlugin();
	
	private YamlConfiguration yaml = new YamlConfiguration();
	private String name;
	private List<String> blockLocations = new ArrayList<String>();
	
	
	public BlockYaml(String name) {
		setName(name);
		yaml.createSection(METADATA);
	}
	
	public void save(){
		try {
			yaml.set(LOCATIONS, blockLocations);
			yaml.save(getPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void load(){
		try {
			yaml.load(getPath());
			setLocations();
			for (String[] location : getLocationsStringArrayList()){
				
				World world = Bukkit.getWorld(location[0]);
				Double x = Double.parseDouble(location[1]);
				Double y = Double.parseDouble(location[2]);
				Double z = Double.parseDouble(location[3]);
				
				Location loc = new Location(world, x , y, z);
				
				loc.getBlock().setMetadata(NAME, new FixedMetadataValue(plugin , getName()));
				if(hasDropItemName()){
					loc.getBlock().setMetadata(DROP_ITEM_NAME, new FixedMetadataValue(plugin , getDropItemName()));
				}
				if(hasStructureName()){
					loc.getBlock().setMetadata(STRUCTURE_NAME, new FixedMetadataValue(plugin , getStructureName()));
				}
				if(hasInventoryName()){
					loc.getBlock().setMetadata(INVENTORY_NAME, new FixedMetadataValue(plugin , getInventoryName()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Name
	 */
	private void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	/*
	 * DropItemName
	 */
	public void setDropItemName(String itemName){
		yaml.set(DROP_ITEM_NAME, itemName);
	}
	
	public String getDropItemName(){
		return yaml.getString(DROP_ITEM_NAME);
	}
	
	public boolean hasDropItemName(){
		return yaml.contains(DROP_ITEM_NAME);
	}
	
	/*
	 * StructureName
	 */
	public void setStructureName(String structureName){
		yaml.set(STRUCTURE_NAME, structureName);
	}
	
	public String getStructureName(){
		return yaml.getString(STRUCTURE_NAME);
	}
	
	public boolean hasStructureName(){
		return yaml.contains(STRUCTURE_NAME);
	}
	
	/*
	 * InventoryName
	 */
	public void setInventoryName(String inventoryName){
		yaml.set(INVENTORY_NAME, inventoryName);
	}
	
	public String getInventoryName(){
		return yaml.getString(INVENTORY_NAME);
	}
	
	public boolean hasInventoryName(){
		return yaml.contains(INVENTORY_NAME);
	}
	
	/*
	 * Locations
	 */
	private void setLocations(){
		this.blockLocations = yaml.getStringList(LOCATIONS);
	}
	
	/** String Liste mit den Locations. Ein String ist so aufgebaut: "world, x, y, z" **/
	public List<String> getLocationsStringList(){
		return blockLocations;
	}
	
	
	/** String[] Liste mit den Locations. Ein String Array ist so aufgebaut: [0] == "world" [1] == "x" [2] == "y" [3] == "z" **/
	public List<String[]> getLocationsStringArrayList(){
		
		List<String[]> result = new ArrayList<String[]>();
		for(String location:blockLocations){
			String[] parts = location.split(", ");
			result.add(parts);
		}
		return result;
	}
	
	public void addLocation(Location location){
		blockLocations.add(getString(location));
		save();
		plugin.loadBlockYaml(getName());
	}
	
	public void removeLocation(Location location){
		if(blockLocations.contains(getString(location))){
			blockLocations.remove(getString(location));
			save();
			plugin.loadBlockYaml(getName());
		}
	}
	
	/*
	 * Util
	 */
	private String getPath(){
		return P.getPath() + "/blocks/" + getName() + ".yml";
	}
	
	private String getString(Location location){
		String x = Double.toString(location.getX());
		String y = Double.toString(location.getY());
		String z = Double.toString(location.getZ());
		String worldName = location.getWorld().getName();
		return worldName + ", " + x + ", " + y + ", " + z;
	}

}
