package me.hapyl.mmu3.feature.candle;

import me.hapyl.mmu3.util.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CandleTextureImpl implements CandleTexture {
    
    private final String identifier;
    private final String texture;
    
    CandleTextureImpl(@NotNull String identifier, @NotNull String texture) {
        this.identifier = identifier;
        this.texture = texture;
    }
    
    @NotNull
    @Override
    public String identifier() {
        return identifier;
    }
    
    @NotNull
    @Override
    public String texture() {
        return texture;
    }
    
    @Override
    public @NotNull ItemBuilder createBuilder() {
        return ItemBuilder.playerHead(texture())
                          .setName(Component.text("Candle (%s)".formatted(identifier().replace("_", " "))));
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.identifier);
    }
    
    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        
        final CandleTextureImpl that = (CandleTextureImpl) object;
        return Objects.equals(this.identifier, that.identifier);
    }
}