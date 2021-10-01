package fr.achillebourgault.defensetowers.towers;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class Components {

	public static boolean isComponents(Block b, HashMap<String, Location> towers) {
		boolean res = false;
		
		if ((b.getType().equals(Material.EMERALD_BLOCK) || b.getType().equals(Material.BEDROCK)) && (b.getWorld().getBlockAt(b.getLocation().clone().add(0, 1, 0)).getType().toString().contains("WOOL") || b.getWorld().getBlockAt(b.getLocation().clone().add(0, 1, 0)).getType().equals(Material.REDSTONE_LAMP)) &&
				b.getWorld().getBlockAt(b.getLocation().clone().add(0, 2, 0)).getType().equals(Material.END_ROD)) {
			if (towers.containsValue(b.getLocation()))
				res = true;
		} else if ((b.getWorld().getBlockAt(b.getLocation()).getType().toString().contains("WOOL") || b.getWorld().getBlockAt(b.getLocation()).getType().equals(Material.REDSTONE_LAMP)) 
				&& b.getWorld().getBlockAt(b.getLocation().clone().add(0, 1, 0)).getType().equals(Material.END_ROD) &&
				(b.getWorld().getBlockAt(b.getLocation().clone().subtract(0, 1, 0)).getType().equals(Material.EMERALD_BLOCK) || b.getWorld().getBlockAt(b.getLocation().clone().subtract(0, 1, 0)).getType().equals(Material.BEDROCK))) {
			if (towers.containsValue(b.getLocation().clone().subtract(0, 1, 0)))
				res = true;
		} else if (b.getType().equals(Material.END_ROD) && (b.getWorld().getBlockAt(b.getLocation().clone().subtract(0, 1, 0)).getType().toString().contains("WOOL") || b.getWorld().getBlockAt(b.getLocation().clone().subtract(0, 1, 0)).getType().equals(Material.REDSTONE_LAMP)) &&
				(b.getWorld().getBlockAt(b.getLocation().clone().subtract(0, 2, 0)).getType().equals(Material.EMERALD_BLOCK) || b.getWorld().getBlockAt(b.getLocation().clone().subtract(0, 2, 0)).getType().equals(Material.BEDROCK))) {
			if (towers.containsValue(b.getLocation().clone().subtract(0, 2, 0)))
				res = true;
		}
		return res;
	}
	
	public static boolean isComponentsNoCheckConfig(Block b) {
		boolean res = false;
		
		if ((b.getType().equals(Material.EMERALD_BLOCK) || b.getType().equals(Material.BEDROCK)) && (b.getWorld().getBlockAt(b.getLocation().clone().add(0, 1, 0)).getType().toString().contains("WOOL") || b.getWorld().getBlockAt(b.getLocation().clone().add(0, 1, 0)).getType().equals(Material.REDSTONE_LAMP)) &&
				b.getWorld().getBlockAt(b.getLocation().clone().add(0, 2, 0)).getType().equals(Material.END_ROD)) {
				res = true;
		} else if ((b.getWorld().getBlockAt(b.getLocation()).getType().toString().contains("WOOL") || b.getWorld().getBlockAt(b.getLocation()).getType().equals(Material.REDSTONE_LAMP)) 
				&& b.getWorld().getBlockAt(b.getLocation().clone().add(0, 1, 0)).getType().equals(Material.END_ROD) &&
				(b.getWorld().getBlockAt(b.getLocation().clone().subtract(0, 1, 0)).getType().equals(Material.EMERALD_BLOCK) || b.getWorld().getBlockAt(b.getLocation().clone().subtract(0, 1, 0)).getType().equals(Material.BEDROCK))) {
				res = true;
		} else if (b.getType().equals(Material.END_ROD) && (b.getWorld().getBlockAt(b.getLocation().clone().subtract(0, 1, 0)).getType().toString().contains("WOOL") || b.getWorld().getBlockAt(b.getLocation().clone().subtract(0, 1, 0)).getType().equals(Material.REDSTONE_LAMP)) &&
				(b.getWorld().getBlockAt(b.getLocation().clone().subtract(0, 2, 0)).getType().equals(Material.EMERALD_BLOCK) || b.getWorld().getBlockAt(b.getLocation().clone().subtract(0, 2, 0)).getType().equals(Material.BEDROCK))) {
				res = true;
		}
		return res;
	}
	
	public static Block getCoreComponent(Block b) {
		Block res = null;
		
		if (b != null && (b.getType().equals(Material.EMERALD_BLOCK) || b.getType().equals(Material.BEDROCK)) && (b.getWorld().getBlockAt(b.getLocation().clone().add(0, 1, 0)).getType().toString().contains("WOOL") || b.getWorld().getBlockAt(b.getLocation().clone().add(0, 1, 0)).getType().equals(Material.REDSTONE_LAMP)) &&
				b.getWorld().getBlockAt(b.getLocation().clone().add(0, 2, 0)).getType().equals(Material.END_ROD)) {
				res = b;
		} else if (b != null && (b.getWorld().getBlockAt(b.getLocation()).getType().toString().contains("WOOL") || b.getWorld().getBlockAt(b.getLocation()).getType().equals(Material.REDSTONE_LAMP)) 
				&& b.getWorld().getBlockAt(b.getLocation().clone().add(0, 1, 0)).getType().equals(Material.END_ROD) &&
				(b.getWorld().getBlockAt(b.getLocation().clone().subtract(0, 1, 0)).getType().equals(Material.EMERALD_BLOCK) || b.getWorld().getBlockAt(b.getLocation().clone().subtract(0, 1, 0)).getType().equals(Material.BEDROCK))) {
				res = b.getWorld().getBlockAt(b.getLocation().clone().subtract(0, 1, 0));
		} else if (b != null && b.getType().equals(Material.END_ROD) && (b.getWorld().getBlockAt(b.getLocation().clone().subtract(0, 1, 0)).getType().toString().contains("WOOL") || b.getWorld().getBlockAt(b.getLocation().clone().subtract(0, 1, 0)).getType().equals(Material.REDSTONE_LAMP)) &&
				(b.getWorld().getBlockAt(b.getLocation().clone().subtract(0, 2, 0)).getType().equals(Material.EMERALD_BLOCK) || b.getWorld().getBlockAt(b.getLocation().clone().subtract(0, 2, 0)).getType().equals(Material.BEDROCK))) {
			res = b.getWorld().getBlockAt(b.getLocation().clone().subtract(0, 2, 0));
		}
		return res;
	}
	
	public static Block getStatusComponent(Block b) {
		Block res = null;
		
		if (b != null && (b.getType().equals(Material.EMERALD_BLOCK) || b.getType().equals(Material.BEDROCK)) && (b.getWorld().getBlockAt(b.getLocation().clone().add(0, 1, 0)).getType().toString().contains("WOOL") || b.getWorld().getBlockAt(b.getLocation().clone().add(0, 1, 0)).getType().equals(Material.REDSTONE_LAMP)) &&
				b.getWorld().getBlockAt(b.getLocation().clone().add(0, 2, 0)).getType().equals(Material.END_ROD)) {
				res = b.getWorld().getBlockAt(b.getLocation().clone().add(0, 1, 0));
		} else if (b != null && (b.getWorld().getBlockAt(b.getLocation()).getType().toString().contains("WOOL") || b.getWorld().getBlockAt(b.getLocation()).getType().equals(Material.REDSTONE_LAMP)) 
				&& b.getWorld().getBlockAt(b.getLocation().clone().add(0, 1, 0)).getType().equals(Material.END_ROD) &&
				(b.getWorld().getBlockAt(b.getLocation().clone().subtract(0, 1, 0)).getType().equals(Material.EMERALD_BLOCK) || b.getWorld().getBlockAt(b.getLocation().clone().subtract(0, 1, 0)).getType().equals(Material.BEDROCK))) {
				res = b;
		} else if (b != null && b.getType().equals(Material.END_ROD) && (b.getWorld().getBlockAt(b.getLocation().clone().subtract(0, 1, 0)).getType().toString().contains("WOOL") || b.getWorld().getBlockAt(b.getLocation().clone().subtract(0, 1, 0)).getType().equals(Material.REDSTONE_LAMP)) &&
				(b.getWorld().getBlockAt(b.getLocation().clone().subtract(0, 2, 0)).getType().equals(Material.EMERALD_BLOCK) || b.getWorld().getBlockAt(b.getLocation().clone().subtract(0, 2, 0)).getType().equals(Material.BEDROCK))) {
			res = b.getWorld().getBlockAt(b.getLocation().clone().subtract(0, 1, 0));
		}
		return res;
	}
}
