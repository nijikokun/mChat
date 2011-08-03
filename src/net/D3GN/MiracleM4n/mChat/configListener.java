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
		plugin.joinFormat = config.getString("mchat-playerEvent-format", plugin.joinFormat);
		plugin.dateFormat = config.getString("mchat-date-format", plugin.dateFormat);
		plugin.joinMessage = config.getString("mchat-join-message", plugin.joinMessage);
		plugin.leaveMessage = config.getString("mchat-leave-message", plugin.leaveMessage);
		plugin.kickMessage	= config.getString("mchat-kick-message", plugin.kickMessage);
	}

	public void defaultConfig() {
		Configuration config = new Configuration(new File(plugin.getDataFolder(), "config.yml"));
		config.save();
		config.setHeader(
	            "# mChat configuration file",
	            "# ",
				"#           **IMPORTANT**",
				"#   usage of mchat-message-format is restricted to:",
				"#       +suffix,+s, +prefix,+p, +group,+g, +world,+w, +time,+t, +name,+n, +dname,+dn, +health,+h +healthbar,+hb, +message,+msg,+m",
				"# ",
				"#   usage of mchat-name-format, mchat-playerEvent-format are restricted to:",
				"#       +suffix,+s, +prefix,+p, +group,+g, +world,+w, +time,+t, +name,+n, +dname,+dn, +health,+h +healthbar,+hb",
				"#           **************",
	            "# ",
	            "# Use of mchat: is only if your using PermissionsBukkit (superperms)",
	            "# ignore it if you don't know what that is.",
	            "");
		config.setProperty("mchat-date-format", plugin.dateFormat);
		config.setProperty("mchat-message-format", plugin.chatFormat);
		config.setProperty("mchat-name-format", plugin.nameFormat);
		config.setProperty("mchat-playerEvent-format", plugin.joinFormat);
		config.setProperty("mchat-join-message", plugin.joinMessage);
		config.setProperty("mchat-leave-message", plugin.leaveMessage);
		config.setProperty("mchat-kick-message", plugin.kickMessage);
		config.setProperty("auto-Changed", 1);
		config.save();
	}
	
	public void checkConfig() {
		Configuration config = plugin.config;
		PluginDescriptionFile pdfFile = plugin.getDescription();
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
			
			if (config.getProperty("mchat-playerEvent-format") == null) {
				config.setProperty("mchat-playerEvent-format", plugin.joinFormat);
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
					"#   usage of mchat-name-format, mchat-playerEvent-format are restricted to:",
					"#       +suffix,+s, +prefix,+p, +group,+g, +world,+w, +time,+t, +name,+n, +dname,+dn, +health,+h +healthbar,+hb",
					"#           **************",
		            "# ",
		            "# Use of mchat: is only if your using PermissionsBukkit (superperms)",
		            "# ignore it if you don't know what that is.",
		            "");
			plugin.console.sendMessage("[" + pdfFile.getName() + "]" + " config.yml has been updated.");
			config.save();
		}
	}
}
