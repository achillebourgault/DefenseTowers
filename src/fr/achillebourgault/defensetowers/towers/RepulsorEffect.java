package fr.achillebourgault.defensetowers.towers;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Phantom;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import fr.achillebourgault.defensetowers.Main;

public class RepulsorEffect {
	
	private String owner;
	private ArrayList<ArmorStand> nearbyGuns;
	
	public RepulsorEffect(String owner, Location source, int radius, boolean displayInfo) {
		nearbyGuns = new ArrayList<>();
		this.owner = owner;
		Random r = new Random();
		Location l = source.clone().add(0.5,1,0.5);
		Location lamp = source.clone().subtract(0,1,0);
		int cpt = 0;
		ArrayList<Entity> details = new ArrayList<>();
		ArmorStand name;

		if (l != null && l.getWorld() != null && l.getWorld().getNearbyEntities(source, radius, radius, radius).size() > 0) {
			
			for (Entity ent : l.getWorld().getNearbyEntities(source, radius, radius, radius)) {
				int y_offset = source.getBlockY() - ((int)source.distance(ent.getLocation()));
				
				if (cpt > 10)
 					return;
				if (ent.getLocation().getBlockY() + radius < l.getBlockY())
					continue;
				if (ent.getName().equalsIgnoreCase(owner) || ent.getType() == EntityType.DROPPED_ITEM)
					continue;
				if (ent instanceof Monster || ent instanceof Phantom) {
					
					l.getWorld().playSound(source, Sound.ENTITY_BLAZE_SHOOT, 0.1f, 1f);
					Main.getInstance().getUtils().drawRepulsorShot(l, ent.getLocation().clone().add(0, 1, 0), 0.5, Particle.SPELL_MOB);
					//Main.getInstance().drawLine(l, ent.getLocation().clone().add(0, 1, 0), 0.5, c);
					cpt++;
					details.add(ent);
					
					int burn = r.nextInt(4 - 1 + 1) + 1;
					Color c = Color.YELLOW;
					
					if (burn == 2) {
						ent.setFireTicks(20);
						c = c.RED;
					} else {
						if (ent instanceof LivingEntity) {
							((org.bukkit.entity.LivingEntity)ent).damage(r.nextInt(4 - 1 + 1) + 1);
							c = Color.ORANGE;
							double vX = 0 + Math.random() * 0.25 - 0;
							double vY = 0 + Math.random() * 0.15 - 0;
							double vZ = 0 + Math.random() * 0.25 - 0;
							ent.setVelocity(ent.getVelocity().add(new Vector(vX, vY, vZ)));
						}
					}
					
					if (cpt >= 5)
						break;
				}
					
			}
				if (details.size() <= 0 && lamp.getWorld().getBlockAt(lamp).getType().toString().equals("RED_WOOL")) {
					lamp.getWorld().getBlockAt(lamp).setType(Material.LIME_WOOL);
					
					moveTurret(lamp);
					Bukkit.getScheduler ().runTaskLater (Main.getInstance(), () -> reloadNearbyGuns(lamp), 3);
		    		Bukkit.getScheduler ().runTaskLater (Main.getInstance(), () -> reloadNearbyGuns(lamp), 9);
		    		Bukkit.getScheduler ().runTaskLater (Main.getInstance(), () -> lamp.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, lamp.clone().add(0, 2, 0), 1), 17);
					Bukkit.getScheduler ().runTaskLater (Main.getInstance(), () -> lamp.getWorld().playSound(lamp, Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON, 0.2f, 0.3f), 3);
					Bukkit.getScheduler ().runTaskLater (Main.getInstance(), () -> lamp.getWorld().playSound(lamp, Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON, 0.2f, 0.3f), 6);
					Bukkit.getScheduler ().runTaskLater (Main.getInstance(), () -> lamp.getWorld().playSound(lamp, Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON, 0.2f, 0.3f), 9);
					Bukkit.getScheduler ().runTaskLater (Main.getInstance(), () -> lamp.getWorld().playSound(lamp, Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON, 0.2f, 0.3f), 12);
					Bukkit.getScheduler ().runTaskLater (Main.getInstance(), () -> lamp.getWorld().playSound(lamp, Sound.BLOCK_CONDUIT_DEACTIVATE, 0.2f, 0.3f), 15);
					Bukkit.getScheduler ().runTaskLater (Main.getInstance(), () -> resetTurret(lamp), r.nextInt(21-16) + 16);
				}
		}
		
		if (details.size() > 0) {
			if (lamp.getWorld().getBlockAt(lamp).getType().toString().contains("WOOL")) {
				lamp.getWorld().getBlockAt(lamp).setType(Material.RED_WOOL);
				if (displayInfo) {
					name = source.getWorld().spawn(source.clone().add(0.5, 0, 0), ArmorStand.class);
					name.setInvisible(true);
					name.setGravity(false);
					name.setInvulnerable(true);
					name.setCustomName("§f§l[§nDefenseTower§r§f§l] §fFound enemies: §c" + details.toString());
					name.setCustomNameVisible(true);
					
					new BukkitRunnable() {

					    @Override
					    public void run() {
					    	
					    	name.setCustomNameVisible(false);
					    	name.remove();
					    }
					          
					}.runTaskLater(Main.getInstance(), 15L);
				}
				
			}

		}
			
	}

	private void moveTurret(Location lamp) {
		if (lamp.getWorld().getBlockAt(lamp.clone().add(0, 2, 0)).getType() != Material.AIR)
			return;
		lamp.getWorld().playSound(lamp, Sound.BLOCK_PISTON_CONTRACT, 1f, 0.3f);
		lamp.getWorld().getBlockAt(lamp.clone().add(0, 2, 0)).setType(Material.END_ROD);
		lamp.getWorld().getBlockAt(lamp.clone().add(0, 1, 0)).setType(Material.LIME_WOOL);
		lamp.getWorld().getBlockAt(lamp).setType(Material.EMERALD_BLOCK);
		lamp.getWorld().getBlockAt(lamp.clone().subtract(0, 1, 0)).setType(Material.OAK_FENCE);
	}
	
	private void resetTurret(Location lamp) {
		Random r = new Random();
		if (Main.getInstance().getConfiguration().getTurretCannotDestroyed() == false) {
			if (Main.getInstance().getConfig().getInt("defenseTowers." + owner + ".health") <= 0) {
				Main.getInstance().getConfig().set("defenseTowers." + owner + ".destroyed", true);
				Main.getInstance().getConfig().set("defenseTowers." + owner + ".health", 0);
			} else
				Main.getInstance().getConfig().set("defenseTowers." + owner + ".health", Main.getInstance().getConfig().getInt("defenseTowers." + owner + ".health") - (r.nextInt(7-1) + 1));
			Main.getInstance().saveConfig();
			Main.getInstance().reloadConfig();
		}
		lamp.getWorld().playSound(lamp, Sound.BLOCK_PISTON_EXTEND, 1f, 0.3f);
		lamp.getWorld().getBlockAt(lamp.clone().add(0, 2, 0)).setType(Material.AIR);
		lamp.getWorld().getBlockAt(lamp.clone().add(0, 1, 0)).setType(Material.END_ROD);
		lamp.getWorld().getBlockAt(lamp.clone().add(0, 0, 0)).setType(Material.LIME_WOOL);
		if (Main.getInstance().getConfig().getBoolean("defenseTowers." + owner + ".destroyed")) {
			lamp.getWorld().getBlockAt(lamp.clone().add(0, -1, 0)).setType(Material.BEDROCK);
			lamp.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, lamp, 1, 1.0, 1.0, 1.0);
			lamp.getWorld().playSound(lamp, Sound.BLOCK_ANCIENT_DEBRIS_BREAK, 1f, 0.6f);
		} else
			lamp.getWorld().getBlockAt(lamp.clone().add(0, -1, 0)).setType(Material.EMERALD_BLOCK);
		Main.getInstance().getTowers().loadTowers();
	}

	private void reloadNearbyGuns(Location source) {
		for (Entity ent : source.getWorld().getNearbyEntities(source, 10, 10, 10)) {
			if (ent instanceof ArmorStand) {
				ArmorStand as = (ArmorStand) ent;
				nearbyGuns.add(as);
			}
		}
		Location point = source.clone();
		if (nearbyGuns.size() <= 1) {
			nearbyGuns.clear();
			return;
		}
			
		for (Entity ent : nearbyGuns) {
			if (ent instanceof ArmorStand && ((ArmorStand) ent).isInvisible()) {
				Main.getInstance().getUtils().drawLine(source, ent.getLocation().clone().add(0, 1, 0), 0.7, Color.BLUE);
				point = ent.getLocation();
			}
		}
		nearbyGuns.clear();
	}

}
