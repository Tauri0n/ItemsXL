package com.dre.Itemsxl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {

	public Map<String, Object> config = new HashMap<String, Object>();
	
	private static List<String> defaultLore0 = new ArrayList<String>();
	private static List<String> defaultRecipe0 = new ArrayList<String>();
	private static List<String> defaultEnchantments0 = new ArrayList<String>();
	
	private static List<String> defaultLore1 = new ArrayList<String>();
	private static List<String> defaultRecipe1 = new ArrayList<String>();
	private static List<String> defaultEnchantments1 = new ArrayList<String>();
	
	public static final String MATERIAL = ".Material";
	public static final String DISPLAYNAME = ".DisplayName";
	public static final String LORE = ".Lore";
	public static final String RECIPE = ".Recipe";
	public static final String ENCHANTMENTS = ".Enchantments";
	public static final String ENCHANTMENTS_LEVEL = ".EnchantmentsLevel";
	public static final String PERMISSION = ".CraftPermission";
	
	
	
	public Config(){		
		config.put("0" + MATERIAL, "BREAD");
		config.put("0" + DISPLAYNAME, "§rKürbiskernbrot");
		defaultLore0.add("§7Regeneriert §a3.5 §7Hungerpunkte.");
		config.put("0" + LORE, defaultLore0);
		defaultRecipe0.add("MELON_SEEDS");
		defaultRecipe0.add("MELON_SEEDS");
		defaultRecipe0.add("MELON_SEEDS");
		defaultRecipe0.add("MELON_SEEDS");
		defaultRecipe0.add("BREAD");
		defaultRecipe0.add("MELON_SEEDS");
		defaultRecipe0.add("MELON_SEEDS");
		defaultRecipe0.add("MELON_SEEDS");
		defaultRecipe0.add("MELON_SEEDS");
		config.put("0" + RECIPE, defaultRecipe0);
		defaultEnchantments0.add("LUCK");
		config.put("0" + ENCHANTMENTS, defaultEnchantments0);
		config.put("0" + ENCHANTMENTS_LEVEL + ".LUCK" , 2);
		config.put("0" + PERMISSION, "ixl.bread");
		
		defaultLore1.add("§7Regeneriert §a3 §7Hungerpunkte.");
		defaultRecipe1.add("SEEDS");
		defaultRecipe1.add("SEEDS");
		defaultRecipe1.add("SEEDS");
		defaultRecipe1.add("WHEAT");
		defaultRecipe1.add("WHEAT");
		defaultRecipe1.add("WHEAT");
		defaultRecipe1.add("SEEDS");
		defaultRecipe1.add("SEEDS");
		defaultRecipe1.add("SEEDS");
		defaultEnchantments1.add("LUCK");
		config.put("1" + MATERIAL, "BREAD");
		config.put("1" + DISPLAYNAME, "§rVollkornbrot");
		config.put("1" + LORE, defaultLore1);
		config.put("1" + RECIPE, defaultRecipe1);
		config.put("1" + ENCHANTMENTS, defaultEnchantments1);
		config.put("1" + ENCHANTMENTS_LEVEL + ".LUCK" , 1);
		config.put("1" + PERMISSION, "ixl.bread");
	}
	
	public Map<String, Object> getConfig(){
		return config;
	}
	
	public void setConfig(Map<String, Object> config){
		this.config = config;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getStringList(String key){
		return (List<String>) config.get(key);
	}
	
	public String getString(String key){
		return (String) config.get(key);
	}
	
	public Integer getInt(String key){
		return (Integer) config.get(key);
	}
	
	public Boolean contains(String key){
		if(config.containsKey(key)){
			return true;
		}
		return false;
	}
}
