package me.bscal.stats;

import me.bscal.items.LoreLine;
import me.bscal.items.LoreManager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
		if (im.hasLore())
		{
			LoreLine line = FindLine(item.getLore(), stat.name, 2);

			Stat loreStat = Stat.LoreLineToStat(line);

			if (loreStat != null && loreStat.equals(stat))
			{
				// TODO add stat
			}
		}
	}
}
