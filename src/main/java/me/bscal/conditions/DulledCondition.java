package me.bscal.conditions;

import me.bscal.items.ItemConstants;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

public class DulledCondition extends Condition
{
	public DulledCondition()
	{
		super("Dulled", BAD, ItemConstants.WEAPONS);
	}

	@Override
	public void Apply(ItemStack item)
	{

	}

	@Override
	public void Remove(ItemStack item)
	{

	}

	@Override
	public void Update(ItemStack item)
	{

	}
}
