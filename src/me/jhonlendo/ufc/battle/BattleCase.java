package me.jhonlendo.ufc.battle;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.jhonlendo.ufc.UFC;
import me.jhonlendo.ufc.events.FallBackCause;
import me.jhonlendo.ufc.events.FightStartEvent;
import me.jhonlendo.ufc.events.PlayerFallBackEvent;
import me.jhonlendo.ufc.statics.ConfigParam;
import me.jhonlendo.ufc.statics.InvSetup;

public class BattleCase implements Listener {

	private UUID playerOne;

	private UUID playerTwo;

	private BukkitTask endTask;

	private Plugin plugin;

	private Map<UUID, ItemStack[]> armor = new HashMap<>();

	private Map<UUID, ItemStack[]> inv = new HashMap<>();

	public BattleCase(Player one, Player two) {
		// There are other ways to retrieve the Plugin class instance... whatever tho
		plugin = JavaPlugin.getPlugin(UFC.class);
		Bukkit.getPluginManager().registerEvents(this, plugin);
		Bukkit.getPluginManager().callEvent(new FightStartEvent(one, two));
		this.playerOne = one.getUniqueId();
		this.playerTwo = two.getUniqueId();
		one.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 255));
		one.teleport(ConfigParam.UFCSpawnOne);
		two.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 255));
		two.teleport(ConfigParam.UFCSpawnTwo);
		setItems(one);
		setItems(two);
		if (ConfigParam.timeLimit > 10) {
			this.endTask = new BukkitRunnable() {
				public void run() {
					stop(ConfigParam.tookTooLong, false, FallBackCause.TOO_LONG);
				}
			}.runTaskLater(plugin, ConfigParam.timeLimit * 20);
		}
	}

	@EventHandler
	public void onDamageByEntity(EntityDamageByEntityEvent ev) {
		Entity en = ev.getEntity();
		Entity dmg = ev.getDamager();
		if (isPlaying(en.getUniqueId())) {
			if (!isPlaying(dmg.getUniqueId())) {
				ev.setCancelled(true);
			}
		} else if (isPlaying(dmg.getUniqueId())) {
			if (!isPlaying(en.getUniqueId())) {
				ev.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent ev) {
		if (isPlaying(ev.getPlayer().getUniqueId())) {
			if (ConfigParam.blockAllCommands) {
				ev.setCancelled(true);
				ev.getPlayer().sendMessage("§cCommands are disabled during the fight.");
			}
			else {
				String cmd = ev.getMessage().split(" ")[0].replaceAll("/", "");
				if (cmd.equalsIgnoreCase("fight")) {
					ev.getPlayer().sendMessage("§cYou are already in a fight.");
					ev.setCancelled(true);
				}
				else if (cmd.equalsIgnoreCase("fightqueue")) {
					ev.getPlayer().sendMessage("§cYou are already in a fight.");
					ev.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent ev) {
		if (isPlaying(ev.getPlayer().getUniqueId()) && (ev.getAction().name().contains("RIGHT"))) {
			if (ev.getItem() != null && (ev.getItem().hasItemMeta() && (ev.getItem().getItemMeta().hasDisplayName()))) {
				if (ev.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6Instant Gapple")) {
					Player p = ev.getPlayer();
					if (ev.getItem().getAmount() - 1 > 0) {
						ev.getItem().setAmount(ev.getItem().getAmount() - 1);
					} else {
						p.setItemInHand(null);
					}
					p.setHealth(((Damageable) p).getHealth() + 2);
					p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 260, 2));
					p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 260, 2));
					p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EAT, 1, 1);
				}
			}
		}
	}

	@EventHandler
	public void onBreak(BlockBreakEvent ev) {
		if (isPlaying(ev.getPlayer().getUniqueId())) {
			ev.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent ev) {
		if (isPlaying(ev.getPlayer().getUniqueId())) {
			ev.setCancelled(true);
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent ev) {
		if (ev.getPlayer().getUniqueId().equals(playerOne)) {
			// Player Two can't be null here, If it is, something very wrong happened :O
			stopPlayer(Bukkit.getPlayer(playerTwo), ConfigParam.disconnectedOther, FallBackCause.DISCONNECT_OTHER);
			stopPlayer(ev.getPlayer(), null, FallBackCause.DISCONNECT_SELF);
			this.endTask.cancel();
		} else if (ev.getPlayer().getUniqueId().equals(playerTwo)) {
			// Player One can't be null here, If it is, something very wrong happened :O
			stopPlayer(Bukkit.getPlayer(playerOne), ConfigParam.disconnectedOther, FallBackCause.DISCONNECT_OTHER);
			stopPlayer(ev.getPlayer(), null, FallBackCause.DISCONNECT_SELF);
			this.endTask.cancel();
		}
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent ev) {
		if (ev.getPlayer().getUniqueId().equals(playerOne)) {
			// Player Two can't be null here, If it is, something very wrong happened :O
			stopPlayer(Bukkit.getPlayer(playerTwo), ConfigParam.disconnectedOther, FallBackCause.DISCONNECT_OTHER);
			stopPlayer(ev.getPlayer(), null, FallBackCause.DISCONNECT_SELF);
			this.endTask.cancel();
		} else if (ev.getPlayer().getUniqueId().equals(playerTwo)) {
			// Player One can't be null here, If it is, something very wrong happened :O
			stopPlayer(Bukkit.getPlayer(playerOne), ConfigParam.disconnectedOther, FallBackCause.DISCONNECT_OTHER);
			stopPlayer(ev.getPlayer(), null, FallBackCause.DISCONNECT_SELF);
			this.endTask.cancel();
		}
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent ev) {
		if (ev.getEntity().getUniqueId().equals(playerOne)) {
			stopPlayer(ev.getEntity(), ConfigParam.loss, FallBackCause.LOSE);
			stopPlayer(Bukkit.getPlayer(playerTwo), ConfigParam.win, FallBackCause.WIN);
		} else if (ev.getEntity().getUniqueId().equals(playerTwo)) {
			stopPlayer(ev.getEntity(), ConfigParam.loss, FallBackCause.LOSE);
			stopPlayer(Bukkit.getPlayer(playerOne), ConfigParam.win, FallBackCause.WIN);
		}
	}

	@EventHandler
	public void onTeleport(PlayerTeleportEvent ev) {
		if (ev.getPlayer().getUniqueId().equals(playerOne)) {
			// Player Two can't be null here, If it is, something very wrong
			// happened :O
			stopPlayer(Bukkit.getPlayer(playerTwo), ConfigParam.teleportedAwayOther, FallBackCause.TELEPORT_OTHER);
			stopPlayer(ev.getPlayer(), ConfigParam.teleportedAwaySelf, FallBackCause.TELEPORT_SELF);
			this.endTask.cancel();
		} else if (ev.getPlayer().getUniqueId().equals(playerTwo)) {
			// Player One can't be null here, If it is, something very wrong
			// happened :O
			stopPlayer(Bukkit.getPlayer(playerOne), ConfigParam.teleportedAwayOther, FallBackCause.TELEPORT_OTHER);
			stopPlayer(ev.getPlayer(), ConfigParam.teleportedAwaySelf, FallBackCause.TELEPORT_SELF);
			this.endTask.cancel();
		}
	}

	private boolean isPlaying(UUID id) {
		return (id.equals(playerOne) || (id.equals(playerTwo)));
	}

	public void stop(String message, boolean cancel, FallBackCause cause) {
		HandlerList.unregisterAll(this);
		Player p = Bukkit.getPlayer(playerOne);
		Player o = Bukkit.getPlayer(playerTwo);
		if (p != null) {
			stopPlayer(p, message, cause);
		}
		if (o != null) {
			stopPlayer(o, message, cause);
		}
		if (cancel)
			this.endTask.cancel();
	}

	private void backup(Player p) {
		this.armor.put(p.getUniqueId(), p.getInventory().getArmorContents());
		this.inv.put(p.getUniqueId(), p.getInventory().getContents());
	}

	private void setBackupItems(Player p) {
		p.getInventory().setArmorContents(this.armor.remove(p.getUniqueId()));
		p.getInventory().setContents(this.armor.remove(p.getUniqueId()));
	}

	private void stopPlayer(Player p, String message, FallBackCause cause) {
		p.teleport(ConfigParam.UFCFallBack);
		new BukkitRunnable() {
			public void run() {
				p.getInventory().setArmorContents(null);
				p.getInventory().clear();
				p.setItemOnCursor(null);
				p.closeInventory();
				if (message != null)
					p.sendMessage(message);
				setBackupItems(p);
				p.updateInventory();
			}
		}.runTaskAsynchronously(plugin);
		Bukkit.getPluginManager().callEvent(new PlayerFallBackEvent(p, cause, ConfigParam.UFCFallBack));
	}

	private void setItems(Player p) {
		new BukkitRunnable() {
			public void run() {
				backup(p);
				InvSetup.setArmor(p);
				InvSetup.setItems(p);
			}
		}.runTaskAsynchronously(plugin);
	}

}
