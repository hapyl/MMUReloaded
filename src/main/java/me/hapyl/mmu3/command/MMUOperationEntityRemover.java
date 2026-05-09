package me.hapyl.mmu3.command;

import me.hapyl.mmu3.MMULogger;
import me.hapyl.mmu3.data.PlayerData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MMUOperationEntityRemover implements MMUOperation {

    @Override
    public @NotNull String name() {
        return "entity_remover";
    }

    @Override
    public @NotNull String description() {
        return "Toggles the entity remover.";
    }

    @Override
    public void process(@NotNull Player player, @NotNull ArgumentList args) {
        final PlayerData playerData = PlayerData.ofPlayer(player);
        
        final boolean value = playerData.entityRemover.value();
        final boolean newValue = !value;
        
        playerData.entityRemover.value(newValue);

        MMULogger.info(
                player,
                Component.empty()
                        .append(Component.text("Entity remover is now "))
                        .append(newValue ? Component.text("enabled", NamedTextColor.GREEN) : Component.text("disabled", NamedTextColor.RED))
                        .append(Component.text("!"))
        );

        if (newValue) {
            MMULogger.info(player, Component.text("Punching entities will remove them instantly."));
        }

    }

}
