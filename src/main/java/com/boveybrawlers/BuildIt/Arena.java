package com.boveybrawlers.BuildIt;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class Arena implements Listener {
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
		if(Game.playing == true && event.getAction() == Action.LEFT_CLICK_BLOCK) {
			if(Game.getBuilderByName(event.getPlayer().getName()) != -1) {
				Builder builder = Game.builders.get(Game.getBuilderByName(event.getPlayer().getName()));
				if(Turn.chosen.getName() == builder.getName()) {
					if(insideBuildArea(event.getClickedBlock())) {
						event.getClickedBlock().setType(Material.AIR);
					} else {
						event.setCancelled(true);
					}
				}
			}
		}
	}
	
	private boolean insideBuildArea(Block block) {
		Location loc = block.getLocation();
	    int px = loc.getBlockX();
	    int py = loc.getBlockY();
	    int pz = loc.getBlockZ();
	    
	    int ax = BuildIt.plugin.minBuildArea.getBlockX();
	    int ay = BuildIt.plugin.minBuildArea.getBlockY();
	    int az = BuildIt.plugin.minBuildArea.getBlockZ();
	    int bx = BuildIt.plugin.maxBuildArea.getBlockX();
	    int by = BuildIt.plugin.maxBuildArea.getBlockY();
	    int bz = BuildIt.plugin.maxBuildArea.getBlockZ();
	    
	    if(px >= ax && px <= bx && py >= ay && py <= by && pz >= az && pz <= bz) {
	    	return true;
	    }
	    
	    return false;
	}
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event){
		if(Game.getBuilderByName(event.getPlayer().getName()) != -1) {
			event.setCancelled(true);
		}
	}
}
