package me.hapyl.mmu3.util.menu;

import me.hapyl.mmu3.util.ButtonComponents;
import me.hapyl.mmu3.util.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;

public interface MenuIcons {
    
    ItemStack PAGE_PREVIOUS = ItemBuilder.playerHead("de1dfc11a837111d22b001a14461f9a7fc093522f88c58faefd6adeffcd4e9ab")
                                         .setName(Component.text("Previous Page"))
                                         .addLore()
                                         .addLore(ButtonComponents.left("open previous page"))
                                         .build();
    
    ItemStack PAGE_NEXT = ItemBuilder.playerHead("7c69d41076a8dea4f06d3f1a9ac47cc996988b74a0913ab2ac1a74caf7081918")
                                     .setName(Component.text("Next Page"))
                                     .addLore()
                                     .addLore(ButtonComponents.left("open next page"))
                                     .build();
    
}
