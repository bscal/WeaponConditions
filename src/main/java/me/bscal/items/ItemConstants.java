package me.bscal.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemConstants
{

	/**
	 * ItemStacks
	 */
	public static final ItemStack VANILLA_IRON_SWORD = new ItemStack(Material.IRON_SWORD);
	public static final ItemStack VANILLA_GOLD_SWORD = new ItemStack(Material.GOLDEN_SWORD);
	public static final ItemStack VANILLA_DIAMOND_SWORD = new ItemStack(Material.DIAMOND_SWORD);
	public static final ItemStack VANILLA_NETHERITE_SWORD = new ItemStack(Material.NETHERITE_SWORD);

	public static final ItemStack VANILLA_IRON_AXE = new ItemStack(Material.IRON_AXE);
	public static final ItemStack VANILLA_GOLD_AXE = new ItemStack(Material.GOLDEN_AXE);
	public static final ItemStack VANILLA_DIAMOND_AXE = new ItemStack(Material.DIAMOND_AXE);
	public static final ItemStack VANILLA_NETHERITE_AXE = new ItemStack(Material.NETHERITE_AXE);

	public static final ItemStack[] SWORDS;
	public static final ItemStack[] AXES;
	public static final ItemStack[] WEAPONS;

	static
	{
		SWORDS = new ItemStack[] { VANILLA_IRON_SWORD, VANILLA_GOLD_SWORD, VANILLA_DIAMOND_SWORD,
				VANILLA_NETHERITE_SWORD
		};

		AXES = new ItemStack[] { VANILLA_IRON_AXE, VANILLA_GOLD_AXE, VANILLA_DIAMOND_AXE,
				VANILLA_NETHERITE_AXE
		};

		WEAPONS = new ItemStack[SWORDS.length + AXES.length];
		System.arraycopy(SWORDS, 0, WEAPONS,0, SWORDS.length);
		System.arraycopy(AXES, 0, WEAPONS, SWORDS.length, AXES.length);

	}

}
