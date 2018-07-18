package me.jhonlendo.ufc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import me.jhonlendo.ufc.commands.FightCommand;
import me.jhonlendo.ufc.commands.FightQueueCommand;
import me.jhonlendo.ufc.commands.UFCCommand;
import me.jhonlendo.ufc.manager.FightManager;
import me.jhonlendo.ufc.manager.StorageManager;
import me.jhonlendo.ufc.statics.ConfigParam;
import me.jhonlendo.ufc.statics.InvSetup;

public class UFC extends JavaPlugin {
	
	private CommandSender console;
	
	private StorageManager storageManager;
	
	private FightManager fightManager;
	
	public void onEnable() {
		long enable = System.currentTimeMillis();
		this.console = getServer().getConsoleSender();
		this.registerCommands();
		this.saveDefaultConfig();
		this.setStatics();
		this.storageManager = new StorageManager(this);
		this.fightManager = new FightManager();
		getServer().getPluginManager().registerEvents(fightManager, this);
		InvSetup.setupItems();
		long past = System.currentTimeMillis() - enable;
		this.console.sendMessage("§aUFC enabled in " + past + "ms (" + (past / 1000) + "sec).");
	}
	
	public void onDisable() {
		HandlerList.unregisterAll(this);
	}
	
	public void registerCommands() {
		getCommand("ufc").setExecutor(new UFCCommand(this));
		getCommand("fight").setExecutor(new FightCommand(this));
		FightQueueCommand fqueue = new FightQueueCommand(this);
		getCommand("fightqueue").setExecutor(fqueue);
		Bukkit.getPluginManager().registerEvents(fqueue, this);
	}
	
	// Get all config defined stuff and set as a constant here inside the plugin
	public void setStatics() {
		ConfigParam.permission_message = getConfig().getString("permission-denied-message").replaceAll("&", "§");
		ConfigParam.permission_node = getConfig().getString("permission-node");
		ConfigParam.timeLimit = getConfig().getInt("TimeLimit");
		ConfigParam.blockAllCommands = getConfig().getBoolean("BlockAllCommands");
		ConfigParam.tookTooLong = getConfig().getString("TookTooLong").replaceAll("&", "§");
		ConfigParam.teleportedAwayOther = getConfig().getString("TeleportedAwayOther").replaceAll("&", "§");
		ConfigParam.teleportedAwaySelf = getConfig().getString("TeleportedAwaySelf").replaceAll("&", "§");
		ConfigParam.disconnectedOther = getConfig().getString("DisconnectedOpponent").replaceAll("&", "§");
		ConfigParam.loss = getConfig().getString("loss").replaceAll("&", "§");
		ConfigParam.win = getConfig().getString("win").replaceAll("&", "§");
		ConfigParam.interrupted = getConfig().getString("interrupted").replaceAll("&", "§");
		// Setup Locations
		Location UFCSpawnOne = new Location(getWorld("SpawnONE"), getInt("SpawnONE.x") + 0.5, getInt("SpawnONE.y") + 0.5, getInt("SpawnONE.z") + 0.5);
		UFCSpawnOne.setPitch(getLong("SpawnONE.pitch"));
		UFCSpawnOne.setYaw(getLong("SpawnONE.yaw"));
		Location UFCSpawnTwo = new Location(getWorld("SpawnTWO"), getInt("SpawnTWO.x") + 0.5, getInt("SpawnTWO.y") + 0.5, getInt("SpawnTWO.z") + 0.5);
		UFCSpawnTwo.setPitch(getLong("SpawnTWO.pitch"));
		UFCSpawnTwo.setYaw(getLong("SpawnTWO.yaw"));
		Location UFCFallBack = new Location(getWorld("FallBack"), getInt("FallBack.x") + 0.5, getInt("FallBack.y") + 0.5, getInt("FallBack.z") + 0.5);
		UFCFallBack.setPitch(getLong("FallBack.pitch"));
		UFCFallBack.setYaw(getLong("FallBack.yaw"));
		ConfigParam.UFCSpawnOne = UFCSpawnOne;
		ConfigParam.UFCSpawnTwo = UFCSpawnTwo;
		ConfigParam.UFCFallBack = UFCFallBack;
	}

	public StorageManager getStorageManager() {
		return this.storageManager;
	}
	
	public FightManager getFightManager() {
		return this.fightManager;
	}
	
	private int getInt(String path) {
		return getConfig().getInt("Locations." + path);
	}
	
	private long getLong(String path) {
		return getConfig().getLong("Locations." + path);
	}
	
	private World getWorld(String path) {
		return getServer().getWorld("Locations." + getConfig().getString(path) + ".world");
	}

}
