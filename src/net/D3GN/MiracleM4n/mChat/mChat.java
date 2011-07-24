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

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class mChat extends JavaPlugin {
	
	private playerListener pListener = new playerListener(this);
	private commandSender cSender = new commandSender(this);
	
	private PluginManager pm;
	public static PermissionHandler permissions;
	ColouredConsoleSender console = null;
  	Boolean contrib = false;
  	Boolean permissions3 = false;
  	Boolean bukkitPermission = false;
  	Boolean hasChanged = false;
  	Boolean oldPerm = false;
	Configuration config;
	
	String chatFormat = "+p+dn+s&f: +message";
	String nameFormat = "+p+dn+s&e";
	String dateFormat = "HH:mm:ss";
	String prefix = "";
	String suffix = "";
	String groups = "";
	
	HashMap<Player, String> channelInfo = new HashMap<Player, String>();
	HashMap<String, Object> prefixes = new HashMap<String, Object>();
	HashMap<String, Object> suffixes = new HashMap<String, Object>();
	HashMap<String, Object> groupes = new HashMap<String, Object>();
	HashMap<String, Object> mchat = new HashMap<String, Object>();
	HashMap<Player, Boolean> chatt = new HashMap<Player, Boolean>();
	
	public void onEnable() {
		pm = getServer().getPluginManager();
		config = getConfiguration();
		console = new ColouredConsoleSender((CraftServer)getServer());
		PluginDescriptionFile pdfFile = getDescription();
		
		getContrib();
		
		if (!(new File(getDataFolder(), "config.yml")).exists()) {
			defaultConfig();
			checkConfig();
			loadConfig();
		} else {
			checkConfig();
			loadConfig();
		}
		
		pm.registerEvent(Event.Type.PLAYER_KICK, pListener, Priority.High, this);
		pm.registerEvent(Event.Type.PLAYER_CHAT, pListener, Priority.High, this);
		pm.registerEvent(Event.Type.PLAYER_JOIN, pListener, Priority.High, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, pListener, Priority.High, this);
		getCommand("mchannel").setExecutor(cSender);
		getCommand("mchat").setExecutor(cSender);
		
		if (contrib) {
			customListener cListener = new customListener(this);
			pm.registerEvent(Event.Type.CUSTOM_EVENT, cListener, Event.Priority.High, this);
		}
		
		setupPermissions();
		
		console.sendMessage("[" + (pdfFile.getName()) + "]" + " version " + 
				pdfFile.getVersion() + " is enabled!");
		
		for (Player players : getServer().getOnlinePlayers()) {
			channelInfo.put(players, "public");
			if (contrib) {
				BukkitContrib.getAppearanceManager().setGlobalTitle(players, parseNameFormat(players));
				chatt.put(players, false);
			}
		}
	}
	
	public void onDisable() {
		PluginDescriptionFile pdfFile = getDescription();
		
		console.sendMessage("[" + (pdfFile.getName()) + "]" + " version " + 
				pdfFile.getVersion() + " is disabled!");
	}
	
	public void loadConfig() {
		config.load();
		chatFormat = config.getString("mchat-message-format", chatFormat);
		nameFormat = config.getString("mchat-name-format", nameFormat);
		dateFormat = config.getString("mchat-date-format", dateFormat);
		if (config.getNode("mchat.prefix") != null) {
			prefixes.putAll(config.getNode("mchat.prefix").getAll());
		}
		
		if (config.getNode("mchat.suffix") != null) {
			suffixes.putAll(config.getNode("mchat.suffix").getAll());
		}
		
		if (config.getNode("mchat.group") != null) {
			groupes.putAll(config.getNode("mchat.group").getAll());
		}
	}

	public void defaultConfig() {
		config.setHeader(
	            "# mChat configuration file",
	            "# ",
	            "# Use of mchat: is only if your using PermissionsBukkit (superperms)",
	            "# ignore it if you don't know what that is.",
	            "# usage of mchat-message-format is restricted to:",
	            "# +suffix,+s, +prefix,+p, +group,+g, +world,+w, +time,+t, +name,+n, +dname,+dn, +message,+m",
	            "");
		groupes.put("admin", "");
		groupes.put("sadmin", "");
		groupes.put("jadmin", "");
		groupes.put("member", "");
		prefixes.put("admin", "&4DtK [SO] &7");
		prefixes.put("sadmin", "&9DtK [SA] &7");
		prefixes.put("jadmin", "&aDtK [JA] &7");
		prefixes.put("member", "&cDtK [M] &7");
		suffixes.put("admin", "");
		suffixes.put("sadmin", "");
		suffixes.put("jadmin", "");
		suffixes.put("member", "");
		mchat.put("group", groupes);
		mchat.put("prefix", prefixes);
		mchat.put("suffix", suffixes);
		config.setProperty("mchat-date-format", dateFormat);
		config.setProperty("mchat-message-format", chatFormat);
		config.setProperty("mchat-name-format", nameFormat);
		config.setProperty("mchat", mchat);
		config.save();
	}
	
	public void checkConfig() {
		PluginDescriptionFile pdfFile = getDescription();
		Configuration config = new Configuration(new File(getDataFolder(), "config.yml"));
		config.load();
		if (config.getProperty("auto-Changed") == null) {
			config.setProperty("auto-Changed", "1");
		}
		
		if (config.getProperty("auto-Changed") == "1") {
			if (config.getProperty("mchat-date-format") == null) {
				config.setProperty("mchat-date-format", dateFormat);
				hasChanged = true;
			}
			
			if (config.getProperty("mchat-message-format") == null) {
				config.setProperty("mchat-message-format", chatFormat);
				hasChanged = true;
			}
			
			if (config.getProperty("mchat-name-format") == null) {
				config.setProperty("mchat-name-format", nameFormat);
				hasChanged = true;
			}
			
			if (config.getProperty("mchat") == null) {
				groupes.put("admin", "");
				groupes.put("sadmin", "");
				groupes.put("jadmin", "");
				groupes.put("member", "");
				prefixes.put("admin", "&4DtK [SO] &7");
				prefixes.put("sadmin", "&9DtK [SA] &7");
				prefixes.put("jadmin", "&aDtK [JA] &7");
				prefixes.put("member", "&cDtK [M] &7");
				suffixes.put("admin", "");
				suffixes.put("sadmin", "");
				suffixes.put("jadmin", "");
				suffixes.put("member", "");
				mchat.put("group", groupes);
				mchat.put("prefix", prefixes);
				mchat.put("suffix", suffixes);
				config.setProperty("mchat", mchat);
				hasChanged = true;
			}
		}
		if (config.getProperty("auto-Changed") == "2") {
			hasChanged = true;
			config.setProperty("auto-Changed", "1");
		}
		
		if (hasChanged) {
			config.setProperty("mchat", mchat);
			config.setHeader(
		            "# mChat configuration file",
		            "# ",
		            "# Use of mchat: is only if your using PermissionsBukkit (superperms)",
		            "# ignore it if you don't know what that is.",
		            "# usage of mchat-message-format is restricted to:",
		            "# +suffix,+s, +prefix,+p, +group,+g, +world,+w, +time,+t, +name,+n, +dname,+dn, +message,+m",
		            "");
			System.out.println("[" + pdfFile.getName() + "]" + " config.yml has been updated.");
			config.save();
		}
	}
	
	/*
	 * Beginning of work initially taken from Drakia's iChat.
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
	
	public String parseChat(Player player, String msg, String chatFormat) {
		String prefix = getPrefix(player);
		String suffix = getSuffix(player);
		String group = getGroup(player);
		if (prefix == null) prefix = "";
		if (suffix == null) suffix = "";
		if (group == null) group = "";
		String world = player.getWorld().getName();
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(this.dateFormat);
		String time = dateFormat.format(now);
		msg = msg.replaceAll("%", "%%");
		String format = (chatFormat);
		if (format == null) return msg;
		String[] search = new String[] {"+suffix,+s", "+prefix,+p", "+group,+g", "+world,+w", "+time,+t", "+name,+n", "+dname,+dn", "+message,+m"};
		String[] replace = new String[] { suffix, prefix, group, world, time, player.getName(), player.getDisplayName(), msg };
		return replaceVars(format, search, replace);
	}
	
	public String parseChat(Player player, String msg) {
		return parseChat(player, msg, this.chatFormat);
	}
	
	public String parseNameFormat(Player player, String nameFormat) {
		String prefix = getPrefix(player);
		String suffix = getSuffix(player);
		String group = getGroup(player);
		if (prefix == null) prefix = "";
		if (suffix == null) suffix = "";
		if (group == null) group = "";
		String world = player.getWorld().getName();
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(this.dateFormat);
		String time = dateFormat.format(now);
		String format = (nameFormat);
		String[] search = new String[] {"+suffix,+s", "+prefix,+p", "+group,+g", "+world,+w", "+time,+t", "+name,+n", "+dname,+dn"};
		String[] replace = new String[] { suffix, prefix, group, world, time, player.getName(), player.getDisplayName() };
		return replaceVars(format, search, replace);
	}

	public String parseNameFormat(Player player) {
		return parseNameFormat(player, nameFormat);
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
			        groups = entry.getValue().toString();
					if (groups != null && !groups.isEmpty()) {
						return groups;
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
			} 
		} else {
			bukkitPermission = true;
			console.sendMessage("[" + (pdfFile.getName()) + "]" + " Permissions plugin not found, Defaulting to Bukkit Methods.");
		}
	}
	
	public void getContrib(){
		PluginDescriptionFile pdfFile = getDescription();
		Plugin contibTest = this.getServer().getPluginManager().getPlugin("BukkitContrib");
		if(contibTest != null){
			this.contrib = true;
			console.sendMessage("[" + (pdfFile.getName()) + "]" + " BukkitContrib found now using.");
		}
		else {
			this.contrib = false;
			console.sendMessage("[" + (pdfFile.getName()) + "]" + " BukkitContrib not found not using.");
		}
	}
}
