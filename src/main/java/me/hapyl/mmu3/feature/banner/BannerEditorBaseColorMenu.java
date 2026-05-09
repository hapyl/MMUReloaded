package me.hapyl.mmu3.feature.banner;

import me.hapyl.mmu3.util.ButtonComponents;
import me.hapyl.mmu3.util.menu.*;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BannerEditorBaseColorMenu extends Menu {
    
    private final BannerData data;
    
    BannerEditorBaseColorMenu(@NotNull Player player, @NotNull BannerData data) {
        super(player, Component.text("Select Base Color"), Size.SIZE_3);
        
        this.data = data;
        this.openMenu();
    }
    
    @Override
    public void updateMenu() {
        final MenuPatternApplier applier = new MenuPatternApplier(this, MenuPattern.DEFAULT, 1);
        
        for (final BannerColor bannerColor : BannerColor.values()) {
            final BannerItemBuilder builder = data.createBuilder();
            
            builder.setType(bannerColor.getMaterial());
            builder.setName(bannerColor.asComponent());
            
            builder.addLore();
            builder.addLore(ButtonComponents.left("select"));
            
            applier.add(
                    builder.build(),
                    MenuAction.builder()
                              .left(player -> {
                                  data.setBaseColor(bannerColor);
                                  new BannerEditorMenu(player, 1);
                              })
            );
        }
        
        applier.apply();
    }
    
}