package me.hapyl.mmu3.command;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.warp.Warp;
import me.hapyl.mmu3.feature.warp.Warps;
import me.hapyl.mmu3.message.Message;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class WarpCommand extends SimplePlayerAdminCommand {
    public WarpCommand(String name) {
        super(name);
    }

    @Override
    protected void execute(Player player, String[] args) {
        /**
         * warp (Name) [Player]
         * warp create/delete (Name)
         */

        if (args.length != 2) {
            Message.NOT_ENOUGH_ARGUMENTS_EXPECTED.send(player, 2);
            return;
        }

        final Warps warps = Main.getRegistry().warps;

        final String arg0 = args[0].toLowerCase();
        final String arg1 = args[1].toLowerCase();

        final Warp warp = warps.byName(arg0);

        if (warp != null) {
            final Player target = Bukkit.getPlayer(arg1);
            warp.teleport(target == null ? player : target);
            return;
        }

        switch (arg0) {
            case "create", "new" -> {

            }
            case "delete", "remove" -> {

            }
        }

    }
}
