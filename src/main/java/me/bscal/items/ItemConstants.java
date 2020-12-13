package me.bscal.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemConstants
{

	public static final ItemStack VANILLA_IRON_SWORD = new ItemStack(Material.IRON_SWORD);
	public static final ItemStack VANILLA_GOLD_SWORD = new ItemStack(Material.GOLDEN_SWORD);
	public static final ItemStack VANILLA_DIAMOND_SWORD = new ItemStack(Material.DIAMOND_SWORD);
	public static final ItemStack VANILLA_NETHERITE_SWORD = new ItemStack(Material.NETHERITE_SWORD);

	public static final ItemStack[] SWORDS;

	static
	{
		SWORDS = new ItemStack[] { VANILLA_IRON_SWORD, VANILLA_GOLD_SWORD, VANILLA_DIAMOND_SWORD,
				VANILLA_NETHERITE_SWORD
		};
	}

}
