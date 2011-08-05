package net.D3GN.MiracleM4n.mChat;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

//import net.D3GN.MiracleM4n.mInfo.PlayerInfo;
import net.D3GN.MiracleM4n.mInfo.mInfo;

import org.anjocaido.groupmanager.permissions.AnjoPermissionsHandler;
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

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class mChat extends JavaPlugin {
	
	playerListener pListener = new playerListener(this);
	commandSender cSender = new commandSender(this);
	configListener cListener = new configListener(this);
	
	private PluginManager pm;
	public static mChat API = null;
	//public PlayerInfo IService;
	
	public static PermissionHandler permissions;
	Boolean permissionsB = false;
	
	public static AnjoPermissionsHandler gmPermissions;
	Boolean gmPermissionsB = false;
	
	ColouredConsoleSender console = null;
	Configuration config = null;
	
	Boolean mInfoB = false;
	
	String chatFormat = "+hb+p+dn+s&f: +message";
	String nameFormat = "+p+dn+s&e";
	String joinFormat = "+p+dn+s&e";
	String dateFormat = "HH:mm:ss";
	String joinMessage = "has joined the game.";
	String leaveMessage = "has left the game.";
	String kickMessage = "has been kicked from the game.";

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

		pm.registerEvent(Event.Type.PLAYER_KICK, pListener, Priority.High, this);
		pm.registerEvent(Event.Type.PLAYER_CHAT, pListener, Priority.High, this);
		pm.registerEvent(Event.Type.PLAYER_JOIN, pListener, Priority.High, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, pListener, Priority.High, this);
		getCommand("mchat").setExecutor(cSender);
		
		mChat.API = this;
		
		getmInfo();
		setupUselessPermissions();
		setupUselessGroupManager();
		/*
		try {
			IService = getServer().getServicesManager().load(PlayerInfo.class);
		} catch (java.lang.NoClassDefFoundError error) {
			mInfoB = false;
			console.sendMessage("[" + (pdfFile.getName()) + "]" + " mInfo not found shutting down.");
			pm.disablePlugin(this);
		} 
		*/
		if (mInfoB) {
			console.sendMessage("[" + (pdfFile.getName()) + "]" + " version " + 
					pdfFile.getVersion() + " is enabled!");
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
	
	public Boolean checkPermissions(Player player, String node) {
		if (permissionsB) {
			if (mChat.permissions.has(player, node)) {
				return true;
			}
		} else if (gmPermissionsB) {
			if (mChat.gmPermissions.has(player, node)) {
				return true;
			}
		} else {
			if (player.hasPermission(node)) {
				return true;
			}
		}
		return false;
	}
	
	public String parseName(Player player) {
		return parseChat(player, "", this.nameFormat);
	}
	
	public String parseJoin(Player player) {
		return parseChat(player, "", this.joinFormat);
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
	
	private void setupUselessPermissions() {
		Plugin permTest = this.getServer().getPluginManager().getPlugin("Permissions");
		PluginDescriptionFile pdfFile = getDescription();		
		if (permissions == null) {
			if (permTest != null) {
				permissions = ((Permissions)permTest).getHandler();
				permissionsB = true;
				System.out.println("[" + pdfFile.getName() + "]" + " Old-OutDated Permissions " + (permTest.getDescription().getVersion()) + " found hooking in.");
			} else {
				System.out.println("[" + pdfFile.getName() + "]" + " Failmissions not found using the right Permissions.");
			}
		}
	}
	
	private void setupUselessGroupManager() {
		Plugin permTest = this.getServer().getPluginManager().getPlugin("GroupManager");
		PluginDescriptionFile pdfFile = getDescription();		
		if (permissions == null) {
			if (permTest != null) {
				gmPermissionsB = true;
				System.out.println("[" + pdfFile.getName() + "]" + " Old-OutDated GroupManager " + (permTest.getDescription().getVersion()) + " found hooking in.");
			} else {
				System.out.println("[" + pdfFile.getName() + "]" + " FailManager not found, using the right Permissions.");
			}
		}
	}
}
