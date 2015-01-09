package com.boveybrawlers.BuildIt;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
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
	
	public void addScore(Integer amount) {
		this.score += amount;
	}
	
	public void setScore(Integer score) {
		this.score = score;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	
	}
	
	public boolean hasId() {
		if(this.id != null) {
			return true;
		}
		
		return false;
	}
	
	public void setGuessed(boolean guessed) {
		this.guessed = guessed;
	}
	
	public boolean hasGuessed() {
		return this.guessed;
	}

	public void sendMessage(String message) {
		Bukkit.getServer().getPlayer(this.username).sendMessage(message);
	}
	
	public void teleport(Location location) {
		Bukkit.getServer().getPlayer(this.username).teleport(location);
	}
	
	public void setFly(boolean mode) {
		if(mode == false) {
			Bukkit.getServer().getPlayer(this.username).setFlying(false);
		} else {
			Bukkit.getServer().getPlayer(this.username).setFlying(true);
		}
	}
	
	public void setInventory(String type) {
		Bukkit.getServer().getPlayer(this.username).getInventory().clear();
		
		if(type == "blocks") {
			for(short i = 0; i < 16; i++){
				Bukkit.getServer().getPlayer(this.username).getInventory().addItem(new ItemStack(Material.STAINED_CLAY, 64, i));
			}
		}
	}
}
