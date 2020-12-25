package me.bscal.lore.stats.statspack.base;

import me.bscal.WeaponConditions;
import me.bscal.lore.stats.Stat;
import me.bscal.lore.stats.ValueType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageStat extends Stat
{

	public DamageStat()
	{
		super("Damage", ChatColor.GREEN, ChatColor.BLUE, ValueType.FLAT);
	}

	@EventHandler
	public void OnDamageDone(EntityDamageByEntityEvent e)
	{
		WeaponConditions.Logger.Log("RUNNING?", e.getCause());
		if (e.getDamager() instanceof Player && e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK)
		{
			WeaponConditions.Logger.Log("RAN!");
			Player damager = (Player) e.getDamager();
			e.setDamage(e.getDamage() + 15);// TODO bugged not working?
		}
	}
}
