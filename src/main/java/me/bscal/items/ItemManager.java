package me.bscal.items;

import me.bscal.WeaponConditions;
import me.bscal.conditions.Condition;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemManager implements Listener
{

	private static final long UPDATE_PERIOD = 20 * 60;
	private static final String LORE_HEADER = ChatColor.GRAY + "+---- Conditions ----+";
	private static final String LORE_FOOTER = ChatColor.GRAY + "+--------------------+";

	private Map<String, Condition> m_conditions = new HashMap<>();

	public static final NamespacedKey OILED_KEY = new NamespacedKey(WeaponConditions.Get(),
			"Oiled");
	public static final NamespacedKey SHARPENED_KEY = new NamespacedKey(WeaponConditions.Get(),
			"Sharpened");

	public void RegisterCondition(Condition cond)
	{
		m_conditions.put(cond.GetLocalizedName(), cond);
	}

	public Condition GetCondition(String name)
	{
		return m_conditions.get(name);
	}

	public void AddCondition(ItemStack item, Condition cond)
	{
		ItemMeta im = item.getItemMeta();

		if (im.hasLore())
		{
			LoreLookupData data = FindNextConditionLine(im.getLore());
			PushLineToLore(im.getLore(), cond.GetLocalizedName(), data.index, data.contains);
		}
		else
		{
			List<String> lore = new ArrayList<>(3);
			PushLineToLore(lore, cond.GetLocalizedName(), 0, true);
			im.setLore(lore);
		}
		item.setItemMeta(im);

		UpdateItem(item);
	}

	public void RemoveCondition(ItemStack item, Condition cond)
	{
		ItemMeta im = item.getItemMeta();

		if (im.hasLore())
		{
			LoreLookupData data = FindCondition(im.getLore(), cond);
			if (data.contains)
				im.getLore().remove(data.index);
		}
	}

	public void RemoveAllConditions(ItemStack item)
	{
		ItemMeta im = item.getItemMeta();

		if (im.hasLore())
			RemoveConditions(im.getLore());
	}

	public boolean HasAnyCondition(ItemStack item)
	{
		ItemMeta im = item.getItemMeta();

		if (im.hasLore())
		{
			LoreLookupData data = FindNextConditionLine(im.getLore());
			return (data.contains && !im.getLore().get(data.index).equals(LORE_FOOTER));
		}
		return false;
	}

	public boolean HasCondition(ItemStack item, Condition cond)
	{
		ItemMeta im = item.getItemMeta();
		return im.hasLore() && FindCondition(im.getLore(), cond).contains;
	}

	/**
	 * -
	 * *************************************************
	 * * Events to check state of ItemStack conditions *
	 * *************************************************
	 */

	@EventHandler
	public void OnOpenInv(InventoryOpenEvent e)
	{
		UpdateInv(e.getPlayer().getInventory());
	}

	@EventHandler
	public void OnItemDrop(PlayerDropItemEvent e)
	{
		UpdateItem(e.getItemDrop().getItemStack());
	}

	@EventHandler
	public void OnItemDamage(PlayerItemDamageEvent e)
	{
		UpdateItem(e.getItem());
	}

	@EventHandler
	public void OnItemHeld(PlayerItemHeldEvent e)
	{
		PlayerInventory inv = e.getPlayer().getInventory();
		UpdateItem(inv.getItemInMainHand());
		UpdateItem(inv.getItemInOffHand());
	}

	public void UpdateItem(ItemStack item)
	{
		if (item.getType() == Material.AIR || !item.hasItemMeta() || !item.getItemMeta().hasLore())
			return;

		LoreLookupData data = FindNextConditionLine(item.getLore());

		if (!data.contains)
			return;

		List<String> lore = item.getLore().subList(data.index, item.getLore().size());
		for (int i = 0; i < lore.size(); i++)
		{
			String line = lore.get(i);
			if (line.equals(LORE_FOOTER))
				break;

			String[] split = line.split(" ");
			Condition cond = m_conditions.get(split[0]);

			if (cond == null)
				continue;

			cond.Update(item);
		}
	}

	public void UpdateInv(Inventory inv)
	{
		inv.forEach(this::UpdateItem);
	}

	private LoreLookupData FindNextConditionLine(List<String> lore)
	{
		if (lore.size() < 2)
			return new LoreLookupData(lore.size(), false);

		for (int i = 0; i < lore.size(); i++)
		{
			if (lore.get(i).equals(LORE_HEADER))
				return new LoreLookupData(i + 1, true);
		}
		return new LoreLookupData(lore.size(), false);
	}

	private void RemoveConditions(List<String> lore)
	{
		boolean found = false;
		for (int i = lore.size() - 1; i > -1; i--)
		{
			String line = lore.get(i);
			if (line.equals(LORE_FOOTER))
				found = true;
			if (found)
			{
				lore.remove(i);
				if (line.equals(LORE_HEADER))
					break;
			}
		}
	}

	private List<Condition> GetConditions(List<String> lore)
	{
		List<Condition> results = new ArrayList<>();
		for (String line : lore)
		{
			if (line.equals(LORE_FOOTER))
				break;

			String[] split = line.split(" ");
			Condition cond = m_conditions.get(split[0]);

			if (cond == null)
				continue;

			results.add(cond);
		}
		return results;
	}

	private LoreLookupData FindCondition(List<String> lore, Condition condToFind)
	{
		for (int i = 0; i < lore.size(); i++)
		{
			String line = lore.get(i);

			if (line.equals(LORE_FOOTER))
				break;

			String[] split = line.split(" ");
			Condition cond = m_conditions.get(split[0]);

			if (cond == null || !cond.equals(condToFind))
				continue;

			return new LoreLookupData(i, true);
		}
		return new LoreLookupData(-1, false);
	}

	private void PushLineToLore(List<String> lore, String line, int index, boolean createHeaders)
	{
		if (createHeaders)
		{
			lore.add(index, LORE_FOOTER);
			lore.add(index, line);
			lore.add(index, LORE_HEADER);
			return;
		}

		lore.add(index, line);
	}

	private static class LoreLookupData
	{
		final int index;
		final boolean contains;

		public LoreLookupData(int i, boolean contains)
		{
			index = i;
			this.contains = contains;
		}
	}

}
