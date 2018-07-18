package me.jhonlendo.ufc.statics;

import org.bukkit.Location;

public class ConfigParam {
	
	public static volatile Location UFCSpawnOne;
	
	public static volatile Location UFCSpawnTwo;
	
	public static volatile Location UFCFallBack;
	
	public static volatile String permission_node = "ufc.command.admin";
	
	public static volatile String permission_message = "§cSorry, you do not have permission to execute this command.";
	
	public static volatile int timeLimit = 0;
	
	public static boolean blockAllCommands = true;
	
	public static String tookTooLong = "§cYou took to long to finish the battle. That's a draw.";
	
	public static String teleportedAwayOther = "§aYour opponent was teleported away from the battle. You win.";
	
	public static String teleportedAwaySelf = "§cYou were teleported away from the battle. You lose.";
	
	public static String win = "§aYou win!";
	
	public static String loss = "§cYou lose!";
	
	public static String disconnectedOther = "§cYour opponent has disconnected! You win!";
	
	public static String interrupted = "§cAn admin has stopped all battles!";

}
