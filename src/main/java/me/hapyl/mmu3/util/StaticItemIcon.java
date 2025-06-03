package me.hapyl.mmu3.util;

import me.hapyl.eterna.module.inventory.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Stores icons for GUI's statically.
 */
public class StaticItemIcon {

    public static class LoreEditor {

        public static final ItemStack CLEAR = new ItemBuilder(Material.RED_GLAZED_TERRACOTTA)
                .setName("&4Clear Lore")
                .addTextBlockLore("""
                        Clears any existing lore from the item.
                        
                        &4&lWARNING!
                        This action cannot be undone!
                        """)
                .asIcon();

        public static final ItemStack EDIT = new ItemBuilder(Material.ROSE_BUSH)
                .setName("&2Add Smart Lore")
                .addTextBlockLore("""
                        Adds smart lore to the item.
                        
                        Smart lore automatically wraps lines in the best spots.
                        &8&o;;You can also use '&8&o_&8&o_' to manually wrap.
                        
                        
                        """)
                .asIcon();

        public static final ItemStack PAGE_LIMIT = new ItemBuilder(Material.REDSTONE_BLOCK)
                .setName("&2Last Page!")
                .addTextBlockLore("""
                        No more will fit on the screen!
                        """)
                .asIcon();

    }

}
