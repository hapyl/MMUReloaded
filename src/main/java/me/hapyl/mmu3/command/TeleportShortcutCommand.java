package me.hapyl.mmu3.command;

import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TeleportShortcutCommand extends SimplePlayerAdminCommand {
    public TeleportShortcutCommand(String name) {
        super(name);
        setDescription("A shortcut for a teleport command.");
    }

    @Override
    protected void execute(Player player, String[] args) {
        Bukkit.dispatchCommand(player, "tp " + Chat.arrayToString(args, 0).trim());
    }
}
