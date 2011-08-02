package net.D3GN.MiracleM4n.mChat;

import org.bukkit.ChatColor;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.event.input.InputListener;
import org.getspout.spoutapi.event.input.KeyPressedEvent;
import org.getspout.spoutapi.keyboard.Keyboard;
import org.getspout.spoutapi.player.SpoutPlayer;

public class customListener extends InputListener {
	mChat plugin;
	
	public customListener(mChat plugin) {
		this.plugin = plugin;
	}
	
	public void onKeyPressedEvent(KeyPressedEvent event) {
		SpoutPlayer player = (SpoutPlayer) event.getPlayer();
		Keyboard key = event.getKey();
		Keyboard chatKey = player.getChatKey();
		Keyboard forwardKey = player.getForwardKey();
		Keyboard backwardKey = player.getBackwardKey();
		Keyboard leftKey = player.getLeftKey();
		Keyboard rightKey = player.getRightKey();
		if (plugin.chatt.get(player) == null) {
			plugin.chatt.put(player, false);
		}
		if (key == null) return;
		if (key.equals(chatKey)) {
			SpoutManager.getAppearanceManager().setGlobalTitle(player, ChatColor.valueOf(plugin.spoutChatColour.toUpperCase()) + plugin.addColour(plugin.typingMessage) + '\n' + plugin.parseName(player));
			plugin.chatt.put(player, true);
		}
		if (plugin.chatt.get(player) == true) {
			if ((key.equals(forwardKey)) ||
					(key.equals(backwardKey)) ||
							(key.equals(leftKey)) ||
									(key.equals(rightKey))) {
				SpoutManager.getAppearanceManager().setGlobalTitle(player, plugin.parseName(player));
				plugin.chatt.put(player, false);
			}
		}
	}
}
