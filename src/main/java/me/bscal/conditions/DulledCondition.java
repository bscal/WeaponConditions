package me.bscal.conditions;

import me.bscal.items.ItemConstants;
import me.bscal.lore.ColorConstants;
import org.bukkit.inventory.ItemStack;

public class DulledCondition extends Condition
{
	public DulledCondition()
	{
		super("Dulled", ColorConstants.BAD, ItemConstants.WEAPONS);
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
