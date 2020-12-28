package me.bscal.conditions;

import me.bscal.WeaponConditions;
import me.bscal.items.ItemConstants;
import me.bscal.lore.ColorConstants;
import me.bscal.lore.stats.Operation;
import me.bscal.lore.stats.Stat;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class SharpenedCondition extends Condition
{

	private static final String NAME = "Sharpened";
	private static final float BONUS_DMG = 4.0f;
	private static final AttributeModifier MODIFIER = new AttributeModifier(UUID.randomUUID(), NAME,
			BONUS_DMG, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);

	private final WeaponConditions m_plugin;
	private final Stat m_damageStat;

	public SharpenedCondition()
	{
		super(NAME, ColorConstants.GOOD, (ItemStack[]) ItemConstants.WEAPONS);
		m_plugin = WeaponConditions.Get();
		m_damageStat = m_plugin.GetLoreStats().GetStat("Damage");
	}

	@Override
	public void Apply(ItemStack item)
	{
		//ItemUtils.AddAttribute(item, Attribute.GENERIC_ATTACK_DAMAGE, MODIFIER, true);
		m_plugin.GetLoreStats()
				.AddStatToItem(item, m_damageStat.CreateContainer(BONUS_DMG, Operation.ADD));
	}

	@Override
	public void Remove(ItemStack item)
	{
		m_plugin.GetLoreStats()
				.RemoveStatFromItem(item, m_damageStat.CreateContainer(-BONUS_DMG, Operation.ADD));
		//ItemMeta im = item.getItemMeta();
		//im.removeAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, MODIFIER);
		//item.setItemMeta(im);
	}

	@Override
	public void Update(ItemStack item)
	{
	}
}
