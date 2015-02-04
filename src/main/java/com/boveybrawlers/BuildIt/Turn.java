package com.boveybrawlers.BuildIt;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class Turn {
	public static Integer number = -1;
	
	public static GuessHandler guessTask = null;
	public static boolean guessCountdown = false;
	
	public static GuessHandler shortGuessTask = null;
	public static boolean shortGuessCountdown = false;
	
	public static Builder chosen = null;
	public static String word = null;
	public static boolean guessed = false;
	
	public static void next() {
		number++;
		
		if(Game.buildersAtStart > Game.builders.size()) {
			if(number == Game.builders.size() + Game.turnDiff) {
				Round.next();
				return;
			}
		} else if(number == Game.builders.size()) {
			Round.next();
			return;
		}
		
		do {
			int rand = new Random().nextInt(Game.builders.size());
			chosen = Game.builders.get(rand);
		} while(Game.buildersGone.contains(chosen)); // Makes sure a builder without an ID is chosen for the turn
		
		Game.buildersGone.add(chosen);
		
		Turn.chosen.setBuilt(true);
		
		BuildIt.plugin.getLogger().info("Round " + Round.number + " -- Turn " + number + " Builder: " + Turn.chosen.getName());
		
		do {
			word = getRandomWord();
		} while(Game.usedWords.contains(word));
		Game.usedWords.add(word);
		
		chosen.teleport(BuildIt.plugin.builder);
		chosen.setFly(true);
		chosen.setInventory("blocks");
		chosen.sendMessage(BuildIt.prefix + ChatColor.WHITE + "The word to build is " + ChatColor.BOLD + ChatColor.RED + word);
		
		for(Builder builder : Game.builders) {
			builder.setGuessed(false);
			builder.sendMessage(BuildIt.prefix + ChatColor.GREEN + ChatColor.BOLD + chosen.getName() + ChatColor.RESET + " is building this time!");
			builder.sendMessage(BuildIt.prefix + ChatColor.AQUA + "You have" + ChatColor.BOLD +  " 90 seconds" + ChatColor.RESET + ChatColor.AQUA + " to guess the word!");
		}
		
		Game.acceptGuesses = true;
		guessCountdown();
	}
	
	public static void end() {
		Game.acceptGuesses = false;
		
		if(Turn.chosen != null) {
			Turn.chosen.removeInventory();
			Turn.chosen.setFly(false);
			Turn.chosen.teleport(BuildIt.plugin.spawn);
		}
		
		if(word != null) {
			for(Builder builder : Game.builders) {
				builder.sendMessage(BuildIt.prefix + ChatColor.YELLOW + "END OF TURN - " + ChatColor.GREEN + "The word was " + ChatColor.BOLD + word);
			}
		}
		
		word = null;
		guessed = false;
		
		resetBuildArea();
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(BuildIt.plugin, new Runnable() {
			public void run() {
				Turn.next();
			}
		}, 60); // 3 second timer
	}
	
	public static void resetBuildArea() {
		Location min = BuildIt.plugin.minBuildArea;
		Location max = BuildIt.plugin.maxBuildArea;
 
        int topBlockX = (min.getBlockX() < max.getBlockX() ? max.getBlockX() : min.getBlockX());
        int bottomBlockX = (min.getBlockX() > max.getBlockX() ? max.getBlockX() : min.getBlockX());
 
        int topBlockY = (min.getBlockY() < max.getBlockY() ? max.getBlockY() : min.getBlockY());
        int bottomBlockY = (min.getBlockY() > max.getBlockY() ? max.getBlockY() : min.getBlockY());
 
        int topBlockZ = (min.getBlockZ() < max.getBlockZ() ? max.getBlockZ() : min.getBlockZ());
        int bottomBlockZ = (min.getBlockZ() > max.getBlockZ() ? max.getBlockZ() : min.getBlockZ());
 
        for(int x = bottomBlockX; x <= topBlockX; x++) {
            for(int z = bottomBlockZ; z <= topBlockZ; z++) {
                for(int y = bottomBlockY; y <= topBlockY; y++) {
                    Block block = min.getWorld().getBlockAt(x, y, z);
                    block.setType(Material.AIR);
                }
            }
        }
	}
	
	private static String getRandomWord() {
		int index = BuildIt.plugin.words.size();
		Random random = new Random();
		return BuildIt.plugin.words.get(random.nextInt(index));
	}
	
	public static void guessCountdown() {
		guessCountdown = true;
		
		guessTask = new GuessHandler(90);
		guessTask.runTaskTimer(BuildIt.plugin, 0, 20);
	}
	
	public static void shortGuessCountdown() {
		for(Builder builder : Game.builders) {
			builder.sendMessage(BuildIt.prefix + ChatColor.WHITE + "10 seconds left for other guessers!");
		}
		shortGuessCountdown = true;
		
		shortGuessTask = new GuessHandler(10);
		shortGuessTask.runTaskTimer(BuildIt.plugin, 0, 20);
	}
}