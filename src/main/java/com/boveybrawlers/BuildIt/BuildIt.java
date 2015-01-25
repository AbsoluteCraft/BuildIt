package com.boveybrawlers.BuildIt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class BuildIt extends JavaPlugin {
	public static BuildIt plugin = null;
	public static String prefix = ChatColor.RED + "" + ChatColor.BOLD + "BuildIt" + ChatColor.RESET + ChatColor.DARK_GRAY + " | " + ChatColor.RESET;
	
	public List<String> words = new ArrayList<String>(Arrays.asList(new String[] {"house", "creeper", "pickaxe", "boat", "dog", "apple", "bow", "bone", "minecart", "zombie", "pig", "chicken", "skeleton", "tree", "cloud", "sun", "moon", "cave", "slime", "flower", "mountain", "volcano", "potato", "mushroom", "sword", "armor", "diamond", "cat", "book", "sheep", "squid", "enderman", "snowman", "bread", "wheat"}));
	
	public World world;
	
	public Location lobby = null;
	public Location spawn = null;
	public Location builder = null;
	
	public ScoreboardManager manager;
	public Scoreboard board;
	public Objective objective;
	
	public Team teamOne;
	public Team teamTwo;
	public Team teamThree;
	public Team teamFour;
	public Team teamFive;
	public Team teamSix;
	public Team teamSeven;
	public Team teamEight;
	public Team teamNine;
	public Team teamTen;
	
	@Override
	public void onEnable() {
		plugin = this;
		
		this.saveDefaultConfig();
		
		manager = Bukkit.getScoreboardManager();
		board = manager.getNewScoreboard();
		
		if(!this.getConfig().getStringList("words").isEmpty()) {
			words = this.getConfig().getStringList("words");
		} else {
			this.getLogger().log(Level.WARNING, "The words list was empty, using default words");
		}
		
		objective = board.registerNewObjective("buildit", "dummy");
		objective.setDisplayName(ChatColor.BOLD + "" + ChatColor.RED + "BuildIt");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);

		teamOne = board.registerNewTeam("One");
		teamTwo = board.registerNewTeam("Two");
		teamThree = board.registerNewTeam("Three");
		teamFour = board.registerNewTeam("Four");
		teamFive = board.registerNewTeam("Five");
		teamSix = board.registerNewTeam("Six");
		teamSeven = board.registerNewTeam("Seven");
		teamEight = board.registerNewTeam("Eight");
		teamNine = board.registerNewTeam("Nine");
		teamTen = board.registerNewTeam("Ten");
		
		this.getCommand("buildit").setExecutor(new Commands());
		getServer().getPluginManager().registerEvents(new Game(), this);
	}
	
	@Override
	public void onDisable() {
		
	}
}
