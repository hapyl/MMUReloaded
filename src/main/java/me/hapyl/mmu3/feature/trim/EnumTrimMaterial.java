package me.hapyl.mmu3.feature.trim;

import me.hapyl.spigotutils.module.chat.Chat;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public enum EnumTrimMaterial implements EnumTrim {

    QUARTZ(TrimMaterial.QUARTZ, Material.QUARTZ),
    IRON(TrimMaterial.IRON, Material.IRON_INGOT),
    NETHERITE(TrimMaterial.NETHERITE, Material.NETHERITE_INGOT),
    REDSTONE(TrimMaterial.REDSTONE, Material.REDSTONE),
    COPPER(TrimMaterial.COPPER, Material.COPPER_INGOT),
    GOLD(TrimMaterial.GOLD, Material.GOLD_INGOT),
    EMERALD(TrimMaterial.EMERALD, Material.EMERALD),
    DIAMOND(TrimMaterial.DIAMOND, Material.DIAMOND),
    LAPIS(TrimMaterial.LAPIS, Material.LAPIS_LAZULI),
    AMETHYST(TrimMaterial.AMETHYST, Material.AMETHYST_SHARD);

    public final TrimMaterial bukkit;
    public final Material material;

    EnumTrimMaterial(TrimMaterial bukkit, Material material) {
        this.bukkit = bukkit;
        this.material = material;
    }

    @Override
    @Nonnull
    public String getName() {
        return Chat.format(name());
    }

    @Override
    @Nonnull
    public Material getMaterial() {
        return material;
    }

    @Nullable
    public static EnumTrimMaterial fromItem(ItemStack item) {
        final ArmorTrim trim = EnumTrimPattern.getTrim(item);

        return trim != null ? fromBukkit(trim.getMaterial()) : null;
    }

    @Nonnull
    public static EnumTrimMaterial fromBukkit(@Nonnull TrimMaterial material) {
        for (EnumTrimMaterial trimMaterial : values()) {
            if (trimMaterial.bukkit == material) {
                return trimMaterial;
            }
        }

        throw new IllegalArgumentException("Invalid material.");
    }
}
