package net.D3GN.MiracleM4n.mChat;

import java.io.File;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.util.config.Configuration;

public class configListener {
	mChat plugin;
	
	public configListener(mChat plugin) {
		this.plugin = plugin;
	}
	
  	Boolean hasChanged = false;
	
	public void loadConfig() {
		Configuration config = plugin.config;
		config.load();
		plugin.chatFormat = config.getString("mchat-message-format", plugin.chatFormat);
		plugin.nameFormat = config.getString("mchat-name-format", plugin.nameFormat);
		plugin.dateFormat = config.getString("mchat-date-format", plugin.dateFormat);
		plugin.joinMessage = config.getString("mchat-join-message", plugin.joinMessage);
		plugin.leaveMessage = config.getString("mchat-leave-message", plugin.leaveMessage);
		plugin.kickMessage	= config.getString("mchat-kick-message", plugin.kickMessage);
		plugin.contribChatColour = config.getString("mchat-colouring", plugin.contribChatColour);
		plugin.contribEnabled = config.getBoolean("mchat-contrib-enabled", plugin.contribEnabled);
		plugin.healthNotify = config.getBoolean("mchat-notifyHealth-enabled", plugin.healthNotify);

		if (config.getNode("mchat.prefix") != null) {
			plugin.prefixes.putAll(config.getNode("mchat.prefix").getAll());
		}
		
		if (config.getNode("mchat.suffix") != null) {
			plugin.suffixes.putAll(config.getNode("mchat.suffix").getAll());
		}
		
		if (config.getNode("mchat.group") != null) {
			plugin.groupes.putAll(config.getNode("mchat.group").getAll());
		}
	}

	public void defaultConfig() {
		Configuration config = plugin.config;
		config.setHeader(
	            "# mChat configuration file",
	            "# ",
				"#           **IMPORTANT**",
				"#   usage of mchat-message-format is restricted to:",
				"#       +suffix,+s, +prefix,+p, +group,+g, +world,+w, +time,+t, +name,+n, +dname,+dn, +health,+h +healthbar,+hb, +message,+msg,+m",
				"# ",
				"#   usage of mchat-name-format is restricted to:",
				"#       +suffix,+s, +prefix,+p, +group,+g, +world,+w, +time,+t, +name,+n, +dname,+dn, +health,+h +healthbar,+hb",
				"#           **************",
	            "# ",
	            "# Use of mchat: is only if your using PermissionsBukkit (superperms)",
	            "# ignore it if you don't know what that is.",
	            "");
		plugin.groupes.put("admin", "");
		plugin.groupes.put("sadmin", "");
		plugin.groupes.put("jadmin", "");
		plugin.groupes.put("member", "");
		plugin.prefixes.put("admin", "&4DtK [SO] &7");
		plugin.prefixes.put("sadmin", "&9DtK [SA] &7");
		plugin.prefixes.put("jadmin", "&aDtK [JA] &7");
		plugin.prefixes.put("member", "&cDtK [M] &7");
		plugin.suffixes.put("admin", "");
		plugin.suffixes.put("sadmin", "");
		plugin.suffixes.put("jadmin", "");
		plugin.suffixes.put("member", "");
		plugin.mchat.put("group", plugin.groupes);
		plugin.mchat.put("prefix", plugin.prefixes);
		plugin.mchat.put("suffix", plugin.suffixes);
		config.setProperty("mchat-date-format", plugin.dateFormat);
		config.setProperty("mchat-message-format", plugin.chatFormat);
		config.setProperty("mchat-name-format", plugin.nameFormat);
		config.setProperty("mchat-join-message", plugin.joinMessage);
		config.setProperty("mchat-leave-message", plugin.leaveMessage);
		config.setProperty("mchat-kick-message", plugin.kickMessage);
		config.setProperty("mchat", plugin.mchat);
		config.setProperty("mchat-colouring", plugin.contribChatColour);
		config.setProperty("mchat-contrib-enabled", plugin.contribEnabled);
		config.setProperty("mchat-notifyHealth-enabled", plugin.healthNotify);
		config.setProperty("auto-Changed", 1);
		config.save();
	}
	
	public void checkConfig() {
		PluginDescriptionFile pdfFile = plugin.getDescription();
		Configuration config = new Configuration(new File(plugin.getDataFolder(), "config.yml"));
		config.load();
		if (config.getProperty("auto-Changed") == null) {
			config.setProperty("auto-Changed", 1);
		}
		
		if (config.getInt("auto-Changed", 1) == 1) {
			if (config.getProperty("mchat-date-format") == null) {
				config.setProperty("mchat-date-format", plugin.dateFormat);
				hasChanged = true;
			}
			
			if (config.getProperty("mchat-message-format") == null) {
				config.setProperty("mchat-message-format", plugin.chatFormat);
				hasChanged = true;
			}
			
			if (config.getProperty("mchat-name-format") == null) {
				config.setProperty("mchat-name-format", plugin.nameFormat);
				hasChanged = true;
			}
			
			if (config.getProperty("mchat-join-message") == null) {
				config.setProperty("mchat-join-message", plugin.joinMessage);
				hasChanged = true;
			}
			
			if (config.getProperty("mchat-leave-message") == null) {
				config.setProperty("mchat-leave-message", plugin.leaveMessage);
				hasChanged = true;
			}
			
			if (config.getProperty("mchat-kick-message") == null) {
				config.setProperty("mchat-kick-message", plugin.kickMessage);
				hasChanged = true;
			}
			
			if (config.getProperty("mchat-colouring") == null) {
				config.setProperty("mchat-colouring", plugin.contribChatColour);
				hasChanged = true;
			}
			
			if (config.getProperty("mchat-contrib-enabled") == null) {
				config.setProperty("mchat-contrib-enabled", plugin.contribEnabled);
				hasChanged = true;
			}
			
			if (config.getProperty("mchat-notifyHealth-enabled") == null) {
				config.setProperty("mchat-notifyHealth-enabled", plugin.healthNotify);
				hasChanged = true;
			}

			if (config.getProperty("mchat") == null) {
				plugin.groupes.put("admin", "");
				plugin.groupes.put("sadmin", "");
				plugin.groupes.put("jadmin", "");
				plugin.groupes.put("member", "");
				plugin.prefixes.put("admin", "&4DtK [SO] &7");
				plugin.prefixes.put("sadmin", "&9DtK [SA] &7");
				plugin.prefixes.put("jadmin", "&aDtK [JA] &7");
				plugin.prefixes.put("member", "&cDtK [M] &7");
				plugin.suffixes.put("admin", "");
				plugin.suffixes.put("sadmin", "");
				plugin.suffixes.put("jadmin", "");
				plugin.suffixes.put("member", "");
				plugin.mchat.put("group", plugin.groupes);
				plugin.mchat.put("prefix", plugin.prefixes);
				plugin.mchat.put("suffix", plugin.suffixes);
				config.setProperty("mchat", plugin.mchat);
				hasChanged = true;
			}
		}
		if (!(config.getInt("auto-Changed", 1) == 1)) {
			hasChanged = true;
			config.setProperty("auto-Changed", 1);
		}
		
		if (hasChanged) {
			config.setHeader(
		            "# mChat configuration file",
		            "# ",
					"#           **IMPORTANT**",
					"#   usage of mchat-message-format is restricted to:",
					"#       +suffix,+s, +prefix,+p, +group,+g, +world,+w, +time,+t, +name,+n, +dname,+dn, +health,+h +healthbar,+hb, +message,+msg,+m",
					"# ",
					"#   usage of mchat-name-format is restricted to:",
					"#       +suffix,+s, +prefix,+p, +group,+g, +world,+w, +time,+t, +name,+n, +dname,+dn, +health,+h +healthbar,+hb",
					"#           **************",
		            "# ",
		            "# Use of mchat: is only if your using PermissionsBukkit (superperms)",
		            "# ignore it if you don't know what that is.",
		            "");
			System.out.println("[" + pdfFile.getName() + "]" + " config.yml has been updated.");
			config.save();
		}
	}
	
}
