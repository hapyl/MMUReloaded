package me.hapyl.mmu3.feature.banner;

import me.hapyl.eterna.module.util.ColorConverter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;

public enum BaseBannerColor {

    WHITE(DyeColor.WHITE, Material.WHITE_BANNER),
    LIGHT_GRAY(DyeColor.LIGHT_GRAY, Material.LIGHT_GRAY_BANNER),
    GRAY(DyeColor.GRAY, Material.GRAY_BANNER),
    BLACK(DyeColor.BLACK, Material.BLACK_BANNER),
    BROWN(DyeColor.BROWN, Material.BROWN_BANNER),
    RED(DyeColor.RED, Material.RED_BANNER),
    ORANGE(DyeColor.ORANGE, Material.ORANGE_BANNER),
    YELLOW(DyeColor.YELLOW, Material.YELLOW_BANNER),
    LIME(DyeColor.LIME, Material.LIME_BANNER),
    GREEN(DyeColor.GREEN, Material.GREEN_BANNER),
    CYAN(DyeColor.CYAN, Material.CYAN_BANNER),
    LIGHT_BLUE(DyeColor.LIGHT_BLUE, Material.LIGHT_BLUE_BANNER),
    BLUE(DyeColor.BLUE, Material.BLUE_BANNER),
    PURPLE(DyeColor.PURPLE, Material.PURPLE_BANNER),
    MAGENTA(DyeColor.MAGENTA, Material.MAGENTA_BANNER),
    PINK(DyeColor.PINK, Material.PINK_BANNER);

    public final ChatColor color;
    public final Material material;

    BaseBannerColor(DyeColor color, Material material) {
        this.color = ColorConverter.DYE_COLOR.toChatColor(color);
        this.material = material;
    }
}
