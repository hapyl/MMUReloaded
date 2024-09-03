package me.hapyl.mmu3.feature.banner;

import me.hapyl.eterna.module.inventory.ItemBuilder;
import me.hapyl.eterna.module.registry.Key;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Consumer;

public class BannerItemBuilder extends ItemBuilder {

    BannerItemBuilder(BannerData data) {
        this(data.baseColor.material);
    }

    public BannerItemBuilder setPattern(@Nonnull PatternType type, @Nonnull DyeColor color) {
        return modify(meta -> {
            meta.setPatterns(List.of(new Pattern(color, type)));
        });
    }

    public BannerItemBuilder setPattern(Pattern pattern) {
        return modify(meta -> meta.setPatterns(List.of(pattern)));
    }

    public BannerItemBuilder setPattern(List<Pattern> patterns) {
        return modify(meta -> meta.setPatterns(patterns));
    }

    private BannerItemBuilder modify(Consumer<BannerMeta> consumer) {
        this.modifyMeta(BannerMeta.class, consumer);
        return this;
    }

    private BannerItemBuilder(@Nonnull Material material) {
        super(material);
    }

    private BannerItemBuilder(@Nonnull ItemStack stack) {
        super(stack);
    }

    private BannerItemBuilder(@Nonnull Material material, @Nonnull Key key) {
        super(material, key);
    }

    private BannerItemBuilder(@Nonnull ItemStack stack, @Nonnull Key key) {
        super(stack, key);
    }
}
