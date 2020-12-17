package me.bscal.conditions;

import me.bscal.WeaponConditions;
import me.bscal.items.ItemConstants;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

public class OiledCondition extends Condition
{

	protected static final NamespacedKey KEY = new NamespacedKey(WeaponConditions.Get(), "Oiled");
	protected static final float DURATION = 60.0f;

	public OiledCondition()
	{
		super("Oiled", GOOD, ItemConstants.SWORDS);
	}

	@Override
	public void Apply(ItemStack item)
	{
		ItemMeta im = item.getItemMeta();
		im.getPersistentDataContainer().set(KEY, PersistentDataType.FLOAT, DURATION);
		item.setItemMeta(im);
	}

	@Override
	public void Remove(ItemStack item)
	{
		ItemMeta im = item.getItemMeta();
		PersistentDataContainer pdc = im.getPersistentDataContainer();
		if (pdc.has(KEY, PersistentDataType.FLOAT))
		{
			pdc.remove(KEY);
			item.setItemMeta(im);
		}
	}

	@Override
	public void Update(ItemStack item)
	{
	}
}
