package me.bscal.lore.stats.statspack.base;

import me.bscal.WeaponConditions;
import me.bscal.lore.stats.Stat;
import me.bscal.lore.stats.ValueType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Map;

public class DamageStat extends Stat
{

	public DamageStat()
	{
		super("Damage", ChatColor.GREEN, ChatColor.BLUE, ValueType.FLAT);
	}

	@EventHandler
	public void OnDamageDone(EntityDamageByEntityEvent e)
	{
		if (e.getDamager() instanceof Player && e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK)
		{
			Player damager = (Player) e.getDamager();

			Map<String, Float> stats = WeaponConditions.Get().GetLoreStats().GetAllStats(damager);

			e.setDamage(e.getDamage() + stats.get("Damage"));
		}
	}
}
