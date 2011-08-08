package net.D3GN.MiracleM4n.mChat;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

public class MPlayerListener extends PlayerListener implements Runnable {
	mChat plugin;
	mChatAPI mAPI;
	
	public MPlayerListener(mChat plugin) {
		this.plugin = plugin;
	}
	
	public void onPlayerChat(PlayerChatEvent event) {
		if (event.isCancelled()) return;
		final Player player = event.getPlayer();
		String msg = event.getMessage();
		if (msg == null) return;
		event.setFormat(mAPI.parseChat(player, msg));
	}
	
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String msg = event.getJoinMessage();
		if (msg == null) return;
		event.setJoinMessage(mAPI.parseJoin(player) + " " + replaceMess("joinMessage"));
	}
	
	public void onPlayerKick(PlayerKickEvent event) {
		if (event.isCancelled()) return;
		Player player = event.getPlayer();
		String msg = event.getLeaveMessage();
		if (msg == null) return;
		event.setLeaveMessage(mAPI.parseJoin(player) + " " + replaceMess("kickMessage"));
	}
	
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		String msg = event.getQuitMessage();
		if (msg == null) return;
		event.setQuitMessage(mAPI.parseJoin(player) + " " + replaceMess("leaveMessage"));
	}
	
	private String replaceMess(String string) {
		if (string.equals("joinMessage")) {
			string = plugin.joinMessage;
		} else if (string.equals("kickMessage")) {
			string = plugin.kickMessage;
		} else if (string.equals("leaveMessage")) {
			string = plugin.leaveMessage;
		}
		return mAPI.addColour(string);
	}

	public void run() {
	}
}

