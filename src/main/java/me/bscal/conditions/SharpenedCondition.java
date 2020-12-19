package me.bscal.conditions;

import me.bscal.items.ItemConstants;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class SharpenedCondition extends Condition
{

	private static final String NAME = "Sharpened";
	private static final double BONUS_DMG = 4.0;
	private static final AttributeModifier MODIFIER = new AttributeModifier(UUID.randomUUID(), NAME,
			BONUS_DMG, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);

	public SharpenedCondition()
	{
		super(NAME, GOOD, ItemConstants.WEAPONS);
	}

	@Override
	public void Apply(ItemStack item)
	{
		ItemMeta im = item.getItemMeta();

		im.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, MODIFIER);

		item.setItemMeta(im);
	}

	@Override
	public void Remove(ItemStack item)
	{
		ItemMeta im = item.getItemMeta();

		im.removeAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, MODIFIER);

		item.setItemMeta(im);
	}

	@Override
	public void Update(ItemStack item)
	{
	}
}
