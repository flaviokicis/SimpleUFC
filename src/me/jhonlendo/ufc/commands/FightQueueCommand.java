package me.jhonlendo.ufc.commands;

import java.util.LinkedList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.jhonlendo.ufc.UFC;
import me.jhonlendo.ufc.events.FightStartEvent;

public class FightQueueCommand implements CommandExecutor, Listener {

	private UFC ufc;
	
	private LinkedList<UUID> queue = new LinkedList<>();

	public FightQueueCommand(UFC ufc) {
		this.ufc = ufc;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
           Player p = (Player)sender;
           // Double-Checking
           if (!this.ufc.getFightManager().isFighting(p.getUniqueId())) {
        	   if (queue.contains(p.getUniqueId())) {
        		   queue.remove(p.getUniqueId());
        		   p.sendMessage("§cYou've left the queue!");
        	   } else {
        		   if (queue.getFirst() != null) {
        			   UUID oID = queue.getFirst();
        			   Player o = Bukkit.getPlayer(oID);
        			   // Double-Checking again...
        			   if (!this.ufc.getFightManager().isFighting(oID))
        			   this.ufc.getFightManager().startFight(p, o);
        		   } else {
        		   queue.add(p.getUniqueId());
        		   p.sendMessage("§aYou've entered the queue!");
        		   }
        	   }
           }
		} else {
			sender.sendMessage("§cSorry, this command is for players only.");
		}
		return true;
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent ev) {
		if (queue.contains(ev.getPlayer().getUniqueId())) {
			queue.remove(ev.getPlayer().getUniqueId());
		}
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent ev) {
		if (queue.contains(ev.getPlayer().getUniqueId())) {
			queue.remove(ev.getPlayer().getUniqueId());
		}
	}
	
	@EventHandler
	public void onStart(FightStartEvent ev) {
		if (queue.contains(ev.getPlayerOne().getUniqueId())) {
			queue.remove(ev.getPlayerOne().getUniqueId());
		} else if (queue.contains(ev.getPlayerTwo().getUniqueId())) {
			queue.remove(ev.getPlayerTwo().getUniqueId());
		}
	}

}
