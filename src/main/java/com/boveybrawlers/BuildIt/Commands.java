package com.boveybrawlers.BuildIt;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length == 0) {
			sender.sendMessage(BuildIt.prefix + "v0.9 by boveybrawlers");
			sender.sendMessage(ChatColor.DARK_GRAY + "------------------------------");
			sender.sendMessage(ChatColor.WHITE + "/buildit join");
			sender.sendMessage(ChatColor.WHITE + "/buildit leave");
			if(sender.hasPermission("buildit.start") || sender.isOp()) {
				sender.sendMessage(ChatColor.WHITE + "/buildit start");
			}
			return true;
		} else {
			if(args.length == 1) {
				if(sender instanceof Player) {
					Player player = (Player) sender;
					
					if(args[0].equalsIgnoreCase("join")) {
						if(Game.getBuilderByName(player.getName()) !=  -1) {
							player.sendMessage(BuildIt.prefix + "You are already in game");
						} else if(Game.builders.size() < 8 && Game.playing == false) { 
							Game.addPlayer(player.getName());
						} else if(Game.playing == true) {
							player.sendMessage(BuildIt.prefix + ChatColor.RED + "There's already a game playing, try again in a minute");
						} else {
							player.sendMessage(BuildIt.prefix + ChatColor.RED + "The current game is full");
						}
						return true;
					} else if(args[0].equalsIgnoreCase("leave")) {
						int index = Game.getBuilderByName(player.getName());
						if(index != -1) {
							Game.removePlayer(index, false);
						}
						return true;
					} else if(args[0].equalsIgnoreCase("start") && player.isOp()) {
						if(Game.queueCountdown == true) {
							Game.queueTask.time = 1;
						}
					}
				}
			}
		}
		return false;
	}

}
