package net.D3GN.MiracleM4n.mChat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkitcontrib.BukkitContrib;
import org.bukkitcontrib.player.ContribPlayer;

public class playerListener extends PlayerListener implements Runnable {
	mChat plugin;
	
	public playerListener(mChat plugin) {
		this.plugin = plugin;
	}
	
	public void onPlayerChat(PlayerChatEvent event) {
		if (event.isCancelled()) return;
		final Player player = event.getPlayer();
		String msg = event.getMessage();
		if (msg == null) return;
		if (plugin.channelInfo.get(player).equals(null)) {
			plugin.channelInfo.put(player, "public");
		}
		for (final Player players : plugin.getServer().getOnlinePlayers()) {
			if (plugin.channelInfo.get(players).equals(plugin.channelInfo.get(player))) {
				if (plugin.contrib) {
					ContribPlayer cplayers = (ContribPlayer) players;
					BukkitContrib.getAppearanceManager().setPlayerTitle(cplayers, player, ChatColor.DARK_RED + "- " + msg + " -" + '\n' + "[" + plugin.channelInfo.get(player).toUpperCase() + "] " + plugin.parseNameFormat(player));
					plugin.chatt.put(player, false);
					Runnable runnable = new Runnable() { 
						public void run() {
							BukkitContrib.getAppearanceManager().setGlobalTitle(player, plugin.parseNameFormat(player));
						}
					};
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, runnable, 7*20);
				}
				continue;
			} else {
				event.getRecipients().remove(players);
			}
		}
		if (!(plugin.channelInfo.get(player).equals("public"))) {
			for (final Player players : plugin.getServer().getOnlinePlayers()) {
				if (plugin.channelInfo.get(players).equals(plugin.channelInfo.get(player))) {
					plugin.console.sendMessage("[" + plugin.channelInfo.get(player).toUpperCase() + "] " + plugin.parseChat(player, msg) + " ");
					players.sendMessage("[" + plugin.channelInfo.get(player).toUpperCase() + "] " + plugin.parseChat(player, msg) + " ");
					event.setCancelled(true);
				}
			}
		} else {
			event.setFormat(plugin.parseChat(player, msg) + " ");
		}
	}
	
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String pName = player.getName();
		String msg = event.getJoinMessage();
		if (msg == null) return;
		plugin.channelInfo.put(player, "public");
		event.setJoinMessage((msg).replace(pName, plugin.parseNameFormat(player)));
		if (plugin.contrib) {
			BukkitContrib.getAppearanceManager().setGlobalTitle(player, plugin.parseNameFormat(player));
			plugin.chatt.put(player, false);
		}
	}
	
	public void onPlayerKick(PlayerKickEvent event) {
		if (event.isCancelled()) return;
		Player player = event.getPlayer();
		String pName = player.getName();
		String msg = event.getLeaveMessage();
		if (msg == null) return;
		event.setLeaveMessage((msg).replace(pName, plugin.parseNameFormat(player)));
	}
	
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		String pName = player.getName();
		String msg = event.getQuitMessage();
		if (msg == null) return;
		event.setQuitMessage((msg).replace(pName, plugin.parseNameFormat(player)));
	}

	public void run() {
	}
}
