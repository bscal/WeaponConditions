package me.bscal.items;

import me.bscal.WeaponConditions;
import me.bscal.conditions.Condition;
import me.bscal.logcraft.LogLevel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoreManager<T>
{

	private String m_header;
	private String m_footer;
	private String m_prefix;
	private String m_splitStr;
	private int m_splitOffset;

	private final Map<String, T> m_keywords;

	public LoreManager()
	{
		this("", "", "");
	}

	public LoreManager(String header, String footer)
	{
		this(header, footer, "");
	}

	public LoreManager(String header, String footer, String prefix)
	{
		m_header = header;
		m_footer = footer;
		if (!prefix.isBlank())
		{
			m_prefix = prefix;
			m_splitStr = " ";
			m_splitOffset = 1;
		}
		m_keywords = new HashMap<>();
	}

	public void UpdateItem(ItemStack item)
	{
		if (item.getType() == Material.AIR || !item.hasItemMeta() || !item.getItemMeta().hasLore())
			return;

		GetKeywords(item.getLore()).forEach((key) -> {
			T val = m_keywords.get(key);
			if (val instanceof LoreItem)
				((LoreItem) val).Update(item);
		});
	}

	public void UpdateInv(Inventory inv)
	{
		inv.forEach(this::UpdateItem);
	}

	public void AddCondition(ItemStack item, Condition cond)
	{
		ItemMeta im = item.getItemMeta();

		List<String> lore;
		if (im.hasLore())
		{
			lore = im.getLore();
			LoreLookupData data = FindFirstLine(lore);
			PushLineToLore(lore, cond.GetLocalizedName(), data.index, data.contains);
		}
		else
		{
			lore = new ArrayList<>(3);
			PushLineToLore(lore, cond.GetLocalizedName(), 0, false);

		}
		im.setLore(lore);
		item.setItemMeta(im);
		//UpdateItem(item);

		if (WeaponConditions.Logger.IsLevel(LogLevel.DEVELOPER))
			WeaponConditions.Logger.Log("Adding Condition to item!");
	}

	public void RemoveCondition(ItemStack item, String key)
	{
		ItemMeta im = item.getItemMeta();

		if (im.hasLore())
		{
			List<String> lore = im.getLore();
			LoreLookupData data = Find(lore, key);
			if (data.contains)
			{
				lore.remove(data.index);
				im.setLore(lore);
				item.setItemMeta(im);
			}
		}

		if (WeaponConditions.Logger.IsLevel(LogLevel.DEVELOPER))
			WeaponConditions.Logger.Log("Removing Condition to item!");
	}

	public void RemoveAllConditions(ItemStack item, boolean removeHeaders)
	{
		ItemMeta im = item.getItemMeta();

		if (im.hasLore())
			RemoveAllKeywords(im.getLore(), removeHeaders);
	}

	public boolean HasAnyCondition(ItemStack item)
	{
		ItemMeta im = item.getItemMeta();

		if (im.hasLore())
		{
			LoreLookupData data = FindFirstLine(im.getLore());
			return data.contains && m_keywords.containsKey(
					ExtractKeyword(im.getLore().get(data.index)));
		}
		return false;
	}

	public boolean HasCondition(ItemStack item, String key)
	{
		ItemMeta im = item.getItemMeta();
		return im.hasLore() && Find(im.getLore(), key).contains;
	}

	public LoreLookupData FindFirstLine(List<String> lore)
	{
		if (lore.size() < 2)
			return new LoreLookupData(lore.size(), false);

		for (int i = 0; i < lore.size(); i++)
		{
			if (lore.get(i).equals(m_header))
				return new LoreLookupData(i + 1, true);
		}
		return new LoreLookupData(lore.size(), false);
	}

	/**
	 * Finds a keyword in lore.
	 *
	 * @param lore - Lore list
	 * @param key  - Key to check for
	 * @return If true LoreLookupData's index will equal line index, contains will equal true
	 */
	public LoreLookupData Find(List<String> lore, String key)
	{
		boolean has = false;
		for (int i = 0; i < lore.size(); i++)
		{
			String line = lore.get(i);

			if (line.equals(m_header))
				has = true;
			if (!has)
				continue;
			else if (line.equals(m_footer))
				break;

			line = ChatColor.stripColor(lore.get(i));

			// Makes sures that any ChatColor charaters are not blocking
			if (!line.startsWith(m_prefix))
				break;

			if (ExtractKeyword(line).equals(key))
				return new LoreLookupData(i, true);
		}
		return new LoreLookupData(-1, false);
	}

	private List<String> GetKeywords(List<String> lore)
	{
		List<String> list = new ArrayList<>();
		boolean has = false;
		for (int i = 0; i < lore.size(); i++)
		{
			String line = lore.get(i);

			if (line.equals(m_header))
				has = true;
			if (!has)
				continue;
			else if (line.equals(m_footer))
				break;

			line = ChatColor.stripColor(lore.get(i));

			// Makes sures that any ChatColor charaters are not blocking
			if (!line.startsWith(m_prefix))
				break;

			String key = ExtractKeyword(line);
			if (m_keywords.containsKey(key))
				list.add(key);
		}
		return list;
	}

	private void RemoveAllKeywords(List<String> lore, boolean removeHeaders)
	{
		boolean has = false;
		for (int i = lore.size() - 1; i > -1; i--)
		{
			String line = lore.get(i);
			if (!has)
			{
				if (line.equals(m_footer))
				{
					has = true;
					if (removeHeaders)
						lore.remove(i);
				}

				line = ChatColor.stripColor(lore.get(i));
				if (m_keywords.containsKey(line))
					has = true;
				continue;
			}
			lore.remove(i);
			if (line.equals(m_header))
			{
				if (removeHeaders)
					lore.remove(i);
				break;
			}
		}
	}

	/**
	 * Pushes a line to the inserted lore.
	 *
	 * @param lore       - List to add line at index
	 * @param line       - String to add
	 * @param index      - Insert index
	 * @param hasHeaders - Does list have headers. If false will generate headers
	 */
	private void PushLineToLore(List<String> lore, String line, int index, boolean hasHeaders)
	{
		if (!hasHeaders)
		{
			lore.add(index, m_footer);
			lore.add(index, m_prefix + m_splitStr + line);
			lore.add(index, m_header);
			return;
		}
		lore.add(index, m_prefix + m_splitStr + line);
	}

	/**
	 * -
	 * ********************
	 * * Public functions *
	 * ********************
	 */

	public String ExtractKeyword(String line)
	{
		String[] split = line.split(m_splitStr);
		return (split.length > m_splitOffset) ? split[m_splitOffset] : "";
	}

	public boolean ContainsKey(String key)
	{
		return m_keywords.containsKey(key);
	}

	public void AddKeyword(String key, T obj)
	{
		m_keywords.put(key, obj);
	}

	public Object GetKeyword(String key)
	{
		return m_keywords.get(key);
	}

	public Map<String, T> GetKeywords()
	{
		return m_keywords;
	}

	public String getHeader()
	{
		return m_header;
	}

	public void setHeader(String m_header)
	{
		this.m_header = m_header;
	}

	public String getFooter()
	{
		return m_footer;
	}

	public void setFooter(String m_footer)
	{
		this.m_footer = m_footer;
	}

	public String getPrefix()
	{
		return m_prefix;
	}

	public void setPrefix(String m_prefix)
	{
		this.m_prefix = m_prefix;
	}

	public String getSplitStr()
	{
		return m_splitStr;
	}

	public void setSplitStr(String m_splitStr)
	{
		this.m_splitStr = m_splitStr;
	}

	public int getSplitOffset()
	{
		return m_splitOffset;
	}

	public void setSplitOffset(int m_splitOffset)
	{
		this.m_splitOffset = m_splitOffset;
	}

	private static class LoreLookupData
	{
		final int index;
		final boolean contains;

		public LoreLookupData(int i, boolean contains)
		{
			this.index = i;
			this.contains = contains;
		}
	}

}
