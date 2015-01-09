package com.boveybrawlers.BuildIt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class BuildIt extends JavaPlugin {
	public static BuildIt plugin = null;
	public static String prefix = ChatColor.WHITE + "[" + ChatColor.DARK_RED + "BuildIt!" + ChatColor.WHITE + "] " + ChatColor.RESET;
	
	public static List<String> words = new ArrayList<String>(Arrays.asList(new String[] {"house", "creeper", "pickaxe", "boat", "dog", "apple", "bow", "bone", "minecart", "zombie", "pig", "chicken", "skeleton", "tree", "cloud", "sun", "moon", "cave", "slime", "flower", "mountain", "volcano", "potato", "mushroom", "sword", "armor", "diamond", "cat", "book", "sheep", "squid", "enderman", "snowman", "bread", "wheat"}));
	
	@Override
	public void onEnable() {
		plugin = this;
		
		this.saveDefaultConfig();
		
		if(!this.getConfig().getStringList("words").isEmpty()) {
			words = this.getConfig().getStringList("words");
		} else {
			this.getLogger().log(Level.WARNING, "The words list was empty, using default words");
		}
		
		this.getCommand("buildit").setExecutor(new Commands());
	}
	
	@Override
	public void onDisable() {
		
	}
}
