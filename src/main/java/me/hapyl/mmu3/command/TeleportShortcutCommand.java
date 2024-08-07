package me.hapyl.mmu3.command;

import me.hapyl.mmu3.message.Message;
import me.hapyl.eterna.module.command.SimplePlayerAdminCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TeleportShortcutCommand extends SimplePlayerAdminCommand {
    public TeleportShortcutCommand(String name) {
        super(name);
        setDescription("A shortcut for a teleport command.");
    }

    @Override
    protected void execute(Player player, String[] args) {
        // Handle "tp <player>"
        if (args.length == 1) {
            final Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                Message.PLAYER_NOT_ONLINE.send(player, args[0]);
                return;
            }

            player.teleport(target);
            Message.success(player, "Teleported to %s.", target.getName());
            return;
        }

        Bukkit.dispatchCommand(player, "tp " + String.join(" ", args));
    }
}
