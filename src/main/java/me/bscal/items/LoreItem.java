package me.bscal.items;

import org.bukkit.inventory.ItemStack;

public interface LoreItem
{

	public abstract void Apply(ItemStack item);

	public abstract void Remove(ItemStack item);

	public abstract void ApplyPersistentData(ItemStack item);

	public abstract void RemovePersistenData(ItemStack item);

	public abstract void Update(ItemStack item);

}
