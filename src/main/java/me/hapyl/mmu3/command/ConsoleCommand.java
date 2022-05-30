package me.hapyl.mmu3.command;

import me.hapyl.mmu3.Message;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.command.SimplePlayerCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ConsoleCommand extends SimplePlayerCommand {

    private final UUID[] allowedUUIDs = { UUID.fromString("b58e578c-8e36-4789-af50-1ee7400307c0") };

    public ConsoleCommand(String name) {
        super(name);
        setDescription("Dispatches a set command from the console.");
    }

    @Override
    protected void execute(Player player, String[] args) {
        if (args.length == 0) {
            Message.NOT_ENOUGH_ARGUMENTS_EXPECTED_AT_LEAST.send(player, 1);
            return;
        }

        if (!isAllowedToUse(player.getUniqueId())) {
            Message.NO_PERMISSIONS.send(player);
            return;
        }

        final String command = Chat.arrayToString(args, 0);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        Message.broadcast("%s dispatched '%s' from the console.", player.getName(), command);
    }

    private boolean isAllowedToUse(UUID uuid) {
        for (UUID allowedUUID : allowedUUIDs) {
            if (allowedUUID.equals(uuid)) {
                return true;
            }
        }
        return false;
    }

}
