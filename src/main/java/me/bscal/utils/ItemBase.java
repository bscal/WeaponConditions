package me.bscal.utils;

import org.bukkit.Material;

public enum ItemBase
{

	DEFAULT_BASE(Material.AIR, 0, 0),

	WOOD_SWORD(Material.WOODEN_SWORD,4, 1.6f),
	GOLD_SWORD(Material.GOLDEN_SWORD,4, 1.6f),
	STONE_SWORD(Material.STONE_SWORD,5, 1.6f),
	IRON_SWORD(Material.IRON_SWORD,6, 1.6f),
	DIAMOND_SWORD(Material.DIAMOND_SWORD,7, 1.6f),
	NETHERITE_SWORD(Material.NETHERITE_SWORD,8, 1.6f),

	WOOD_AXE(Material.WOODEN_AXE,7, .8f),
	GOLD_AXE(Material.GOLDEN_AXE,7, 1f),
	STONE_AXE(Material.STONE_AXE,9, .8f),
	IRON_AXE(Material.IRON_SWORD,9, .9f),
	DIAMOND_AXE(Material.DIAMOND_AXE,9, 1f),
	NETHERITE_AXE(Material.NETHERITE_AXE,10, 1f);

	final Material type;
	final float damage;
	final float as;

	private ItemBase(Material type, float damage, float as)
	{
		this.type = type;
		this.damage = damage;
		this.as = as;
	}

	public static ItemBase MatToBase(Material type)
	{
		for (ItemBase base : values())
		{
			if (base.type == type)
				return base;
		}
		return DEFAULT_BASE;
	}

}
