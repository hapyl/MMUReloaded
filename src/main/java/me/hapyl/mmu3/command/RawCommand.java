package me.hapyl.mmu3.command;

import kz.hapyl.spigotutils.module.chat.Chat;
import kz.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import me.hapyl.mmu3.Message;
import org.bukkit.entity.Player;

public class RawCommand extends SimplePlayerAdminCommand {
    public RawCommand(String name) {
        super(name);
        setDescription("Sends a string formatted with colors.");
    }

    @Override
    protected void execute(Player player, String[] args) {
        Message.info(player, Chat.arrayToString(args, 0));
    }
}
