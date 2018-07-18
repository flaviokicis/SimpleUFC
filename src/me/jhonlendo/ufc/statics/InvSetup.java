package me.jhonlendo.ufc.statics;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InvSetup {
	
	// Static class. We don't wanna instantiate the items everytime
	
	private static Set<ItemStack> items = new HashSet<>();
	
	private static ItemStack[] armor = new ItemStack[] {
			new ItemStack(Material.DIAMOND_HELMET), new ItemStack(Material.DIAMOND_CHESTPLATE), 
			new ItemStack(Material.DIAMOND_LEGGINGS), new ItemStack(Material.DIAMOND_BOOTS)
		};
	
	public static void setArmor(Player p) {
		p.getInventory().setArmorContents(armor);
	}
	
	public static void setItems(Player p) {
		for (ItemStack item : items)
		p.getInventory().addItem(item);
	}
	
	public static void setupItems() {
		ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
		sword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
		items.add(sword);
		items.add(new ItemStack(Material.FISHING_ROD));
		items.add(new ItemStack(Material.BOW));
		items.add(new ItemStack(Material.GOLDEN_APPLE, 6));
		// I don't have any skin id which has a golden apple head currently. But if I had, I'd use SkullItemMeta... (duh)
		ItemStack head = new ItemStack(Material.SKULL_ITEM, 3);
		ItemMeta meta = head.getItemMeta();
		meta.setDisplayName("§6Instant Gapple");
		head.setItemMeta(meta);
		items.add(head);
		items.add(new ItemStack(Material.ARROW, 30));
	}

}
