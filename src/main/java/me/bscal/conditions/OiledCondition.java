package me.bscal.conditions;

import me.bscal.WeaponConditions;
import me.bscal.items.ItemConstants;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public class OiledCondition extends Condition
{

	protected static final NamespacedKey KEY = new NamespacedKey(WeaponConditions.Get(), "Oiled");

	public OiledCondition()
	{
		super("Oiled", GOOD, ItemConstants.SWORDS);
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
	public void ApplyPersistentData(ItemStack item)
	{

	}

	@Override
	public void RemovePersistenData(ItemStack item)
	{

	}

	@Override
	public void Update(ItemStack item)
	{

	}
}
