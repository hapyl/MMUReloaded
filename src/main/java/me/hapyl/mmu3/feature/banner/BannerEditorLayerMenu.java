package me.hapyl.mmu3.feature.banner;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.util.ButtonComponents;
import me.hapyl.mmu3.util.ComponentHelper;
import me.hapyl.mmu3.util.ItemBuilder;
import me.hapyl.mmu3.util.menu.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class BannerEditorLayerMenu extends Menu {
    
    private final Op op;
    private final int page;
    
    private final BannerData data;
    private final PatternBuilder builder;
    
    public BannerEditorLayerMenu(@NotNull Player player, @NotNull Op op, @NotNull BannerEditorMenu menu) {
        super(
                player,
                ComponentHelper.join(
                        Component.text("➜"),
                        Component.text("Banner Editor"),
                        op.name()
                ),
                Size.SIZE_3
        );
        
        this.op = op;
        this.page = menu.getPage();
        this.data = Main.featureRegistry().bannerEditor.getOrCreateBannerData(player);
        this.builder = op.builder(data);
        
        this.openMenu();
    }
    
    @Override
    public void updateMenu() {
        setItem(
                12,
                data.createBuilder()
                    .setPattern(builder.build())
                    .setName(Component.text("Pattern: ").append(builder.getPatternName()))
                    .addLore()
                    .addLore(ButtonComponents.left("change"))
                    .build(),
                MenuAction.builder()
                          .left(player -> new BannerEditorLayerSelectPatternMenu(player, data, builder))
        );
        
        setItem(
                14,
                builder.getColorAsBuilder()
                       .setName(Component.text("Color: ").append(builder.getColorName()))
                       .addLore()
                       .addLore(ButtonComponents.left("change"))
                       .build(),
                MenuAction.builder()
                          .left(player -> new BannerEditorLayerSelectColorMenu(player, data, builder))
        );
        
        // Set panel buttons
        setPanelItem(
                2,
                new ItemBuilder(Material.RED_CARPET)
                        .setName(Component.text("Cancel", NamedTextColor.RED))
                        .addLore()
                        .addLore(ButtonComponents.left("cancel"))
                        .build(),
                MenuAction.builder()
                          .left(player -> new BannerEditorMenu(player, page))
        );
        
        setPanelItem(
                6,
                new ItemBuilder(Material.LIME_CARPET)
                        .setName(Component.text("Confirm", NamedTextColor.GREEN))
                        .addLore()
                        .addLore(ButtonComponents.left("confirm"))
                        .build(),
                MenuAction.builder()
                          .left(player -> {
                              op.op(data, builder.build());
                              new BannerEditorMenu(player, page);
                          })
        );
    }
    
    public interface Op {
        
        @NotNull
        Component name();
        
        void op(@NotNull BannerData bannerData, @NotNull Pattern pattern);
        
        @NotNull
        PatternBuilder builder(@NotNull BannerData bannerData);
        
        @NotNull
        static Op newLayer() {
            return new ImplNewLayer();
        }
        
        @NotNull
        static Op editLayer(int layer) {
            return new ImplEditLayer(layer);
        }
        
        class ImplNewLayer implements Op {
            @Nonnull
            @Override
            public Component name() {
                return Component.text("Add Layer");
            }
            
            @Override
            public void op(@NotNull BannerData bannerData, @NotNull Pattern pattern) {
                bannerData.addPattern(pattern);
            }
            
            @NotNull
            @Override
            public PatternBuilder builder(@NotNull BannerData bannerData) {
                return PatternBuilder.empty();
            }
        }
        
        class ImplEditLayer implements Op {
            private final int layer;
            
            ImplEditLayer(int layer) {
                this.layer = layer;
            }
            
            @Nonnull
            @Override
            public Component name() {
                return Component.text("Edit Layer %s".formatted(layer + 1));
            }
            
            @Override
            public void op(@NotNull BannerData bannerData, @NotNull Pattern pattern) {
                bannerData.setPattern(layer, pattern);
            }
            
            @NotNull
            @Override
            public PatternBuilder builder(@NotNull BannerData bannerData) {
                // Unless a retard somehow calls this op with illegal index, it's technically impossible
                // to get null pattern here, but the retard question is me, so adding a check
                final Pattern pattern = bannerData.getPattern(layer);
                
                return pattern != null ? PatternBuilder.builder(pattern) : PatternBuilder.empty();
            }
        }
    }
    
    public static abstract class BannerEditorLayerSubMenu extends Menu {
        
        protected final BannerData bannerData;
        protected final PatternBuilder patternBuilder;
        
        BannerEditorLayerSubMenu(@NotNull Player player, @NotNull Component title, @NotNull Size size, @NotNull BannerData bannerData, @NotNull PatternBuilder patternBuilder) {
            super(player, title, size);
            
            this.bannerData = bannerData;
            this.patternBuilder = patternBuilder;
            
            this.openMenu();
        }
    }
    
    public class BannerEditorLayerSelectPatternMenu extends BannerEditorLayerSubMenu {
        
        BannerEditorLayerSelectPatternMenu(@NotNull Player player, @NotNull BannerData bannerData, @NotNull PatternBuilder patternBuilder) {
            super(player, Component.text("Select Banner Pattern"), Size.SIZE_5, bannerData, patternBuilder);
        }
        
        @Override
        public void updateMenu() {
            final MenuPatternApplier applier = new MenuPatternApplier(this, MenuPattern.DEFAULT, 1);
            
            for (PatternType patternType : BannerEditor.REGISTRY) {
                final NamespacedKey patternKey = BannerEditor.REGISTRY.getKey(patternType);
                final BannerItemBuilder builder = bannerData.createBuilder();
                
                applier.add(
                        builder.setPattern(patternType, patternBuilder.getColor())
                               .setName(Component.text(patternKey != null ? patternKey.getKey() : "Unknown"))
                               .addLore()
                               .addLore(ButtonComponents.left("select"))
                               .build(),
                        MenuAction.builder()
                                  .left(player -> {
                                      patternBuilder.setPattern(patternType);
                                      BannerEditorLayerMenu.this.openMenu();
                                  })
                );
            }
            
            applier.apply();
        }
    }
    
    public class BannerEditorLayerSelectColorMenu extends BannerEditorLayerSubMenu {
        
        BannerEditorLayerSelectColorMenu(@NotNull Player player, @NotNull BannerData bannerData, @NotNull PatternBuilder patternBuilder) {
            super(player, Component.text("Select Color"), Size.SIZE_3, bannerData, patternBuilder);
        }
        
        @Override
        public void updateMenu() {
            final MenuPatternApplier applier = new MenuPatternApplier(this, MenuPattern.DEFAULT, 1);
            
            for (DyeColor dyeColor : DyeColor.values()) {
                applier.add(
                        new ItemBuilder(PatternBuilder.dyeColorToMaterial(dyeColor))
                                .setName(ComponentHelper.capitalize(dyeColor).color(TextColor.color(dyeColor.getColor().asRGB())))
                                .addLore()
                                .addLore(ButtonComponents.left("select"))
                                .build(),
                        MenuAction.builder()
                                  .left(player -> {
                                      patternBuilder.setColor(dyeColor);
                                      BannerEditorLayerMenu.this.openMenu();
                                  })
                );
            }
            
            applier.apply();
        }
    }
    
}