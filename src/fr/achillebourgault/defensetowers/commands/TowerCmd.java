package fr.achillebourgault.defensetowers.commands;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.achillebourgault.defensetowers.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class TowerCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	
    	if (label.equalsIgnoreCase("tower")) {
    		
    		if (sender instanceof Player) {
    			Player p = (Player)sender;
    			
    			if (!p.isOp() && !p.hasPermission(Main.getInstance().getConfiguration().getPermissionUse())) {
    				Main.getInstance().getUtils().sendError(p, "§c§lYOU CANNOT EXECUTE THIS COMMAND.");
    				return true;
    			}
    			if (args.length == 0) {
    				usage(p);
    			} else {
    				if (args.length >= 1) {
    					if (args[0].equalsIgnoreCase("create")) {
    						Main.getInstance().getTowers().saveTowers(p);
    					} else if (args[0].equalsIgnoreCase("remove")) {
    						Main.getInstance().getTowers().removeTowers(p);
    					} else if (args[0].equalsIgnoreCase("radius")) {
    						Main.getInstance().getTowers().setRadius(p, args.length == 2 ? args[1] : null);
    					} else if (args[0].equalsIgnoreCase("info")) {
    						Main.getInstance().getTowers().setInfo(p, args.length == 2 ? args[1] : null);
    					} else if (args[0].equalsIgnoreCase("repair")) {
    						Main.getInstance().getTowers().repairTower(p);
    					} else if (args[0].equalsIgnoreCase("gui")) {
    						Main.getInstance().getTowers().openGUI(p);
    					} else if (args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("off")) {
    						Main.getInstance().getTowers().setState(p, args[0]);
    					} else if (args[0].equalsIgnoreCase("help")) {
    						usage(p);
    					} else {
    						usage(p);
    					}
    				}
    			}
    		}
    	}
		return true;
    }
    
    public void usage(Player p) {
    	p.sendMessage("                          §f§l[§nDefenseTower§r§f§l]");
    	p.sendMessage("");
    	
    	TextComponent createcmd = new TextComponent("  §e/tower §fcreate              ");
    	createcmd.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§f§lClick here to copy the command.").create()));
    	createcmd.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tower create"));
		p.spigot().sendMessage(createcmd, new TextComponent("§e§oCreate a new turret"));
		
		TextComponent removecmd = new TextComponent("  §e/tower §fremove§7/§fdestroy  ");
		removecmd.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§f§lClick here to copy the command.").create()));
		removecmd.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tower remove"));
		p.spigot().sendMessage(removecmd, new TextComponent("§e§oDelete the selected turret."));
		
		TextComponent activatecmd = new TextComponent("  §e/tower §fon§7/§foff              ");
		activatecmd.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§f§lClick here to copy the command.").create()));
		activatecmd.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tower off"));
		p.spigot().sendMessage(activatecmd, new TextComponent("§e§oActivate/Desactivate a turret"));
		
		TextComponent infocmd = new TextComponent("  §e/tower info §fon§7/§foff       ");
		infocmd.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§f§lClick here to copy the command.").create()));
		infocmd.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tower info on"));
		p.spigot().sendMessage(infocmd, new TextComponent("§e§oActivate/Desactivate turret output."));
		
		TextComponent radiuscmd = new TextComponent("  §e/tower radius §f0§7-§f100   ");
		radiuscmd.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§f§lClick here to copy the command.").create()));
		radiuscmd.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tower radius 5"));
		p.spigot().sendMessage(radiuscmd, new TextComponent("§e§oSet engagment distance."));
		
		TextComponent guicmd = new TextComponent("  §e/tower §fgui                   ");
		guicmd.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§f§lClick here to copy the command.").create()));
		guicmd.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tower gui"));
		p.spigot().sendMessage(guicmd, new TextComponent("§e§oOpen the overview of your turrets."));
    }
    

}