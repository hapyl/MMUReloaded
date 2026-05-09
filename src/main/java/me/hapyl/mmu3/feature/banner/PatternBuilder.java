package me.hapyl.mmu3.feature.banner;

import me.hapyl.mmu3.util.ComponentHelper;
import me.hapyl.mmu3.util.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;

public class PatternBuilder {
    
    private static final Map<DyeColor, Material> COLOR_TO_MATERIAL_MAP = Map.ofEntries(
            Map.entry(DyeColor.WHITE, Material.WHITE_DYE),
            Map.entry(DyeColor.ORANGE, Material.ORANGE_DYE),
            Map.entry(DyeColor.MAGENTA, Material.MAGENTA_DYE),
            Map.entry(DyeColor.LIGHT_BLUE, Material.LIGHT_BLUE_DYE),
            Map.entry(DyeColor.YELLOW, Material.YELLOW_DYE),
            Map.entry(DyeColor.LIME, Material.LIME_DYE),
            Map.entry(DyeColor.PINK, Material.PINK_DYE),
            Map.entry(DyeColor.GRAY, Material.GRAY_DYE),
            Map.entry(DyeColor.LIGHT_GRAY, Material.LIGHT_GRAY_DYE),
            Map.entry(DyeColor.CYAN, Material.CYAN_DYE),
            Map.entry(DyeColor.PURPLE, Material.PURPLE_DYE),
            Map.entry(DyeColor.BLUE, Material.BLUE_DYE),
            Map.entry(DyeColor.BROWN, Material.BROWN_DYE),
            Map.entry(DyeColor.GREEN, Material.GREEN_DYE),
            Map.entry(DyeColor.RED, Material.RED_DYE),
            Map.entry(DyeColor.BLACK, Material.BLACK_DYE)
    );
    
    private static final Pattern DEFAULT_PATTERN = new Pattern(DyeColor.WHITE, PatternType.BASE);
    
    private DyeColor color;
    private PatternType pattern;
    
    private PatternBuilder(@NotNull Pattern pattern) {
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
    public Pattern build() {
        return new Pattern(color, pattern);
    }
    
    @Nonnull
    public ItemBuilder getColorAsBuilder() {
        return new ItemBuilder(dyeColorToMaterial(color));
    }
    
    public @NotNull Component getPatternName() {
        final NamespacedKey patternKey = BannerEditor.REGISTRY.getKey(pattern);
        
        return Component.text(patternKey != null ? patternKey.getKey() : "unknown");
    }
    
    public @NotNull Component getColorName() {
        return ComponentHelper.capitalize(color);
    }
    
    @NotNull
    public static PatternBuilder empty() {
        return new PatternBuilder(DEFAULT_PATTERN);
    }
    
    @Nonnull
    public static PatternBuilder builder(@NotNull Pattern pattern) {
        return new PatternBuilder(pattern);
    }
    
    @NotNull
    public static Material dyeColorToMaterial(@NotNull DyeColor color) {
        return Objects.requireNonNull(COLOR_TO_MATERIAL_MAP.get(color), "Illegal dye color: %s".formatted(color));
    }
}