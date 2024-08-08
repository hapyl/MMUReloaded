package me.hapyl.mmu3.feature.trim;

import me.hapyl.eterna.module.chat.Chat;
import org.bukkit.Material;

import javax.annotation.Nonnull;

public enum TrimArmor implements EnumTrim {

    LEATHER(Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS),
    CHAINMAIL(Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS),
    IRON(Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS),
    GOLD(Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS),
    DIAMOND(Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS),
    NETHERITE(Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS),
    TURTLE(Material.TURTLE_HELMET, Material.TURTLE_HELMET, Material.TURTLE_HELMET, Material.TURTLE_HELMET);

    private final Material[] materials;

    TrimArmor(Material helmet, Material chest, Material legs, Material feet) {
        this.materials = new Material[] { helmet, chest, legs, feet };
    }

    @Nonnull
    public Material getMaterial(@Nonnull TrimType slot) {
        return materials[slot == TrimType.HELMET ? 0 : slot == TrimType.CHESTPLATE ? 1 : slot == TrimType.LEGGINGS ? 2 : 3];
    }

    public boolean contains(@Nonnull Material material) {
        for (Material mat : this.materials) {
            if (mat == material) {
                return true;
            }
        }

        return false;
    }

    public Material[] getMaterials() {
        return materials;
    }

    @Override
    @Nonnull
    public String getName() {
        return Chat.format(name());
    }

    @Override
    @Nonnull
    public Material getMaterial() {
        return materials[0];
    }
}
