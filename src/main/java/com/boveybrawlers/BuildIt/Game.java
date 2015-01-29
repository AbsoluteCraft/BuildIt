package com.boveybrawlers.BuildIt;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Team;

import com.boveybrawlers.AbsoluteCraft.AbsoluteCraft;

public class Game implements Listener {
	public static boolean playing = false;
	public static ArrayList<Builder> builders = new ArrayList<Builder>();
	public static ArrayList<Builder> buildersGone = new ArrayList<Builder>();
	
	static List<String> usedWords = new ArrayList<String>();
	
	public static QueueHandler queueTask = null;
	public static boolean queueCountdown = false;
	
	public static boolean acceptGuesses = false;
	
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
		
		Player player = builders.get(getBuilderByName(username)).getPlayer();
		
		switch(builders.size()) {
			case 1:
				BuildIt.plugin.teamOne.addPlayer(player);
				BuildIt.plugin.teamOne.setDisplayName(player.getName());
			break;
			case 2:
				BuildIt.plugin.teamTwo.addPlayer(player);
				BuildIt.plugin.teamTwo.setDisplayName(player.getName());
			break;
			case 3:
				BuildIt.plugin.teamThree.addPlayer(player);
				BuildIt.plugin.teamThree.setDisplayName(player.getName());
			break;
			case 4:
				BuildIt.plugin.teamFour.addPlayer(player);
				BuildIt.plugin.teamFour.setDisplayName(player.getName());
			break;
			case 5:
				BuildIt.plugin.teamFive.addPlayer(player);
				BuildIt.plugin.teamFive.setDisplayName(player.getName());
			break;
			case 6:
				BuildIt.plugin.teamSix.addPlayer(player);
				BuildIt.plugin.teamSix.setDisplayName(player.getName());
			break;
			case 7:
				BuildIt.plugin.teamSeven.addPlayer(player);
				BuildIt.plugin.teamSeven.setDisplayName(player.getName());
			break;
			case 8:
				BuildIt.plugin.teamEight.addPlayer(player);
				BuildIt.plugin.teamEight.setDisplayName(player.getName());
			break;
			case 9:
				BuildIt.plugin.teamNine.addPlayer(player);
				BuildIt.plugin.teamNine.setDisplayName(player.getName());
			break;
			case 10:
				BuildIt.plugin.teamTen.addPlayer(player);
				BuildIt.plugin.teamTen.setDisplayName(player.getName());
			break;
		}
		
		player.setScoreboard(BuildIt.plugin.board);
		BuildIt.plugin.objective.getScore(player.getName()).setScore(0);
		
		if(builders.size() == 10) {
			start();
			queueTask.cancel();
		} else if(builders.size() == 4) {
			if(queueCountdown == false) {
				queueCountdown();
			}
		} else if(builders.size() == 1) {
			if(Bukkit.getWorld("Games") == null) {
				BuildIt.plugin.getLogger().info("World 'Games' doesn't exist");
			} else {
				BuildIt.plugin.world = Bukkit.getWorld("Games");
			}
			
			BuildIt.plugin.lobby = new Location(BuildIt.plugin.world, -197, 71, 1, 90, -30);
			BuildIt.plugin.spawn = new Location(BuildIt.plugin.world, -229, 79, 1, 90, 0);
			BuildIt.plugin.builder = new Location(BuildIt.plugin.world, -261, 71, 1, -90, 0);
			BuildIt.plugin.minBuildArea = new Location(BuildIt.plugin.world, -251, 71, 11);
			BuildIt.plugin.maxBuildArea = new Location(BuildIt.plugin.world, -271, 101, -9);

			BuildIt.plugin.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		}
		
		Builder builder = builders.get(getBuilderByName(username));
		if(Bukkit.getWorld("Games") == null) {
			builder.sendMessage("Can't teleport you because world is null");
		} else {
			builder.teleport(BuildIt.plugin.lobby);
		}
		builder.setGameMode(0);
		
		for(Builder b : builders) {
			b.sendMessage(BuildIt.prefix + ChatColor.GREEN + username + " has joined");
		}
	}
	
	public static void removePlayer(int index, boolean ending) {
		BuildIt.plugin.getLogger().info("Removing player " + index);
		String username = builders.get(index).getName();
		
		Player player = builders.get(index).getPlayer();
		
		if(playing == true) {
			Team dead = player.getScoreboard().getPlayerTeam(player);
			dead.unregister();
		}
		
		player.setScoreboard(BuildIt.plugin.manager.getNewScoreboard());
		
//		if(BuildIt.useHolographicDisplays) {
//			if(hologram == null) {
//				Location where = new Location(BuildIt.plugin.world, -197.5, 73.5, 0);
//				hologram = HologramsAPI.createHologram(BuildIt.plugin, where);
//			}
//			
//			hologram.appendTextLine(BuildIt.prefix + ChatColor.WHITE + builders.size() + " / 10 Players");
//		}
		
		builders.get(index).removeInventory();
		builders.get(index).heal();
		builders.get(index).setFly(false);
		builders.get(index).teleport(BuildIt.plugin.lobby);
		builders.get(index).getPlayer().setLevel(0);
		
		builders.remove(index);
		
		for(Builder builder : builders) {
			builder.sendMessage(BuildIt.prefix + ChatColor.RED +  username + " has left");
		}
		
		if(playing == false) {
			for(int i = 1; i < builders.size(); i++) {
				if(builders.get(i - 1) != null) {
					Player builderplayer = builders.get(i - 1).getPlayer();
					switch(i) {
						case 1:
							BuildIt.plugin.teamOne.removePlayer(builderplayer);
							BuildIt.plugin.teamOne.addPlayer(builderplayer);
							BuildIt.plugin.teamOne.setDisplayName(builderplayer.getName());
						break;
						case 2:
							BuildIt.plugin.teamTwo.removePlayer(builderplayer);
							BuildIt.plugin.teamTwo.addPlayer(builderplayer);
							BuildIt.plugin.teamTwo.setDisplayName(builderplayer.getName());
						break;
						case 3:
							BuildIt.plugin.teamThree.removePlayer(builderplayer);
							BuildIt.plugin.teamThree.addPlayer(builderplayer);
							BuildIt.plugin.teamThree.setDisplayName(builderplayer.getName());
						break;
						case 4:
							BuildIt.plugin.teamFour.removePlayer(builderplayer);
							BuildIt.plugin.teamFour.addPlayer(builderplayer);
							BuildIt.plugin.teamFour.setDisplayName(builderplayer.getName());
						break;
						case 5:
							BuildIt.plugin.teamFive.removePlayer(builderplayer);
							BuildIt.plugin.teamFive.addPlayer(builderplayer);
							BuildIt.plugin.teamFive.setDisplayName(builderplayer.getName());
						break;
						case 6:
							BuildIt.plugin.teamSix.removePlayer(builderplayer);
							BuildIt.plugin.teamSix.addPlayer(builderplayer);
							BuildIt.plugin.teamSix.setDisplayName(builderplayer.getName());
						break;
						case 7:
							BuildIt.plugin.teamSeven.removePlayer(builderplayer);
							BuildIt.plugin.teamSeven.addPlayer(builderplayer);
							BuildIt.plugin.teamSeven.setDisplayName(builderplayer.getName());
						break;
						case 8:
							BuildIt.plugin.teamEight.removePlayer(builderplayer);
							BuildIt.plugin.teamEight.addPlayer(builderplayer);
							BuildIt.plugin.teamEight.setDisplayName(builderplayer.getName());
						break;
						case 9:
							BuildIt.plugin.teamNine.removePlayer(builderplayer);
							BuildIt.plugin.teamNine.addPlayer(builderplayer);
							BuildIt.plugin.teamNine.setDisplayName(builderplayer.getName());
						break;
						case 10:
							BuildIt.plugin.teamTen.removePlayer(builderplayer);
							BuildIt.plugin.teamTen.addPlayer(builderplayer);
							BuildIt.plugin.teamTen.setDisplayName(builderplayer.getName());
						break;
					}
				}
			}
		}
		
		if(playing == false && builders.size() == 1) {
			if(queueCountdown == true) {
				queueTask.cancel();
				queueCountdown = false;
			}
		}
		
		if(ending == false && playing == true && builders.size() == 3) {
			if(Turn.guessCountdown == true) {
				Turn.guessTask.cancel();
			}
			if(Turn.shortGuessCountdown == true) {
				Turn.shortGuessCountdown();
			}
			
			for(Builder builder : builders) {
				builder.sendMessage(BuildIt.prefix + ChatColor.RED + "Not enough players to continue");
				
				builder.removeInventory();
				builder.heal();
				builder.teleport(BuildIt.plugin.lobby);
			}
			
			// Reset game
			builders.clear();
			playing = false;
		}
	}
	
	private static void queueCountdown() {
		queueCountdown = true;
		queueTask = new QueueHandler(60);
		queueTask.runTaskTimer(BuildIt.plugin, 0, 20);
	}

	public static void start() {
		for(Builder builder : builders) {
			builder.teleport(BuildIt.plugin.spawn);
			builder.removeInventory();
		}
		
		playing = true;
		Turn.resetBuildArea();
		Round.next();
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		if(playing == true && acceptGuesses == true) {
			Player player = event.getPlayer();
			
			int index = getBuilderByName(player.getName());
			if(index != -1) {
				Builder guesser = builders.get(getBuilderByName(player.getName()));
				String guess = event.getMessage();
				
				if(Turn.chosen != null) {
					if(player.getName() == Turn.chosen.getName()) {
						player.sendMessage(BuildIt.prefix + "You can't chat while being the builder");
						event.setCancelled(true);
						return;
					}
				}
				
				if(guesser.hasGuessed() == true) {
					player.sendMessage("You already found the word");
					event.setCancelled(true);
					return;
				}
				
				if(guess.equalsIgnoreCase(Turn.word)) {
					if(Turn.guessed == false) {
						for(Builder builder : builders) {
							builder.sendMessage(BuildIt.prefix + ChatColor.GOLD + player.getName() + ChatColor.WHITE + " has guessed the word! " + ChatColor.RESET + ChatColor.GREEN + "[+3]");
						}
						
						AbsoluteCraft.tokens.add(player.getUniqueId(), player.getName(), 3);
						AbsoluteCraft.leaderboard.add(player.getName(), "buildit", 3);
						guesser.addScore(3);
						
						AbsoluteCraft.tokens.add(Turn.chosen.getPlayer().getUniqueId(), Turn.chosen.getName(), 2);
						AbsoluteCraft.leaderboard.add(Turn.chosen.getName(), "buildit", 2);
						Turn.chosen.addScore(2);
						
						Turn.guessTask.cancel();
						Turn.guessCountdown = false;
						
						Turn.shortGuessCountdown();
						
						Turn.guessed = true;
					} else {
						guesser.addScore(1);
						for(Builder builder : builders) {
							builder.sendMessage(BuildIt.prefix + ChatColor.GOLD + player.getName() + ChatColor.WHITE + " has also guessed the word! " + ChatColor.RESET + ChatColor.GREEN + "[+1]");
						}
						
						AbsoluteCraft.tokens.add(player.getUniqueId(), player.getName(), 1);
						AbsoluteCraft.leaderboard.add(player.getName(), "buildit", 1);
						Turn.chosen.addScore(1);
					}
					
					AbsoluteCraft.leaderboard.add(player.getName(), "buildit", 1);
					
					guesser.setGuessed(true);
					
					player.getWorld().playSound(player.getLocation(), Sound.ANVIL_LAND, 5, 1);
					
					event.setCancelled(true);
					return;
				} else {
					for(Builder builder: builders) {
						builder.sendMessage(BuildIt.prefix + ChatColor.BOLD + player.getName() + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.WHITE + guess);
					}
				}
			}
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler
    public void onCommandType(PlayerCommandPreprocessEvent event){
        Player player = event.getPlayer();
        String msg = event.getMessage();
        
        if(playing == true && (!player.isOp() || !player.hasPermission("buildit.admin"))) {
	        int index = getBuilderByName(player.getName());
	        if (index != -1) {
	        	if(msg.contains("/buildit leave")) {
	        		return;
	        	} else if (msg.startsWith("/")){
	                player.sendMessage(BuildIt.prefix + ChatColor.RED + " You cannot use commands whilst playing!");
	                event.setCancelled(true);
	            }
	        }
        }
    }
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerPlaceBlock(BlockPlaceEvent event) {
		if(playing == true) {
			if(getBuilderByName(event.getPlayer().getName()) != -1) {
				event.getPlayer().getInventory().addItem(new ItemStack(event.getBlockPlaced().getType(), 1, event.getBlockPlaced().getData()));
			}
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		BuildIt.plugin.getLogger().info(event.getAction().toString());
		if(playing == true && event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			if(getBuilderByName(event.getPlayer().getName()) != -1) {
				Builder builder = builders.get(getBuilderByName(event.getPlayer().getName()));
				if(Turn.chosen.getName() == builder.getName()) {
					event.getClickedBlock().setType(Material.AIR);
					BuildIt.plugin.getLogger().info("Replacing block");
				} else {
					BuildIt.plugin.getLogger().info("Player isn't Turn.chosen");
				}
			} else {
				BuildIt.plugin.getLogger().info("Player isn't in builders");
			}
		} else {
			BuildIt.plugin.getLogger().info("Not playing or not left click");
		}
	}
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event){
		if(getBuilderByName(event.getPlayer().getName()) != -1) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		
		int index = getBuilderByName(player.getName());
		if(index != -1) {
			removePlayer(index, false);
		}
	}
}