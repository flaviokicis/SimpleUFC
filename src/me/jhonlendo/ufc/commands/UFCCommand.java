package me.jhonlendo.ufc.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.jhonlendo.ufc.UFC;
import me.jhonlendo.ufc.statics.ConfigParam;

public class UFCCommand implements CommandExecutor {

	private UFC ufc;

	public UFCCommand(UFC ufc) {
		this.ufc = ufc;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player caller = (Player) sender;
			if (caller.hasPermission(ConfigParam.permission_node)) {

				if (args.length == 0) {
					caller.sendMessage("§eUsage for this command below:");
					caller.sendMessage("§esetspawn [one/two] - Set spawn in arena for player one or two");
					caller.sendMessage("§esetfallback - Set the location where players will go back to");
					caller.sendMessage("§estopall - Stop all fights and teleport players back");
				} else {
					String arg = args[0];
					if (arg.equalsIgnoreCase("setspawn")) {
						if (args.length == 1) {
							caller.sendMessage("§cPlease, choose ONE or TWO.");
						} else {
							if (args[1].equalsIgnoreCase("one")) {
								Location update = caller.getLocation();
								ConfigParam.UFCSpawnOne = update;
								this.ufc.getStorageManager().updateLocation(1, update);
								caller.sendMessage("§aLocation successfully updated!");
							} else if (args[1].equalsIgnoreCase("two")) {
								Location update = caller.getLocation();
								ConfigParam.UFCSpawnTwo = update;
								this.ufc.getStorageManager().updateLocation(2, update);
								caller.sendMessage("§aLocation successfully updated!");
							} else {
								caller.sendMessage("§cPlease, choose ONE or TWO.");
							}
						}
					} else if (arg.equalsIgnoreCase("setfallback")) {
						Location update = caller.getLocation();
						ConfigParam.UFCFallBack = update;
						this.ufc.getStorageManager().updateLocation(3, update);
						caller.sendMessage("§aLocation successfully updated!");
					} else if (arg.equalsIgnoreCase("stopall")) {
					    this.ufc.getFightManager().stopAll();
					    caller.sendMessage("§aSuccessfully stopped all fights.");
					} else {
						caller.sendMessage("§eUsage for this command below:");
						caller.sendMessage("§esetspawn [one/two] - Set spawn in arena for player one or two");
						caller.sendMessage("§esetfallback - Set the location where players will go back to");
						caller.sendMessage("§estopall - Stop all fights and teleport players back");
					}
				}

			} else {
				caller.sendMessage(ConfigParam.permission_message);
				caller.sendMessage("§cYou might want to see: /fight and /fightqueue");
			}
		} else {
			sender.sendMessage("§cSorry, this command is for players only.");
		}
		return true;
	}

}
