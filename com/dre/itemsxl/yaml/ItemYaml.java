package com.dre.itemsxl.yaml;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import com.dre.itemsxl.Item;
import com.dre.itemsxl.P;
import com.dre.itemsxl.util.Metadata;

public class ItemYaml {
	
	
	/** Definiert den Namen der .yml Datei. **/
	public static final String NAME = "ItemName";
	
	/** Definiert die Textur des Items. **/
	public static final String MATERIAL = "Material";
	
	
	/** Definiert die art des Items. (Beispiel: 1 für Orangene Wolle) **/
	public static final String DURAPILITY = "Durability";
	
	
	/** Definiert den angezeigten Name des Items. **/
	public static final String DISPLAYNAME = "DisplayName";
	
	
	/** Definiert die Beschreibzung des Items. **/
	public static final String LORE = "Lore";
	
	
	/** Definiert das Rezept mit welchem das Item hergestllt werden kann. **/
	public static final String RECIPE = "Recipe";
	
	
	/** Definiert das Craften nur mit diesen Permissions. **/
	public static final String CRAFT_PERMISSION = "CraftPermission";
	
	
	/** Definiert welche Verzauberungen auf dem Item sind. **/
	public static final String ENCHANTMENTS = "Enchantments";
	
	
	/** Definiert ob die Verzeuberungen sichtbar sind. **/
	public static final String HIDE_ENCHANTMENTS = "HideEnchantments";
	
	/** Definiert welchen Block gesetzt wird bei einem rechtsklick **/
	public static final String BLOCK_NAME = "BlockName";
	
	/** Definiert die Struktur welche auf dem Item gebunden ist.**/
	public static final String STRUCTURE_NAME = "StructureName";
	
	
	/** Definiert wie viele halbe Hungerbalken beim verzehr des Item hinzu oder abgezogen werden. **/
	public static final String SATURATION = "Saturation";
	
	
	private String name;
	
	private YamlConfiguration yaml = new YamlConfiguration();
	
	private P plugin = (P) P.getPlugin();
	
	private ItemStack item;
	private String[] recipe;
	
	/**String name: Definiert den Namen mit welchem der Code arbeitet und die Datei abspeichert.
	 *
	 */
	public ItemYaml(String name){
		setName(name);
	}

	public void save(){
		try{
			yaml.save(getPath());
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Lädt die Item.Yaml aus dem Ordner. Erzeugt ein Item mit den Spezifikationen aus der Datei und 
	 * fügt die ItemYaml der P klasse hinzu.
	 */
	public void load(){
		try{
			yaml.load(getPath());
			Item item = new Item(getMaterial(), getDisplayName(), getLore(), 1);
			if(contains(ENCHANTMENTS)){
				for(String enchantment: getEnchantments()){
					String[] parts = enchantment.split(": ");
					item.addEnchantment(parts[0], Integer.parseInt(parts[1]));
				}
				if(contains(HIDE_ENCHANTMENTS)){
					item.hideEnchants(getHideEnchantments());
				}
			}
			ItemStack itemStack = item;
			itemStack = Metadata.addString(itemStack, NAME, getName());
			if(contains(DURAPILITY)){
				itemStack.setDurability(getDurability());
			}
			if(contains(SATURATION)){
				itemStack = Metadata.addInt(itemStack, SATURATION, getSaturation());
			}
			if(contains(BLOCK_NAME)){
				itemStack = Metadata.addString(itemStack, BLOCK_NAME, getBlockName());
			}
			if(contains(STRUCTURE_NAME)){
				itemStack = Metadata.addString(itemStack, STRUCTURE_NAME, getStructureName());
			}
			if(contains(RECIPE)){
				addRecipe(itemStack, item);
			}
			this.item = itemStack;
			P.addItemYaml(this);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public ItemStack getItem(){
		return item;
	}
	
	private void addRecipe(ItemStack itemStack, Item item) {
		ShapedRecipe recipe = new ShapedRecipe(itemStack);
		recipe.shape("ABC", "DEF", "GHI");
		char[] chars = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I' };
		
		String[] newRecipe = new String[10];
		newRecipe[0] = getName();
		List<String> recipeList = getRecipe();
		for (int i = 0; i <= 8; i++) {
			if (!recipeList.get(i).equals("AIR") || recipeList.get(i) != null) {
				if (recipeList.get(i).endsWith(".yml")){
					String[] parts = recipeList.get(i).split(".yml");
					recipe.setIngredient(chars[i], P.getItemYaml(parts[0]).getItem().getType());
					newRecipe[i + 1] = parts[0];

				} else {
					recipe.setIngredient(chars[i],Material.getMaterial(recipeList.get(i)));
					newRecipe[i + 1] = null;
				}

			}
		}
		setNewRecipe(newRecipe);
		plugin.addRecipe(recipe);
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
	 * Material
	 */
	public void setMaterial(String material){
		setObject(MATERIAL, material);
	}
	
	public String getMaterial(){
		return yaml.getString(MATERIAL);
	}
	
	/*
	 * Durability	
	 */
	public void setDurability(String string){
		setObject(DURAPILITY, string);
	}
	
	public short getDurability(){
		return (Short.parseShort((String) yaml.get(DURAPILITY))) ;
	}
	
	/*
	 * DisplayName
	 */
	public void setDisplayName(String displayName){
		setObject(DISPLAYNAME, displayName);
	}
	
	public String getDisplayName(){
		return  ChatColor.translateAlternateColorCodes('&', (String)yaml.get(DISPLAYNAME));
		
	}
	
	/*
	 * Lore
	 */
	public void setLore(List<String> lore){
		setObject(LORE, lore);
	}
	

	public List<String> getLore(){
		List<String>lore = new ArrayList<String>();
		for(String part : yaml.getStringList(LORE)){
			lore.add(ChatColor.translateAlternateColorCodes('&', part));
		}
		return lore;
	}
	
	/*
	 * Recipe
	 */
	public void setRecipe(List<String> recipe){
		setObject(RECIPE, recipe);
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getRecipe(){
		return (List<String>) yaml.get(RECIPE);
	}
	
	public boolean hasRecipe(){
		if(contains(RECIPE)){
			return true;
		}
		return false;
	}
	
	public void setNewRecipe(String[] newRecipe) {
		recipe = newRecipe;
	}
	
	public String[] getNewRecipe(){
		return recipe;
	}
	
	/*
	 * CraftPermission
	 */
	public void setCraftPermission(String craftPermission){
		setObject(CRAFT_PERMISSION, craftPermission);
	}
	
	public String getCraftPermission(){
		return (String) yaml.get(CRAFT_PERMISSION);
	}
	
	public boolean hasCraftPermissions(){
		if(contains(CRAFT_PERMISSION)){
			return true;
		}
		return false;
	}
	
	/*
	 * Enchantments
	 */
	public void addEnchantment(String enchantment, int level){
		List<String> enchantments = getEnchantments();
		enchantments.add(enchantment + ": " + level); 
		setObject(ENCHANTMENTS, enchantments);
	}
	/** Der Aufbau eines String typs: enchantment + ": " + level**/
	public List<String> getEnchantments(){
		return yaml.getStringList(ENCHANTMENTS);
	}
	
	public boolean getHideEnchantments(){
		return (boolean) yaml.get(HIDE_ENCHANTMENTS);
	}
	
	/*
	 * StructureName
	 */
	public void setStructureName(String structureName){
		setObject(STRUCTURE_NAME, structureName);
	}
	
	public String getStructureName(){
		return yaml.getString(STRUCTURE_NAME);
	}
	
	/*
	 * BlockName
	 */
	public void setBlockName(String blockName){
		setObject(BLOCK_NAME, blockName);
	}
	
	public String getBlockName(){
		return yaml.getString(BLOCK_NAME);
	}
	
	/*
	 * Saturation
	 */
	public void setSaturation(int saturation){
		setObject(SATURATION, saturation);
	}
	
	public int getSaturation(){
		return (int) yaml.get(SATURATION);
	}
	
	private boolean contains(String key){
		if(yaml.contains(key)){
			return true;
		}
		return false;
	}
	
	private void setObject(String key, Object value){
		yaml.set(key, value);
	}
	
	private String getPath(){
		return P.getPath() + "/items/" + getName() + ".yml";
	}
}
