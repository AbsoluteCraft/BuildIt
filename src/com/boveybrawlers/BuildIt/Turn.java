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
		
		if(number == Game.builders.size()) {
			Round.next();
			return;
		}
		
		if(Round.number == 0) {
			do {
				int index = new Random().nextInt(Game.builders.size());
				chosen = Game.builders.get(index);
				chosen.setId(index);
			} while(Game.buildersGone.contains(chosen)); // Makes sure a builder without an ID is chosen for the turn
			
			Game.buildersGone.add(chosen);
		} else {
			BuildIt.plugin.getLogger().info("Round 2 - Selecting Player" + number);
			chosen = Game.builders.get(Game.getBuilderById(number));
		}
		
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
		
		Turn.chosen.removeInventory();
		Turn.chosen.setFly(false);
		Turn.chosen.teleport(BuildIt.plugin.spawn);
		
		for(Builder builder : Game.builders) {
			builder.sendMessage(BuildIt.prefix + ChatColor.GREEN + "The word was " + ChatColor.BOLD + word);
			builder.sendMessage(BuildIt.prefix + ChatColor.YELLOW + "Next round starting in 5 seconds");
		}
		
		word = null;
		guessed = false;
		
		resetBuildArea();
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(BuildIt.plugin, new Runnable() {
			public void run() {
				Turn.next();
			}
		}, 100); // 5 second timer
	}
	
	public static void resetBuildArea() {
		Location min = BuildIt.plugin.minBuildArea;
		Location max = BuildIt.plugin.maxBuildArea;
	    for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
	        for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
	            for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
	                Block block = min.getWorld().getBlockAt(new Location(min.getWorld(), x, y, z));
	                BuildIt.plugin.getLogger().info(block.getX() + " " + block.getY() + " " + block.getZ());
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