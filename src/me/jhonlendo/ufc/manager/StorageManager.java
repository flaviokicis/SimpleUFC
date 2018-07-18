package me.jhonlendo.ufc.manager;

import org.bukkit.Location;

import me.jhonlendo.ufc.UFC;

public class StorageManager {

	private UFC ufc;

	public StorageManager(UFC ufc) {
		this.ufc = ufc;
	}

	public void updateLocation(int id, Location loc) {
		switch (id) {
		case 1:
			set("Locations.SpawnONE.world", loc.getWorld().getName());
			set("Locations.SpawnONE.x", loc.getBlockX());
			set("Locations.SpawnONE.y", loc.getBlockY());
			set("Locations.SpawnONE.z", loc.getBlockZ());
			set("Locations.SpawnONE.yaw", loc.getYaw());
			set("Locations.SpawnONE.pitch", loc.getPitch());
			break;
		case 2:
			set("Locations.SpawnTWO.world", loc.getWorld().getName());
			set("Locations.SpawnTWO.x", loc.getBlockX());
			set("Locations.SpawnTWO.y", loc.getBlockY());
			set("Locations.SpawnTWO.z", loc.getBlockZ());
			set("Locations.SpawnTWO.yaw", loc.getYaw());
			set("Locations.SpawnTWO.pitch", loc.getPitch());
			break;
		case 3:
			set("Locations.FallBack.world", loc.getWorld().getName());
			set("Locations.FallBack.x", loc.getBlockX());
			set("Locations.FallBack.y", loc.getBlockY());
			set("Locations.FallBack.z", loc.getBlockZ());
			set("Locations.FallBack.yaw", loc.getYaw());
			set("Locations.FallBack.pitch", loc.getPitch());
			break;
		}
		this.ufc.saveConfig();
	}

	private void set(String path, Object value) {
		this.ufc.getConfig().set(path, value);
	}

}
