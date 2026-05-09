package me.hapyl.mmu3.feature;

import me.hapyl.mmu3.Main;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class Feature implements FeatureBase {
    
    private final FeatureKey key;
    private final Main plugin;
    
    public Feature(@NotNull FeatureKey key, @NotNull Main plugin) {
        this.key = key;
        this.plugin = plugin;
        
        if (this instanceof Listener listener) {
            plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }
    
    @NotNull
    @Override
    public FeatureKey getKey() {
        return key;
    }
    
    @NotNull
    public Main getPlugin() {
        return plugin;
    }
    
}