package me.hapyl.mmu3.feature.standeditor;

import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class StandEditorConstants {

    public static final ItemStack ITEM_DISABLED_SLOTS = (new ItemBuilder(Material.IRON_TRAPDOOR))
            .setName("Disabled Slots")
            .setSmartLore("Toggles whenever armor stand has disabled slots.")
            .addSmartLore("Disabling slots will disallow putting or taking any items from armor stand.", "&8")
            .build();
    public static final ItemStack ITEM_SHOW_ARMS = (new ItemBuilder(Material.RABBIT_FOOT))
            .setName("Show Arms")
            .setSmartLore("Toggles whenever armor stand has arms.")
            .addSmartLore("Armor stands with arms can hold weapons!", "&8")
            .build();
    public static final ItemStack ITEM_VISIBILITY = (new ItemBuilder(Material.ENDER_EYE))
            .setName("Visibility")
            .setSmartLore("Toggles whenever armor stand is visible.")
            .addSmartLore("You cannot see invisible armor stand.", "&8")
            .build();
    public static final ItemStack ITEM_GRAVITY = (new ItemBuilder(Material.FEATHER))
            .setName("Gravity")
            .setSmartLore("Toggles whenever armor stand has gravity.")
            .addSmartLore("No gravity will allow armor stand to fly.", "&8")
            .build();
    public static final ItemStack ITEM_INVULNERABLE = (new ItemBuilder(Material.BEDROCK))
            .setName("Invulnerable")
            .setSmartLore("Toggles whenever armor stand is invulnerable.")
            .addSmartLore("Invulnerable entities can be destroyed using &e/kill &8command or in creative mode.", "&8")
            .build();
    public static final ItemStack ITEM_SMALL = (new ItemBuilder(Material.PUFFERFISH))
            .setName("Small")
            .setSmartLore("Toggles whenever armor stand is small.")
            .addSmartLore("smol silly amror tsand", "&8")
            .build();
    public static final ItemStack ITEM_MARKER = (new ItemBuilder(Material.REDSTONE_TORCH))
            .setName("Marker")
            .setSmartLore("Toggles whenever armor stand is marker. ")
            .addSmartLore("Marker armor stand has 1 pixel hit box, which makes it impossible to click or destroy.", "&8")
            .addLore("&c&l           WARNING")
            .addSmartLore("You will not be able to open this menu, use the command (&e/editstand&7) instead.", 30)
            .build();
    public static final ItemStack ITEM_BASE_PLATE = (new ItemBuilder(Material.SMOOTH_STONE_SLAB))
            .setName("Base Plate")
            .setSmartLore("Toggles whenever armor stand has base plate.")
            .addSmartLore("Looks so much better without base plate!", "&8")
            .build();
    public static final ItemStack ITEM_TOGGLE_NAME_VISIBILITY = (new ItemBuilder(Material.BOOK))
            .setName("Custom Name Visibility")
            .setSmartLore("Toggles whenever armor stand has custom name visible.")
            .addSmartLore("Don't forget to make it invisible for floating text!", "&8")
            .build();
    public static final ItemStack ITEM_MOVE_STAND = (new ItemBuilder(Material.PISTON))
            .setName("&aEnter Move Mode")
            .setSmartLore("While in the moving mode, you will move armor stand with your body! Jump to move higher, Sneak to move lower.")
            .addLore()
            .addLore("&e&lSwitch Hands &8(F) &7to cycle speed.")
            .addLore("&e&lPunch &7to leave move mode.")
            .addLore()
            .addLore("&eClick to enter move mode")
            .build();

}
