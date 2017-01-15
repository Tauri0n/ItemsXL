package com.dre.itemsxl;

import java.util.HashMap;
import java.util.Map;

public class Config {

	public Map<String, Object> config = new HashMap<String, Object>();
	public static final String MAX_BLOCK = "MaxBlocksForOneTree";
	
	public static final String ME_RELOAD = "Messages.Reload";
	
	
	public Config() {
		config.put(MAX_BLOCK, 2000);
	}
	
	public Map<String, Object> getConfig() {
		return config;
	}
	
	public void setConfig(Map<String, Object> config) {
		this.config = config;
	}
}