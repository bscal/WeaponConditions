package conditions;

import org.bukkit.inventory.ItemStack;

public abstract class Condition
{

	public final String name;
	public final ItemStack[] validItems;

	public Condition(final String name, final ItemStack... validItems)
	{
		this.name = name;
		this.validItems = validItems;
	}

	public boolean IsValidItem(final ItemStack item)
	{
		return true;
	}

	public abstract void AddCondition(final ItemStack item);

}
