package me.hapyl.mmu3.config;

import me.hapyl.mmu3.config.section.ConfigSection;
import me.hapyl.mmu3.config.section.ConfigSectionSupplier;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public interface Config {
    
    @NotNull YamlConfiguration yaml();
    
    @NotNull CompletableFuture<Void> reload();
    
    @NotNull <C extends ConfigSection> C section(@NotNull ConfigSectionSupplier<C> supplier);
}
