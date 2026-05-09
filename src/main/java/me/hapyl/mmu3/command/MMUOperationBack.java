package me.hapyl.mmu3.command;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.MMULogger;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MMUOperationBack implements MMUOperation {

    @Override
    public @NotNull String name() {
        return "back";
    }

    @Override
    public @NotNull String description() {
        return "Teleport the player to their last saved location.";
    }

    @Override
    public void process(@NotNull Player player, @NotNull ArgumentList args) {
        final Location lastLocation = Main.getLastLocation().getLastLocation(player.getUniqueId());

        if (lastLocation == null) {
            MMULogger.error(player, Component.text("You don't have a location saved."));
            return;
        }

        player.teleport(lastLocation);

        MMULogger.success(player, Component.text("Teleported to the last saved location!"));
        MMULogger.sound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f);
    }

}
