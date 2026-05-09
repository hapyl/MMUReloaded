package me.hapyl.mmu3.config.section;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public interface ConfigSection {
    
    @NotNull
    String key();
    
    @NotNull
    ConfigurationSection section();
    
    default boolean isEnabled() {
        return section().getBoolean("enabled");
    }
    
}
