package me.hapyl.mmu3.command;

import com.google.common.collect.Sets;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.EmptyCommandBlockLocator;
import me.hapyl.mmu3.message.Message;
import me.hapyl.eterna.module.chat.LazyEvent;
import me.hapyl.eterna.module.command.SimplePlayerAdminCommand;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Set;

public class FindEmptyCommandBlocksCommand extends SimplePlayerAdminCommand {
    public FindEmptyCommandBlocksCommand(String name) {
        super(name);
        setDescription("Allows admins to locate empty commands blocks that might have been damaged by WorldEdit or other plugins.");
        setUsage("findemptycommandblocks [show, clear]");
        addCompleterValues(1, "show", "clear");
    }

    @Override
    protected void execute(Player player, String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("show")) {
                final Set<Location> emptyCommandBlocks = Main.getRegistry().cbLocator.getEmptyCommandBlocks();

                if (emptyCommandBlocks.isEmpty()) {
                    Message.success(player, "&aThere are no empty command blocks in locator.");
                    return;
                }

                Message.severe(player, "There are %s empty command blocks locator!", emptyCommandBlocks.size());
                showEmptyCBCoordinates(player, emptyCommandBlocks);
                return;
            }

            else if (args[0].equalsIgnoreCase("clear")) {
                Main.getRegistry().cbLocator.getEmptyCommandBlocks().clear();
                Message.info(player, "Cleared.");
                return;
            }

            return;
        }

        Message.info(player, "Looking for empty command blocks...");

        final Set<Location> emptyCommandBlocks = Sets.newHashSet();
        final World world = player.getWorld();

        for (Chunk loadedChunk : world.getLoadedChunks()) {
            final Set<Location> set = EmptyCommandBlockLocator.findEmptyCommandBlocks(loadedChunk);
            if (set.isEmpty()) {
                continue;
            }

            emptyCommandBlocks.addAll(set);
        }

        if (emptyCommandBlocks.isEmpty()) {
            Message.success(player, "There are no empty command blocks in &lloaded &achunks!");
            return;
        }

        Message.severe(player, "Found %s empty command blocks in loaded chunks!", emptyCommandBlocks.size());
        showEmptyCBCoordinates(player, emptyCommandBlocks);
    }

    private void showEmptyCBCoordinates(Player player, Set<Location> set) {
        int shown = 0;

        for (Location location : set) {
            final String locationString = "%s %s %s".formatted(location.getBlockX(), location.getBlockY(), location.getBlockZ());
            Message.clickHover(
                    player,
                    LazyEvent.runCommand("/tp %s".formatted(locationString)),
                    LazyEvent.showText("&eClick to teleport!"),
                    "&a%s &6&lTP".formatted(locationString)
            );

            if (shown >= 15) {
                Message.severe(player, "And %s more!", set.size() - shown);
                break;
            }

            shown++;
        }

    }
}
