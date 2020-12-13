package me.bscal.cmd;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.StringArgument;
import me.bscal.WeaponConditions;
import me.bscal.conditions.Condition;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Commands
{

	public static void RegisterCommands()
	{
		if (CommandAPI.canRegister())
		{
			new CommandAPICommand("itemconditions").withAliases("itemc", "conditions")
					.withPermission("itemc.default")
					.withSubcommand(new CommandAPICommand("add"))
					.withPermission("itemc.admin")
					.withArguments(new StringArgument("condition"))
					.executesPlayer((sender, args) -> {
						ItemStack hand = sender.getInventory()
								.getItemInMainHand();
						if (hand.getType() == Material.AIR)
							return;

						Condition cond = WeaponConditions.Get()
								.GetItemManager()
								.GetCondition((String) args[0]);

						if (cond == null)
							return;

						WeaponConditions.Get()
								.GetItemManager()
								.AddCondition(hand, cond);
						WeaponConditions.Get()
								.GetItemManager()
								.UpdateItem(hand);
					})
					.withSubcommand(new CommandAPICommand("remove"))
					.withPermission("itemc.admin")
					.withArguments(new StringArgument("condition"))
					.executesPlayer((sender, args) -> {
						ItemStack hand = sender.getInventory()
								.getItemInMainHand();
						if (hand.getType() == Material.AIR)
							return;

						Condition cond = WeaponConditions.Get()
								.GetItemManager()
								.GetCondition((String) args[0]);

						if (cond == null)
							return;

						WeaponConditions.Get()
								.GetItemManager()
								.RemoveCondition(hand, cond);
						WeaponConditions.Get()
								.GetItemManager()
								.UpdateItem(hand);
					});
		}
	}

}
