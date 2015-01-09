package com.boveybrawlers.BuildIt;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length == 0) {
			sender.sendMessage(BuildIt.prefix + "v0.1 by boveybrawlers");
			sender.sendMessage(ChatColor.DARK_RED + "------------------------------");
			sender.sendMessage(ChatColor.WHITE + "/buildit join");
			sender.sendMessage(ChatColor.WHITE + "/buildit leave");
		} else {
			if(args.length == 1) {
				if(sender instanceof Player) {
					Player player = (Player) sender;
					
					if(args[0].equalsIgnoreCase("join")) {
						if(Game.getBuilderByName(player.getName()) !=  -1) {
							player.sendMessage(BuildIt.prefix + "You are already in game");
						} else if(Game.builders.size() < 10 && Game.playing == false) { 
							Game.addPlayer(player.getName());
						} else {
							player.sendMessage(BuildIt.prefix + ChatColor.RED + "The current game is full");
						}
					} else if(args[0].equalsIgnoreCase("leave")) {
						int index = Game.getBuilderByName(player.getName());
						if(index != -1) {
							Game.removePlayer(index, false);
						}
					}
				}
			}
		}
		return false;
	}

}
