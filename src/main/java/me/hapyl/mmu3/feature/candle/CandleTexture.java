package me.hapyl.mmu3.feature.candle;

import me.hapyl.mmu3.util.ItemBuilder;
import me.hapyl.mmu3.util.ItemCreator;
import org.jetbrains.annotations.NotNull;

public interface CandleTexture extends ItemCreator {
    
    @NotNull
    String identifier();
    
    @NotNull
    String texture();
    
    @NotNull
    @Override
    ItemBuilder createBuilder();
    
    @NotNull
    static CandleTexture create(@NotNull String identifier, @NotNull String texture) {
        return new CandleTextureImpl(identifier, texture);
    }
    
}
