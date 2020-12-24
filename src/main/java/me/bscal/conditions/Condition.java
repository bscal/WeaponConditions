package me.bscal.conditions;

import me.bscal.lore.LoreItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class Condition implements LoreItem
{

	public final String name;
	public final ChatColor color;
	public final List<ItemStack> validItems;

	public Condition(final String name, final ChatColor color, final ItemStack... validItems)
	{
		this.name = name;
		this.color = color;
		if (validItems.length != 0)
			this.validItems = Arrays.asList(validItems);
		else
			this.validItems = new ArrayList<>();
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

	public boolean IsValid(ItemStack item)
	{
		return item.getType() != Material.AIR && item.hasItemMeta() && validItems.contains(item);
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
