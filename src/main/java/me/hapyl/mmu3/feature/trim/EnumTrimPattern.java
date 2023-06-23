package me.hapyl.mmu3.feature.trim;

import me.hapyl.spigotutils.module.chat.Chat;
import org.bukkit.Material;
import org.bukkit.inventory.meta.trim.TrimPattern;

public enum EnumTrimPattern implements EnumTrim {
    SENTRY(TrimPattern.SENTRY, Material.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE),
    DUNE(TrimPattern.DUNE, Material.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE),
    COAST(TrimPattern.COAST, Material.COAST_ARMOR_TRIM_SMITHING_TEMPLATE),
    WILD(TrimPattern.WILD, Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE),
    WARD(TrimPattern.WARD, Material.WARD_ARMOR_TRIM_SMITHING_TEMPLATE),
    EYE(TrimPattern.EYE, Material.EYE_ARMOR_TRIM_SMITHING_TEMPLATE),
    VEX(TrimPattern.VEX, Material.VEX_ARMOR_TRIM_SMITHING_TEMPLATE),
    TIDE(TrimPattern.TIDE, Material.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE),
    SNOUT(TrimPattern.SNOUT, Material.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE),
    RIB(TrimPattern.RIB, Material.RIB_ARMOR_TRIM_SMITHING_TEMPLATE),
    SPIRE(TrimPattern.SPIRE, Material.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE),
    WAYFINDER(TrimPattern.WAYFINDER, Material.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE),
    SHAPER(TrimPattern.SHAPER, Material.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE),
    SILENCE(TrimPattern.SILENCE, Material.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE),
    RAISER(TrimPattern.RAISER, Material.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE),
    HOST(TrimPattern.HOST, Material.HOST_ARMOR_TRIM_SMITHING_TEMPLATE);

    public final TrimPattern bukkit;
    public final Material material;

    EnumTrimPattern(TrimPattern bukkit, Material material) {
        this.bukkit = bukkit;
        this.material = material;
    }

    @Override
    public String getName() {
        return Chat.format(name());
    }

    @Override
    public Material getMaterial() {
        return material;
    }
}
