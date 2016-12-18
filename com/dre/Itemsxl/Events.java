package com.dre.Itemsxl;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;


public class Events implements Listener{
	
	@EventHandler
	public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent e){
		
		if(e.getItem().getItemMeta().getEnchants().containsKey(Enchantment.LUCK)){
			int duration = e.getItem().getEnchantmentLevel(Enchantment.LUCK);
			e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, duration, 0), true);
		}
	}
	
	@EventHandler
	public void onCraft(CraftItemEvent e){
		
		
		if(P.items.contains(e.getRecipe().getResult())){
			PermissionUser user = PermissionsEx.getUser((Player) e.getWhoClicked());
			for(int i = 0; i < P.items.size(); i++){
				if(P.items.get(i).equals(e.getRecipe().getResult())){
					if(user.has(P.getPConfig().getString(i + Config.PERMISSION))){
						return;
					}
				}
			}
			e.setCancelled(true);
		}
	}
}
			

