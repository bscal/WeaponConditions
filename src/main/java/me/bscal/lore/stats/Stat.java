package me.bscal.stats;

import me.bscal.items.LoreItem;
import me.bscal.items.LoreLine;
import me.bscal.items.LoreManager;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Objects;

public class Stat implements LoreItem
{

	private static final DecimalFormat fFormat = new DecimalFormat("#.00");

	public final char prefix;
	public final String name;
	public final float value;

	public Operation operation;
	public ValueType type;
	public ChatColor prefixColor;
	public ChatColor nameColor;

	public Stat(char prefix, String name, float value)
	{
		this.name = name;
		this.prefix = prefix;
		this.value = value;
	}

	public Stat(char prefix, String name, float value, Operation operation, ValueType type,
			ChatColor prefixColor, ChatColor nameColor)
	{
		this.prefix = prefix;
		this.name = name;
		this.value = value;
		this.operation = operation;
		this.type = type;
		this.prefixColor = prefixColor;
		this.nameColor = nameColor;
	}

	public static Stat FromLore(LoreManager.LoreLookupStat line)
	{
		if (!line.contains)
			return null;

		return new Stat(line.prefix, line.name, line.value);
	}

	public String CreateLine(float newVal)
	{
		return MessageFormat.format("{3}{0}{1} {4}{2}",
				(prefix == Character.MIN_VALUE) ? "" : prefix + " ", fFormat.format(newVal), name, prefixColor,
				nameColor);
	}

	@Override
	public void Apply(ItemStack item)
	{
	}

	@Override
	public void Remove(ItemStack item)
	{
	}

	@Override
	public void Update(ItemStack item)
	{
	}

	@Override
	public String GetName()
	{
		return name;
	}

	@Override
	public String GetLocalizedName()
	{
		return MessageFormat.format("{3}{0}{1} {4}{2}",
				(prefix == Character.MIN_VALUE) ? "" : prefix + " ", fFormat.format(value), name, prefixColor,
				nameColor);
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Stat stat = (Stat) o;
		return Objects.equals(name, stat.name);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(name);
	}
}
