package net.D3GN.MiracleM4n.mChat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.getspout.spoutapi.SpoutManager;

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
		event.setFormat(plugin.parseChat(player, msg));
		if (plugin.spout) {
			SpoutManager.getAppearanceManager().setGlobalTitle(player, ChatColor.valueOf(plugin.spoutChatColour.toUpperCase()) + "- " + plugin.addColour(msg) + ChatColor.valueOf(plugin.spoutChatColour.toUpperCase()) + " -" + '\n' + plugin.parseName(player));
			plugin.chatt.put(player, false);
			Runnable runnable = new Runnable() { 
				public void run() {
					SpoutManager.getAppearanceManager().setGlobalTitle(player, plugin.parseName(player));
				}
			};
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, runnable, 7*20);
		}
	}
	
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String msg = event.getJoinMessage();
		plugin.chatt.put(player, false);
		plugin.isAFK.put(player, false);
		if (msg == null) return;
		event.setJoinMessage(plugin.parseJoin(player) + " " + replaceMess(player, "joinMessage"));
		if (plugin.spout) {
			SpoutManager.getAppearanceManager().setGlobalTitle(player, plugin.parseName(player));
		}
	}
	
	public void onPlayerKick(PlayerKickEvent event) {
		if (event.isCancelled()) return;
		Player player = event.getPlayer();
		String msg = event.getLeaveMessage();
		if (msg == null) return;
		event.setLeaveMessage(plugin.parseJoin(player) + " " + replaceMess(player, "kickMessage"));
	}
	
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		String msg = event.getQuitMessage();
		if (msg == null) return;
		event.setQuitMessage(plugin.parseJoin(player) + " " + replaceMess(player, "leaveMessage"));
	}
	
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (plugin.isAFK == null) return;
		if (plugin.isAFK.get(player) == null) return;
		if (plugin.isAFK.get(player)) {
			player.teleport(plugin.AFKLoc.get(player));
		}
	}
	
	public String replaceMess(Player player, String string) {
		if (string.equals("joinMessage")) {
			string = plugin.joinMessage;
		} else if (string.equals("kickMessage")) {
			string = plugin.kickMessage;
		} else if (string.equals("leaveMessage")) {
			string = plugin.leaveMessage;
		}
		return plugin.addColour(string);
	}

	public void run() {
	}
}
