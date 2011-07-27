package net.D3GN.MiracleM4n.mChat;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkitcontrib.BukkitContrib;
import org.bukkitcontrib.player.ContribPlayer;

public class entityListener extends EntityListener implements Runnable {
	mChat plugin;
	
	public entityListener(mChat plugin) {
		this.plugin = plugin;
	}
	
	Boolean messageTimeout = true;
	
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.isCancelled()) return;
		if (plugin.healthNotify) {
			if (event.getEntity() instanceof Player) {
				final Player player = (Player) event.getEntity();
				Runnable timeRunnable = new Runnable() { 
					public void run() {
						messageTimeout = true;
					}
				};
				Runnable runnable = new Runnable() { 
					public void run() {
						BukkitContrib.getAppearanceManager().setGlobalTitle(player, plugin.parseChat(player));
					}
				};
				if (messageTimeout) {
					for (Player players : plugin.getServer().getOnlinePlayers()) {
						if (players != player) {
							if (plugin.contrib) {
								ContribPlayer cplayers = (ContribPlayer) players;
								if(player.getName().length() > 25) {
									if ((player.getHealth() - event.getDamage()) < 1) {
										players.sendMessage(healthBarDamage(player, event.getDamage()) + " " + plugin.parseChat(player) + " has died!");
									} else {
										players.sendMessage(healthBarDamage(player, event.getDamage()) + " " + plugin.parseChat(player) + " has lost health! " + (player.getHealth() - event.getDamage()) + " health left.");
									}
								} else {
									cplayers.sendNotification(healthBarDamage(player, event.getDamage()), player.getName(), Material.LAVA);	
								}
							} else {
								if ((player.getHealth() - event.getDamage()) < 1) {
									players.sendMessage(healthBarDamage(player, event.getDamage()) + " " + plugin.parseChat(player) + " has died!");
								} else {
									players.sendMessage(healthBarDamage(player, event.getDamage()) + " " + plugin.parseChat(player) + " has lost health! " + (player.getHealth() - event.getDamage()) + " health left.");
								}
							}
						}
					}
					if (plugin.contrib) {
						ContribPlayer cplayer = (ContribPlayer) player;
						if ((player.getHealth() - event.getDamage()) < 1) {
							cplayer.sendNotification(healthBarDamage(player, event.getDamage()), "You have died!", Material.LAVA);
						} else {
							cplayer.sendNotification(healthBarDamage(player, event.getDamage()), "You have " + (player.getHealth() - event.getDamage()) + " health left.", Material.LAVA);
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
				if (plugin.contrib) {
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, runnable, 4*20);
					BukkitContrib.getAppearanceManager().setGlobalTitle(player, ChatColor.valueOf(plugin.contribChatColour.toUpperCase()) + "- " + healthBarDamage(player, event.getDamage()) + " -" + '\n' + plugin.parseChat(player));
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




