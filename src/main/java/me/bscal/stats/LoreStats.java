package me.bscal.stats;

import me.bscal.items.LoreLine;
import me.bscal.items.LoreManager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class LoreStats extends LoreManager<Stat>
{

	public void RegisterStat(Stat stat)
	{
		m_keywords.put(stat.name, stat);
	}

	public Stat GetStat(String name)
	{
		return m_keywords.get(name);
	}

	public void AddStatToItem(ItemStack item, Stat stat)
	{
		ItemMeta im = item.getItemMeta();
		List<String> lore;
		if (im.hasLore())
		{
			lore = item.getLore();
			LoreLookupStat line = FindStat(lore, stat.name);

			Stat loreStat = Stat.FromLore(line);

			if (loreStat != null && loreStat.equals(stat))
			{
				// TODO add stat

				float total = 0;

				if (stat.operation == Operation.ADD)
					total = loreStat.value + stat.value;
				else if (stat.operation == Operation.MULTIPLY)
					total = loreStat.value * stat.value;

				SetStat(lore, line, loreStat.CreateLine(total));

			}
		}
	}

	public void SetStatLine(LoreLine line)
	{

	}
}
