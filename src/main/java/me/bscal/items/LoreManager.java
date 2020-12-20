package me.bscal.items;

import me.bscal.WeaponConditions;
import me.bscal.logcraft.LogLevel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class LoreManager<T extends LoreItem>
{

	protected String m_header;
	protected String m_footer;
	protected String m_prefix;
	protected String m_splitStr;
	protected int m_splitOffset;

	protected final Map<String, T> m_keywords;

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
			m_keywords.get(key).Update(item);
		});
	}

	public void UpdateInv(Inventory inv)
	{
		inv.forEach(this::UpdateItem);
	}

	public void AddKeywordToItem(ItemStack item, LoreItem loreItem)
	{
		ItemMeta im = item.getItemMeta();

		List<String> lore;
		if (im.hasLore())
		{
			lore = im.getLore();
			LoreLookupData data = FindFirstLine(lore);
			PushLineToLore(lore, loreItem.GetLocalizedName(), data.index, data.contains);
		}
		else
		{
			lore = new ArrayList<>(3);
			PushLineToLore(lore, loreItem.GetLocalizedName(), 0, false);

		}
		im.setLore(lore);
		item.setItemMeta(im);

		loreItem.Apply(item);

		UpdateItem(item);

		if (WeaponConditions.Logger.IsLevel(LogLevel.DEVELOPER))
			WeaponConditions.Logger.Log("Adding Condition to item!");
	}

	public void RemoveKeywordFromItem(ItemStack item, LoreItem loreItem)
	{
		ItemMeta im = item.getItemMeta();

		if (im.hasLore())
		{
			List<String> lore = im.getLore();
			LoreLookupData data = Find(lore, loreItem.GetName());
			WeaponConditions.Logger.Log(data);
			if (data.contains)
			{
				lore.remove(data.index);
				im.setLore(lore);
				item.setItemMeta(im);
				loreItem.Remove(item);
			}
		}

		UpdateItem(item);

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
			if (line.equals(m_footer) || line.isBlank())
				break;

			line = ChatColor.stripColor(lore.get(i));

			// Makes sures that any ChatColor charaters are not blocking
			//if (!line.startsWith(m_prefix))
			//continue;
			//WeaponConditions.Logger.Log(line.charAt(0));

			if (ExtractKeyword(line).equals(key))
				return new LoreLookupData(i, true);
		}
		return new LoreLookupData(-1, false);
	}

	protected List<String> GetKeywords(List<String> lore)
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
			if (line.equals(m_footer) || line.isBlank())
				break;

			line = ChatColor.stripColor(lore.get(i));

			// Makes sures that any ChatColor charaters are not blocking
			//if (!line.startsWith(m_prefix))
			//continue;
			//WeaponConditions.Logger.Log(line.charAt(0));

			String key = ExtractKeyword(line);
			if (m_keywords.containsKey(key))
				list.add(key);
		}
		return list;
	}

	protected List<String> GetKeywordLines(List<String> lore)
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
			if (line.equals(m_footer) || line.isBlank())
				break;

			line = ChatColor.stripColor(lore.get(i));

			// Makes sures that any ChatColor charaters are not blocking
			//if (!line.startsWith(m_prefix))
			//continue;
			//WeaponConditions.Logger.Log(line.charAt(0));

			String key = ExtractKeyword(line);
			if (m_keywords.containsKey(key))
				list.add(line);
		}
		return list;
	}

	protected void RemoveAllKeywords(List<String> lore, boolean removeHeaders)
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
	protected void PushLineToLore(List<String> lore, String line, int index, boolean hasHeaders)
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

	public List<String> SetLoreVariable(List<String> lore, String keyword, String var)
	{
		int i = Find(lore, keyword).index;
		String[] split = lore.get(i).split("\\[");
		lore.set(i, MessageFormat.format("{0}{1}[{2}]", split[0], split[0].endsWith(" ") ? "" : " ",
				var));
		return lore;
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

	public T GetKeyword(String key)
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

	protected static class LoreLookupData
	{
		final int index;
		final boolean contains;

		public LoreLookupData(int i, boolean contains)
		{
			this.index = i;
			this.contains = contains;
		}

		@Override
		public String toString()
		{
			return "LoreLookupData{" + "index=" + index + ", contains=" + contains + '}';
		}
	}

	protected static class LoreLookupLine extends LoreLookupData
	{

		final String line;

		public LoreLookupLine(int i, boolean contains, String line)
		{
			super(i, contains);
			this.line = line;
		}
	}

	public static class LoreVariable
	{
		final String keyword;
		final int index;
		final String var;
		final Object value;
		final boolean isSet;

		public LoreVariable(String keyword, int index, String var, Object value, boolean isSet)
		{
			this.keyword = keyword;
			this.index = index;
			this.var = var;
			this.value = value;
			this.isSet = isSet;
		}
	}

}
