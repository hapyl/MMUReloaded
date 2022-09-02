package me.hapyl.mmu3.utils;

import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Stores icons for GUI's statically.
 */
public class StaticItemIcon {

    public static class LoreEditor {

        public static final ItemStack CLEAR = new ItemBuilder(Material.RED_GLAZED_TERRACOTTA)
                .setName("&cClear Lore")
                .addSmartLore("Clears all all from the item.")
                .addSmartLore("&c&lWarning! &7This action cannot be undone.")
                .build();

        public static final ItemStack EDIT = new ItemBuilder(Material.ROSE_BUSH)
                .setName("&aAdd Smart Lore")
                .addSmartLore("Adds smart lore to the item.")
                .addSmartLore(
                        "Smart lore automatically splits lines for you in the best spots. You can also use '_&7_' to put a manual split.")
                .build();

        public static final ItemStack PAGE_LIMIT = new ItemBuilder(Material.REDSTONE_BLOCK)
                .setName("&aLast Page!")
                .addSmartLore("Lines are limited because lore will not fit screen of a player if there would have been more than 28 lines!")
                .build();

    }

    public static final ItemStack PAGE_NEXT = new ItemBuilder(Material.ARROW).setName("&aNext Page").build();
    public static final ItemStack PAGE_PREVIOUS = new ItemBuilder(Material.ARROW).setName("&aPrevious Page").build();


}
