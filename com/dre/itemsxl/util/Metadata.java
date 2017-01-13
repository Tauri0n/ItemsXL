package com.dre.itemsxl.util;

import net.minecraft.server.v1_9_R2.NBTTagCompound;


import org.bukkit.craftbukkit.v1_9_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class Metadata {

	
	/*
	 * Items
	 */
	public static ItemStack addInt(ItemStack item, String key, Integer value){
		net.minecraft.server.v1_9_R2.ItemStack nms = CraftItemStack.asNMSCopy(item);
		NBTTagCompound tag = new NBTTagCompound();
		if(nms.getTag() != null){
			tag = nms.getTag();
		}else{
			nms.setTag(tag);
		}
		tag.setInt(key, value);
		return CraftItemStack.asCraftMirror(nms);
	}
	
	public static ItemStack addString(ItemStack item, String key, String value){
		net.minecraft.server.v1_9_R2.ItemStack nms = CraftItemStack.asNMSCopy(item);
		NBTTagCompound tag = new NBTTagCompound();
		if(nms.getTag() != null){
			tag = nms.getTag();
		}else{
			nms.setTag(tag);
		}
		tag.setString(key, value);
		return CraftItemStack.asCraftMirror(nms);
	}
	
	public static Integer getInt(ItemStack item, String key){
		net.minecraft.server.v1_9_R2.ItemStack nms = CraftItemStack.asNMSCopy(item);
		NBTTagCompound tag = new NBTTagCompound();
		if(nms.getTag() != null){
			tag = nms.getTag();
			if(tag.hasKey(key)){
				return tag.getInt(key);
			}
		}
		return null;
	}
	
	public static String getString(ItemStack item, String key){
		net.minecraft.server.v1_9_R2.ItemStack nms = CraftItemStack.asNMSCopy(item);
		NBTTagCompound tag = new NBTTagCompound();
		if(nms.getTag() != null){
			tag = nms.getTag();
			return tag.getString(key);
		}
		return null;
	}
	
	public static boolean hasKey(ItemStack item, String key){
		try{
			net.minecraft.server.v1_9_R2.ItemStack nms = CraftItemStack.asNMSCopy(item);
			NBTTagCompound tag = new NBTTagCompound();
			if(nms.getTag() != null){
				tag = nms.getTag();
				if(tag.hasKey(key)){
					return true;
				}
			}
			return false;
		}catch(Exception e){
			return false;
		}
	}

}
