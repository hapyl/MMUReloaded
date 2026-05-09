package me.hapyl.mmu3.util;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface ItemCreator {
    
    @NotNull
    ItemBuilder createBuilder();
    
    @NotNull
    default ItemStack createItem() {
        return this.createBuilder().build();
    }
    
}
