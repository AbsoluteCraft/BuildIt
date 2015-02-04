package com.boveybrawlers.BuildIt;

import org.bukkit.scheduler.BukkitRunnable;

public class QueueHandler extends BukkitRunnable {
	public int time;
	
	public QueueHandler(int time) {
		this.time = time;
	}
	
	public void run() {
		if(time == 0) {
			this.cancel();
			Game.queueCountdown = false;
			Game.start();
		} else {
			for(Builder builder : Game.builders) {
				builder.getPlayer().setLevel(time);
			}
		}
		this.time--;
	}	
}
