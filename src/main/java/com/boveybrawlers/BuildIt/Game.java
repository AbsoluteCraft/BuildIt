package com.boveybrawlers.BuildIt;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.boveybrawlers.AbsoluteCraft.AbsoluteCraft;

public class Game implements Listener {
	public static boolean playing = false;
	public static ArrayList<Builder> builders = new ArrayList<Builder>();
	
	public static Location arenaSpawn = new Location(Bukkit.getWorld("world"), 0, 64, 0);
	public static Location arenaBuilder = new Location(Bukkit.getWorld("world"), 0, 100, 0);
	public static Location outsideArena = new Location(Bukkit.getWorld("world"), 50, 64, 0);
	
	static List<String> usedWords = new ArrayList<String>();
	
	static Integer round = 0;
	static Integer builderId = 0;
	static Builder chosen = null;
	static String word = null;
	static boolean acceptGuesses = false;
	static boolean guessed = false;
	
	static boolean queueTimer = false;
	static boolean turnTimer = false;
	static boolean guessTimer = false;
	
	static int turnTask;
	static int sixtyTurnTask;
	static int thirtyTurnTask;
	static int tenTurnTask;
	
	public static int getBuilderByName(String username) {
        for(Builder builder : builders) {
            if(builder.getName().equals(username)) {
                return builders.indexOf(builder);
            }
        }
        return -1;
    }
	
	public static int getBuilderById(Integer id) {
		for(Builder builder : builders) {
			if(builder.getId().equals(id)) {
				return builders.indexOf(builder);
			}
		}
		return -1;
	}
	
	public static void addPlayer(String username) {
		builders.add(new Builder(username));
		
		Builder builder = builders.get(getBuilderByName(username));
		
		builder.teleport(arenaSpawn);
		
		if(builders.size() == 1 && queueTimer == false) {
			queueTimer();
		} else if(builders.size() == 10) {
			round();
		}
	}
	
	public static void removePlayer(int index, boolean connect) {
		String username = builders.get(index).getName();
		
		for(Builder builder: builders) {
			builder.sendMessage(ChatColor.RED + username + "has left BuildIt!");
		}
		
		if(connect == false && playing == true) {
			builders.get(index).teleport(outsideArena);
		}
		
		if(playing == true && builders.size() == 1) {
			for(Builder builder : builders) {
				builder.sendMessage(BuildIt.prefix + "There are not enough players to continue the game :(");
				builder.teleport(outsideArena);
			}
			builders.clear();
			playing = false;
		}
		
		builders.remove(index);
	}
	
	public static void round() {
		round++;
		if(round == 1) {
			for(Builder builder : builders) {
				builder.sendMessage("Round " + round);
			}
			build();
		} else if(round == 2) {
			if(builderId == builders.size()) {
				builderId = 0;
			}
			build();
		} else if(round == 3) {
			for(final Builder builder : builders) {
				builder.sendMessage(ChatColor.BOLD + "GAME OVER");
				builder.sendMessage(ChatColor.GREEN + "WINNER: " + ChatColor.BOLD + "winner" + ChatColor.RESET + "[score]");
				
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(BuildIt.plugin, new Runnable() {
					public void run() {
						builder.teleport(outsideArena);
					}
				}, 100); // 5 second timer
				
				AbsoluteCraft.tokens.add(builder.getName(), 3);
			}
			builders.clear();
			playing = false;
		}
	}
	
	public static void build() {
		if(word != null) {
			boolean used = false;
			do {
				word = getRandomWord();
				for(String w : usedWords) {
					if(word == w) {
						used = true;
						break;
					}
				}
			} while(used == true);
		}
		
		if(chosen != null) {
			chosen.teleport(arenaSpawn);
			chosen.setFly(false);
			chosen.setInventory("clear");
		}
		
		builderId++;
		if(builderId > builders.size()) { // Finished all turns for this round
			round();
			return;
		}
		if(round == 1) {
			do {
				int index = new Random().nextInt(builders.size());
				chosen = builders.get(index);
				chosen.setId(builderId);
			} while(chosen.hasId()); // Makes sure a builder without an ID is chosen for the turn
			
			chosen.teleport(arenaBuilder);
			chosen.setFly(true);
			chosen.setInventory("blocks");
			
			for(Builder builder : builders) {
				builder.setGuessed(false);
				builder.sendMessage(ChatColor.BOLD + chosen.getName() + ChatColor.RESET + " is building this time!");
				builder.sendMessage("You have" + ChatColor.BOLD +  " 90 seconds" + ChatColor.RESET + " to guess the word!");
			}
			
			acceptGuesses = true;
			chosen.sendMessage(ChatColor.RED + "The word to guess is: " + ChatColor.BOLD + word);
		} else if(round == 2) {
			int index = getBuilderById(builderId);
			chosen = builders.get(index);
			
			chosen.teleport(arenaBuilder);
			chosen.setFly(true);
			chosen.setInventory("blocks");
			
			for(Builder builder : builders) {
				builder.sendMessage(ChatColor.BOLD + chosen.getName() + ChatColor.RESET + " is building this time!");
			}
			
			acceptGuesses = true;
			chosen.sendMessage(ChatColor.RED + "The word to guess is: " + ChatColor.BOLD + "$word");
		}
	}
	
	public static String getRandomWord() {
		int index = BuildIt.words.size();
		Random random = new Random();
		return BuildIt.words.get(random.nextInt(index));
	}
	
	public static void queueTimer() {
		queueTimer = true;
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(BuildIt.plugin, new Runnable() {
			public void run() {
				queueTimer = false;
				round();
			}
		}, 3600); // 3 minute timer
	}
	
	public static void turnTimer() {
		turnTimer = true;
		turnTask = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(BuildIt.plugin, new Runnable() {
			public void run() {
				turnTimer = false;
				nextTurn();
			}
		}, 1800); // 90 second timer
		
		tenTurnTask = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(BuildIt.plugin, new Runnable() {
			public void run() {
				for(Builder builder : builders) {
					builder.sendMessage("10 seconds left!");
				}
			}
		}, 1600); // 80 second timer
		
		thirtyTurnTask = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(BuildIt.plugin, new Runnable() {
			public void run() {
				for(Builder builder : builders) {
					builder.sendMessage("30 seconds left!");
				}
			}
		}, 1200); // 60 second timer
		
		sixtyTurnTask = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(BuildIt.plugin, new Runnable() {
			public void run() {
				for(Builder builder : builders) {
					builder.sendMessage("60 seconds left!");
				}
			}
		}, 600); // 30 second timer
	}
	
	public static void guessTimer() {
		guessTimer = true;
		for(Builder builder : builders) {
			builder.sendMessage("10 seconds left for other guessers!");
		}
		turnTask = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(BuildIt.plugin, new Runnable() {
			public void run() {
				guessTimer = false;
				nextTurn();
			}
		}, 200); // 10 second timer
	}
	
	public static void nextTurn() {
		acceptGuesses = false;
		
		for(Builder builder: builders) {
			builder.sendMessage(ChatColor.GREEN + "The word was: " + ChatColor.BOLD + word);
			builder.sendMessage("Next round starting in 5 seconds");
		}
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(BuildIt.plugin, new Runnable() {
			public void run() {
				build();
			}
		}, 100); // 5 second timer
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		if(playing == true && acceptGuesses == true) {
			Player player = event.getPlayer();
			
			int index = getBuilderByName(player.getName());
			if(index != -1) {
				Builder guesser = builders.get(getBuilderByName(player.getName()));
				String guess = event.getMessage();
				
				if(player.getName() == chosen.getName()) {
					player.sendMessage("You can't chat while being the builder");
					event.setCancelled(true);
					return;
				}
				
				if(guesser.hasGuessed() == true) {
					player.sendMessage("You already found the word");
					event.setCancelled(true);
					return;
				}
				
				for(Builder builder: builders) {
					builder.sendMessage(ChatColor.BOLD + player.getName() + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.WHITE + guess);
				}
				
				if(guess.equalsIgnoreCase(word)) {
					if(guessed == true) {
						guesser.addScore(3);
						for(Builder builder : builders) {
							builder.sendMessage(ChatColor.BOLD + player.getName() + ChatColor.RESET + ChatColor.GREEN + " has found the word! " + ChatColor.RESET + "[+3]");
							AbsoluteCraft.tokens.add(builder.getName(), 3);
						}
					} else {
						guesser.addScore(1);
						for(Builder builder : builders) {
							builder.sendMessage(ChatColor.BOLD + player.getName() + ChatColor.RESET + ChatColor.GREEN + " has found the word! " + ChatColor.RESET + "[+1]");
							AbsoluteCraft.tokens.add(builder.getName(), 1);
						}
					}
					
					guesser.setGuessed(true);
					
					player.getWorld().playSound(player.getLocation(), Sound.ANVIL_LAND, 1, 1);
					
					Bukkit.getScheduler().cancelTask(turnTask);
					turnTimer = false;
					guessTimer();
				}
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event){
        Player player = event.getPlayer();
        String msg = event.getMessage();
        
        if(playing == true && (!player.isOp() || !player.hasPermission("buildit.admin"))) {
	        int index = getBuilderByName(player.getName());
	        if (index != -1) {
	        	if(msg.matches("/buildit leave")) {
	        		return;
	        	} else if (msg.matches("/")){
	                player.sendMessage(BuildIt.prefix + ChatColor.RED + " You cannot use commands whilst playing!");
	                event.setCancelled(true);
	            }
	        }
        }
    }
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		int index = getBuilderByName(player.getName());
		if(index != -1) {
			removePlayer(index, true);
		}
	}
}
