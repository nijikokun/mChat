package net.D3GN.MiracleM4n.mChat;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import net.D3GN.MiracleM4n.mInfo.mInfo;

import org.bukkit.Location;
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
import org.getspout.spoutapi.SpoutManager;

public class mChat extends JavaPlugin {
	
	playerListener pListener = new playerListener(this);
	commandSender cSender = new commandSender(this);
	configListener cListener = new configListener(this);
	entityListener eListener = new entityListener(this);
	
	private PluginManager pm;
	
	public static mChat API = null;
	ColouredConsoleSender console = null;
	Configuration config = null;
	
  	Boolean spoutEnabled = true;
  	Boolean healthNotify = false;
	Boolean spout = false;
	Boolean mInfoB = false;
	
  	String typingMessage = "*Typing*";
	String chatFormat = "+hb+p+dn+s&f: +message";
	String nameFormat = "+p+dn+s&e";
	String joinFormat = "+p+dn+s&e";
	String dateFormat = "HH:mm:ss";
	String joinMessage = "has joined the game.";
	String leaveMessage = "has left the game.";
	String kickMessage = "has been kicked from the game.";
	String spoutChatColour = "dark_red";

	HashMap<Player, Boolean> chatt = new HashMap<Player, Boolean>();
	HashMap<Player, Boolean> isAFK = new HashMap<Player, Boolean>();
	HashMap<Player, Location> AFKLoc = new HashMap<Player, Location>();

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
		
		getSpout();
		getmInfo();
		setupPermissions();
		
		pm.registerEvent(Event.Type.ENTITY_DAMAGE, eListener, Priority.High, this);
		pm.registerEvent(Event.Type.PLAYER_KICK, pListener, Priority.High, this);
		pm.registerEvent(Event.Type.PLAYER_CHAT, pListener, Priority.High, this);
		pm.registerEvent(Event.Type.PLAYER_JOIN, pListener, Priority.High, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, pListener, Priority.High, this);
		pm.registerEvent(Event.Type.PLAYER_MOVE, pListener, Priority.High, this);
		getCommand("mchat").setExecutor(cSender);
		getCommand("mchatme").setExecutor(cSender);
		getCommand("mchatwho").setExecutor(cSender);
		getCommand("mchatafk").setExecutor(cSender);
		
		if (spout) {
			customListener cusListener = new customListener(this);
			pm.registerEvent(Event.Type.CUSTOM_EVENT, cusListener, Event.Priority.High, this);
		}
		
		mChat.API = this;
		
		console.sendMessage("[" + (pdfFile.getName()) + "]" + " version " + 
				pdfFile.getVersion() + " is enabled!");
		
		for (Player players : getServer().getOnlinePlayers()) {
			isAFK.put(players, false);
			chatt.put(players, false);
			if (spout) {	
				SpoutManager.getAppearanceManager().setGlobalTitle(players, parseName(players));
			}
		}
	}
	
	public void onDisable() {
		PluginDescriptionFile pdfFile = getDescription();
		
		console.sendMessage("[" + (pdfFile.getName()) + "]" + " version " + 
				pdfFile.getVersion() + " is disabled!");
	}
	
	public String addColour(String string) {
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
		return addColour(format);
	}
	
	public String parseChat(Player player, String msg, String formatAll) {
		String prefix = mInfo.API.getRawPrefix(player);
		String suffix = mInfo.API.getRawSuffix(player);
		String group = mInfo.API.getRawGroup(player);
		if (prefix == null) prefix = "";
		if (suffix == null) suffix = "";
		if (group == null) group = "";
		Integer locX = (int) player.getLocation().getX();
		Integer locY = (int) player.getLocation().getY();
		Integer locZ = (int) player.getLocation().getZ();
		String loc = ("X: " + locX + ", " + "Y: " + locY + ", " +"Z: " + locZ);
		String healthbar = healthBar(player);
		String health = String.valueOf(player.getHealth());
		String world = player.getWorld().getName();
		if(world.contains("_nether")) {
			world.replace("_nether", " Nether");
		}
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(this.dateFormat);
		String time = dateFormat.format(now);
		String format = formatAll;
		String[] search;
		String[] replace;
		if (msg == "") {
			search = new String[] {"+suffix,+s", "+prefix,+p", "+group,+g", "+world,+w", "+time,+t", "+name,+n", "+displayname,+dname,+dn", "+healthbar,+hb", "+health,+h", "+location,+loc"};
			replace = new String[] { suffix, prefix, group, world, time, player.getName(), player.getDisplayName(), healthbar, health, loc };
		} else {
			msg = msg.replaceAll("%", "%%");
			if (format == null) return msg;
			search = new String[] {"+suffix,+s", "+prefix,+p", "+group,+g", "+world,+w", "+time,+t", "+name,+n", "+displayname,+dname,+dn", "+healthbar,+hb", "+health,+h", "+location,+loc", "+message,+msg,+m"};
			replace = new String[] { suffix, prefix, group, world, time, player.getName(), player.getDisplayName(), healthbar, health, loc, msg };
		}
		return replaceVars(format, search, replace);
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
	
	public String parseChat(Player player, String msg) {
		return parseChat(player, msg, this.chatFormat);
	}
	
	/*
	 * End of work initially taken from Drakia's iChat. 
	 * (I have added and removed A BUNCH.)
	 */	
	
	public String parseName(Player player) {
		return parseChat(player, "", this.nameFormat);
	}
	
	public String parseJoin(Player player) {
		return parseChat(player, "", this.joinFormat);
	}
	
	private void setupPermissions() {
		PluginDescriptionFile pdfFile = getDescription();
		Plugin bukkitPermTest = this.getServer().getPluginManager().getPlugin("PermissionsBukkit");
		if (bukkitPermTest != null) {
			console.sendMessage("[" + (pdfFile.getName()) + "]" + " PermissionsBukkit " + (bukkitPermTest.getDescription().getVersion()) + " found hooking in.");
		} else {
			console.sendMessage("[" + (pdfFile.getName()) + "]" + " Permissions plugin not found, Defaulting to Bukkit Methods.");
		}
	}
	
	public void getSpout() {
		PluginDescriptionFile pdfFile = getDescription();
		Plugin spoutTest = this.getServer().getPluginManager().getPlugin("Spout");
		if(spoutTest != null) {
			if (spoutEnabled) {
				spout = true;
				console.sendMessage("[" + (pdfFile.getName()) + "]" + " Spout " + (spoutTest.getDescription().getVersion()) + " found now using.");
			} else {
				spout = false;
				console.sendMessage("[" + (pdfFile.getName()) + "]" + " Spout features disabled by config.");
			}
		} else {
			spout = false;
			console.sendMessage("[" + (pdfFile.getName()) + "]" + " Spout not found not using.");
		}
	}
	
	public void getmInfo() {
		PluginDescriptionFile pdfFile = getDescription();
		Plugin mInfoTest = this.getServer().getPluginManager().getPlugin("mInfo");
		if(mInfoTest != null) {
			mInfoB = true;
			console.sendMessage("[" + (pdfFile.getName()) + "]" + " mInfo " + (mInfoTest.getDescription().getVersion()) + " found now using.");
		} else {
			mInfoB = false;
			console.sendMessage("[" + (pdfFile.getName()) + "]" + " mInfo not found shutting down.");
			pm.disablePlugin(this);
		}
	}
}
