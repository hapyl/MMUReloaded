package me.hapyl.mmu3.command;

import me.hapyl.mmu3.message.Message;
import me.hapyl.mmu3.outcast.fishing.Fish;
import me.hapyl.mmu3.outcast.fishing.FishData;
import me.hapyl.mmu3.outcast.fishing.FishType;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import me.hapyl.spigotutils.module.util.Validate;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FishingCommand extends SimplePlayerAdminCommand {
    public FishingCommand(String name) {
        super(name);
        addCompleterValues(1, "get");
        addCompleterValues(2, arrayToList(FishType.values()));
    }

    @Override
    protected void execute(Player player, String[] args) {
        // fish (Operation) (Value)
        if (args.length < 1) {
            return;
        }

        final String arg0 = args[0].toLowerCase();

        switch (arg0) {
            case "get" -> {
                if (args.length < 2) {
                    Message.NOT_ENOUGH_ARGUMENTS_EXPECTED.send(player, 2);
                    return;
                }

                final FishType type = Validate.getEnumValue(FishType.class, args[1]);
                if (type == null) {
                    Message.error(player, "Invalid fish type.");
                    return;
                }

                final Fish fish = type.getFish();
                final FishData fishData = new FishData(player);

                if (args.length >= 3) {
                    fishData.setOverrideSize(Validate.getInt(args[2]));
                }

                final ItemStack item = fish.getItem(fishData);
                player.getInventory().addItem(item);

                Message.info(player, "Gave %s to you.", fish.getName());
                Message.info(player, "Fish properties: %s", fish.getProperties());
            }
        }
    }
}
