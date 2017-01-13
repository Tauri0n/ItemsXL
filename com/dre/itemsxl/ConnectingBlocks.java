package com.dre.itemsxl;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;

public class ConnectingBlocks {
	private List<String> blocks = new ArrayList<String>();
	private int x = 0;
	private int y = 0;
	private int z = 0;
	private Location location;
	private boolean isTree = false;
	
	public ConnectingBlocks(Location loc) {
		location = loc;
	}
	
	
	/**@param x Offset 
	 * @param y Offset  
	 * @param z Offset 
	 */
	public void setOffset(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public List<String> start(){
		if(isTree){
			Material mat = location.getBlock().getType();
			if(!mat.equals(Material.GRASS) && !mat.equals(Material.DIRT) && !mat.equals(Material.STONE) && !mat.equals(Material.BEDROCK)){
				connectingBlocks();
			}
		}else{
			connectingBlocks();
		}
		
		return blocks;
	}
	
	public void setTree(){
		isTree = true;
	}
	
	@SuppressWarnings("deprecation")
	private void connectingBlocks(){
		Location newLocation = location;
		newLocation.add(x,y,z);
		String name = newLocation.getBlock().getType().name();
		String data = String.valueOf(newLocation.getBlock().getData());
		if(isTree){
			if(name.equals("LEAVES")){
				if(data.equals("4") || data.equals("8") || data.equals("12")){
					data = "0";
				}
				if(data.equals("5") || data.equals("9") || data.equals("13")){
					data = "1";
				}
				if(data.equals("6") || data.equals("10") || data.equals("14")){
					data = "2";
				}
				if(data.equals("7") || data.equals("11") || data.equals("15")){
					data = "3";
				}
			}
			if(name.equals("LEAVES_2")){
				if(data.equals("4") || data.equals("8") || data.equals("12")){
					data = "0";
				}
				if(data.equals("5") || data.equals("9") || data.equals("13")){
					data = "1";
				}
			}
			
		}
		blocks.add(x + "," + y + "," + z + "," + name + "," + data);
		
		newLocation.add(-x,-y,-z);
		x++;
		connectingBlock();
		x--;
		y++;
		connectingBlock();
		y--;
		z++;
		connectingBlock();
		z--;
		x--;
		connectingBlock();
		x++;
		if(isTree && y > 0 || !isTree){
			y--;
			connectingBlock();
			y++;
			
			x++;
			y--;
			connectingBlock();
			x--;
			y++;
			
			x--;
			y--;
			connectingBlock();
			x++;
			y++;
			
			y--;
			z++;
			connectingBlock();
			y++;
			z--;
		
			y--;
			z--;
			connectingBlock();
			y++;
			z++;
		}
		z--;
		connectingBlock();
		z++;
		
		/* >>TEST<< */
		x++;
		y++;
		connectingBlock();
		x--;
		y--;
		
		x--;
		y++;
		connectingBlock();
		x++;
		y--;
		
		x++;
		z++;
		connectingBlock();
		x--;
		z--;
		
		x++;
		z--;
		connectingBlock();
		x--;
		z++;
		
		x--;
		z++;
		connectingBlock();
		x++;
		z--;
		
		x--;
		z--;
		connectingBlock();
		x++;
		z++;
		
		y++;
		z++;
		connectingBlock();
		y--;
		z--;
		
		y++;
		z--;
		connectingBlock();
		y--;
		z++;
			
		
	}
	
	@SuppressWarnings("deprecation")
	private void connectingBlock(){
		String lastName = location.getBlock().getType().name();
		String lastData = String.valueOf(location.getBlock().getData());
		location.add(x, y, z);
		String name = location.getBlock().getType().name();
		String data = String.valueOf(location.getBlock().getData());
		if(isTree){
			if(name.equals("LEAVES")){
				if(data.equals("4") || data.equals("8") || data.equals("12")){
					data = "0";
				}
				if(data.equals("5") || data.equals("9") || data.equals("13")){
					data = "1";
				}
				if(data.equals("6") || data.equals("10") || data.equals("14")){
					data = "2";
				}
				if(data.equals("7") || data.equals("11") || data.equals("15")){
					data = "3";
				}
			}
			if(name.equals("LEAVES_2")){
				if(data.equals("4") || data.equals("8") || data.equals("12")){
					data = "0";
				}
				if(data.equals("5") || data.equals("9") || data.equals("13")){
					data = "1";
				}
			}
			
		}
		
		if(!blocks.contains(x + "," + y + "," + z + "," + name + "," + String.valueOf(data)) && blocks.size() < 2000){
			
			if(isTree){
				if (!location.getBlock().getType().name().equals(Material.AIR.name())){
					location.add(-x,-y,-z);
					connectingBlocks();
					return;
				}
			}else{
				if(lastName.equals(name) && lastData == data){
					location.add(-x,-y,-z);
					connectingBlocks();
					return;
				}
			}
		}
		location.add(-x,-y,-z);
	}
}
