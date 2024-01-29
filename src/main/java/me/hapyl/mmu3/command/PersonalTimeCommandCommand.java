package me.hapyl.mmu3.command;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.message.Message;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PersonalTimeCommandCommand extends SimplePlayerAdminCommand {

    private final String[] validArguments = { "reset", "add", "set", "remove", "pause" };
    private final Map<String, Long> linkedTimeMap;

    public PersonalTimeCommandCommand(String name) {
        super(name);
        setDescription("Allows to modify player's personal time.");
        setAliases("ptime");
        linkedTimeMap = Maps.newHashMap();
        linkedTimeMap.put("day", 1000L);
        linkedTimeMap.put("midnight", 18000L);
        linkedTimeMap.put("night", 13000L);
        linkedTimeMap.put("noon", 6000L);
    }

    @Override
    protected void execute(Player player, String[] args) {
        if (args.length == 0) {
            Message.info(
                    player,
                    "Your personal time is %s. &7(%s&7)",
                    player.getPlayerTime(),
                    player.isPlayerTimeRelative() ? "&a&lRELATIVE" : "&c&lABSOLUTE"
            );
            return;
        }

        final String argument = args[0].toLowerCase(Locale.ROOT);
        final long time = args.length >= 2 ? getTime(args[1].toLowerCase()) : 0L;

        switch (argument) {
            case "reset" -> {
                player.resetPlayerTime();
                Message.info(player, "Reset your personal time.");
            }

            case "add" -> {
                player.setPlayerTime(player.getPlayerTime() + time, player.isPlayerTimeRelative());
                Message.info(player, "Added %s to your personal time, now %s.", time, player.getPlayerTime());
            }

            case "remove" -> {
                player.setPlayerTime(player.getPlayerTime() - time, player.isPlayerTimeRelative());
                Message.info(player, "Subtracted %s from your personal time, now %s.", time, player.getPlayerTime());
            }

            case "set" -> {
                player.setPlayerTime(time, player.isPlayerTimeRelative());
                Message.info(player, "Set your personal time to %s.", time);
            }

            case "pause" -> {
                player.setPlayerTime(player.getPlayerTime(), !player.isPlayerTimeRelative());
                Message.info(player, "Your personal time is now %s.", player.isPlayerTimeRelative() ? "unpause" : "paused");
            }

        }

    }


    @Override
    protected List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return completerSort(validArguments, args);
        }
        else if (args.length == 2) {
            return completerSort(linkedTimeMap.keySet(), args);
        }
        return Collections.emptyList();
    }

    private long getTime(String arg) {
        if (linkedTimeMap.containsKey(arg)) {
            return linkedTimeMap.get(arg);
        }
        return NumberConversions.toLong(arg);
    }
}
