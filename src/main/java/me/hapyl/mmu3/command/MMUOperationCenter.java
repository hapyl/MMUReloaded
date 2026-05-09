package me.hapyl.mmu3.command;

import me.hapyl.mmu3.MMULogger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MMUOperationCenter implements MMUOperation {

    @Override
    public @NotNull String name() {
        return "center";
    }

    @Override
    public @NotNull String description() {
        return "Centers you at the block.";
    }

    @Override
    public void process(@NotNull Player player, @NotNull ArgumentList args) {
        final Location centerLocation = center(player.getLocation());

        player.teleport(centerLocation);

        MMULogger.success(player, Component.text("Centered you at %s!".formatted(locationToString(centerLocation))));
        MMULogger.sound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f);

        // Generators
        final double x = centerLocation.getX();
        final double y = centerLocation.getY();
        final double z = centerLocation.getZ();

        MMULogger.copy(
                player,
                Component.text("Minecraft Teleport"),
                ClickEvent.copyToClipboard("/tp @s %.1f %.1f %.1f".formatted(x, y, z))
        );

        MMULogger.copy(
                player,
                Component.text("Bukkit Coordinates"),
                ClickEvent.copyToClipboard("%.1f %.1f %.1f".formatted(x, y, z))
        );
    }

    @NotNull
    public static Location center(@NotNull Location location) {
        return new Location(
                location.getWorld(),
                location.getBlockX() + 0.5d,
                location.getY(),
                location.getBlockZ() + 0.5d,
                location.getYaw(),
                location.getPitch()
        );
    }

    @NotNull
    public static String locationToString(@NotNull Location location) {
        return "%.1f, %.1f, %.1f".formatted(location.getX(), location.getY(), location.getZ());
    }

}
