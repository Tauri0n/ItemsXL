package com.dre.itemsxl;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.dre.itemsxl.inventorys.BlastFurnaceInventory;
import com.dre.itemsxl.util.Metadata;
import com.dre.itemsxl.yaml.BlockYaml;
import com.dre.itemsxl.yaml.ItemYaml;
import com.dre.itemsxl.yaml.SchematicYaml;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Events implements Listener {
	static Boolean index = false;
	private P plugin = (P) P.getPlugin();

	
	
	@EventHandler
	public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent e) {

		if (Metadata.getInt(e.getItem(), ItemYaml.SATURATION) != null) {
			int duration = Metadata.getInt(e.getItem(), ItemYaml.SATURATION);
			if (duration > 0) {
				PotionEffectType type = PotionEffectType.SATURATION;
				PotionEffect potion = new PotionEffect(type, duration, 0);
				e.getPlayer().addPotionEffect(potion, true);
			} else if (duration < 0) {
				PotionEffectType type = PotionEffectType.SATURATION;
				PotionEffect potion = new PotionEffect(type, -duration, -2);
				e.getPlayer().addPotionEffect(potion, true);
			}
		}
	}

	@EventHandler
	public void onCraft(CraftItemEvent e) {
		ItemStack resultItem = e.getRecipe().getResult();
		if(Metadata.hasKey(resultItem, ItemYaml.NAME)){
		
			String name = Metadata.getString(resultItem, ItemYaml.NAME);
			ItemYaml config = P.getItemYaml(name);
			
			PermissionUser user = PermissionsEx.getUser((Player) e.getWhoClicked());
			if(config.hasCraftPermissions()){
				if(user.has(config.getCraftPermission())){
					boolean rightItems = true;
					int recipeIndex = 0;
					String[] recipe = config.getNewRecipe();
					for(ItemStack item : e.getInventory().getStorageContents()){
						if(Metadata.hasKey(item, ItemYaml.NAME)){
							if(Metadata.getString(item, ItemYaml.NAME) != recipe[recipeIndex]){
								rightItems = false;
							}
						}
						
						recipeIndex++;
					}
					if(!rightItems){
						e.setCancelled(true);
					}
					return;
				}
				e.setCancelled(true);
			}
			
		}
		
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onGrow(StructureGrowEvent e) {
		Location location = e.getLocation();
		if(Metadata.hasKey(location, BlockYaml.STRUCTURE_NAME)){
			e.setCancelled(true);
			String sName = Metadata.getString(location, BlockYaml.STRUCTURE_NAME);
			for (String block : P.getSchematic(sName).getBlocks()) {
				String[] parts = block.split(",");
				location.add(Short.parseShort(parts[0]),Short.parseShort(parts[1]), Short.parseShort(parts[2]));
				Material material = location.getBlock().getType();
				if(!material.isSolid()){
					if(Metadata.hasKey(location, BlockYaml.NAME)){
						String bName = Metadata.getString(location, BlockYaml.NAME);
						BlockYaml blockYaml = new BlockYaml(bName);
						if(P.containsBlockYaml(bName)){
							blockYaml = P.getBlockYaml(bName);
						}
						blockYaml.removeLocation(location);
						blockYaml.save();
					}
					location.getBlock().setType(Material.getMaterial(parts[3]));
					location.getBlock().setData(Byte.parseByte(parts[4]));
				}
				
				location.add(-Short.parseShort(parts[0]),-Short.parseShort(parts[1]), -Short.parseShort(parts[2]));
			}
		}
		
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onClick(PlayerInteractEvent e) {

		if (index) {
			index = false;
		} else {
			index = true;
		}
		if (e.getAction().toString().equalsIgnoreCase("right_click_block")) {
			Material type = e.getPlayer().getInventory().getItemInMainHand().getType();
			if (type.equals(Material.PAPER) && index) {
				Location location = e.getClickedBlock().getLocation();
				SchematicYaml schematic = new SchematicYaml("test");
				
				ConnectingBlocks connectingBlocks = new  ConnectingBlocks(location);
				connectingBlocks.setTree();
				schematic.setBlocks(connectingBlocks.start());
				schematic.save();
				plugin.loadSchematics();
				e.getPlayer().sendMessage(schematic.getBlocks().size() + " Blöcke wurden hinzugefügt.");
			}
			else if(type.equals(Material.BOOK)){
				Location location = e.getClickedBlock().getLocation().add(0,1,0);
				for (String block : P.getSchematic("test").getBlocks()) {
					String[] parts = block.split(",");
					location.add(Short.parseShort(parts[0]),Short.parseShort(parts[1]), Short.parseShort(parts[2]));
					location.getBlock().setType(Material.getMaterial(parts[3]));
					location.getBlock().setData(Byte.parseByte(parts[4]));
					location.add(-Short.parseShort(parts[0]),-Short.parseShort(parts[1]), -Short.parseShort(parts[2]));
				}
			}
			else if(type.equals(Material.SAPLING)){
				if(Metadata.hasKey(e.getItem(), ItemYaml.STRUCTURE_NAME)){
					if(Metadata.getString(e.getItem(), ItemYaml.STRUCTURE_NAME).equals("-1")){
						Location location = e.getClickedBlock().getLocation();
						SchematicYaml schematic = new SchematicYaml(e.getPlayer().getUniqueId().toString());
						
						ConnectingBlocks connectingBlocks = new ConnectingBlocks(location);
						connectingBlocks.setTree();
						schematic.setBlocks(connectingBlocks.start());
						schematic.save();
						e.getPlayer().sendMessage(schematic.getBlocks().size() + " Blöcke wurden hinzugefügt.");
					}
				}
			}
			else if (e.getClickedBlock().getType().equals(Material.BRICK)) {
				if(!e.getPlayer().isSneaking()){
					new BlastFurnaceInventory(e.getPlayer());
					e.setCancelled(true);
				}
			} 
		}
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e){
		ItemStack item = e.getItemInHand();
		if(item.hasItemMeta()){
			String sName = Metadata.getString(item, ItemYaml.STRUCTURE_NAME);
			if(sName.equalsIgnoreCase("-1")){
				e.setCancelled(true);
				return;
			}
			BlockYaml blockYaml;
			
			if(Metadata.hasKey(item, ItemYaml.BLOCK_NAME)){
				String bName = Metadata.getString(item, ItemYaml.BLOCK_NAME);
				blockYaml = P.getBlockYaml(bName);
				Location location = e.getBlock().getLocation();
				blockYaml.addLocation(location);
			}
		}
	}
	
	
	@EventHandler
	public void onBreak(BlockBreakEvent e){
		Location loc = e.getBlock().getLocation();
		if(Metadata.hasKey(loc, BlockYaml.NAME)){
			e.getBlock().setType(Material.AIR);
			e.setCancelled(true);
			String bName = Metadata.getString(loc, BlockYaml.NAME);
			BlockYaml blockYaml = P.getBlockYaml(bName);
			if(Metadata.hasKey(loc, BlockYaml.DROP_ITEM_NAME)){
				String sName = Metadata.getString(loc, BlockYaml.DROP_ITEM_NAME);
				e.getBlock().getWorld().dropItem(loc, P.getItemYaml(sName).getItem());
			}
			blockYaml.removeLocation(loc);
			
			
			
		}
	}
	
	@EventHandler
	public void onInvClick(InventoryClickEvent e){
		if(e.getInventory().getName().equalsIgnoreCase("§6Wähle den Setzling")){
			e.setCancelled(true);
			if(e.getClickedInventory().getName().equalsIgnoreCase("§6Wähle den Setzling")){
				ItemStack sapling = new ItemStack(Material.SAPLING);
				ItemMeta meta = sapling.getItemMeta();
				meta.setDisplayName("§rUndefinierter Setzling");
				sapling.setItemMeta(meta);
				sapling.setDurability(e.getCurrentItem().getDurability());
				sapling = Metadata.addString(sapling, ItemYaml.STRUCTURE_NAME, "-1");
				e.getWhoClicked().getInventory().setItemInMainHand(sapling);
				e.getWhoClicked().closeInventory();
			}
		}
	}
}
