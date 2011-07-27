package net.D3GN.MiracleM4n.mChat;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkitcontrib.player.ContribPlayer;

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
								sender.sendMessage(formatMessage("Config Reloaded."));
								return true;
							 } else {
								sender.sendMessage(formatMessage("You are not allowed to reload mChat."));
								return true;
							 }
						} else if (plugin.oldPerm) {
							if(mChat.permissions.has(player, ("mchat.reload"))) {
								plugin.cListener.checkConfig();
								plugin.cListener.loadConfig();
								sender.sendMessage(formatMessage("Config Reloaded."));
								return true;
							} else {
								sender.sendMessage(formatMessage("You are not allowed to reload mChat."));
								return true;
							}
						} else if (player.isOp()) {
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
			if (args.length > 2) {
                message = "";
                for (int i = 2; i < args.length; ++i) {
                	message += " " + args[i];
                }
                if (args[0].equalsIgnoreCase("pm")) {
    			 	if (plugin.getServer().getPlayer(args[1]) == null) {
    			 		sender.sendMessage(formatPNF(args[1]));
    			 		return true;
    			 	} else {
    	                Player recipient = (Player) plugin.getServer().getPlayer(args[1]);
    	                String recipientName = plugin.parseChat(recipient);
    			 		if (sender instanceof Player) {
    			 			Player player = (Player) sender;
    			 			if (plugin.bukkitPermission) {
    	                		if (player.hasPermission("mchat.pm")) {
    	    			 			final String senderName = plugin.parseChat(player);
    	                			sender.sendMessage(formatPMSend(senderName, recipientName, message));
    	    			 			if (plugin.contrib) {
    	    			 				if (plugin.contribPM) {
        	    			 				final ContribPlayer crecipient = (ContribPlayer) recipient;
        	    							Runnable runnable = new Runnable() { 
        	    								public void run() {
        	    	    			 				for (int i = 0; i < ((message.length() / 40) + 1); i++) {
            	    	    			 				crecipient.sendNotification(formatPM(message, ((40*i)+1), ((i*40)+20)), formatPM(message, ((i*40)+21), ((i*40)+40)), Material.PAPER);
            	    	    			 				waiting(2000);
        	    	    			 				}
        	    								}
        	    							};
        	    							crecipient.sendNotification("[pmChat] From:", senderName, Material.PAPER);
        	    							plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, runnable, 2*20);
        	    							return true;
        	    			 			}
    	    			 			}
        	    			 		recipient.sendMessage(formatPMRecieve(senderName, message));
    	    			 			return true;
    	                		} else {
    	                			sender.sendMessage(formatMessage("You are not allowed to use pm functions."));
    	                			return true;
    	                		}
    	                	} else if (plugin.oldPerm) {
    	                		if (mChat.permissions.has(player, "mchat.pm")) {
    	    			 			String senderName = plugin.parseChat(player);
    	    			 			sender.sendMessage(formatPMSend(senderName, recipientName, message));
    	    			 			recipient.sendMessage(formatPMRecieve(senderName, message));
    	    			 			return true;
    	                		} else {
    	                			sender.sendMessage(formatMessage("You are not allowed to use pm functions."));
    	                			return true;
    	                		}
    	                	} else {
    	                		if (player.hasPermission("mchat.pm")) {
    	    			 			String senderName = plugin.parseChat(player);
    	    			 			sender.sendMessage(formatPMSend(senderName, recipientName, message));
    	    			 			recipient.sendMessage(formatPMRecieve(senderName, message));
    	    			 			return true;
    	                		} else {
    	                			sender.sendMessage(formatMessage("You are not allowed to use pm functions."));
    	                			return true;
    	                		}
    	                	}
    			 		} else {
    			 			sender.sendMessage(formatPMSend("*Console*", recipientName, message));
    			 			recipient.sendMessage(formatPMRecieve("*Console*", message));
    			 			return true;
    			 		}
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
                			sender.sendMessage(formatMessage("You are not allowed to use /mchatme."));
                			return true;
                		}
                	} else if (plugin.oldPerm) {
                		if (mChat.permissions.has(player, "mchat.me")) {
            	            plugin.getServer().broadcastMessage("*" + " " + senderName + message);
            	            plugin.console.sendMessage("*" + " " + senderName + message);
            	            return true;
                		} else {
                			sender.sendMessage(formatMessage("You are not allowed to use /mchatme."));
                			return true;
                		}
                	} else {
                		if (player.hasPermission("mchat.me")) {
            	            plugin.getServer().broadcastMessage("*" + " " + senderName + message);
            	            plugin.console.sendMessage("*" + " " + senderName + message);
            	            return true;
                		} else {
                			sender.sendMessage(formatMessage("You are not allowed to use /mchatme."));
                			return true;
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
	
	public static void waiting(int n){
        long t0, t1;
        t0 =  System.currentTimeMillis();
        do{
            t1 = System.currentTimeMillis();
        }
        while ((t1 - t0) < n);
    }

	public String formatPM(String message, Integer start, Integer finish) {
		while (message.length() < finish) message += " ";
		return message.substring(start, finish);
	}
	
	public String formatMessage(String message) {
		PluginDescriptionFile pdfFile = plugin.getDescription();
		return("&4[" + (pdfFile.getName()) + "] " + message).replaceAll("(&([A-Fa-f0-9]))", "\u00A7$2");
	}
	
	public String formatPNF(String playerNotFound) {
		PluginDescriptionFile pdfFile = plugin.getDescription();
		return("&4[p" + (pdfFile.getName()) + "]" + " Player &e" + playerNotFound + " &4not found.").replaceAll("(&([A-Fa-f0-9]))", "\u00A7$2");
	}
	
	public String formatPMSend(String sender, String recipient, String message) {
		PluginDescriptionFile pdfFile = plugin.getDescription();
		return("&4[p" + (pdfFile.getName()) + "] &f" + sender + " &1-&2-&3-&4> &f" + recipient + "&f: " + message).replaceAll("(&([A-Fa-f0-9]))", "\u00A7$2");
	}
	
	public String formatPMRecieve(String sender, String message) {
		PluginDescriptionFile pdfFile = plugin.getDescription();
		return("&4[p" + (pdfFile.getName()) + "] &f" + sender + "&f: " + message).replaceAll("(&([A-Fa-f0-9]))", "\u00A7$2");
	}

	public void run() {
	}
}
