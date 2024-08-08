package me.hapyl.mmu3.feature.banner;

import com.google.common.collect.Maps;
import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.util.ColorConverter;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;

public class MutablePattern {

    private static final Map<DyeColor, Material> colorToMaterial;

    static {
        colorToMaterial = Maps.newHashMap();

        colorToMaterial.put(DyeColor.WHITE, Material.WHITE_DYE);
        colorToMaterial.put(DyeColor.ORANGE, Material.ORANGE_DYE);
        colorToMaterial.put(DyeColor.MAGENTA, Material.MAGENTA_DYE);
        colorToMaterial.put(DyeColor.LIGHT_BLUE, Material.LIGHT_BLUE_DYE);
        colorToMaterial.put(DyeColor.YELLOW, Material.YELLOW_DYE);
        colorToMaterial.put(DyeColor.LIME, Material.LIME_DYE);
        colorToMaterial.put(DyeColor.PINK, Material.PINK_DYE);
        colorToMaterial.put(DyeColor.GRAY, Material.GRAY_DYE);
        colorToMaterial.put(DyeColor.LIGHT_GRAY, Material.LIGHT_GRAY_DYE);
        colorToMaterial.put(DyeColor.CYAN, Material.CYAN_DYE);
        colorToMaterial.put(DyeColor.PURPLE, Material.PURPLE_DYE);
        colorToMaterial.put(DyeColor.BLUE, Material.BLUE_DYE);
        colorToMaterial.put(DyeColor.BROWN, Material.BROWN_DYE);
        colorToMaterial.put(DyeColor.GREEN, Material.GREEN_DYE);
        colorToMaterial.put(DyeColor.RED, Material.RED_DYE);
        colorToMaterial.put(DyeColor.BLACK, Material.BLACK_DYE);
    }

    private DyeColor color;
    private PatternType pattern;

    public MutablePattern() {
        this.color = DyeColor.BLACK;
        this.pattern = PatternType.BASE;
    }

    public MutablePattern(@Nonnull Pattern pattern) {
        this.color = pattern.getColor();
        this.pattern = pattern.getPattern();
    }

    @Nonnull
    public DyeColor getColor() {
        return color;
    }

    public void setColor(@Nonnull DyeColor color) {
        this.color = color;
    }

    @Nonnull
    public PatternType getPattern() {
        return pattern;
    }

    public void setPattern(@Nonnull PatternType pattern) {
        this.pattern = pattern;
    }

    @Nonnull
    public Pattern asPattern() {
        return new Pattern(this.color, this.pattern);
    }

    @Nonnull
    public Material getColorAsItem() {
        return colorToMaterial(color);
    }

    @Nonnull
    public static Material colorToMaterial(@Nonnull DyeColor color) {
        return Objects.requireNonNull(colorToMaterial.get(color));
    }

    @Nonnull
    public static DyeColor materialToColor(@Nonnull Material material) {
        for (Map.Entry<DyeColor, Material> entry : colorToMaterial.entrySet()) {
            if (entry.getValue() == material) {
                return entry.getKey();
            }
        }

        throw new IllegalArgumentException(material.name());
    }

    @Nonnull
    public String getPatternName() {
        return Chat.capitalize(pattern);
    }

    @Nonnull
    public String getColorName() {
        return ColorConverter.DYE_COLOR.toChatColor(color) + Chat.capitalize(color);
    }

    @Nonnull
    public static MutablePattern of(@Nullable Pattern pattern) {
        return pattern != null ? new MutablePattern(pattern) : new MutablePattern();
    }
}
