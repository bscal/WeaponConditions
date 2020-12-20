package me.bscal;

import dev.jorel.commandapi.CommandAPI;
import me.bscal.cmd.Commands;
import me.bscal.conditions.OiledCondition;
import me.bscal.conditions.SharpenedCondition;
import me.bscal.items.ItemManager;
import me.bscal.logcraft.LogCraft;
import me.bscal.logcraft.LogLevel;
import me.bscal.utils.DamageMeter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class WeaponConditions extends JavaPlugin
{

	public static LogCraft Logger;

	private static WeaponConditions m_singleton;

	private ItemManager m_itemManager;

	@Override
	public void onLoad()
	{
		CommandAPI.onLoad(true);
	}

	@Override
	public void onEnable()
	{
		m_singleton = this;
		Logger = new LogCraft(this, LogLevel.DEVELOPER);
		CommandAPI.onEnable(this);
		Commands.RegisterCommands();

		m_itemManager = new ItemManager();

		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(m_itemManager, this);
		pm.registerEvents(new DamageMeter(), this);

		m_itemManager.RegisterCondition(new OiledCondition());
		m_itemManager.RegisterCondition(new SharpenedCondition());

		Logger.Log("Conditions registered: ", m_itemManager.GetKeywords().size());

	}

	public static WeaponConditions Get()
	{
		return m_singleton;
	}

	public ItemManager GetItemManager()
	{
		return m_itemManager;
	}
}
