package me.hapyl.mmu3.config;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.config.section.ConfigSection;
import me.hapyl.mmu3.config.section.ConfigSectionSupplier;
import me.hapyl.mmu3.config.section.ConfigSections;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class ConfigImpl implements Config {
    
    private static final String CONFIG_FILE_NAME = "config.yml";
    private static final String FEATURE_PREFIX = "feature";
    
    private final Plugin plugin;
    private final File file;
    private final Map<String, ConfigSection> sections;
    
    private YamlConfiguration yaml;
    
    public ConfigImpl(@NotNull Plugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), CONFIG_FILE_NAME);
        this.sections = Maps.newHashMap();
        
        // Call reload to create the file and load the configuration
        this.reload();
    }
    
    @NotNull
    @Override
    public YamlConfiguration yaml() {
        return Objects.requireNonNull(yaml, "Config isn't loaded yet!");
    }
    
    @NotNull
    @Override
    public CompletableFuture<Void> reload() {
        final CompletableFuture<Void> future = new CompletableFuture<>();
        final InputStream defaultConfigStream = Objects.requireNonNull(plugin.getResource(CONFIG_FILE_NAME), "Missing `%s` in the jar file!".formatted(CONFIG_FILE_NAME));
        
        // Save config to the disk in case first startup or missing
        if (!file.exists()) {
            plugin.saveResource(CONFIG_FILE_NAME, false);
        }
        
        try {
            yaml = new YamlConfiguration();
            yaml.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defaultConfigStream)));
            yaml.load(file);
            
            defaultConfigStream.close();
            
            // Loads sections
            sections.clear();
            
            for (ConfigSectionSupplier<?> supplier : ConfigSections.values()) {
                final String identifier = supplier.identifier();
                final ConfigurationSection configurationSection = yaml.getConfigurationSection(createFeatureKey(identifier));
                
                if (configurationSection == null) {
                    throw new IllegalArgumentException("Missing required field `%s` in the `%s`!".formatted(identifier, CONFIG_FILE_NAME));
                }
                
                sections.put(identifier, supplier.supply(identifier, configurationSection));
            }
            
            // Complete the future
            future.complete(null);
        }
        catch (Exception ex) {
            future.completeExceptionally(ex);
        }
        
        return future;
    }
    
    @NotNull
    @Override
    public <C extends ConfigSection> C section(@NotNull ConfigSectionSupplier<C> supplier) {
        final String identifier = supplier.identifier();
        final Class<C> sectionClass = supplier.sectionClass();
        final ConfigSection section = Objects.requireNonNull(sections.get(identifier), "Missing section for `%s`!".formatted(identifier));
        
        if (!sectionClass.isInstance(section)) {
            throw new IllegalArgumentException("Config section `%s` is not accessible for `%s`!".formatted(identifier, sectionClass.getSimpleName()));
        }
        
        return sectionClass.cast(section);
    }
    
    private static @NotNull String createFeatureKey(@NotNull String identifier) {
        return FEATURE_PREFIX + "." + identifier;
    }
    
}