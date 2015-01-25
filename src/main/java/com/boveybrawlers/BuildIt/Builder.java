package com.boveybrawlers.BuildIt;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Builder {
	String username = null;
	Integer score = 0;
	Integer id = null;
	boolean guessed = false;
	
	public Builder(String username) {
		this.username = username;
	}
	
	public String getName() {
		return this.username;
	}
	
	public Integer getScore() {
		return this.score;
	}
	
	@SuppressWarnings("deprecation")
	public void addScore(int score) {
		this.score += score;
		BuildIt.plugin.objective.getScore(Bukkit.getServer().getPlayer(this.username)).setScore(this.score);
	}
	
	public boolean hasGuessed() {
		return this.guessed;
	}
	
	public void setGuessed(boolean guessed) {
		this.guessed = guessed;
	}
	
	public Player getPlayer() {
		return Bukkit.getServer().getPlayer(this.username);
	}
	
	public void heal() {
		Player player = Bukkit.getServer().getPlayer(this.username);
		player.setHealth((double) 20);
		player.setFoodLevel(20);
	}
	
	public void setGameMode(int mode) {
		if(mode == 0) {
			Bukkit.getServer().getPlayer(this.username).setGameMode(GameMode.SURVIVAL);
		} else if(mode == 1) {
			Bukkit.getServer().getPlayer(this.username).setGameMode(GameMode.CREATIVE);
		}
	}
	
	public void sendMessage(String message) {
		Bukkit.getServer().getPlayer(this.username).sendMessage(message);
	}
	
	public void teleport(Location location) {
		Player player = Bukkit.getServer().getPlayer(this.username);
		player.teleport(location);
	}
	
	public void removeInventory() {
		Player player = Bukkit.getServer().getPlayer(this.username);
		player.getInventory().clear();
	}
	
	public void setInventory(String type) {
		Player player = Bukkit.getServer().getPlayer(this.username);
		player.getInventory().clear();
		
		if(type == "blocks") {
			for(short i = 0; i < 16; i++){
				player.getInventory().addItem(new ItemStack(Material.STAINED_CLAY, 64, i));
			}
		}
	}
	
	public boolean hasId() {
		if(this.id != null) {
			return true;
		}
		
		return false;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}

	public void setFly(boolean mode) {
		Player player = Bukkit.getServer().getPlayer(this.username);
		if(mode == false) {
			player.setAllowFlight(false);
		} else {
			player.setAllowFlight(true);
		}
	}
}
