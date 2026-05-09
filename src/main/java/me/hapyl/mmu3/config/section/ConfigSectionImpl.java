package me.hapyl.mmu3.config.section;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class ConfigSectionImpl implements ConfigSection {
    
    private final String key;
    private final ConfigurationSection section;
    
    public ConfigSectionImpl(@NotNull String key, @NotNull ConfigurationSection section) {
        this.key = key;
        this.section = section;
    }
    
    @NotNull
    @Override
    public String key() {
        return key;
    }
    
    @NotNull
    @Override
    public ConfigurationSection section() {
        return section;
    }
    
    @Override
    public String toString() {
        return key;
    }
}