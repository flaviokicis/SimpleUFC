package me.jhonlendo.ufc.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.jhonlendo.ufc.UFC;

public class FightCommand implements CommandExecutor {

	private UFC ufc;

	public FightCommand(UFC ufc) {
		this.ufc = ufc;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (args.length == 0) {
				p.sendMessage("§cPlease, insert a valid player name.");
			} else if (args[0].length() > 16) {
				p.sendMessage("§cPlease, insert a valid player name.");
			} else {
				// Double-Checking
				if (this.ufc.getFightManager().isFighting(p.getUniqueId())) {
					return true;
				}
				Player o = Bukkit.getPlayer(args[0]);
				if (o.getName().equalsIgnoreCase(p.getName())) {
					p.sendMessage("§cYou can't fight yourself.");
				} else {
					if (this.ufc.getFightManager().isDaring(o.getUniqueId(), p.getUniqueId())) {
						if (this.ufc.getFightManager().isFighting(o.getUniqueId())) {
							this.ufc.getFightManager().removeChallenge(o.getUniqueId(), p.getUniqueId());
							p.sendMessage("§cThis player is currently in a fight.");
							return true;
						}
						this.ufc.getFightManager().startFight(p, o);
						p.sendMessage("§cYou accepted their challenge!");
						o.sendMessage("§aChallenge accepted!");
					} else {
						if (this.ufc.getFightManager().isFighting(o.getUniqueId())) {
							p.sendMessage("§cThis player is currently in a fight.");
							return true;
						}
						if (this.ufc.getFightManager().dare(p.getUniqueId(), o.getUniqueId())) {
							p.sendMessage("§aYou've challenged " + o.getDisplayName() + "!");
							o.sendMessage("§aYou've been challenged for a fight by " + p.getDisplayName() + "!");
							o.sendMessage("§aUse /fight " + p.getDisplayName() + " to accept!");
						} else {
							p.sendMessage("§cYou are already challenging this player.");
						}
					}
				}
			}
		} else {
			sender.sendMessage("§cSorry, this command is for players only.");
		}
		return true;
	}

}
