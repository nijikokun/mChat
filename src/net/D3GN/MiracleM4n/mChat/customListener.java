package net.D3GN.MiracleM4n.mChat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkitcontrib.BukkitContrib;
import org.bukkitcontrib.event.input.InputListener;
import org.bukkitcontrib.event.input.KeyPressedEvent;
import org.bukkitcontrib.keyboard.Keyboard;
import org.bukkitcontrib.player.ContribPlayer;

public class customListener extends InputListener {
	mChat plugin;
	
	public customListener(mChat plugin) {
		this.plugin = plugin;
	}
	
	public void onKeyPressedEvent(KeyPressedEvent event) {
		ContribPlayer player = (ContribPlayer) event.getPlayer();
		Keyboard key = event.getKey();
		Keyboard chatKey = player.getChatKey();
		Keyboard forwardKey = player.getForwardKey();
		Keyboard backwardKey = player.getBackwardKey();
		Keyboard leftKey = player.getLeftKey();
		Keyboard rightKey = player.getRightKey();
		if (plugin.chatt.get(player) == null) {
			plugin.chatt.put(player, false);
		}
		if (key.equals(chatKey)) {
			for (final Player players : plugin.getServer().getOnlinePlayers()) {
				if (plugin.channelInfo.get(players).equals(plugin.channelInfo.get(player))) {
					ContribPlayer cplayers = (ContribPlayer) players;
					BukkitContrib.getAppearanceManager().setPlayerTitle(cplayers, player, ChatColor.DARK_RED + "*Typing*" + '\n' + plugin.parseNameFormat(player));
					plugin.chatt.put(player, true);
				}
			}
		}
		if (plugin.chatt.get(player) == true) {
			if ((key.equals(forwardKey)) ||
					(key.equals(backwardKey)) ||
							(key.equals(leftKey)) ||
									(key.equals(rightKey))) {
				BukkitContrib.getAppearanceManager().setGlobalTitle(player, plugin.parseNameFormat(player));
				plugin.chatt.put(player, false);
			}
		}
	}
}
