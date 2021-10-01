package fr.achillebourgault.defensetowers;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.Validate;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Utils {
	
	public Utils() {
		
	}
	
	public void sendError(Player p, String message) {
		p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
		p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1f, 0.1f);
	}
	
	public boolean drawRepulsorShot(Location point1, Location point2, double space, Particle p) {
	    World world = point1.getWorld();
	    Validate.isTrue(point2.getWorld().equals(world), "Lines cannot be in different worlds!");
	    double distance = point1.distance(point2);
	    Vector p1 = point1.toVector();
	    Vector p2 = point2.toVector();
	    Vector vector = p2.clone().subtract(p1).normalize().multiply(space);
	    double covered = 0;
	    
	    for (; covered < distance; p1.add(vector)) {
	    	world.spawnParticle(p, p1.getX(), p1.getY(), p1.getZ(), 1);
	        covered += space;
	    }
	    return true;
	}
	
	public boolean isCollatBlockRepulsor(Location point1, Location point2, double space, Color c) {
	    World world = point1.getWorld();
	    Validate.isTrue(point2.getWorld().equals(world), "Lines cannot be in different worlds!");
	    double distance = point1.distance(point2);
	    Vector p1 = point1.toVector();
	    Vector p2 = point2.toVector();
	    Vector vector = p2.clone().subtract(p1).normalize().multiply(space);
	    double covered = 0;
	    ArrayList<Block> test = new ArrayList<>();
	    
	    for (; covered < distance; p1.add(vector)) {
	        if (world.getBlockAt(p1.getBlockX(), p1.getBlockY(), p1.getBlockZ()).getType() != Material.AIR)
	        	return false;
	        test.add(world.getBlockAt(p1.getBlockX(), p1.getBlockY(), p1.getBlockZ()));
	        world.getBlockAt(p1.getBlockX(), p1.getBlockY(), p1.getBlockZ()).setType(Material.GLASS);
	        
	        Bukkit.broadcastMessage(world.getBlockAt(p1.getBlockX(), p1.getBlockY(), p1.getBlockZ()).getType() + "");
	        covered += space;
	    }
	    try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    for (Block b : test) {
	    	test.remove(b);
	    	b.setType(Material.GLASS);
	    }
	    return true;
	}
	
	public void drawLine(Location point1, Location point2, double space, Color c) {
		 
	    World world = point1.getWorld();
	    Validate.isTrue(point2.getWorld().equals(world), "Lines cannot be in different worlds!");
	    double distance = point1.distance(point2);
	    Vector p1 = point1.toVector();
	    Vector p2 = point2.toVector();
	    Vector vector = p2.clone().subtract(p1).normalize().multiply(space);
	    double covered = 0;
	    Particle.DustOptions dust = new Particle.DustOptions(
                c, 1);
	    
	    for (; covered < distance; p1.add(vector)) {
	        world.spawnParticle(Particle.REDSTONE, p1.getX(), p1.getY(), p1.getZ(), 0, 0, 0, 0, dust);
	        covered += space;
	    }
	}
	
	public void drawLineParticle(Location point1, Location point2, double space, Particle p) {
	    World world = point1.getWorld();
	    Validate.isTrue(point2.getWorld().equals(world), "Lines cannot be in different worlds!");
	    double distance = point1.distance(point2);
	    Vector p1 = point1.toVector();
	    Vector p2 = point2.toVector();
	    Vector vector = p2.clone().subtract(p1).normalize().multiply(space);
	    double covered = 0;
	   
	    for (; covered < distance; p1.add(vector)) {
	        world.spawnParticle(p, p1.getX(), p1.getY(), p1.getZ(), 1);
	        covered += space;
	    }
	}

}
