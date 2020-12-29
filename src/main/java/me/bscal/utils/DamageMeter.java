package me.bscal.utils;

import me.bscal.WeaponConditions;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class DamageMeter implements Listener
{

	private static final DecimalFormat NUM_FORMAT = new DecimalFormat("#.00");

	@EventHandler(priority = EventPriority.MONITOR)
	public void OnPlayerDamage(EntityDamageByEntityEvent e)
	{
		if (e.getDamager() instanceof Player)
		{
			Player p = (Player) e.getDamager();
			p.sendMessage(
					MessageFormat.format("{0} You dealt {1}{2}{0} damage. Before reductions: {3}",
							ChatColor.GRAY, ChatColor.GREEN, NUM_FORMAT.format(e.getFinalDamage()),
							NUM_FORMAT.format(e.getDamage())));
		}
	}

	public class DamageMeterEntity
	{
		public final Entity ent;
		public final List<DamageMeterEntry> done = new LinkedList<>();
		public final List<DamageMeterEntry> taken = new LinkedList<>();

		public double totalDamage;
		public int duration;

		private final long m_timer;
		private final BukkitTask m_runnable;

		public DamageMeterEntity(Entity ent, long combatDuration)
		{
			this.ent = ent;
			this.m_timer = System.currentTimeMillis() + combatDuration;

			m_runnable = new BukkitRunnable()
			{
				@Override
				public void run()
				{
					if (IsOutOfCombat())
						m_runnable.cancel();
					duration++;
				}
			}.runTaskTimer(WeaponConditions.Get(), 0, 20);
		}

		public double GetDPS()
		{
			return totalDamage / duration;
		}

		public boolean IsOutOfCombat()
		{
			return m_timer < System.currentTimeMillis();
		}

		public void PushEntry(DamageMeterEntry entry)
		{
			if (entry.damager != null && entry.damager == ent)
				done.add(entry);
			else
				taken.add(entry);
		}
	}

	public enum DamageType
	{
		PURE,
		PHYSICAL,
		PHYSICAL_PIERCING,
		PHYSICAL_FIRE,
		MAGIC,
		MAGIC_FIRE,
		MAGIC_FROST,
		MAGIC_NATURE,
		MAGIC_VOID
	}

	protected class DamageMeterEntry
	{
		public Entity damager;
		public Entity damagee;
		public double damage;
		public EntityDamageEvent.DamageCause cause;
		public DamageType type;
		public long time;
	}

}
