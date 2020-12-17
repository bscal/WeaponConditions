package me.bscal.conditions;

import me.bscal.items.LoreItem;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public abstract class Condition implements LoreItem
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

	@Override
	public String GetLocalizedName()
	{
		return color + name;
	}

	@Override
	public String GetName()
	{
		return name;
	}

	/**
	 *
	 * @param item
	 */
	public abstract void Apply(ItemStack item);

	public abstract void Remove(ItemStack item);

	public abstract void Update(ItemStack item);

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Condition condition = (Condition) o;
		return Objects.equals(name, condition.name);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(name);
	}
}
