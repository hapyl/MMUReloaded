package me.hapyl.mmu3.feature.itemcreator;

import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlot;

import javax.annotation.Nullable;

public enum LinkedEquipmentSlot {

    ALL(null, Material.PUFFERFISH, "All Slots"),

    HAND(EquipmentSlot.HAND, Material.STONE_SWORD, "Main Hand"),
    OFF_HAND(EquipmentSlot.OFF_HAND, Material.SHIELD, "Off Hand"),
    FEET(EquipmentSlot.FEET, Material.IRON_BOOTS, "Boots"),
    LEGS(EquipmentSlot.LEGS, Material.IRON_LEGGINGS, "Leggings"),
    CHEST(EquipmentSlot.CHEST, Material.IRON_CHESTPLATE, "Chestplate"),
    HEAD(EquipmentSlot.HEAD, Material.IRON_HELMET, "Helmet"),
    BODY(EquipmentSlot.BODY, Material.LEATHER_HORSE_ARMOR, "Body (Non-player)"),
    ;

    private final EquipmentSlot link;
    private final Material material;
    private final String name;

    LinkedEquipmentSlot(EquipmentSlot link, Material material, String name) {
        this.link = link;
        this.material = material;
        this.name = name;
    }

    public static LinkedEquipmentSlot fromLink(@Nullable EquipmentSlot slot) {
        if (slot == null) {
            return ALL;
        }

        return switch (slot) {
            case HAND -> LinkedEquipmentSlot.HAND;
            case OFF_HAND -> LinkedEquipmentSlot.OFF_HAND;
            case FEET -> LinkedEquipmentSlot.FEET;
            case LEGS -> LinkedEquipmentSlot.LEGS;
            case CHEST -> LinkedEquipmentSlot.CHEST;
            case HEAD -> LinkedEquipmentSlot.HEAD;
            case BODY -> LinkedEquipmentSlot.BODY;
        };
    }

    public EquipmentSlot getLink() {
        return link;
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }
}
