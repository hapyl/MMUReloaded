package me.hapyl.mmu3.feature.standeditor;

import me.hapyl.eterna.module.inventory.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class StandEditorConstants {

    public static final ItemStack ITEM_DISABLED_SLOTS = new ItemBuilder(Material.IRON_TRAPDOOR)
            .setName("Lock")
            .addTextBlockLore("""
                    Toggles whether this armor stand is locked.
                    &8&o;;Locked armor stands cannot be interacted with, preventing taking/putting items on it.
                    """)
            .asIcon();

    public static final ItemStack ITEM_SHOW_ARMS = new ItemBuilder(Material.RABBIT_FOOT)
            .setName("Show Arms")
            .addTextBlockLore("""
                    Toggles whether this armor stand has arms.
                    &8&o;;Armor stands with arms can hold weapons properly!
                    """)
            .asIcon();

    public static final ItemStack ITEM_VISIBILITY = new ItemBuilder(Material.ENDER_EYE)
            .setName("Visibility")
            .addTextBlockLore("""
                    Toggles whether this armor stand is visible.
                    &8&oYou cannot see invisible armor stands, duh.
                    """)
            .asIcon();

    public static final ItemStack ITEM_GRAVITY = new ItemBuilder(Material.FEATHER)
            .setName("Gravity")
            .addTextBlockLore("""
                    Toggles whether this armor stand has gravity.
                    &8&o;;Armor stands without gravity don't fall.
                    """)
            .asIcon();

    public static final ItemStack ITEM_INVULNERABLE = new ItemBuilder(Material.BEDROCK)
            .setName("Invulnerable")
            .addTextBlockLore("""
                    Toggles whether this armor stand is invulnerable.
                    &8&o;;Invulnerable armor stands can only be destroying using &6&o/kill&8&o or in creative mode.
                    """)
            .asIcon();

    public static final ItemStack ITEM_SMALL = new ItemBuilder(Material.PUFFERFISH)
            .setName("Small")
            .addTextBlockLore("""
                    Toggles whether this armor stand is small.
                    &8&o;;smol silly amror sand
                    """)
            .asIcon();

    public static final ItemStack ITEM_MARKER = new ItemBuilder(Material.REDSTONE_TORCH)
            .setName("Marker")
            .addTextBlockLore("""
                    Toggles whether this armor is marker.
                    &8&o;;Marker armor stands has 0 pixel hit box, making it impossible to interact with.
                    
                    &4&lWARNING
                    &6&o;;You will not be able to interact with the armor stand by Shift Right-Clicking on it, use &e&o/editstand&6&o instead!
                    """)
            .asIcon();

    public static final ItemStack ITEM_BASE_PLATE = new ItemBuilder(Material.SMOOTH_STONE_SLAB)
            .setName("Base Plate")
            .addTextBlockLore("""
                    Toggles whether this armor stand has a base plate.
                    &8&o;;Looks so much better without it!
                    """)
            .asIcon();

    public static final ItemStack ITEM_TOGGLE_NAME_VISIBILITY = new ItemBuilder(Material.BOOK)
            .setName("Custom Name Visibility")
            .addTextBlockLore("""
                    Toggles whether this armor stand has custom name visible.
                    &8&o;;Don't forget to actually name it!
                    """)
            .asIcon();

    public static final ItemStack ITEM_MOVE_STAND = new ItemBuilder(Material.PISTON)
            .setName("Enter Move Mode")
            .addTextBlockLore("""
                    While in the &6Move Mode&7, you will be able to move the armor stand with you body!
                    
                    &eClick to enter move mode!
                    """)
            .asIcon();

}
