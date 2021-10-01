package fr.achillebourgault.defensetowers;

import org.bukkit.block.Block;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import fr.achillebourgault.defensetowers.commands.TabTowerCompletter;
import fr.achillebourgault.defensetowers.commands.TowerCmd;
import fr.achillebourgault.defensetowers.config.Config;
import fr.achillebourgault.defensetowers.towers.Components;
import fr.achillebourgault.defensetowers.towers.DefenseTowers;
import fr.achillebourgault.defensetowers.towers.DefenseTowersEvent;
import fr.achillebourgault.defensetowers.towers.RepulsorEffect;

public class Main extends JavaPlugin implements Listener { 
	
	private static Main main;
	private Utils utils;
	public static String prefix = "§f§l[§nDefenseTower§r§f§l] §7";
	private static Config configuration;
	private DefenseTowers defenseTowers;
	private BukkitTask DefenseTask;

	public Main() {
		main = this;
		utils = new Utils();
		configuration = new Config();
		defenseTowers = new DefenseTowers();
	}
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new DefenseTowersEvent(), this);
		getServer().getPluginManager().registerEvents(new Config(), this);
		getCommand("tower").setExecutor(new TowerCmd());
		getCommand("tower").setTabCompleter(new TabTowerCompletter());
		
        doTowerRunnable();
	}
	
	private void doTowerRunnable() {
		DefenseTask = new BukkitRunnable() {

		    @Override
		    public void run() {
		    	if (defenseTowers.getTowers().size() > 0) {
		    		for (String e : defenseTowers.getTowers().keySet()) {
		    			if (!Main.getInstance().getConfig().getBoolean("defenseTowers." + e + ".destroyed")) {
		    				Block source = Components.getCoreComponent(defenseTowers.getTowers().get(e).getWorld().getBlockAt(defenseTowers.getTowers().get(e).clone()));
		    				Block source2 = source;
		    				
		    				if (source == null)
		    					return;
		    				source = Components.getCoreComponent(source) != null ? Components.getCoreComponent(source) : source;
		    				source2 = Components.getStatusComponent(source2);
		    				new RepulsorEffect(e,
			    					defenseTowers.getTowers().get(e).clone().add(0, 2, 0),
			    					Main.getInstance().getTowers().getTowersRange().get(defenseTowers.getTowers().get(e)), Main.getInstance().getConfig().getBoolean("defenseTowers." + e + ".display_info"));
		    			}
		    				
		    		}
		    	}
		    }
		          
		}.runTaskTimer(Main.getInstance(), 0, 5L);
	}

	@Override
	public void onDisable() {
		reloadConfig();
		DefenseTask.cancel();
	}
	
	public static Main getInstance() {
		return main;
	}
	
	public Config getConfiguration() {
		return configuration;
	}
	
	public DefenseTowers getTowers() {
		return defenseTowers;
	}

	public Utils getUtils() {
		return utils;
	}
}
