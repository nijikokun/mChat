package net.D3GN.MiracleM4n.mChat;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.craftbukkit.command.ColouredConsoleSender;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;
import org.bukkitcontrib.BukkitContrib;
import org.bukkitcontrib.player.ContribPlayer;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class mChat extends JavaPlugin {
	
	playerListener pListener = new playerListener(this);
	commandSender cSender = new commandSender(this);
	configListener cListener = new configListener(this);
	entityListener eListener = new entityListener(this);
	
	private PluginManager pm;
	public static PermissionHandler permissions;
	ColouredConsoleSender console = null;
	Configuration config;
	
  	Boolean healthNotify = false;
	Boolean contrib = false;
  	Boolean contribEnabled = true;
  	Boolean permissions3 = false;
  	Boolean bukkitPermission = false;
  	Boolean oldPerm = false;
	
	String chatFormat = "+p+dn+s&f: +message";
	String nameFormat = "+p+dn+s&e";
	String dateFormat = "HH:mm:ss";
	String joinMessage = "has joined the game.";
	String leaveMessage = "has left the game.";
	String kickMessage = "has been kicked from the game.";
	String contribChatColour = "dark_red";
	String prefix = "";
	String suffix = "";
	String group = "";
	
	HashMap<String, Object> prefixes = new HashMap<String, Object>();
	HashMap<String, Object> suffixes = new HashMap<String, Object>();
	HashMap<String, Object> groupes = new HashMap<String, Object>();
	HashMap<String, Object> mchat = new HashMap<String, Object>();
	HashMap<Player, Boolean> chatt = new HashMap<Player, Boolean>();
	HashMap<Player, Boolean> playerEvent = new HashMap<Player, Boolean>();
	HashMap<Player, Boolean> contribSP = new HashMap<Player, Boolean>();
	
	public void onEnable() {
		pm = getServer().getPluginManager();
		config = getConfiguration();
		console = new ColouredConsoleSender((CraftServer)getServer());
		PluginDescriptionFile pdfFile = getDescription();
		
		if (!(new File(getDataFolder(), "config.yml")).exists()) {
			cListener.defaultConfig();
			cListener.checkConfig();
			cListener.loadConfig();
		} else {
			cListener.checkConfig();
			cListener.loadConfig();
		}
		
		getContrib();
		
		pm.registerEvent(Event.Type.ENTITY_DAMAGE, eListener, Priority.High, this);
		pm.registerEvent(Event.Type.PLAYER_KICK, pListener, Priority.High, this);
		pm.registerEvent(Event.Type.PLAYER_CHAT, pListener, Priority.High, this);
		pm.registerEvent(Event.Type.PLAYER_JOIN, pListener, Priority.High, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, pListener, Priority.High, this);
		getCommand("mchat").setExecutor(cSender);
		getCommand("mchatme").setExecutor(cSender);
		
		if (contrib) {
			customListener cusListener = new customListener(this);
			pm.registerEvent(Event.Type.CUSTOM_EVENT, cusListener, Event.Priority.High, this);
		}
		
		setupPermissions();
		
		console.sendMessage("[" + (pdfFile.getName()) + "]" + " version " + 
				pdfFile.getVersion() + " is enabled!");
		
		for (Player players : getServer().getOnlinePlayers()) {
			contribSP.put(players, false);
			playerEvent.put(players, false);
			chatt.put(players, false);
			if (contrib) {
				BukkitContrib.getAppearanceManager().setGlobalTitle(players, parseChat(players));
				ContribPlayer cplayers = (ContribPlayer) players;
				if (cplayers.isBukkitContribEnabled()) {
					contribSP.put(cplayers, true);
				}
			}
		}
	}
	
	public void onDisable() {
		PluginDescriptionFile pdfFile = getDescription();
		
		console.sendMessage("[" + (pdfFile.getName()) + "]" + " version " + 
				pdfFile.getVersion() + " is disabled!");
	}
	
	public String replaceMess(Player player, String string) {
		if (string == "joinMessage") {
			string = joinMessage;
		} else if (string == "kickMessage") {
			string = kickMessage;
		} else if (string == "leaveMessage") {
			string = leaveMessage;
		}
		return string.replaceAll("(&([A-Fa-f0-9]))", "\u00A7$2");
	}
	
	/*
	 * Beginning of work initially taken from Drakia's iChat.
	 * (I have added and removed A BUNCH.)
	 */
	
	public String replaceVars(String format, String[] search, String[] replace) {
		if (search.length != replace.length) return "";
		for (int i = 0; i < search.length; i++) {
			if (search[i].contains(",")) {
				for (String s : search[i].split(",")) {
					if (s == null || replace[i] == null) continue;
					format = format.replace(s, replace[i]);
				}
			} else {
				format = format.replace(search[i], replace[i]);
			}
		}
		return format.replaceAll("(&([A-Fa-f0-9]))", "\u00A7$2");
	}
	
	public String parseChat(Player player, String msg, String formatAll) {
		playerEvent.put(player, false);
		String prefix = getPrefix(player);
		String suffix = getSuffix(player);
		String group = getGroup(player);
		if (prefix == null) prefix = "";
		if (suffix == null) suffix = "";
		if (group == null) group = "";
		String healthbar = healthBar(player);
		String health = String.valueOf(player.getHealth());
		String world = player.getWorld().getName();
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(this.dateFormat);
		String time = dateFormat.format(now);
		String format = (formatAll);
		String[] search;
		String[] replace;
		if (msg == "") {
			search = new String[] {"+suffix,+s", "+prefix,+p", "+group,+g", "+world,+w", "+time,+t", "+name,+n", "+dname,+dn", "+healthbar,+hb", "+health,+h"};
			replace = new String[] { suffix, prefix, group, world, time, player.getName(), player.getDisplayName(), healthbar, health };
		} else {
			msg = msg.replaceAll("%", "%%");
			if (format == null) return msg;
			search = new String[] {"+suffix,+s", "+prefix,+p", "+group,+g", "+world,+w", "+time,+t", "+name,+n", "+dname,+dn", "+healthbar,+hb", "+health,+h", "+message,+msg,+m"};
			replace = new String[] { suffix, prefix, group, world, time, player.getName(), player.getDisplayName(), healthbar, health, msg };
		}
		return replaceVars(format, search, replace);
	}
	
	public String parseChat(Player player, String msg) {
		if (playerEvent.get(player)) {
			return parseChat(player, msg, this.nameFormat);
		} else {
			return parseChat(player, msg, this.chatFormat);
		}
	}
	
	public String parseChat(Player player) {
		return parseChat(player, "", this.nameFormat);
	}
	
	public String healthBar(Player player) {
		float maxHealth = 20;
		float barLength = 10;
		float health = player.getHealth();
		int fill = Math.round( (health / maxHealth) * barLength );
		String barColor = "&2";
		if (fill <= 4) barColor = "&4";
		else if (fill <= 7) barColor = "&e";
		else barColor = "&2";
		StringBuilder out = new StringBuilder();
		out.append(barColor);
		for (int i = 0; i < barLength; i++) {
			if (i == fill) out.append("&8");
			out.append("|");
		}
		out.append("&f");
		return out.toString();
	}
	
	@SuppressWarnings("deprecation")
	public String getPrefix(Player player) {
		String world = player.getWorld().getName();
		String pl = player.getName();
		if (bukkitPermission) {
			for (Entry<String, Object> entry : prefixes.entrySet()) {
			    if (player.hasPermission("mchat.prefix." + entry.getKey())) {
			        prefix = entry.getValue().toString();
					if (prefix != null && !prefix.isEmpty()) {
						return prefix;
					}
			        break;
			    }
			}
			return null;
		} else if (permissions != null) {
			if (permissions3) {
				String userPrefix = permissions.getUserPrefix(world, pl);
				if (userPrefix != null && !userPrefix.isEmpty()) {
					return userPrefix;
				}
				String group = permissions.getPrimaryGroup(world, pl);
				if (group == null) return null;
				String groupPrefix = permissions.getGroupRawPrefix(world, group);
				if (groupPrefix != null && !groupPrefix.isEmpty()) {
					return groupPrefix;
				}
			} else {
				String userPrefix = permissions.getUserPermissionString(world, pl, "prefix");
				if (userPrefix != null && !userPrefix.isEmpty()) {
					return userPrefix;
				}
				String group = permissions.getGroup(world, pl);
				if (group == null) return null;
				String groupPrefix = permissions.getGroupPrefix(world, group);
				return groupPrefix;
			}
		}
		return null;
	}
	
	@SuppressWarnings("deprecation")
	public String getSuffix(Player player) {
		String world = player.getWorld().getName();
		String pl = player.getName();
		if (bukkitPermission) {
			for (Entry<String, Object> entry : suffixes.entrySet()) {
			    if (player.hasPermission("mchat.suffix." + entry.getKey())) {
			        suffix = entry.getValue().toString();
					if (suffix != null && !suffix.isEmpty()) {
						return suffix;
					}
			        break;
			    }
			}
			return null;
		} else if (permissions != null) {
			if (permissions3) {
				String userSuffix = permissions.getUserSuffix(world, pl);
				if (userSuffix != null && !userSuffix.isEmpty()) {
					return userSuffix;
				}
				String group = permissions.getPrimaryGroup(world, pl);
				if (group == null) return null;
				String groupSuffix = permissions.getGroupRawSuffix(world, group);
				if (groupSuffix != null && !groupSuffix.isEmpty()) {
					return groupSuffix;
				}
			} else {
				String userSuffix = permissions.getUserPermissionString(world, pl, "suffix");
				if (userSuffix != null && !userSuffix.isEmpty()) {
					return userSuffix;
				}
				String group = permissions.getGroup(world, pl);
				if (group == null) return null;
				String groupSuffix = permissions.getGroupSuffix(world, group);
				return groupSuffix;
			}
		}
		return null;
	}
	
	@SuppressWarnings("deprecation")
	public String getGroup(Player player) {
		String world = player.getWorld().getName();
		String pl = player.getName();
		if (bukkitPermission) {
			for (Entry<String, Object> entry : groupes.entrySet()) {
			    if (player.hasPermission("mchat.group." + entry.getKey())) {
			        group = entry.getValue().toString();
					if (group != null && !group.isEmpty()) {
						return group;
					}
			        break;
			    }
			} 
			return null;
		} else if (permissions != null) {
			if (permissions3) {
				String group = permissions.getPrimaryGroup(world, pl);
				return group;
			} else {
				String group = permissions.getGroup(world, pl);
				return group;
			}
		}
		return null;
	}
	
	/*
	 * End of work initially taken from Drakia's iChat. 
	 * (I have added and removed A BUNCH.)
	 */	
	
	private void setupPermissions() {
		PluginDescriptionFile pdfFile = getDescription();
		Plugin permTest = this.getServer().getPluginManager().getPlugin("Permissions");	
		Plugin bukkitPermTest = this.getServer().getPluginManager().getPlugin("PermissionsBukkit");
		if (bukkitPermTest != null) {
			bukkitPermission = true;
			console.sendMessage("[" + (pdfFile.getName()) + "]" + " PermissionsBukkit " + (bukkitPermTest.getDescription().getVersion()) + " found hooking in.");
		} else if (permissions == null) {
			if (permTest != null) {
				oldPerm = true;
				permissions = ((Permissions)permTest).getHandler();
				permissions3 = permTest.getDescription().getVersion().startsWith("3");
				console.sendMessage("[" + (pdfFile.getName()) + "]" + " Permissions "  + (permTest.getDescription().getVersion()) + " found hooking in.");
			} else {
				bukkitPermission = true;
				console.sendMessage("[" + (pdfFile.getName()) + "]" + " Permissions plugin not found, Defaulting to Bukkit Methods.");
			}
		} else {
			bukkitPermission = true;
			console.sendMessage("[" + (pdfFile.getName()) + "]" + " Permissions plugin not found, Defaulting to Bukkit Methods.");
		}
	}
	
	public void getContrib() {
		PluginDescriptionFile pdfFile = getDescription();
		Plugin contibTest = this.getServer().getPluginManager().getPlugin("BukkitContrib");
		if(contibTest != null) {
			if (contribEnabled) {
				contrib = true;
				console.sendMessage("[" + (pdfFile.getName()) + "]" + " BukkitContrib found now using.");
			} else {
				contrib = false;
				console.sendMessage("[" + (pdfFile.getName()) + "]" + " BukkitContrib features disabled by config.");
			}
		} else {
			contrib = false;
			console.sendMessage("[" + (pdfFile.getName()) + "]" + " BukkitContrib not found not using.");
		}
	}
}
