package com.boveybrawlers.BuildIt;

import org.bukkit.scheduler.BukkitRunnable;

public class GuessHandler extends BukkitRunnable {
	private int time;
	
	public GuessHandler(int time) {
		this.time = time;
	}
	
	public void run() {
		if(time == 0) {
			this.cancel();
			Turn.guessCountdown = false;
			Turn.shortGuessCountdown = false;
			Turn.end();
		}
		else {
			for(Builder builder : Game.builders) {
				builder.getPlayer().setLevel(time);
			}
		}
		
		this.time--;
	}
}