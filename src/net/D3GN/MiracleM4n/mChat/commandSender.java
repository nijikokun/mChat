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

	public boolean onCommand (CommandSender sender, Command command, String label, String[] args) {
		PluginDescriptionFile pdfFile = plugin.getDescription();
		String commandName = command.getName();
		if (commandName.equalsIgnoreCase("mchat")) {
			if(args.length == 0) {
				return false;
			}
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("reload")) {
					if (sender instanceof Player) {
						Player player = (Player) sender;
						if (plugin.bukkitPermission) {
							 if (player.hasPermission("mchat.reload")) {
								plugin.cListener.checkConfig();
								plugin.cListener.loadConfig();
								sender.sendMessage("[" + (pdfFile.getName()) + "]" + " Config Reloaded.");
								return true;
							 } else {
								sender.sendMessage("[" + (pdfFile.getName()) + "]" + " You are not allowed to reload mChat.");
								return true;
							 }
						} else if (plugin.oldPerm) {
							if(mChat.permissions.has(player, ("mchat.reload"))) {
								plugin.cListener.checkConfig();
								plugin.cListener.loadConfig();
								sender.sendMessage("[" + (pdfFile.getName()) + "]" + " Config Reloaded.");
								return true;
							} else {
								sender.sendMessage("[" + (pdfFile.getName()) + "]" + " You are not allowed to reload mChat.");
								return true;
							}
						} else if (player.isOp()) {
							plugin.cListener.checkConfig();
							plugin.cListener.loadConfig();
							sender.sendMessage("[" + (pdfFile.getName()) + "]" + " Config Reloaded.");
							return true;
						} else {
							sender.sendMessage("[" + (pdfFile.getName()) + "]" + " You are not allowed to reload mChat.");
							return true;
						}
					} else {
						plugin.cListener.checkConfig();
						plugin.cListener.loadConfig();
						plugin.console.sendMessage("[" + (pdfFile.getName()) + "]" + " Config Reloaded.");
						return true;
					}
				}
			}
			return false;
    	} else if (commandName.equalsIgnoreCase("mchatme")) {
            if (args.length > 0) {
                String message = "";
                for (int i = 0; i < args.length; ++i) {
                	message += " " + args[i];
                }
                if (sender instanceof Player) {
                	Player player = (Player) sender;
                	String senderName = plugin.parseChat(player);
                	if (plugin.bukkitPermission) {
                		if (player.hasPermission("mchat.me")) {
            	            plugin.getServer().broadcastMessage("*" + " " + senderName + message);
            	            plugin.console.sendMessage("*" + " " + senderName + message);
            	            return true;
                		} else {
                			sender.sendMessage("[" + (pdfFile.getName()) + "]" + " You are not allowed to use /mchatme.");
                		}
                	} else if (plugin.oldPerm) {
                		if (mChat.permissions.has(player, "mchat.me")) {
            	            plugin.getServer().broadcastMessage("*" + " " + senderName + message);
            	            plugin.console.sendMessage("*" + " " + senderName + message);
            	            return true;
                		} else {
                			sender.sendMessage("[" + (pdfFile.getName()) + "]" + " You are not allowed to use /mchatme.");
                		}
                	} else {
                		if (player.hasPermission("mchat.me")) {
            	            plugin.getServer().broadcastMessage("*" + " " + senderName + message);
            	            plugin.console.sendMessage("*" + " " + senderName + message);
            	            return true;
                		} else {
                			sender.sendMessage("[" + (pdfFile.getName()) + "]" + " You are not allowed to use /mchatme.");
                		}
                	}
    			} else {
    				String senderName = "Console";
    	            plugin.getServer().broadcastMessage("*" + " " + senderName + message);
    	            plugin.console.sendMessage("*" + " " + senderName + message);
    	            return true;
    			}
            } else {
            	return false;
            }
        }
		return false;
	}
}
