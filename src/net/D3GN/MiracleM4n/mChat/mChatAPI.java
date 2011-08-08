package net.D3GN.MiracleM4n.mChat;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.D3GN.MiracleM4n.mInfo.mInfo;

import org.bukkit.entity.Player;

public class mChatAPI{
	mChat plugin;
	
	public mChatAPI(mChat plugin) {
		this.plugin = plugin;
	}
	
	String chatFormat = plugin.chatFormat;
	String nameFormat = plugin.nameFormat;
	String joinFormat = plugin.joinFormat;
	String dateFormat = plugin.dateFormat;
	String joinMessage = plugin.joinMessage;
	String leaveMessage = plugin.leaveMessage;
	String kickMessage = plugin.kickMessage;
	
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
	
	public String addColour(String string) {
		return string.replaceAll("(&([A-Fa-f0-9]))", "\u00A7$2");
	}
	
	public Boolean checkPermissions(Player player, String node) {
		if (plugin.permissionsB) {
			if (mChat.permissions.has(player, node)) {
				return true;
			}
		} else if (plugin.gmPermissionsB) {
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
}
