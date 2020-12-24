package me.bscal.cmd;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.FloatArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import me.bscal.WeaponConditions;
import me.bscal.lore.stats.Operation;
import me.bscal.lore.stats.Stat;
import me.bscal.lore.stats.StatContainer;
import org.bukkit.inventory.ItemStack;

public class StatCommands
{

	public static void RegisterStatCommand()
	{
		new CommandAPICommand("itemstat")
				.withPermission("itemc.admin")
				.withSubcommand(
						new CommandAPICommand("add")
								.withArguments(new StringArgument("stat"))
								.withArguments(new FloatArgument("amount"))
								.executesPlayer((sender, args)-> {
									ItemStack main = sender.getInventory().getItemInMainHand();

									if (main.getType().isEmpty())
										return;

									Stat stat = WeaponConditions.Get().GetLoreStats().GetStat(
											(String) args[0]);
									WeaponConditions.Logger.Log(stat == null);
									if (stat == null)
										return;

									StatContainer container = stat.CreateContainer('+',
											(Float) args[1], Operation.ADD);
									WeaponConditions.Logger.Log(container);
									WeaponConditions.Get().GetLoreStats().AddStatToItem(main, container);

								}))
				.withSubcommand(
						new CommandAPICommand("remove")
								.withArguments(new StringArgument("stat"))
								.executesPlayer((sender, args) -> {
									ItemStack main = sender.getInventory().getItemInMainHand();

									if (main.getType().isEmpty())
										return;

									Stat stat = WeaponConditions.Get().GetLoreStats().GetStat("Damage");

									if (stat == null)
										return;

									StatContainer container = stat.CreateContainer('+', 5, Operation.ADD);
									WeaponConditions.Get().GetLoreStats().RemoveStatFromItem(main, container);
								})
				)
				.register();
	}

}
