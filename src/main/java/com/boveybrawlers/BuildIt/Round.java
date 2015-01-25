package com.boveybrawlers.BuildIt;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.boveybrawlers.AbsoluteCraft.AbsoluteCraft;

public class Round {
	public static Integer number = -1;
	
	public static void next() {
		number++;
		if(number == 0) {			
			for(Builder builder: Game.builders) {
				builder.sendMessage(BuildIt.prefix + ChatColor.DARK_PURPLE + "Round 1 / 2");
			}
			
			Turn.next();
		} else if(number == 1) {
			for(Builder builder: Game.builders) {
				builder.sendMessage(BuildIt.prefix + ChatColor.DARK_PURPLE + "Round 2 / 2");
			}
			
			if(Turn.number == Game.builders.size()) {
				Turn.number = -1;
			}
			
			Turn.next();
		} else if(number == 2) {
			Builder winner = null;
			Integer topScore = 0;
			
			for(Builder builder : Game.builders) {
				if(builder.getScore() > topScore) {
					winner = builder;
					topScore = builder.getScore();
				}
			}
			
			for(final Builder builder : Game.builders) {
				builder.sendMessage(BuildIt.prefix + ChatColor.BOLD + "GAME OVER");
				builder.sendMessage(BuildIt.prefix + ChatColor.GREEN + "WINNER: " + ChatColor.BOLD + ChatColor.AQUA + winner.getName() + ChatColor.RESET + " [" + ChatColor.GOLD + winner.getScore() + " points" + ChatColor.WHITE + "]");
				
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(BuildIt.plugin, new Runnable() {
					public void run() {
						builder.teleport(BuildIt.plugin.lobby);
					}
				}, 100); // 5 second timer
				
				AbsoluteCraft.tokens.add("winner", 3);
				
				builder.removeInventory();
				builder.heal();
				builder.teleport(BuildIt.plugin.lobby);
				builder.getPlayer().setTotalExperience(0);
			}
			
			// Clear takenTeams, builders and reset playing
			Game.usedWords.clear();
			Game.buildersGone.clear();
			Game.builders.clear();
			Game.playing = false;
		}
	}
}