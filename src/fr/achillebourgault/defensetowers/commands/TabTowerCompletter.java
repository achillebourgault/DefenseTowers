package fr.achillebourgault.defensetowers.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class TabTowerCompletter implements TabCompleter {
	
	@Override
    public List<String> onTabComplete(CommandSender sender, Command label, String arg, String[] args) {
		if (label.getName().equalsIgnoreCase("tower")) {
			if (args.length >= 1) {
				if (args.length == 1)
					return Arrays.asList("create", "remove", "radius", "info", "gui", "on", "off", "help");
				if (args[0].equalsIgnoreCase("create")) {
					return null;
				} else if (args[0].equalsIgnoreCase("remove")) {
					return null;
				} else if (args[0].equalsIgnoreCase("radius")) {
					return null;
				} else if (args[0].equalsIgnoreCase("info")) {
					return Arrays.asList("on", "off");
				} else if (args[0].equalsIgnoreCase("gui")) {
					return null;
				} else if (args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("off")) {
					return Arrays.asList("on", "off");
				} else if (args[0].equalsIgnoreCase("help")) {
					return null;
				} else {
					return null;
				}
			}
    	}
		return null;
    }

}
