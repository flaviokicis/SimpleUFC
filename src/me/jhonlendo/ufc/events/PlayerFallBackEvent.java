package me.jhonlendo.ufc.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerFallBackEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();

	private FallBackCause cause;
	
	private Player p;
	
	private Location fbLoc;
	
	public PlayerFallBackEvent(Player p, FallBackCause cause, Location l) {
		this.cause = cause;
		this.p = p;
		this.fbLoc = l;
	}
	
	public Player getPlayer() {
		return this.p;
	}
	
	public FallBackCause getCause() {
		return this.cause;
	}
	
	public Location getFallBackLocation() {
		return this.fbLoc;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

}
