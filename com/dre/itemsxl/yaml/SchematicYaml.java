package com.dre.itemsxl.yaml;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

import com.dre.itemsxl.P;





public class SchematicYaml {
	

	public static final String BLOCKS = "Blocks";
	
	
	private YamlConfiguration yaml = new YamlConfiguration();
	
	private String name;
	private List<String> blocks = new ArrayList<String>();

	
	
	public SchematicYaml(String name){
		setName(name);
	}
	
	public void save(){
		yaml.set(BLOCKS, getBlocks());
		
		try{
			yaml.save(getPath());
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void load(){
		try{
			yaml.load(getPath());
			setBlocks(yaml.getStringList(BLOCKS));
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/*
	 * Name
	 */
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	/*
	 * Blocks
	 */
	public List<String> getBlocks() {
		return blocks;
	}

	public void setBlocks(List<String> blocks) {
		this.blocks = blocks;
	}
	
	/*
	 * Util
	 */
	private String getPath(){
		return P.getPath() + "/schematics/" + getName() + ".yml";
	}
}
