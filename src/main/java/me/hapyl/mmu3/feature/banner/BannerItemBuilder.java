package me.hapyl.mmu3.feature.banner;

import me.hapyl.mmu3.util.ItemBuilder;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.meta.BannerMeta;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;

public class BannerItemBuilder extends ItemBuilder {

    BannerItemBuilder(@NotNull BannerData data) {
        super(data.getBaseColor().getMaterial());
    }

    @NotNull
    public BannerItemBuilder setPattern(@NotNull List<Pattern> patterns) {
        return (BannerItemBuilder) editMeta(BannerMeta.class, meta -> meta.setPatterns(patterns));
    }

    @NotNull
    public BannerItemBuilder setPattern(@NotNull Pattern pattern) {
        return setPattern(List.of(pattern));
    }

    @NotNull
    public BannerItemBuilder setPattern(@Nonnull PatternType type, @Nonnull DyeColor color) {
        return setPattern(List.of(new Pattern(color, type)));
    }

}