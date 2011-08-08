package net.D3GN.MiracleM4n.mChat;

import java.io.File;

//import net.D3GN.MiracleM4n.mInfo.PlayerInfo;

import org.anjocaido.groupmanager.permissions.AnjoPermissionsHandler;
import org.bukkit.craftbukkit.command.ColouredConsoleSender;
import org.bukkit.craftbukkit.CraftServer;
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
	
	MPlayerListener pListener = new MPlayerListener(this);
	MCommandSender cSender = new MCommandSender(this);
	MConfigListener cListener = new MConfigListener(this);
	MIConfigListener mIListener = new MIConfigListener(this);
	public mChatAPI API = new mChatAPI(this);
	
	private PluginManager pm;
	//public static mChat API = null;
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
		
		//mChat.API = this;
		
		getmInfo();
		setupPermissions();
		setupGroupManager();
		
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

	private void getmInfo() {
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
	
	private void setupPermissions() {
		Plugin permTest = this.getServer().getPluginManager().getPlugin("Permissions");
		PluginDescriptionFile pdfFile = getDescription();		
		if (permissions == null) {
			if (permTest != null) {
				permissions = ((Permissions)permTest).getHandler();
				permissionsB = true;
				System.out.println("[" + pdfFile.getName() + "]" + " Permissions " + (permTest.getDescription().getVersion()) + " found hooking in.");
			} else {
				System.out.println("[" + pdfFile.getName() + "]" + " Permissions not found, Checking for GroupManager.");
			}
		}
	}
	
	private void setupGroupManager() {
		Plugin permTest = this.getServer().getPluginManager().getPlugin("GroupManager");
		PluginDescriptionFile pdfFile = getDescription();		
		if (permissions == null) {
			if (permTest != null) {
				gmPermissionsB = true;
				System.out.println("[" + pdfFile.getName() + "]" + " GroupManager " + (permTest.getDescription().getVersion()) + " found hooking in.");
			} else {
				System.out.println("[" + pdfFile.getName() + "]" + " GroupManager not found, using superperms.");
			}
		}
	}
}
