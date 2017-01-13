package com.dre.itemsxl;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.dre.itemsxl.inventorys.SaplingSelectInventory;
import com.dre.itemsxl.util.Metadata;
import com.dre.itemsxl.yaml.ItemYaml;
import com.dre.itemsxl.yaml.SchematicYaml;


public class OnCommand {
	private P plugin = (P) P.plugin;
	public boolean getBoolean(CommandSender sender, Command cmd, String label,String[] args){
		if (cmd.getName().equalsIgnoreCase("ixl")) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("reload")) {
					plugin.reloadPConfig();
					sender.sendMessage("Items wurden neu aus der Config geladen!");
					return true;
				}
				if(args[0].equalsIgnoreCase("test")){
					sender.sendMessage("" + P.getItemYaml("Vollkornbrot").getName());
					return true;
				}
			}
			if (sender instanceof Player) {
				Player player = (Player) sender;
				ItemStack item = player.getInventory().getItemInMainHand();
				if(args.length == 1){
					if(args[0].equalsIgnoreCase("save")){
						player.sendMessage("�4/ixl save <File Name>");
						return true;
					}
				}
				if(args.length == 2){
					if(args[0].equalsIgnoreCase("new")){
						if(args[1].equalsIgnoreCase("tree")){
							if(item.getType().equals(Material.AIR)){
								new SaplingSelectInventory(player);
								return true;
							}
							player.sendMessage("�4Ihr d�rft kein Item in der Hand halten!");
							return true;
						}
						player.sendMessage("�4/ixl new <tree|item>");
						return true;
					}
					if(args[0].equalsIgnoreCase("save")){														//�berpr�ft ob args[0] und "save" dasselbe ist 
						if(item.getType().equals(Material.SAPLING)){												//�berpr�ft ob das Item in der Hand des Spielers ein Setzling ist
							if(Metadata.hasKey(item, ItemYaml.STRUCTURE_NAME)){											//�berpr�ft ob das Item den Schl�ssel besitzt "StructureName" 
								String structureName = Metadata.getString(item, ItemYaml.STRUCTURE_NAME);					//Ein String wird angelegt der gleich heisst wie der datenwert des Schl�ssels
								if(structureName.equals("-1")){																//�berpr�ft ob noch keine Struktur auf dem Item abgespeichert ist
									if(P.containSchematic(player.getUniqueId().toString())){										//�berpr�ft ob die Schematic in der Liste existirt
										SchematicYaml schematic = P.getSchematic(player.getUniqueId().toString());					//SchematicYaml wird aus der Liste gesetzt 
										schematic.setName(args[1]);																	//schematic wird umbenannt 
										schematic.save();																			//schematic wird abgespeichert
										plugin.loadSchematics();																	//alle schematics werden neu geladen um m�gliche �berschneidungen abzufangen
										
										ItemYaml itemYaml = new ItemYaml(args[1]);													//neue ItemYaml
										itemYaml.setDisplayName("&r" + args[1]);													//Anzeige Name wird gesetzt
										itemYaml.setMaterial("SAPLING");															//Material wird festgelegt zu setzling 
										itemYaml.setDurability(Short.toString(item.getDurability()));								//Setzling wird der typ zugeordnet (Eiche, Birke, usw.) 
										List<String> lore = new ArrayList<String>();												//Neue Liste wird erzeugt f�r die Lore/Beschreibung vom Item
										lore.add("&7Enth�lt &a" + schematic.getBlocks().size() + " &7Bl�cke");						//Eine Zeile wird der Lore hinzugef�gt
										itemYaml.setLore(lore);																		//Lore wird der ItemYaml �bergeben
										itemYaml.setStructureName(args[1]);															//Die oben erzeugte Struktur wird in der ItemYaml per name festgelegt
										itemYaml.save();																			//Die ItemYaml wird abgespeichert
										plugin.loadItems();																			//Alle Items werden neu geladen um m�gliche �berschneidungen abzufangen
											
										player.getInventory().setItemInMainHand(P.getItemYaml(args[1]).getItem());					//Das neue Item wird in die Hand des Spielers gesetzt 
										return true;																				//Methode wird abgschlossen und "richtig" zur�ck gegeben
									}
									player.sendMessage("�4Ihr habt noch keine Struktur zugewiesen!");
									player.sendMessage("�7Schaut auf den Block an dem Sp�ter der Setzling steht und f�hrt einen Rechts Klick aus.");
									return true;
								}
								player.sendMessage("�4Dieser Setzling hat schon eine Struktur zugewiesen!");
								return true;
							}
							player.sendMessage("�4Ihr m�sst einen Undefinierten Setzling in der Hand halten!");
							return true;
						}
						player.sendMessage("�4Ihr m�sst einen Setzling in der Hand halten!");
						return true;
					}
					if(args[0].equalsIgnoreCase("give")){
						if(P.containsItem(args[1])){
							player.getInventory().addItem(P.getItemYaml(args[1]).getItem());
							player.sendMessage("�6Ein St�ck von dem Item '" + args[1] + "' wurde deinem Inventar hinzugef�gt!");
							return true;
						}
						player.sendMessage("�4Item nicht gefunden!");
						return true;
					}
				}
				if(args.length == 3){
					if(args[0].equalsIgnoreCase("give")){
						if(P.containsItem(args[1])){
							try {
								int amount = Integer.parseInt(args[2]);
								if(amount >= 1 && amount <= 64){
									ItemStack itemStack = P.getItemYaml(args[1]).getItem();
									itemStack.setAmount(amount);
									player.getInventory().addItem(itemStack);
									if(amount == 1){
										player.sendMessage("�6Ein St�ck von dem Item '" + args[1] + "' wurde deinem Inventar hinzugef�gt!");
										return true;
									}
									player.sendMessage("�6" + amount + " St�cke von dem Item '" + args[1] + "' wurden deinem Inventar hinzugef�gt!");
									return true;
								}
								player.sendMessage("�4Anzahl nicht zwischen 1 und 64!");
								return true;
							} catch (Exception e) {
								player.sendMessage("�4/ixl give <ItemName> <anzahl>");
								player.sendMessage("�4Die Anzahl muss eine Ganzzahl sein z.B '16'");
								return true;
							}							
						}
						player.sendMessage("�4Item nicht gefunden!");
						return true;
					}
				}
			}
		}
		return false;
	}
}
