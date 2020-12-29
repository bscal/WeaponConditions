package me.bscal.lore.stats.statspack.base;

import me.bscal.WeaponConditions;
import me.bscal.lore.ColorConstants;
import me.bscal.lore.stats.Stat;
import me.bscal.lore.stats.ValueType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Map;

public class PArmorStat extends Stat
{

	public static final String NAME = "Physical Armor";

	public PArmorStat()
	{
		super(NAME, ColorConstants.GOOD, ChatColor.WHITE, ValueType.FLAT);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void OnDamageTaken(EntityDamageByEntityEvent e)
	{
		if (e.getEntity() instanceof Player && e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK)
		{
			Player damagee = (Player) e.getEntity();

			Map<String, Float> stats = WeaponConditions.Get().GetLoreStats().GetAllStats(damagee);

			e.setDamage(e.getDamage() - stats.get(NAME));
		}
	}
}
