package fr.achillebourgault.defensetowers.towers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.achillebourgault.defensetowers.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class DefenseTowers {
	
	private HashMap<String, Location> towers;
	private HashMap<String, Integer> towersHealth;
	private HashMap<String, Boolean> states;
	private HashMap<Location, Integer> towersRange;
	private int total;
	
	
	public HashMap<String, Location> getTowers() {
		return towers;
	}

	public DefenseTowers() {
		Main.getInstance().reloadConfig();
		if (Main.getInstance().getConfig().getConfigurationSection("defenseTowers") == null)
			Main.getInstance().getConfig().createSection("defenseTowers");
        towers = new HashMap<>();
        towersHealth = new HashMap<>();
        states = new HashMap<>();
        towersRange = new HashMap<>();
		this.total = 0;
		loadTowers();
	}
	
	public HashMap<Location, Integer> getTowersRange() {
		return towersRange;
	}

	public void saveTowers(Player p) {
		String name = p.getName().toLowerCase();
		Block b = p.getTargetBlock(null, 1000);

		if (!Components.isComponentsNoCheckConfig(b) || b.getType() != Material.EMERALD_BLOCK) {
			p.sendMessage(Main.prefix + "§cPlease do this command with the cursor on Emerald Block");
			return;
		}
		loadTowers();
		b = Components.getCoreComponent(b);
		if (Main.getInstance().getConfig().getConfigurationSection("defenseTowers") == null)
			Main.getInstance().getConfig().createSection("defenseTowers");
		
		if (!p.getGameMode().equals(GameMode.CREATIVE) && !p.getInventory().containsAtLeast(new ItemStack(Material.IRON_INGOT), 15)) {
			Main.getInstance().getUtils().sendError(p, Main.prefix + "§cYou need 15 irons to repair the turret.");
			return;
		}
		p.getInventory().removeItem(new ItemStack(Material.IRON_INGOT, 20));
		Date time = new Date();
		DateFormat dfl = new SimpleDateFormat("dd-MM-yyyy");
        int i = 0;
        for (; Main.getInstance().getConfig().getConfigurationSection("defenseTowers").contains(name); i++)
			name = p.getName().toLowerCase() + "-" + i;
        if (Main.getInstance().getConfiguration().getMaxTurretsByPlayer() != -1 && i + 1 > Main.getInstance().getConfiguration().getMaxTurretsByPlayer()) {
        	Main.getInstance().getUtils().sendError(p, "§c§l YOU CAN ONLY HAVE " + (i + 1) + " TURRETS."); 
        	return;
        }
        if (i == Main.getInstance().getConfig().getInt("max_turrets")) {
			p.sendMessage(Main.prefix + "§cThe turret limit has been reached, contact server administrator.");
			return;
		}
		
		ConfigurationSection tmp = Main.getInstance().getConfig().getConfigurationSection("defenseTowers").createSection(name);
		tmp.set(".location.world", p.getTargetBlock(null, 1000).getLocation().getWorld().getName());
		tmp.set(".location.x", p.getTargetBlock(null, 1000).getLocation().getBlockX());
		tmp.set(".location.y", p.getTargetBlock(null, 1000).getLocation().getBlockY());
		tmp.set(".location.z", p.getTargetBlock(null, 1000).getLocation().getBlockZ());
		
		Location core = b.getLocation().clone();
		Location status = b.getLocation().clone().add(0, 1, 0);
		Location gun = b.getLocation().clone().add(0, 2, 0);
		
		tmp.set(".components.core.location.world", b.getWorld().getName());
		tmp.set(".components.core.location.x", core.getBlockX());
		tmp.set(".components.core.location.y", core.getBlockY());
		tmp.set(".components.core.location.z", core.getBlockZ());
		
		tmp.set(".components.gun.location.world", b.getWorld().getName());
		tmp.set(".components.gun.location.x", gun.getBlockX());
		tmp.set(".components.gun.location.y", gun.getBlockY());
		tmp.set(".components.gun.location.z", gun.getBlockZ());
		
		tmp.set(".components.status.location.world", b.getWorld().getName());
		tmp.set(".components.status.location.x", status.getBlockX());
		tmp.set(".components.status.location.y", status.getBlockY());
		tmp.set(".components.status.location.z", status.getBlockZ());
		
		tmp.set(".radius", 15);
		tmp.set(".health", Main.getInstance().getConfiguration().getDefaultHeatlthTurret());
		tmp.set(".owner", p.getName());
		tmp.set(".active", true);
		tmp.set(".destroyed", false);
		tmp.set(".display_info", false);
        
        tmp.set(".datebirth", dfl.format(time).toString());
        
		Main.getInstance().saveConfig();
		Main.getInstance().reloadConfig();
		TextComponent success = new TextComponent("§aAutomatic turret deployed: ");
		TextComponent details = new TextComponent("§e§lStatus: §r§aArmed");
		p.spigot().sendMessage(success);
		p.spigot().sendMessage(details);
		loadTowers();
	}

	public void removeTowers(Player p) {
		boolean res = true;
		Block block = p.getTargetBlock(null, 1000);
		final Location source = block.getLocation();
		Location components[] = new Location[3];
		
		if (!Components.isComponents(block, towers)) {
			p.sendMessage(Main.prefix + "§cPlease do this command with the cursor on Emerald Block");
			return;
		}
		
		loadTowers();
		for(String region : Main.getInstance().getConfig().getConfigurationSection("defenseTowers").getKeys(false)){
			if (states.containsKey(region) && towers.get(region).distance(source) <= 1) {
				components[0] = new Location(
						Bukkit.getWorld(Main.getInstance().getConfig().get("defenseTowers." + region + ".components.core.location.world").toString()),
						Main.getInstance().getConfig().getInt("defenseTowers." + region + ".components.core.location.x"),
						Main.getInstance().getConfig().getInt("defenseTowers." + region + ".components.core.location.y"),
						Main.getInstance().getConfig().getInt("defenseTowers." + region + ".components.core.location.z"));
				components[1] = new Location(
						Bukkit.getWorld(Main.getInstance().getConfig().get("defenseTowers." + region + ".components.gun.location.world").toString()),
						Main.getInstance().getConfig().getInt("defenseTowers." + region + ".components.gun.location.x"),
						Main.getInstance().getConfig().getInt("defenseTowers." + region + ".components.gun.location.y"),
						Main.getInstance().getConfig().getInt("defenseTowers." + region + ".components.gun.location.z"));
				components[2] = new Location(
						Bukkit.getWorld(Main.getInstance().getConfig().get("defenseTowers." + region + ".components.status.location.world").toString()),
						Main.getInstance().getConfig().getInt("defenseTowers." + region + ".components.status.location.x"),
						Main.getInstance().getConfig().getInt("defenseTowers." + region + ".components.status.location.y"),
						Main.getInstance().getConfig().getInt("defenseTowers." + region + ".components.status.location.z"));
				Main.getInstance().getConfig().set("defenseTowers." + region, null);
				p.sendMessage("§f§l[§nDefenseTower§r§f§l] §7Tower destroyed.");
				
				Bukkit.getScheduler ().runTaskLater (Main.getInstance(), () -> source.getWorld().getBlockAt(components[0]).breakNaturally(), 5);
				Bukkit.getScheduler ().runTaskLater (Main.getInstance(), () -> source.getWorld().getBlockAt(components[1]).breakNaturally(), 10);
				Bukkit.getScheduler ().runTaskLater (Main.getInstance(), () -> source.getWorld().getBlockAt(components[2]).breakNaturally(), 15);
				Bukkit.getScheduler ().runTaskLater (Main.getInstance(), () -> source.getWorld().dropItemNaturally(components[0], new ItemStack(Material.IRON_INGOT, 10)), 15);
				Main.getInstance().saveConfig();
				Main.getInstance().reloadConfig();
				loadTowers();
				return;
			}
			
			
		}
	}
	
	
	public void loadTowers() {
		Main.getInstance().reloadConfig();
		if (!Main.getInstance().getConfig().contains("defenseTowers")) {
			Main.getInstance().getConfig().set("defenseTowers", null);
			Main.getInstance().saveConfig();
			return;
		}
		if (towers != null)
			towers.clear();
		if (towersHealth != null)
			towersHealth.clear();
		if (towersRange != null)
			towersRange.clear();
		if (states != null)
			states.clear();
		for(String region : Main.getInstance().getConfig().getConfigurationSection("defenseTowers").getKeys(false)){
			if (Main.getInstance().getConfig().contains("defenseTowers." + region + ".location")) {
				Location l = new Location(
						Bukkit.getWorld(Main.getInstance().getConfig().get("defenseTowers." + region + ".location.world").toString()),
						Main.getInstance().getConfig().getInt("defenseTowers." + region + ".location.x"),
						Main.getInstance().getConfig().getInt("defenseTowers." + region + ".location.y"),
						Main.getInstance().getConfig().getInt("defenseTowers." + region + ".location.z"));
				towers.put(region, l);
				states.put(region, Main.getInstance().getConfig().getBoolean("defenseTowers." + region + ".active"));
				if (Main.getInstance().getConfig().getBoolean("defenseTowers." + region + ".destroyed") == true)
					states.replace(region, false);
				towersHealth.put(region, Main.getInstance().getConfig().getInt("defenseTowers." + region + ".health"));
				towersRange.put(l, Main.getInstance().getConfig().getInt("defenseTowers." + region + ".radius"));
			} else
				Bukkit.broadcast("§cError while loading §e" + region + "§c.", "defenseTowers.bugmsg");
		}
			
	}
	
	public void ToList(Player p) {
		String message = "   §9";
		
		p.sendMessage("                          §f§l[§nDefenseTower§r§f§l]");
		p.sendMessage(" ");
		for(String region : Main.getInstance().getConfig().getConfigurationSection("warps").getKeys(false)){
			message+= region.toUpperCase() + "§f, §9";
	    }
		TextComponent list = new TextComponent(message);
		list.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§e§lVOIR").create()));
		list.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/warp"));
		p.spigot().sendMessage(list);
	}
	
	public void openGUI(Player p) {
		Inventory inv = Bukkit.createInventory(null, towers.size() <= 7 ? 36 : 54, "Your turrets");
		ArrayList<String> regions = getPlayerTowers(p);
		int pos = 9;
		int posBtn = pos + 9;
		
		loadTowers();
		if (towers.size() == 0) {
			p.sendMessage(Main.prefix + "You don't have turret. Please create one.");
			return;
		}
		for (String region : regions) {
			boolean iStatut = Main.getInstance().getConfig().getBoolean("defenseTowers." + region + ".active");
			boolean iDestroyed = Main.getInstance().getConfig().getBoolean("defenseTowers." + region + ".destroyed");
			String iStatutLabel = iStatut ? "§aActive" : "§cInactive";
			int iRange = Main.getInstance().getConfig().getInt("defenseTowers." + region + ".radius");
			String iDateBirth = Main.getInstance().getConfig().getString("defenseTowers." + region + ".datebirth");
			int iPosX = Main.getInstance().getConfig().getInt("defenseTowers." + region + ".location.x");
			int iPosY = Main.getInstance().getConfig().getInt("defenseTowers." + region + ".location.y");
			int iPosZ = Main.getInstance().getConfig().getInt("defenseTowers." + region + ".location.z");
			int IShield = (Main.getInstance().getTowers().getTowersHealth().get(region) * 100) / Main.getInstance().getConfiguration().getDefaultHeatlthTurret();
			Material btnType = iStatut ? Material.LIME_WOOL : Material.RED_WOOL;
			ItemStack isLabel = new ItemStack(Material.LEVER, 1);
			ItemMeta imLabel = isLabel.getItemMeta();
			
			imLabel.setDisplayName("§eTurret §r§7#§f§o§l" + region.toLowerCase());
			if (!iDestroyed) {
				imLabel.setLore(Arrays.asList("",
						"§fState§r§f:            " + iStatutLabel,
						"§fShield§r§f:             §r§e" + IShield + "§f%",
						"§fPosition§r§f:         §r§e" + iPosX + "§7, §e" + iPosY + "§7, §e" + iPosZ,
						"§fRange§r§f:           §r§e" + iRange,
						"§fFirst deploy§r§f:   §r§e" + iDateBirth,
						""));
			} else {
				imLabel.setLore(Arrays.asList("",
						"§fEtat§r§f:            §e§k" + "Ffffff",
						"§fPosition§r§f:         §e§k" + iPosX + "§r§7, §e§k" + iPosY + "§r§7, §e§k" + iPosZ,
						"§fRange§r§f:           §e§k" + iRange,
						"§fFirst deploy§r§f:   §r§e" + iDateBirth,
						"",
						"§c§lERROR !",
						"§c§lUnable to communicate with the turret.",
						"§cPlease check that the turret has not been damaged.",
						"§cIf so, please place the following command §f/tower repair",
						"§cby pointing the block of Bedrock.",
						""));
			}
			isLabel.setItemMeta(imLabel);
			ItemStack isBtn = new ItemStack(btnType, 1);
			ItemMeta imBtn = isBtn.getItemMeta();
			if (iStatut)
				imBtn.setDisplayName("§c§lCLICK HERE TO DEACTIVATE THE TURRET");
			else
				imBtn.setDisplayName("§a§lCLICK HERE TO ACTIVATE THE TURRET");
			if (iDestroyed) {
				isBtn.setType(Material.BARRIER);
				imBtn.setDisplayName("§c§l§kCLICK HERE TO DEACTIVATE THE TURRET");
			}
			isBtn.setItemMeta(imBtn);
			inv.setItem(pos, isLabel);
			inv.setItem(posBtn, isBtn);
			pos++;
	        posBtn++;
			if (pos == 18 || pos == 37) {
				pos += 9;
				posBtn += 9;
			}
		}
		p.openInventory(inv);
		p.playSound(p.getLocation(), Sound.BLOCK_SHULKER_BOX_OPEN, 0.6f, 0.6f);
		
	}

	public void repairTower(Player p) {
		Block block = p.getTargetBlock(null, 1000);
		Location source = block.getLocation().clone();
		
		if (!Components.isComponents(block, towers)) {
			p.sendMessage(Main.prefix + "§cPlease do this command with the cursor on Emerald Block");
			return;
		}
		
		if (!p.getGameMode().equals(GameMode.CREATIVE) && !p.getInventory().containsAtLeast(new ItemStack(Material.IRON_INGOT), 15)) {
			Main.getInstance().getUtils().sendError(p, Main.prefix + "§cYou need 15 irons to repair the turret.");
			return;
		}
		p.getInventory().removeItem(new ItemStack(Material.IRON_INGOT, 15));
		source = Components.getCoreComponent(block).getLocation();
		
		for(String region : Main.getInstance().getConfig().getConfigurationSection("defenseTowers").getKeys(false)){
			if (states.containsKey(region) && towers.get(region).distance(source) <= 1) {
				if (Main.getInstance().getConfig().getBoolean("defenseTowers." + region + ".destroyed")) {
					Main.getInstance().getConfig().set("defenseTowers." + region + ".destroyed", false);
					Main.getInstance().getConfig().set("defenseTowers." + region + ".health", Main.getInstance().getConfiguration().getDefaultHeatlthTurret());
					source.getWorld().getBlockAt(source.clone().add(0, 1, 0)).setType(Material.RED_WOOL);
					source.getWorld().getBlockAt(source).setType(Material.EMERALD_BLOCK);
					block.getWorld().playSound(source, Sound.BLOCK_ANVIL_USE, 2f, 0.3f);
					Main.getInstance().saveConfig();
					Main.getInstance().reloadConfig();
					loadTowers();
					return;
				} else {
					p.sendMessage("§cThis turret does not require repair.");
					return;
				}
			}
			
		}
		p.sendMessage("§cCommunication error with the turret.");
	}

	public void setState(Player p, String state) {
		boolean res = true;
		Block block = p.getTargetBlock(null, 1000);
		Location source = block.getLocation().clone();
		
		if (!Components.isComponents(block, towers)) {
			p.sendMessage(Main.prefix + "§cPlease do this command with the cursor on Emerald Block");
			return;
		}
		source = Components.getCoreComponent(block).getLocation();
		
		if (state.equalsIgnoreCase("on"))
			res = true;
		else if (state.equalsIgnoreCase("off"))
			res = false;
		
		for(String region : Main.getInstance().getConfig().getConfigurationSection("defenseTowers").getKeys(false)){
			if (states.containsKey(region) && towers.get(region).distance(source) <= 1) {
				if (!Main.getInstance().getConfig().getBoolean("defenseTowers." + region + ".destroyed")) {
					Main.getInstance().getConfig().set("defenseTowers." + region + ".active", res);
					break;
				} else {
					p.sendMessage("§cThis turret is damaged. Impossible to change its state.");
					return;
				}
			}
			
			
		}
		String label = res ? "§aActive" : "§cInactive";
		p.sendMessage("§f§l[§nDefenseTower§r§f§l] §7Your turret is now " + label + "§7.");
		
		if (res) {
			Components.getStatusComponent(p.getWorld().getBlockAt(source.clone())).setType(Material.RED_WOOL);
			block.getWorld().playSound(source, Sound.BLOCK_IRON_TRAPDOOR_OPEN, 2f, 0.3f);
		} else {
			Components.getStatusComponent(p.getWorld().getBlockAt(source.clone())).setType(Material.REDSTONE_LAMP);
			block.getWorld().playSound(source, Sound.BLOCK_IRON_TRAPDOOR_CLOSE, 2f, 0.3f);
		}
		Main.getInstance().saveConfig();
		loadTowers();
	}
	
	public void setStateByName(Player p, String state, String region) {
		boolean res = true;
		
		if (state.equalsIgnoreCase("on"))
			res = true;
		else if (state.equalsIgnoreCase("off"))
			res = false;
		
		if (!Main.getInstance().getConfig().getBoolean("defenseTowers." + region + ".destroyed")) {
			Main.getInstance().getConfig().set("defenseTowers." + region + ".active", res);
		} else {
			p.closeInventory();
			p.sendMessage("§cThis turret is damaged. Impossible to change its state.");
			return;
		}
		
		Main.getInstance().saveConfig();
		Main.getInstance().reloadConfig();
		loadTowers();
	}
	
	public boolean isDestroyed(String region) {
		boolean res = Main.getInstance().getConfig().getBoolean("defenseTowers." + region + ".destroyed");
		Location l = new Location(
				Bukkit.getWorld(Main.getInstance().getConfig().get("defenseTowers." + region + ".location.world").toString()),
				Main.getInstance().getConfig().getInt("defenseTowers." + region + ".location.x"),
				Main.getInstance().getConfig().getInt("defenseTowers." + region + ".location.y"),
				Main.getInstance().getConfig().getInt("defenseTowers." + region + ".location.z"));
		
		if (res && l.getWorld().getBlockAt(l).getType() != Material.BEDROCK) {
			l.getWorld().getBlockAt(l.clone().add(0, 1, 0)).setType(Material.REDSTONE_LAMP);
			l.getWorld().getBlockAt(l).setType(Material.BEDROCK);
		}
		return res;
	}
	
	public HashMap<String, Boolean> getStates() {
		return states;
	}
	
	
	private boolean isTower(Block b) {
		boolean res = true;
		
		if (b.getType() != Material.EMERALD_BLOCK || !b.getWorld().getBlockAt(b.getLocation().clone().add(0, 1, 0)).getType().toString().contains("WOOL")
				|| b.getWorld().getBlockAt(b.getLocation().clone().add(0, 2, 0)).getType() != Material.END_ROD) {
			if (!b.getWorld().getBlockAt(b.getLocation().clone().add(0, 1, 0)).getType().toString().contains("LAMP"))
				res = false;
		}
			return res;
	}
	
	private ArrayList<String> getPlayerTowers(Player p) {
		String name = p.getName();
		
		if (Main.getInstance().getConfig().getConfigurationSection("defenseTowers") == null)
			Main.getInstance().getConfig().createSection("defenseTowers");
		ArrayList<String> regions = new ArrayList<>();
		for(String region : Main.getInstance().getConfig().getConfigurationSection("defenseTowers").getKeys(false)){
			if (Main.getInstance().getConfig().getString("defenseTowers." + region + ".owner").equalsIgnoreCase(p.getName())) {
				regions.add(region);
			}
	    }
		return regions;
	}
	
	public void setInfo(Player p, String args) {
		Block block = p.getTargetBlock(null, 1000);
		Location source = block.getLocation().clone();

		if (source.getBlock().getType() != Material.EMERALD_BLOCK) {
			Main.getInstance().getUtils().sendError(p, "§cPlease do this command with the cursor on Emerald Block.");
			return;
		}
	
		source = Components.getCoreComponent(block).getLocation();
		for(String region : Main.getInstance().getConfig().getConfigurationSection("defenseTowers").getKeys(false)){
			if (states.containsKey(region) && towers.get(region).distance(source) <= 1) {
				if (args == null || (!args.equalsIgnoreCase("on") && !args.equalsIgnoreCase("off"))) {
					Main.getInstance().getUtils().sendError(p, "§cUSE TRUE or FALSE.");
					return;
				}
					
				boolean res = args.equalsIgnoreCase("on") ? true : false;
				Main.getInstance().getConfig().set("defenseTowers." + region + ".display_info", res);
				Main.getInstance().saveConfig();
				loadTowers();
				p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§aTurret Info Activated"));
				p.playSound(p.getLocation(), Sound.BLOCK_REDSTONE_TORCH_BURNOUT, 1f, 0.4f);
				return;
			}
			
		}
		p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§cCommunication error with the turret."));
	}

	public void setRadius(Player p, String args) {
		Block block = p.getTargetBlock(null, 1000);
		Location source = block.getLocation().clone();

		
		if (source.getBlock().getType() != Material.EMERALD_BLOCK) {
			Main.getInstance().getUtils().sendError(p, "§cPlease do this command with the cursor on Emerald Block.");
			return;
		}
		if (!p.hasPermission(Main.getInstance().getConfiguration().getPermissionSetRange()))
			Main.getInstance().getUtils().sendError(p, "§c§lYOU CANNOT EXECUTE THIS COMMAND.");
		for(String region : Main.getInstance().getConfig().getConfigurationSection("defenseTowers").getKeys(false)){
			if (states.containsKey(region) && towers.get(region).distance(source) <= 1) {
				if (args == null || !IsInt_ByException(args) || Integer.parseInt(args) < 0 || Integer.parseInt(args) > 100) {
					Main.getInstance().getUtils().sendError(p, "§cRadius must be between 0-100.");
					return;
				}
				Main.getInstance().getConfig().set("defenseTowers." + region + ".radius", Integer.parseInt(args));
				Main.getInstance().saveConfig();
				loadTowers();
				p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§aRadius is now set to §e" + Integer.parseInt(args)));
				p.playSound(p.getLocation(), Sound.BLOCK_REDSTONE_TORCH_BURNOUT, 1f, 0.4f);
				return;
			}
			
		}
		p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§cCommunication error with the turret."));
	}

	private boolean IsInt_ByException(String str) {
	     try {
	         Integer.parseInt(str);
	         return true;
	     } catch(NumberFormatException nfe) {
	         return false;
	     }
	 }
	
	private boolean IsBool_ByException(String str) {
	     try {
	         Boolean.parseBoolean(str);
	         return true;
	     } catch(Exception nfe) {
	         return false;
	     }
	 }

	public HashMap<String, Integer> getTowersHealth() {
		return towersHealth;
	}
	
}
