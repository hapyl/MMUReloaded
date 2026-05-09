package me.hapyl.mmu3.config.section;

import com.google.common.collect.Lists;
import me.hapyl.mmu3.feature.candle.CandleTexture;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

public final class ConfigSectionCandle extends ConfigSectionImpl implements Iterable<CandleTexture> {
    
    private static final String KEY_TEXTURES = "textures";
    
    private final List<CandleTexture> textures;
    
    public ConfigSectionCandle(@NotNull String key, @NotNull ConfigurationSection section) {
        super(key, section);
        
        this.textures = Lists.newArrayList();
        
        // Read textures
        final ConfigurationSection textures = section.getConfigurationSection(KEY_TEXTURES);
        
        if (textures != null) {
            for (String textureKey : textures.getKeys(false)) {
                final String textureValue = textures.getString(textureKey);
                
                if (textureValue == null) {
                    throw new IllegalArgumentException("Candle `%s` is missing a texture!".formatted(textureKey));
                }
                
                this.textures.add(CandleTexture.create(textureValue, textureValue));
            }
        }
    }
    
    @NotNull
    @Override
    public Iterator<CandleTexture> iterator() {
        return textures.iterator();
    }
    
}