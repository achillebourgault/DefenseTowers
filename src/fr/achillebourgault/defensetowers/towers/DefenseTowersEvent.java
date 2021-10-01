package fr.achillebourgault.defensetowers.towers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.achillebourgault.defensetowers.Main;
import net.md_5.bungee.api.ChatColor;

public class DefenseTowersEvent implements Listener {
	
	@EventHandler
	public void BlockBreakEvent(BlockBreakEvent e) {
		Block b = e.getBlock();
		Location l = b.getLocation();
		
		if (Components.isComponents(b, Main.getInstance().getTowers().getTowers())) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void inventoryClick(InventoryClickEvent e) {
		Inventory inv = e.getClickedInventory();
		ItemStack is = e.getCurrentItem();
		
		if (e.getView().getTitle().contains("Your turrets") && e.getWhoClicked() instanceof Player) {
			Player p = (Player) e.getWhoClicked();			
			e.setCancelled(true);
			if (is == null || is.getType() == Material.LEVER)
				return;
			ItemStack id = inv.getItem(e.getSlot() - 9);
			
			if (!id.getItemMeta().getDisplayName().contains("#"))
				return;
			String name = ChatColor.stripColor(id.getItemMeta().getDisplayName().split("#")[1]);
			
			if (name == null) {
				Bukkit.broadcastMessage(e.getSlot() +  " - " + e.getRawSlot());
				return;
			}
			
			World world = Bukkit.getWorld(Main.getInstance().getConfig().getString("defenseTowers." + name + ".components.status.location.world"));
			int x = Main.getInstance().getConfig().getInt("defenseTowers." + name + ".components.status.location.x");
			int y = Main.getInstance().getConfig().getInt("defenseTowers." + name + ".components.status.location.y");
			int z = Main.getInstance().getConfig().getInt("defenseTowers." + name + ".components.status.location.z");
			Block b = world.getBlockAt(x, y, z);
			
			if (is.getType() == Material.RED_WOOL) {
				Main.getInstance().getTowers().setStateByName(p, "on", name);
				b.setType(Material.RED_WOOL);
			} else if (is.getType() == Material.LIME_WOOL) {
				Main.getInstance().getTowers().setStateByName(p, "off", name);
				b.setType(Material.REDSTONE_LAMP);
			} else {
				return;
			}
			
			Main.getInstance().getTowers().openGUI(p);
		}
	}

}
