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
import org.bukkit.metadata.FixedMetadataValue;
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
						if(Metadata.getString(item, ItemYaml.NAME) != recipe[recipeIndex]){
							rightItems = false;
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
		if(location.getBlock().hasMetadata(BlockYaml.STRUCTURE_NAME)){
			e.setCancelled(true);
			for (String block : P.getSchematic(location.getBlock().getMetadata(BlockYaml.STRUCTURE_NAME).get(0).value().toString()).getBlocks()) {
				String[] parts = block.split(",");
				location.add(Short.parseShort(parts[0]),Short.parseShort(parts[1]), Short.parseShort(parts[2]));
				Material material = location.getBlock().getType();
				if(!material.isSolid()){
					if(location.getBlock().hasMetadata(BlockYaml.STRUCTURE_NAME)){
						String sName = location.getBlock().getMetadata(BlockYaml.STRUCTURE_NAME).get(0).value().toString();
						BlockYaml blockYaml = new BlockYaml(sName);
						if(P.containsBlockYaml(sName)){
							blockYaml = P.getBlockYaml(sName);
						}
						blockYaml.removeLocation(location);
						blockYaml.save();
						plugin.loadBlocks();
						location.getBlock().removeMetadata(ItemYaml.STRUCTURE_NAME, P.getPlugin());
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
						P.addSchematic(e.getPlayer().getUniqueId().toString(), schematic);
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
		if(e.getItemInHand().getType().equals(Material.SAPLING)){
			if(e.getItemInHand().hasItemMeta()){
				String sName = Metadata.getString(e.getItemInHand(), ItemYaml.STRUCTURE_NAME);
				if(sName.equals("-1")){
					e.setCancelled(true);
					return;
				}
				if(Metadata.hasKey(e.getItemInHand(), ItemYaml.STRUCTURE_NAME)){
					e.getBlockPlaced().setMetadata(BlockYaml.STRUCTURE_NAME, new FixedMetadataValue(plugin, sName));
					BlockYaml blockYaml = new BlockYaml(sName);
					if(P.containsBlockYaml(sName)){
						blockYaml = P.getBlockYaml(sName);
					}
					blockYaml.addLocation(e.getBlockPlaced().getLocation());
					blockYaml.save();
					plugin.loadBlocks();
				}
			}
		}
	}
	
	
	@EventHandler
	public void onBreak(BlockBreakEvent e){
		if(e.getBlock().hasMetadata(BlockYaml.STRUCTURE_NAME)){
			String sName = e.getBlock().getMetadata(BlockYaml.STRUCTURE_NAME).get(0).value().toString();
			BlockYaml blockYaml = new BlockYaml(sName);
			ItemStack item = P.getItemYaml(sName).getItem();
			e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), item);
			e.getBlock().setType(Material.AIR);
			e.setCancelled(true);
			if(P.containsBlockYaml(sName)){
				blockYaml = P.getBlockYaml(sName);
			}
			blockYaml.removeLocation(e.getBlock().getLocation());
			blockYaml.save();
			plugin.loadBlocks();
			e.getBlock().removeMetadata(BlockYaml.STRUCTURE_NAME, plugin);
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
