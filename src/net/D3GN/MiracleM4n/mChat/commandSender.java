package net.D3GN.MiracleM4n.mChat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

public class commandSender implements CommandExecutor {
	mChat plugin;
	
	public commandSender(mChat plugin) {
		this.plugin = plugin;
	}
	
	String message = "";
	Boolean hasTime = false;

	public boolean onCommand (CommandSender sender, Command command, String label, String[] args) {
		String commandName = command.getName();
		if (commandName.equalsIgnoreCase("mchat")) {
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("reload")) {
					if (sender instanceof Player) {
						Player player = (Player) sender;
						 if (player.hasPermission("mchat.reload")) {
							plugin.cListener.checkConfig();
							plugin.cListener.loadConfig();
							sender.sendMessage(formatMessage("Config Reloaded."));
							return true;
						 } else {
							sender.sendMessage(formatMessage("You are not allowed to reload mChat."));
							return true;
						 }
					} else {
						plugin.cListener.checkConfig();
						plugin.cListener.loadConfig();
						plugin.console.sendMessage(formatMessage("Config Reloaded."));
						return true;
					}
				}
			}
    	}
		return false;
	}
	
	public String formatMessage(String message) {
		PluginDescriptionFile pdfFile = plugin.getDescription();
		return(plugin.addColour("&4[" + (pdfFile.getName()) + "] " + message));
	}
}
