package me.bscal.cmd;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.LongArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import me.bscal.WeaponConditions;
import me.bscal.conditions.Condition;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class Commands
{

	public static void RegisterCommands()
	{
		if (CommandAPI.canRegister())
		{
			new CommandAPICommand("itemconditions").withAliases("itemc", "conditions")
					.withSubcommand(new CommandAPICommand("add").withPermission("itemc.admin")
							.withArguments(new StringArgument("condition"))
							.executesPlayer((sender, args) -> {
								ItemStack hand = sender.getInventory().getItemInMainHand();
								if (hand.getType() == Material.AIR)
									return;

								Condition cond = WeaponConditions.Get()
										.GetItemManager()
										.GetCondition((String) args[0]);

								if (cond == null)
									return;

								WeaponConditions.Get().GetItemManager().AddKeywordToItem(hand, cond);
							}))
					.withSubcommand(new CommandAPICommand("remove")
					.withPermission("itemc.admin")
					.withArguments(new StringArgument("condition"))
					.executesPlayer((sender, args) -> {
						ItemStack hand = sender.getInventory().getItemInMainHand();
						if (hand.getType() == Material.AIR)
							return;

						Condition cond = WeaponConditions.Get()
								.GetItemManager()
								.GetCondition((String) args[0]);

						if (cond == null)
							return;

						WeaponConditions.Get().GetItemManager().RemoveKeywordFromItem(hand, cond);
					}))
					.withSubcommand(new CommandAPICommand("setdata")
							.withPermission("itemc.admin")
							.withArguments(new StringArgument("condition"))
							.withArguments(new LongArgument("secs"))
							.executesPlayer((sender, args) -> {
								ItemStack hand = sender.getInventory().getItemInMainHand();
								if (hand.getType() == Material.AIR)
									return;

								Condition cond = WeaponConditions.Get()
										.GetItemManager()
										.GetCondition((String) args[0]);

								if (cond == null)
									return;
								final NamespacedKey KEY = new NamespacedKey(WeaponConditions.Get(), "Oiled");
								final PersistentDataType TYPE = PersistentDataType.LONG;

								ItemMeta im = hand.getItemMeta();
								im.getPersistentDataContainer().set(KEY, TYPE, System.currentTimeMillis() + (long) args[1] * 1000L);
								hand.setItemMeta(im);
								WeaponConditions.Get().GetItemManager().UpdateItem(hand);
							}))
					.register();
		}
	}

}
