package me.hapyl.mmu3.feature.banner;

import me.hapyl.mmu3.util.ComponentHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public enum BannerColor implements ComponentLike {

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

    private final Material material;
    private final Component component;

    BannerColor(@NotNull DyeColor color, @NotNull Material material) {
        this.material = material;
        this.component = ComponentHelper.capitalize(this).color(TextColor.color(color.getColor().asRGB()));
    }

    @NotNull
    public Material getMaterial() {
        return material;
    }

    @Override
    public @NotNull Component asComponent() {
        return component;
    }
}