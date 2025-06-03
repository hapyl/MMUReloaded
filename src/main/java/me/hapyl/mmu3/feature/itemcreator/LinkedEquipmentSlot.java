package me.hapyl.mmu3.feature.itemcreator;

import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlotGroup;

import javax.annotation.Nonnull;

@SuppressWarnings("UnstableApiUsage")
// honestly fuck paper for deprecating the old methods and adding experimental new methods, like fuck off?
public enum LinkedEquipmentSlot {

    ALL_SLOTS(
            EquipmentSlotGroup.ANY, Material.PUFFERFISH, """
            Applicable to any slot.
            """
    ),

    HAND(
            EquipmentSlotGroup.HAND, Material.STONE_SWORD, """
            Applicable only if in main hand.
            """
    ),
    OFF_HAND(
            EquipmentSlotGroup.OFFHAND, Material.SHIELD, """
            Applicable only if in off hand.
            """
    ),

    HELMET(
            EquipmentSlotGroup.HEAD, Material.IRON_HELMET, """
            Applicable only if on head.
            """
    ),
    CHESTPLATE(
            EquipmentSlotGroup.CHEST, Material.IRON_CHESTPLATE, """
            Applicable only if on chest.
            """
    ),
    LEGGINGS(
            EquipmentSlotGroup.LEGS, Material.IRON_LEGGINGS, """
            Applicable only if on legs.
            """
    ),
    BOOTS(
            EquipmentSlotGroup.FEET, Material.IRON_BOOTS, """
            Applicable only if on feet.
            """
    ),
    BODY(
            EquipmentSlotGroup.BODY, Material.LEATHER_HORSE_ARMOR, """
            Applicable only on non-player body, like horse armor, llama carpet, wolf armor, etc.
            """
    ),
    SADDLE(
            EquipmentSlotGroup.SADDLE, Material.SADDLE, """
            Applicable only on saddles.
            """
    );

    private final EquipmentSlotGroup link;
    private final Material material;
    private final String description;

    LinkedEquipmentSlot(EquipmentSlotGroup link, Material material, String description) {
        this.link = link;
        this.material = material;
        this.description = description;
    }

    public EquipmentSlotGroup getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public Material getMaterial() {
        return material;
    }

    @Nonnull
    public static LinkedEquipmentSlot ofLink(@Nonnull EquipmentSlotGroup slotGroup) {
        for (LinkedEquipmentSlot value : values()) {
            if (value.link == slotGroup) {
                return value;
            }
        }

        throw new IllegalArgumentException("Unsupported slot: " + slotGroup);
    }
}
