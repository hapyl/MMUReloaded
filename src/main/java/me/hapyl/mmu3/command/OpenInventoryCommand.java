package me.hapyl.mmu3.command;

import me.hapyl.mmu3.feature.openinventory.OpenInventoryGUI;
import me.hapyl.mmu3.message.Message;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class OpenInventoryCommand extends SimplePlayerAdminCommand {
    public OpenInventoryCommand(String name) {
        super(name);
        setDescription("Opens an online player's inventory.");
        setAliases("invsee", "openinv");
    }

    @Override
    protected void execute(Player player, String[] args) {
        final Player target = args.length >= 1 ? Bukkit.getPlayer(args[0]) : player;

        if (target == null) {
            Message.error(player, "This player is not online!");
            return;
        }

        Message.info(player, "Opening %s's inventory...", target.getName());
        new OpenInventoryGUI(player, target);
    }
}
