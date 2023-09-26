package me.hapyl.mmu3.feature.trim;

import me.hapyl.spigotutils.module.chat.Chat;
import org.bukkit.Material;
import org.bukkit.inventory.meta.trim.TrimMaterial;

import javax.annotation.Nonnull;

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
}
