package me.hapyl.mmu3.command;

import me.hapyl.mmu3.MMULogger;
import me.hapyl.mmu3.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MMUOperationReload implements MMUOperation {
    @NotNull
    @Override
    public String name() {
        return "reload";
    }
    
    @NotNull
    @Override
    public String description() {
        return "Reloads the configuration file.";
    }
    
    @Override
    public void process(@NotNull Player player, @NotNull ArgumentList args) {
        MMULogger.info(player, Component.text("Reloading the config...", NamedTextColor.YELLOW));
        
        Main.config().reload()
            .thenAccept(_void -> {
                MMULogger.success(player, Component.text("Config successfully reloaded!"));
            })
            .exceptionally(ex -> {
                final Throwable throwable = ex.getCause();
                
                MMULogger.error(player, Component.text("Failed to reload the config!"));
                MMULogger.error(player, Component.text("Error message: %s".formatted(throwable != null ? throwable.getMessage() : ex.getMessage())));
                return null;
            });
    }
}