package me.bscal.conditions;

import me.bscal.WeaponConditions;
import me.bscal.items.ItemConstants;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.text.SimpleDateFormat;
import java.time.Duration;

public class OiledCondition extends Condition
{

	protected static final NamespacedKey KEY = new NamespacedKey(WeaponConditions.Get(), "Oiled");
	protected static final PersistentDataType TYPE = PersistentDataType.LONG;
	protected static final float DURATION = 60.0f * 1000.0f;
	protected static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("hh:mm:ss");

	public OiledCondition()
	{
		super("Oiled", GOOD, ItemConstants.SWORDS);
	}

	@Override
	public void Apply(ItemStack item)
	{
		ItemMeta im = item.getItemMeta();
		im.getPersistentDataContainer().set(KEY, TYPE, System.currentTimeMillis());
		item.setItemMeta(im);
	}

	@Override
	public void Remove(ItemStack item)
	{
		ItemMeta im = item.getItemMeta();
		PersistentDataContainer pdc = im.getPersistentDataContainer();
		if (pdc.has(KEY, TYPE))
		{
			pdc.remove(KEY);
			item.setItemMeta(im);
		}
	}

	@Override
	public void Update(ItemStack item)
	{
		ItemMeta im = item.getItemMeta();
		PersistentDataContainer pdc = im.getPersistentDataContainer();
		if (pdc.has(KEY, TYPE))
		{
			long start = (long) pdc.get(KEY, TYPE);
			long diff = start + (long) DURATION - System.currentTimeMillis();
			WeaponConditions.Logger.Log(diff);
			if (diff < 0)
			{
				WeaponConditions.Get().GetItemManager().RemoveKeywordFromItem(item, this);
				return;
			}
			im.setLore(WeaponConditions.Get()
					.GetItemManager()
					.SetLoreVariable(im.getLore(), name,
							FormattedTimeRemaining(Duration.ofMillis(diff))));
			item.setItemMeta(im);
		}
	}

	public static String FormattedTimeRemaining(Duration dur)
	{
		String str;
		if (dur.toDaysPart() > 0)
			str = dur.toDaysPart() + " Day(s)";
		else if (dur.toHoursPart() > 0)
			str = dur.toHoursPart() + " Hour(s)";
		else if (dur.toMinutesPart() > 0)
			str = dur.toMinutesPart() + " Minute(s)";
		else
			str = dur.toSecondsPart() + " Seconds(s)";
		return "Duration: " + str;
	}

}
