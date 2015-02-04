package com.boveybrawlers.BuildIt;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public class Round {
	public static Integer number = -1;
	
	@SuppressWarnings("deprecation")
	public static void next() {
		number++;
		if(number == 0) {			
			for(Builder builder: Game.builders) {
				builder.sendMessage(BuildIt.prefix + ChatColor.DARK_PURPLE + "Round 1 / 2");
			}
			
			Turn.next();
		} else if(number == 1) {
			Turn.number = -1;
			Game.buildersGone.clear();
			Game.turnDiff = 0;
			
			for(Builder builder: Game.builders) {
				builder.setBuilt(false);
				builder.sendMessage(BuildIt.prefix + ChatColor.DARK_PURPLE + "Round 2 / 2");
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
				Player player = Game.builders.get(i).getPlayer();
					
				Team dead = player.getScoreboard().getPlayerTeam(player);
				if(dead != null) {
					dead.removePlayer(player);
					dead.setDisplayName("");
					dead.setPrefix("");
				}
				
				for(OfflinePlayer p : BuildIt.plugin.board.getPlayers()) {
					if(p.isOnline()) {
						((Player) p).setScoreboard(BuildIt.plugin.manager.getNewScoreboard());
					}
			        BuildIt.plugin.board.resetScores(p);
				}
				
				Game.builders.get(i).removeInventory();
				Game.builders.get(i).heal();
				Game.builders.get(i).setFly(false);
				Game.builders.get(i).teleport(BuildIt.plugin.lobby);
				Game.builders.get(i).getPlayer().setLevel(0);
			}
			
			Game.builders.clear();
			Game.playing = false;
		}
	}
}