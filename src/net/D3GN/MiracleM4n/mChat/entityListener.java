package net.D3GN.MiracleM4n.mChat;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SpoutPlayer;

public class entityListener extends EntityListener implements Runnable {
	mChat plugin;
	
	public entityListener(mChat plugin) {
		this.plugin = plugin;
	}
	
	Boolean messageTimeout = true;
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.isCancelled()) return;
		if (event instanceof EntityDamageByEntityEvent) {
			if (plugin.isAFK == null) return;
			EntityDamageByEntityEvent subEvent = (EntityDamageByEntityEvent) event;
			Entity attacker = null;
			attacker = subEvent.getDamager();
			if (attacker instanceof Player) {
				Player player = (Player) attacker;
				if (plugin.isAFK.get(player) == null) return;
				if (plugin.isAFK.get(player)) {
					event.setCancelled(true);
				}
			}
		}
		if (event.getEntity() instanceof Player) {
			final Player player = (Player) event.getEntity();
			if (plugin.isAFK != null) {
				if (plugin.isAFK.get(player) != null) {
					if (plugin.isAFK.get(player)) {
						event.setCancelled(true);
						return;
					}
				}
			} 
			if (plugin.healthNotify) {
				Runnable timeRunnable = new Runnable() { 
					public void run() {
						messageTimeout = true;
					}
				};
				Runnable runnable = new Runnable() { 
					public void run() {
						SpoutManager.getAppearanceManager().setGlobalTitle(player, plugin.parseName(player));
					}
				};
				if (messageTimeout) {
					for (Player players : plugin.getServer().getOnlinePlayers()) {
						if (players != player) {
							if (plugin.spout) {
								SpoutPlayer splayers = (SpoutPlayer) players;
								if (splayers.isSpoutCraftEnabled()) {
									if(player.getName().length() < 25) {
										splayers.sendNotification(healthBarDamage(player, event.getDamage()), player.getName(), Material.LAVA);	
									} else {
										if ((player.getHealth() - event.getDamage()) < 1) {
											players.sendMessage(healthBarDamage(player, event.getDamage()) + " " + plugin.parseName(player) + " has died!");
										} else {
											players.sendMessage(healthBarDamage(player, event.getDamage()) + " " + plugin.parseName(player) + " has lost health! " + (player.getHealth() - event.getDamage()) + " health left.");
										}
									}
								}
							} else {
								if ((player.getHealth() - event.getDamage()) < 1) {
									players.sendMessage(healthBarDamage(player, event.getDamage()) + " " + plugin.parseName(player) + " has died!");
								} else {
									players.sendMessage(healthBarDamage(player, event.getDamage()) + " " + plugin.parseName(player) + " has lost health! " + (player.getHealth() - event.getDamage()) + " health left.");
								}
							}
						}
					}
					if (plugin.spout) {
						SpoutPlayer splayer = (SpoutPlayer) player;
						if (splayer.isSpoutCraftEnabled()) {
							if ((player.getHealth() - event.getDamage()) < 1) {
								splayer.sendNotification(healthBarDamage(player, event.getDamage()), "You have died!", Material.LAVA);
							} else {
								splayer.sendNotification(healthBarDamage(player, event.getDamage()), "You have " + (player.getHealth() - event.getDamage()) + " health left.", Material.LAVA);
							}
						} else {
							if ((player.getHealth() - event.getDamage()) < 1) {
								player.sendMessage(healthBarDamage(player, event.getDamage()) + " You have died!");
							} else {
								player.sendMessage(healthBarDamage(player, event.getDamage()) + " You have lost health! You have " + (player.getHealth() - event.getDamage()) + " health left.");
							}
						}
					} else {
						if ((player.getHealth() - event.getDamage()) < 1) {
							player.sendMessage(healthBarDamage(player, event.getDamage()) + " You have died!");
						} else {
							player.sendMessage(healthBarDamage(player, event.getDamage()) + " You have lost health! You have " + (player.getHealth() - event.getDamage()) + " health left.");
						}
					}
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, timeRunnable, 1*20);
					messageTimeout = false;
				}
				if (plugin.spout) {
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, runnable, 4*20);
					SpoutManager.getAppearanceManager().setGlobalTitle(player, ChatColor.valueOf(plugin.spoutChatColour.toUpperCase()) + "- " + healthBarDamage(player, event.getDamage()) + " -" + '\n' + plugin.parseName(player));
					plugin.chatt.put(player, false);
				}
			}
		}
	}

	public String healthBarDamage(Player player, Integer damage) {
		float maxHealth = 20;
		float barLength = 10;
		float health = player.getHealth();
		int fill = Math.round( ((health - damage) / maxHealth) * barLength );
		String barColor = "&2";
		if (fill <= 4) barColor = "&4";
		else if (fill <= 7) barColor = "&e";
		else barColor = "&2";
		StringBuilder out = new StringBuilder();
		out.append(barColor);
		for (int i = 0; i < barLength; i++) {
			if (i == fill) out.append("&8");
			out.append("|");
		}
		out.append("&f");
		return out.toString().replaceAll("(&([A-Fa-f0-9]))", "\u00A7$2");
	}

	public void run() {
	}
}




