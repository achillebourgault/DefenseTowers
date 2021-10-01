package fr.achillebourgault.defensetowers.config;

import org.bukkit.event.Listener;

import fr.achillebourgault.defensetowers.Main;

public class Config implements Listener {
	
	public Config() {
		Main.getInstance().saveDefaultConfig();
		initConfig();
		reload();
	}
	
	private void initConfig() {
		Main.getInstance().getConfig().options().header("#                                                      _      _            ___               \r\n" + 
				"#                                                     | \\ _ _|_ _ __  _  _  |  _     _  __ _ \r\n" + 
				"#                                                     |_/(/_ | (/_| |_> (/_ | (_)\\^/(/_ | _> \r\n" + 
				"#                                                     \r\n" + 
				"#                                                     			Configuration File\r\n" + 
				"#   \r\n" +
				"#                    	Author: Achille Bourgault\r\n" + 
				"#   \r\n#   ");
		if (!Main.getInstance().getConfig().contains("max_turrets"))
			Main.getInstance().getConfig().set("max_turrets", 30);
 		if (Main.getInstance().getConfig().get("turrets_cannot_destroyed") == null)
			Main.getInstance().getConfig().set("turrets_cannot_destroyed", false);
		if (!Main.getInstance().getConfig().contains("default_health_turrret"))
			Main.getInstance().getConfig().set("default_health_turrret", 1000);
		if (!Main.getInstance().getConfig().contains("max_turrets_by_player") || Main.getInstance().getConfig().getInt("max_turrets_by_player") > 14)
			Main.getInstance().getConfig().set("max_turrets_by_player", 14);
		if (!Main.getInstance().getConfig().contains("permission_use_turret"))
			Main.getInstance().getConfig().set("permission_use_turret", "defensetowers.use");
		if (!Main.getInstance().getConfig().contains("permission_set_range_turret"))
			Main.getInstance().getConfig().set("permission_set_range_turret", "defensetowers.set_range");
		if (!Main.getInstance().getConfig().contains("defenseTowers"))
			Main.getInstance().getConfig().createSection("defenseTowers");
	}

	public void reload() {
		Main.getInstance().saveConfig();
	}
	
	public String getPermissionSetRange() {
		return Main.getInstance().getConfig().getString("permission_set_range_turret");
	}
	
	public String getPermissionUse() {
		return Main.getInstance().getConfig().getString("permission_use_turret");
	}

	public Integer getMaxTurretsByPlayer() {
		return Main.getInstance().getConfig().getInt("max_turrets_by_player");
	}
	
	public Integer getDefaultHeatlthTurret() {
		return Main.getInstance().getConfig().getInt("default_health_turrret");
	}
	
	public boolean getTurretCannotDestroyed() {
		return Main.getInstance().getConfig().getBoolean("turrets_cannot_destroyed");
	}


}
