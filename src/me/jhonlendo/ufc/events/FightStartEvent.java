package me.jhonlendo.ufc.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FightStartEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	
	private Player one;
	
	private Player two;
	
	public FightStartEvent(Player one, Player two) {
		this.one = one;
		this.two = two;
	}

	public Player getPlayerOne() {
		return this.one;
	}
	
	public Player getPlayerTwo() {
		return this.two;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

}
