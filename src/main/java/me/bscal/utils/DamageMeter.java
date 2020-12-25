package me.bscal.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.text.DecimalFormat;
import java.text.MessageFormat;

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

}
