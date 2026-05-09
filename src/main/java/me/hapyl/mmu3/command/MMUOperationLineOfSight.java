package me.hapyl.mmu3.command;

import me.hapyl.mmu3.MMULogger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MMUOperationLineOfSight implements MMUOperation {

    @Override
    public @NotNull String name() {
        return "los";
    }

    @Override
    public @NotNull String description() {
        return "Displays the block data information about the block in the line of sight.";
    }

    @Override
    public void process(@NotNull Player player, @NotNull ArgumentList args) {
        final Block block = player.getTargetBlockExact(100);

        if (block == null) {
            MMULogger.error(player, Component.text("No block in sight!"));
            return;
        }

        final Material type = block.getType();
        final String blockData = block.getBlockData().getAsString();
        final Location location = block.getLocation(); // don't center the location

        final String locationString = "%s %s %s".formatted(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        final String locationStringCommas = "%s, %s, %s".formatted(location.getBlockX(), location.getBlockY(), location.getBlockZ());

        MMULogger.info(player, Component.text("Target Block Information:", NamedTextColor.AQUA));

        MMULogger.info(
                player,
                Component.empty()
                        .append(Component.text(" Type: ", NamedTextColor.GRAY))
                        .append(Component.translatable(type.translationKey(), NamedTextColor.WHITE))
        );

        copyLocal(player, "Minecraft Coordinates", locationString);
        copyLocal(player, "Bukkit Coordinates", locationStringCommas);

        // Check for block data
        if (blockData.contains("[")) {
            final String blockDataString = blockData.substring(blockData.lastIndexOf("["));

            copyLocal(player, "Block Data", blockDataString);
        }
    }

    private static void copyLocal(@NotNull Player player, @NotNull String message, @NotNull String toCopy) {
        MMULogger.info(
                player,
                Component.empty()
                        .append(Component.text(" %s: ".formatted(message), NamedTextColor.GRAY))
                        .append(Component.text(toCopy, NamedTextColor.WHITE))
                        .appendSpace()
                        .append(MMULogger.SUFFIX_COPY)
                        .hoverEvent(HoverEvent.showText(Component.text("Click to copy!", NamedTextColor.YELLOW)))
                        .clickEvent(ClickEvent.copyToClipboard(toCopy))
        );
    }

}
