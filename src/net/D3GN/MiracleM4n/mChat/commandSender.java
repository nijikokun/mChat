package net.D3GN.MiracleM4n.mChat;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SpoutPlayer;

public class commandSender implements CommandExecutor, Runnable {
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
    	} else if (commandName.equalsIgnoreCase("mchatme")) {
            if (args.length > 0) {
                String message = "";
                for (int i = 0; i < args.length; ++i) {
                	message += " " + args[i];
                }
                if (sender instanceof Player) {
                	Player player = (Player) sender;
                	String senderName = plugin.parseName(player);
              		if (player.hasPermission("mchat.me")) {
        	            plugin.getServer().broadcastMessage("*" + " " + senderName + message);
        	            plugin.console.sendMessage("*" + " " + senderName + message);
        	            return true;
            		} else {
            			sender.sendMessage(formatMessage("You are not allowed to use /mchatme."));
            			return true;
            		}
    			} else {
    				String senderName = "Console";
    	            plugin.getServer().broadcastMessage("*" + " " + senderName + message);
    	            plugin.console.sendMessage("*" + " " + senderName + message);
    	            return true;
    			}
            }
        } else if (commandName.equalsIgnoreCase("mchatwho")) {
            if (args.length == 1) {
                if (sender instanceof Player) {
			 		Player player = (Player) sender;
               		if (player.hasPermission("mchat.who")) {
                    	if (plugin.getServer().getPlayer(args[0]) == null) {
    				 		sender.sendMessage(formatPNF(args[0]));
    				 		return true;
    				 	} else {
    	                	Player receiver = plugin.getServer().getPlayer(args[0]);
                			formatWho(player, receiver);
            	            return true;
    				 	}
            		} else {
            			sender.sendMessage(formatMessage("You are not allowed to use /mchatwho."));
            			return true;
            		}
    			} else {
                	if (plugin.getServer().getPlayer(args[0]) == null) {
				 		sender.sendMessage(formatPNF(args[0]));
				 		return true;
				 	} else {
	                	Player receiver = plugin.getServer().getPlayer(args[0]);
            			plugin.console.sendMessage(formatConsoleWho(receiver));
				 		return true;
				 	}
    			}
            }
        } else if(commandName.equalsIgnoreCase("mchatafk")) {
        	if (args.length == 0) {
        		if (sender instanceof Player) {
					Player player = (Player) sender;
					if (player.hasPermission("mchat.afk")) {
						if (plugin.isAFK.get(player) == true) {
							if (plugin.spout) {
								if (plugin.spoutEnabled) {
									for (Player players : plugin.getServer().getOnlinePlayers()) {
										SpoutPlayer splayers = (SpoutPlayer) players;
										System.out.println(splayers + "Is it ? " + splayers.isSpoutCraftEnabled());
										if (splayers.isSpoutCraftEnabled()) {
											splayers.sendNotification(player.getName(), "is no longer AFK.", Material.PAPER);
										}
									}
									SpoutManager.getAppearanceManager().setGlobalTitle(player, ChatColor.valueOf(plugin.spoutChatColour.toUpperCase()) + "- " + "AFK" + " -" + '\n' + plugin.parseName(player));
								}
							}
							plugin.getServer().broadcastMessage(plugin.parseName(player) + " is no longer AFK.");
							plugin.isAFK.put(player, false);
							return true;
						} else {
							if (plugin.spout) {
								if (plugin.spoutEnabled) {
									for (Player players : plugin.getServer().getOnlinePlayers()) {
										SpoutPlayer splayers = (SpoutPlayer) players;
										if (splayers.isSpoutCraftEnabled()) {
											splayers.sendNotification(player.getName(), "is now AFK.", Material.PAPER);
										}
									}
									SpoutManager.getAppearanceManager().setGlobalTitle(player, ChatColor.valueOf(plugin.spoutChatColour.toUpperCase()) + "- " + "AFK" + " -" + '\n' + plugin.parseName(player));
								}
							}
							plugin.getServer().broadcastMessage(plugin.parseName(player) + " is now AFK.");
							plugin.isAFK.put(player, true);
							plugin.AFKLoc.put(player, player.getLocation());
							return true;
						}
					} else {
						player.sendMessage(formatMessage("You dont have permissions to be AFK...LAWL"));
						return true;
					}
				} else {
					plugin.console.sendMessage(formatMessage("Console's can't be AFK."));
					return true;
				}
        	}
        }
		return false;
	}
	
	public String formatConsoleWho(Player recipient) {
		String recipientName = plugin.parseName(recipient);
		Integer locX = (int) recipient.getLocation().getX();
		Integer locY = (int) recipient.getLocation().getY();
		Integer locZ = (int) recipient.getLocation().getZ();
		String loc = ("X:" + locX + ", " + "Y:" + locY + ", " +"Z:" + locZ);
		String world = recipient.getWorld().getName();
		return(plugin.addColour(recipient + ":" + recipientName + ", " + '\n' + 
				"World: " + world + ", " + '\n' +
				"Location: [ " + loc + " ]"));
	}
	
	public void formatWho(Player sender, Player recipient) {
		String recipientName = plugin.parseName(recipient);
		Integer locX = (int) recipient.getLocation().getX();
		Integer locY = (int) recipient.getLocation().getY();
		Integer locZ = (int) recipient.getLocation().getZ();
		String loc = ("X: " + locX + ", " + "Y: " + locY + ", " +"Z: " + locZ);
		String world = recipient.getWorld().getName();
		sender.sendMessage(plugin.addColour("Name: " + recipientName));
		sender.sendMessage(plugin.addColour("World: " + world));
		sender.sendMessage(plugin.addColour("Location: [ " + loc + " ]"));
	}
	
	public String formatMessage(String message) {
		PluginDescriptionFile pdfFile = plugin.getDescription();
		return(plugin.addColour("&4[" + (pdfFile.getName()) + "] " + message));
	}
	
	public String formatPNF(String playerNotFound) {
		return(plugin.addColour(formatMessage("") + " Player &e" + playerNotFound + " &4not found."));
	}
	
	public void run() {
	}
}
