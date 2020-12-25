package me.bscal.damage;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import java.text.MessageFormat;

public class DamageManager implements Listener
{

	@EventHandler
	public void OnPlayerDamage(EntityDamageByEntityEvent e)
	{
		if (e.getDamager() instanceof LivingEntity)
		{
			LivingEntity damager = (LivingEntity) e.getDamager();

			EntityEquipment gear = damager.getEquipment();

			ItemStack main = gear.getItemInMainHand();
			ItemStack off = gear.getItemInOffHand();
			ItemStack[] armor = gear.getArmorContents();

		}
	}
}
