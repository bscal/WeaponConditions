package me.bscal.items;

import me.bscal.conditions.Condition;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.PlayerInventory;

public class ItemManager extends LoreManager<Condition> implements Listener
{

	private static final ChatColor LORE_PREFIX_COLOR = ChatColor.GRAY;
	private static final char LORE_PREFIX = '-';
	private static final String LORE_HEADER = ChatColor.GRAY + ">> Conditions <<";
	private static final String LORE_FOOTER = " ";

	public ItemManager()
	{
		super(LORE_HEADER, LORE_FOOTER, LORE_PREFIX, LORE_PREFIX_COLOR);
	}

	public Condition GetCondition(String name)
	{
		return GetKeyword(ChatColor.stripColor(name));
	}

	public void RegisterCondition(Condition cond)
	{
		AddKeyword(cond.name, cond);
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

}
