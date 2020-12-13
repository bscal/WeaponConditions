package me.bscal.conditions;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public abstract class Condition
{

	public static final ChatColor GOOD = ChatColor.GREEN;
	public static final ChatColor BAD = ChatColor.RED;
	public static final ChatColor NEUTRAL = ChatColor.GRAY;

	public final String name;
	public final ChatColor color;
	public final ItemStack[] validItems;

	public Condition(final String name, final ChatColor color, final ItemStack... validItems)
	{
		this.name = name;
		this.color = color;
		this.validItems = validItems;
	}

	public String GetLocalizedName()
	{
		return color + name;
	}

	public abstract void Apply(ItemStack item);

	public abstract void Remove(ItemStack item);

	public abstract void ApplyPersistentData(ItemStack item);

	public abstract void RemovePersistenData(ItemStack item);

	public abstract void Update(ItemStack item);

}
