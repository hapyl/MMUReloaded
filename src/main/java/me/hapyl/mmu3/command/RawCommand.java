package me.hapyl.mmu3.command;

import me.hapyl.mmu3.message.Message;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import me.hapyl.spigotutils.module.util.Validate;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class RawCommand extends SimplePlayerAdminCommand {
    public RawCommand(String name) {
        super(name);
        setDescription("Sends a string formatted with colors.");
    }

    @Override
    protected void execute(Player player, String[] args) {
        // raw (string...)
        // raw [type] (string...)
        if (args.length < 1) {
            Message.NOT_ENOUGH_ARGUMENTS_EXPECTED_AT_LEAST.send(player, 1);
            return;
        }

        Message.info(player, "Output:");
        final Type type = Validate.getEnumValue(Type.class, args[0]);

        if (type == null) {
            Chat.sendMessage(player, Chat.arrayToString(args, 0));
            return;
        }

        final String output = Chat.arrayToString(args, 1);
        switch (type) {
            case CHAT -> Chat.sendMessage(player, output);
            case TITLE -> Chat.sendTitle(player, output, "", 1, 30, 1);
            case SUBTITLE -> Chat.sendTitle(player, "", output, 1, 30, 1);
            case TITLE_AND_SUBTITLE -> Chat.sendTitle(player, output, output, 1, 30, 1);
            case ACTIONBAR -> Chat.sendActionbar(player, output);
        }

    }

    @Override
    protected List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return completerSort(Type.values(), args);
        }

        return null;
    }

    private enum Type {
        CHAT,
        ACTIONBAR,
        TITLE,
        SUBTITLE,
        TITLE_AND_SUBTITLE
    }

}
