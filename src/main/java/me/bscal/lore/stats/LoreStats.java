package me.bscal.lore.stats;

import me.bscal.WeaponConditions;
import me.bscal.lore.LoreManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoreStats extends LoreManager<Stat>
{

	private final static int SPLIT_OFFSET = 1;

	public void RegisterStat(Stat stat)
	{
		m_keywords.put(stat.name, stat);
	}

	public void RegisterStatEvent(Stat stat, Plugin plugin)
	{
		m_keywords.put(stat.name, stat);
		Bukkit.getPluginManager().registerEvents(stat, plugin);
	}

	public Stat GetStat(String name)
	{
		return m_keywords.get(name);
	}

	public StatContainer ContainerFromLore(LoreManager.LoreLookupStat line)
	{
		if (!line.contains)
			return null;

		return new StatContainer(line.prefix, line.value, Operation.ADD, m_keywords.get(line.name));
	}

	public Map<String, Float> GetAllStats(LivingEntity ent)
	{
		Map<String, Float> map = new HashMap<>();
		EntityEquipment equip = ent.getEquipment();

		for (ItemStack piece : equip.getArmorContents())
		{
			if (piece.getType().isEmpty() || !piece.hasItemMeta() || !piece.getItemMeta().hasLore())
				continue;

			List<StatContainer> stats = FindStats(piece.getItemMeta().getLore());

			for (StatContainer s : stats)
			{
				float f = map.getOrDefault(s.parent.name, 0f);
				map.put(s.parent.name, f + s.value);
				// TODO handle operations
			}
		}
		return map;
	}

	public Map<String, Float> GetAllOfStat(LivingEntity ent, String stat)
	{
		Map<String, Float> map = new HashMap<>();
		EntityEquipment equip = ent.getEquipment();

		for (ItemStack piece : equip.getArmorContents())
		{
			if (piece.getType().isEmpty() || !piece.hasItemMeta() || !piece.getItemMeta().hasLore())
				continue;

			List<StatContainer> stats = FindStats(piece.getItemMeta().getLore());

			for (StatContainer s : stats)
			{
				if (!s.parent.name.equals(stat))
					continue;
				float f = map.getOrDefault(s.parent.name, 0f);
				map.put(s.parent.name, f + s.value);
				// TODO handle operations
			}
		}
		return map;
	}

	public Map<String, Float> GetStatOfItem(ItemStack item)
	{
		Map<String, Float> map = new HashMap<>();

		if (item.getType().isEmpty() || !item.hasItemMeta() || !item.getItemMeta().hasLore())
			return map;

		List<StatContainer> stats = FindStats(item.getItemMeta().getLore());

		for (StatContainer s : stats)
		{
			float f = map.getOrDefault(s.parent.name, 0f);
			map.put(s.parent.name, f + s.value);
			// TODO handle operations
		}
		return map;
	}

	public void AddStatToItem(ItemStack item, StatContainer container)
	{
		ItemMeta im = item.getItemMeta();
		List<String> lore;
		if (im.hasLore())
		{
			lore = item.getLore();
			LoreLookupStat line = FindStat(lore, container.parent.name);

			StatContainer loreStat = ContainerFromLore(line);

			if (container.equals(loreStat))
			{
				float newTotal = 0;

				if (loreStat.operation == Operation.ADD)
					newTotal = loreStat.value + container.value;
				else if (loreStat.operation == Operation.MULTIPLY)
					newTotal = loreStat.value * container.value;
				WeaponConditions.Logger.Log(newTotal, loreStat.value, container.value);
				if (newTotal == 0.0f)
				{
					lore.remove(line.index);
					im.setLore(lore);
					item.setItemMeta(im);
					return;
				}
				loreStat.value = newTotal;
				lore = SetStat(lore, line, container.parent.CreateLine(loreStat));
			}
			else
				lore.add(container.parent.CreateLine(container));
		}
		else
		{
			lore = new ArrayList<>(1);
			lore.add(container.parent.CreateLine(container));
		}
		im.setLore(lore);
		item.setItemMeta(im);
	}

	public void RemoveStatFromItem(ItemStack item, StatContainer container)
	{
		ItemMeta im = item.getItemMeta();
		if (!im.hasLore())
			return;

		List<String> lore = item.getLore();
		LoreLookupStat line = FindStat(lore, container.parent.name);

		if (!line.contains)
			return;

		StatContainer loreStat = ContainerFromLore(line);

		if (container.equals(loreStat))
		{
			lore.remove(line.index);
			im.setLore(lore);
			item.setItemMeta(im);
		}
	}

	/**-
	 * **************
	 * * Lore Stats *
	 * **************
	 */

	public LoreLookupStat FindStat(List<String> lore, String statName)
	{
		for (int i = 0; i < lore.size(); i++)
		{
			String line = ChatColor.stripColor(lore.get(i));
			LoreLookupStat stat = ExtractStat(i, line);
			if (stat != null)
				return stat;
		}
		return new LoreLookupStat(-1, false, Character.MIN_VALUE, 0, statName);
	}

	public List<StatContainer> FindStats(List<String> lore)
	{
		List<StatContainer> list = new ArrayList<>();
		for (int i = 0; i < lore.size(); i++)
		{
			String line = ChatColor.stripColor(lore.get(i));
			LoreLookupStat stat = ExtractStat(i, line);
			if (stat != null)
				list.add(ContainerFromLore((stat)));
		}
		return list;
	}

	public List<String> SetStat(List<String> lore, LoreLookupStat loreStat, String statStr)
	{
		if (loreStat.value == 0)
			lore.remove(loreStat.index);

		lore.set(loreStat.index, statStr);
		return lore;
	}

	public LoreLookupStat ExtractStat(int i, String line)
	{
		if (line.isBlank())
			return null;

		String[] split = line.split(" ");
		if (split.length < 2)
			return null;

		String keyword = split[1];
		if (m_keywords.containsKey(keyword))
		{
			char c = split[0].charAt(0);
			char prefix = IsPrefix(c) ? c : ' ';
			String valStr = (c == '-' || c == '+') ? split[0] : split[0].substring(1);

			return new LoreLookupStat(i, true, prefix, ParseValue(valStr), keyword);
		}
		return null;
	}

	public static float ParseValue(String valueStr)
	{
		try
		{
			return Float.parseFloat(valueStr);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
}
