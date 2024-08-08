package me.hapyl.mmu3.command;

import me.hapyl.mmu3.message.Message;
import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.command.SimplePlayerAdminCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class RawCommand extends SimplePlayerAdminCommand {
    private final String titleAndSubtitleSplitChar = ";";

    public RawCommand(String name) {
        super(name);
        setDescription("Sends a string formatted with colors.");
    }

    @Override
    protected void execute(Player player, String[] args) {
        if (args.length < 1) {
            Message.NOT_ENOUGH_ARGUMENTS_EXPECTED_AT_LEAST.send(player, 1);
            return;
        }

        final Type type = getArgument(args, 0).toEnum(Type.class);

        if (type == null) {
            Chat.sendMessage(player, Chat.arrayToString(args, 0));
            return;
        }

        final String output = Chat.arrayToString(args, 1);

        switch (type) {
            case CHAT -> Chat.sendMessage(player, output);
            case TITLE -> Chat.sendTitle(player, output, "", 1, 30, 1);
            case SUBTITLE -> Chat.sendTitle(player, "", output, 1, 30, 1);
            case ACTIONBAR -> Chat.sendActionbar(player, output);
            case TITLE_AND_SUBTITLE -> {
                final String[] split = output.split(titleAndSubtitleSplitChar);

                if (split.length != 2) {
                    Message.error(player, "This requires a string to be separated by a '%s'!", titleAndSubtitleSplitChar);
                    Message.error(player, "Example: /raw title_and_subtitle Title goes here; Subtitle goes here");
                    return;
                }

                final String title = split[0].trim();
                final String subtitle = split[1].trim();

                Chat.sendTitle(player, title, subtitle, 1, 30, 1);
            }
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
