package me.jhonlendo.ufc.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.jhonlendo.ufc.battle.BattleCase;
import me.jhonlendo.ufc.events.FallBackCause;
import me.jhonlendo.ufc.events.PlayerFallBackEvent;
import me.jhonlendo.ufc.statics.ConfigParam;

public class FightManager implements Listener {
	
	private HashSet<BattleCase> cases = new HashSet<>();
	
	private HashSet<UUID> fighting = new HashSet<>();
	
	private HashMap<UUID, ArrayList<UUID>> challenge = new HashMap<>();
	
	public void startFight(Player p, Player o) {
		BattleCase bcase = new BattleCase(p, o);
		cases.add(bcase);
		fighting.add(p.getUniqueId());
		fighting.add(o.getUniqueId());
	}
	
	public void stopAll() {
		for (BattleCase bcase : cases) {
			bcase.stop(ConfigParam.interrupted, true, FallBackCause.INTERRUPTED);
		}
	}
	
	public boolean isFighting(UUID id) {
		return this.fighting.contains(id);
	}
	
	@EventHandler
	public void onFightFallBack(PlayerFallBackEvent ev) {
		fighting.remove(ev.getPlayer().getUniqueId());
	}
	
	public boolean dare(UUID challenger, UUID challenged) {
		ArrayList<UUID> id = challenge.get(challenger);
		if (id == null) {
			id = new ArrayList<>();
		}
		if (id.contains(challenged))return false;
		id.add(challenged);
		challenge.put(challenger, id);
		return true;
	}
	
	public boolean isDaring(UUID challenger, UUID challenged) {
		return (challenge.get(challenger) != null && (challenge.get(challenger).contains(challenged)));
	}
	
	public void removeChallenge(UUID challenger, UUID challenged) {
		ArrayList<UUID> id = challenge.get(challenger);
		if (id == null) {
			return;
		}
		id.remove(challenged);
		challenge.put(challenger, id);
	}

}
