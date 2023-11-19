package me.hapyl.mmu3.command;

import com.google.common.collect.Lists;
import me.hapyl.mmu3.message.Message;
import me.hapyl.mmu3.outcast.backpack.Backpack;
import me.hapyl.mmu3.outcast.backpack.BackpackSize;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.chat.LazyEvent;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import me.hapyl.spigotutils.module.util.Validate;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class BackpackCommand extends SimplePlayerAdminCommand {
    public BackpackCommand(String name) {
        super(name);
        setDescription("Allows opening backpacks without an item.");
        addCompleterValues(1, "open", "create", "delete", "item");
    }

    @Override
    protected void execute(Player player, String[] args) {
        // backpack open (uuid)
        // backpack delete (uuid)
        // backpack create (size)

        if (args.length != 2) {
            Message.NOT_ENOUGH_ARGUMENTS_EXPECTED.send(player, 2);
            return;
        }

        final String firstArg = args[0].toLowerCase();
        switch (firstArg) {
            case "open", "delete", "item" -> {
                final UUID uuid = fetchUuid(args[1]);
                if (uuid == null) {
                    Message.error(player, "&cInvalid uuid format!");
                    return;
                }

                if (!Backpack.isBackpackExists(uuid)) {
                    Message.error(player, "&cThis backpack doesn't exist!");
                    return;
                }

                final Backpack backpack = Backpack.loadFromUUID(uuid);
                if (backpack == null) {
                    Message.error(player, "&cBad backpack behaviour!");
                    return;
                }

                switch (firstArg) {
                    case "open" -> {
                        Message.info(player, "Opening a backpack...");
                        backpack.open(player);
                    }

                    case "item" -> {
                        Message.info(player, "Creating item...");
                        player.getInventory().addItem(backpack.createItem(player));
                    }

                    case "delete" -> {
                        Message.info(player, "Deleting backpack...");
                        backpack.delete(player, true);
                    }
                }
            }

            case "create" -> {
                final BackpackSize size = Validate.getEnumValue(BackpackSize.class, args[1], BackpackSize.SMALL);

                Message.info(player, "Creating %s backpack...", Chat.capitalize(size));

                final Backpack backpack = Backpack.createBackpack(size);
                final UUID uuid = backpack.getUuid();

                Message.success(player, "Created %s backpack!", Chat.capitalize(size));
                Message.clickHover(
                        player,
                        LazyEvent.suggestCommand("/backpack open %s", uuid.toString()),
                        LazyEvent.showText("&eClick to copy UUID!"),
                        "&e&lCLICK HERE &7to copy UUID."
                );

                backpack.open(player);
            }
        }
    }

    @Override
    protected List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 2 && args[1].equalsIgnoreCase("create")) {
            return completerSort(BackpackSize.values(), args);
        }

        return Lists.newArrayList();
    }

    private UUID fetchUuid(String str) {
        try {
            return UUID.fromString(str);
        } catch (Exception e) {
            return null;
        }
    }
}
