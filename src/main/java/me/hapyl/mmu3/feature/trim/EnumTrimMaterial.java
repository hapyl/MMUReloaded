package me.hapyl.mmu3.feature.trim;

import me.hapyl.mmu3.util.ComponentHelper;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public enum EnumTrimMaterial implements EnumTrim {

    AMETHYST(TrimMaterial.AMETHYST),
    COPPER(TrimMaterial.COPPER),
    DIAMOND(TrimMaterial.DIAMOND),
    EMERALD(TrimMaterial.EMERALD),
    GOLD(TrimMaterial.GOLD),
    IRON(TrimMaterial.IRON),
    LAPIS(TrimMaterial.LAPIS),
    NETHERITE(TrimMaterial.NETHERITE),
    QUARTZ(TrimMaterial.QUARTZ),
    REDSTONE(TrimMaterial.REDSTONE),
    RESIN(TrimMaterial.RESIN);

    public final TrimMaterial bukkit;
    public final Component trimName;

    EnumTrimMaterial(@NotNull TrimMaterial bukkit) {
        this.bukkit = bukkit;
        this.trimName = ComponentHelper.capitalize(this);
    }

    @Override
    @Nonnull
    public Component trimName() {
        return trimName;
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
