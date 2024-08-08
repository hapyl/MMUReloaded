package me.hapyl.mmu3.command;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.warp.Warp;
import me.hapyl.mmu3.feature.warp.Warps;
import me.hapyl.mmu3.message.Message;
import me.hapyl.eterna.module.command.SimplePlayerAdminCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class WarpCommand extends SimplePlayerAdminCommand {
    public WarpCommand(String name) {
        super(name);
        setDescription("Allows creating and teleporting to warps.");
        addCompleterValues(0, "add", "remove", "move");
    }

    @Override
    protected void execute(Player player, String[] args) {
        if (args.length == 0) {
            Message.NOT_ENOUGH_ARGUMENTS_EXPECTED_AT_LEAST.send(player, 1);
            return;
        }

        final Warps warps = Main.getRegistry().warps;
        final String arg0 = args[0].toLowerCase();

        // Warping
        if (args.length == 1) {
            final Warp warp = warps.byName(arg0);

            if (warp == null) {
                Message.error(player, "Invalid warp \"%s\"!", arg0);
                return;
            }

            warp.teleport(player);
            return;
        }

        final String arg1 = args[1];
        final Warp existing = warps.byName(arg1);

        switch (arg0) {
            case "add" -> {
                if (existing != null) {
                    Message.error(player, "This warp already exists!");
                    return;
                }

                final Warp newWarp = new Warp(arg1, player.getLocation());
                newWarp.setCreator(player.getName());

                warps.register(newWarp);
                Message.success(player, "Added \"%s\" warp.", arg1);
            }

            case "remove" -> {
                if (existing == null) {
                    Message.error(player, "This warp doesn't exist!");
                    return;
                }

                warps.unregister(existing);
                Message.success(player, "Removed \"%s\" warp.", arg1);
            }

            case "move" -> {
                if (existing == null) {
                    Message.error(player, "This warp doesn't exist!");
                    return;
                }

                existing.setLocation(player.getLocation());
                Message.success(player, "Moved \"%s\" location.", arg1);
            }
        }
    }

    @Override
    protected List<String> tabComplete(CommandSender sender, String[] args) {
        final Warps warps = Main.getRegistry().warps;
        return completerSort(warps.names(), args);
    }
}
