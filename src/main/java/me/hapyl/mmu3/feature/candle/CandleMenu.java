package me.hapyl.mmu3.feature.candle;

import me.hapyl.mmu3.MMULogger;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.config.section.ConfigSectionCandle;
import me.hapyl.mmu3.config.section.ConfigSections;
import me.hapyl.mmu3.util.ButtonComponents;
import me.hapyl.mmu3.util.ComponentHelper;
import me.hapyl.mmu3.util.ItemBuilder;
import me.hapyl.mmu3.util.menu.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CandleMenu extends Menu {
    
    private final CandleData candleData;
    
    public CandleMenu(@NotNull Player player, @NotNull CandleData candleData) {
        super(player, Component.text("Candles"), Size.SIZE_5);
        
        this.candleData = candleData;
        this.openMenu();
    }
    
    @Override
    public void updateMenu() {
        final ConfigSectionCandle candleSection = Main.config().section(ConfigSections.CANDLE);
        final CandleTexture currentTexture = candleData.getCandle();
        
        final MenuPatternApplier applier = new MenuPatternApplier(this, MenuPattern.DEFAULT, 1);
        
        candleSection.iterator().forEachRemaining(texture -> {
            final ItemBuilder builder = texture.createBuilder().addLore();
            
            if (currentTexture != null && currentTexture.equals(texture)) {
                builder.addLore(Component.text("Current selected!", NamedTextColor.RED));
                applier.add(builder.build());
            }
            else {
                builder.addLore(ButtonComponents.left("select"));
                
                applier.add(builder.build(), MenuAction.builder().left(player -> {
                    candleData.setCandle(currentTexture);
                    
                    // Give the torch item and close inventory
                    Main.featureRegistry().candleFeature.giveCandleItem(player);
                    closeMenu();
                    
                    MMULogger.sound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 2.0f);
                }));
            }
        });
        
        applier.apply();
        
        // Random offset
        final boolean randomOffset = candleData.isRandomOffset();
        
        setPanelItem(
                6,
                new ItemBuilder(randomOffset ? Material.LIME_DYE : Material.GRAY_DYE)
                        .setName(ComponentHelper.checkmark(Component.text("Random Offset"), randomOffset))
                        .addLore()
                        .addLore(ComponentHelper.wrap("If enabled, the placed candle will have a randomized horizontal offset."))
                        .addLore()
                        .addLore(ButtonComponents.left("toggle"))
                        .build(),
                MenuAction.builder()
                          .left(player -> {
                              candleData.setRandomOffset(!randomOffset);
                              
                              MMULogger.sound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 2.0f);
                              openMenu();
                          })
        );
        
        // Candle type
        final MenuCycle<CandleData.CandleType> menuCycle = new MenuCycle<>(
                Component.text("Candle Type"),
                Component.text("Cycles the candle entity type."),
                CandleData.CandleType.class
        ) {
            @NotNull
            @Override
            public CandleData.CandleType currentValue() {
                return candleData.getCandleType();
            }
            
            @Override
            public void currentValue(@NotNull CandleData.CandleType candleType) {
                candleData.setCandleType(candleType);
            }
            
            @NotNull
            @Override
            public ItemBuilder createBuilder(@NotNull CandleData.CandleType candleType) {
                // Use the candleType material instead of the base one
                return candleType.createBuilder();
            }
        };
        
        setPanelItem(
                4,
                menuCycle.createItem(),
                menuCycle.createMenuAction(this)
        );
    }
    
}