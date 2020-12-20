package me.bscal.utils;

import me.bscal.WeaponConditions;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collection;
import java.util.UUID;

public class ItemUtils
{

	/**
	 * Adds an Attribute modifier but also adds a default modifier because for some reason the 1st
	 * modifier overrides base values. *shrug*
	 * @param item - ItemStack to add to
	 * @param attr - Attribute to use
	 * @param mod - Modifier, default it built off this.
	 * @para addDefault - Should check to add default values.
	 */
	public static void AddAttribute(ItemStack item, Attribute attr, AttributeModifier mod, boolean addDefault)
	{
		ItemMeta im = item.getItemMeta();
		if (addDefault)
		{
			Collection<AttributeModifier> coll = im.getAttributeModifiers(attr);
			if (coll == null || coll.size() < 1)
			{
				AttributeModifier baseMod = new AttributeModifier(UUID.randomUUID(), mod.getName(),
						ItemBase.MatToBase(item.getType()).damage, mod.getOperation(), mod.getSlot());
				im.addAttributeModifier(attr, baseMod);
			}
		}
		im.addAttributeModifier(attr, mod);
		item.setItemMeta(im);
	}

}
