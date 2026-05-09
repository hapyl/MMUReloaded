package me.hapyl.mmu3;

import me.hapyl.mmu3.config.Config;
import me.hapyl.mmu3.config.ConfigImpl;
import me.hapyl.mmu3.feature.standeditor.StandEditorListener;
import me.hapyl.mmu3.feature.statechanger.StateChangerListener;
import me.hapyl.mmu3.listener.EntityListener;
import me.hapyl.mmu3.listener.EntityRemovalListener;
import me.hapyl.mmu3.listener.PlayerHandler;
import me.hapyl.mmu3.util.menu.MenuHandler;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class Main extends JavaPlugin {
    
    private static Main instance;
    
    private Config config;
    private FeatureRegistry featureRegistry;
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
    
    @Override
    public void onEnable() {
        instance = this;
        
        // Load config
        config = new ConfigImpl(this);
        
        // Register Commands
        new CommandRegistry();
        
        // Register events
        final PluginManager pluginManager = getServer().getPluginManager();
        
        pluginManager.registerEvents(new PlayerHandler(), this);
        pluginManager.registerEvents(new EntityRemovalListener(), this);
        pluginManager.registerEvents(new MenuHandler(), this);
        
        // Register features events
        new StandEditorListener(this);
        new StateChangerListener(this);
        new EntityListener(this);
        
        // Initiate features
        featureRegistry = new FeatureRegistry(this);
    }
    
    private void runSafe(Runnable runnable, String name) {
        try {
            runnable.run();
        }
        catch (Exception e) {
            Bukkit.getLogger().severe("Could not run %s! %s".formatted(name, e.getMessage()));
        }
    }
    
    @NotNull
    public static Config config() {
        return instance.config;
    }
    
    @NotNull
    public static FeatureRegistry featureRegistry() {
        return instance.featureRegistry;
    }
    
    @NotNull
    public static Main getPlugin() {
        return instance;
    }
    
    @NotNull
    public static NamespacedKey createNamespacedKey(@NotNull String key) {
        return new NamespacedKey(instance, key);
    }
    
}