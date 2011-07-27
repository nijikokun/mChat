package net.D3GN.MiracleM4n.mChat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkitcontrib.BukkitContrib;

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
		event.setFormat(plugin.parseChat(player, msg) + " ");
		if (plugin.contrib) {
			BukkitContrib.getAppearanceManager().setGlobalTitle(player, ChatColor.valueOf(plugin.contribChatColour.toUpperCase()) + "- " + msg + " -" + '\n' + plugin.parseChat(player));
			plugin.chatt.put(player, false);
			Runnable runnable = new Runnable() { 
				public void run() {
					BukkitContrib.getAppearanceManager().setGlobalTitle(player, plugin.parseChat(player));
				}
			};
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, runnable, 7*20);
		}
	}
	
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String msg = event.getJoinMessage();
		plugin.playerEvent.put(player, false);
		plugin.chatt.put(player, false);
		plugin.contribSP.put(player, false);
		if (msg == null) return;
		event.setJoinMessage(plugin.parseChat(player) + " " + plugin.replaceMess(player, "joinMessage"));
		if (plugin.contrib) {
			BukkitContrib.getAppearanceManager().setGlobalTitle(player, plugin.parseChat(player));
		}
	}
	
	public void onPlayerKick(PlayerKickEvent event) {
		if (event.isCancelled()) return;
		Player player = event.getPlayer();
		String msg = event.getLeaveMessage();
		if (msg == null) return;
		event.setLeaveMessage(plugin.parseChat(player) + " " + plugin.replaceMess(player, "kickMessage"));
	}
	
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		String msg = event.getQuitMessage();
		if (msg == null) return;
		event.setQuitMessage(plugin.parseChat(player) + " " + plugin.replaceMess(player, "leaveMessage"));
	}

	public void run() {
	}
}
