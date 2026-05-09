package me.hapyl.mmu3.feature.candle;

import me.hapyl.mmu3.data.Data;
import me.hapyl.mmu3.data.DataConstructor;
import me.hapyl.mmu3.util.ComponentHelper;
import me.hapyl.mmu3.util.ItemBuilder;
import me.hapyl.mmu3.util.ItemCreator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CandleData extends Data {
    
    @Nullable private CandleTexture candle;
    private boolean randomOffset;
    private CandleType candleType;
    
    @DataConstructor
    private CandleData(@NotNull Player player) {
        super(player);
        
        this.candle = null;
        this.randomOffset = true;
        this.candleType = CandleType.ARMOR_STAND;
    }
    
    @Override
    public void reset() {
        candle = null;
        randomOffset = true;
    }
    
    @Nullable
    public CandleTexture getCandle() {
        return candle;
    }
    
    public void setCandle(@Nullable CandleTexture candle) {
        this.candle = candle;
    }
    
    public boolean isRandomOffset() {
        return randomOffset;
    }
    
    public void setRandomOffset(boolean randomOffset) {
        this.randomOffset = randomOffset;
    }
    
    @NotNull
    public CandleType getCandleType() {
        return candleType;
    }
    
    public void setCandleType(@NotNull CandleType candleType) {
        this.candleType = candleType;
    }
    
    public enum CandleType implements ComponentLike, ItemCreator {
        ARMOR_STAND {
            @Override
            public @NotNull ItemBuilder createBuilder() {
                return new ItemBuilder(Material.ARMOR_STAND);
            }
            
            @NotNull
            @Override
            public Entity createEntity(@NotNull Location location, @NotNull CandleTexture candleTexture) {
                // Offset location
                location.subtract(0, 1.5, 0);
                
                return location.getWorld().spawn(location, ArmorStand.class, self -> {
                    self.setInvisible(true);
                    self.setMarker(true);
                    self.setSilent(true);
                    
                    self.getEquipment().setHelmet(candleTexture.createItem());
                });
            }
        },
        
        DISPLAY_ENTITY {
            @NotNull
            @Override
            public ItemBuilder createBuilder() {
                return new ItemBuilder(Material.ITEM_FRAME);
            }
            
            @NotNull
            @Override
            public Entity createEntity(@NotNull Location location, @NotNull CandleTexture candleTexture) {
                // Offset location
                
                return location.getWorld().spawn(location, ItemDisplay.class, self -> {
                    self.setItemStack(candleTexture.createItem());
                });
            }
        };
        
        private final Component description;
        
        CandleType() {
            this.description = ComponentHelper.capitalize(this);
        }
        
        @NotNull
        @Override
        public Component asComponent() {
            return description;
        }
        
        @NotNull
        public Entity createEntity(@NotNull Location location, @NotNull CandleTexture candleTexture) {
            throw new IllegalArgumentException("%s does not override createEntiy()!".formatted(this.name()));
        }
        
    }
    
}