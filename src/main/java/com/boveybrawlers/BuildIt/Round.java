package com.boveybrawlers.BuildIt;

import java.util.ArrayList;

import org.bukkit.ChatColor;

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
			Integer topScore = 0;
			for(Builder builder : Game.builders) {
				if(builder.getScore() > topScore) {
					topScore = builder.getScore();
				}
			}
			
			ArrayList<Builder> winners = new ArrayList<Builder>();
			
			for(Builder builder : Game.builders) {
				if(builder.getScore() == topScore) {
					winners.add(builder);
				}
			}
			
			for(Builder builder : Game.builders) {
				builder.sendMessage(BuildIt.prefix + ChatColor.BOLD + "GAME OVER");
				
				for(Builder winner : winners) {
					builder.sendMessage(BuildIt.prefix + ChatColor.GREEN + "WINNER: " + ChatColor.BOLD + ChatColor.AQUA + winner.getName() + ChatColor.RESET + " [" + ChatColor.GOLD + winner.getScore() + " points" + ChatColor.WHITE + "]");
				}
			}
			
			// Clear takenTeams, builders and reset playing
			Game.usedWords.clear();
			Game.buildersGone.clear();
			
			if(Turn.guessCountdown == true) {
				Turn.guessTask.cancel();
				Turn.guessCountdown = false;
			}
			if(Turn.shortGuessCountdown == true) {
				Turn.shortGuessCountdown();
				Turn.shortGuessCountdown = false;
			}
			
			for(int i = 0; i < Game.builders.size(); i++) {
				if(Game.builders.get(i) != null) {
					Game.removePlayer(i, true);
				}
			}
			Game.playing = false;
		}
	}
}