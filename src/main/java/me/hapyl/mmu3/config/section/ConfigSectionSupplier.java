package me.hapyl.mmu3.config.section;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public interface ConfigSectionSupplier<C extends ConfigSection> {
    
    @NotNull
    String identifier();
    
    @NotNull
    Class<C> sectionClass();
    
    @NotNull
    C supply(@NotNull String identifier, @NotNull ConfigurationSection section);
    
}
