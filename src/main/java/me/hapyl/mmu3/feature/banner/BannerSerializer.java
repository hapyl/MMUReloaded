package me.hapyl.mmu3.feature.banner;

import me.hapyl.mmu3.util.EncodedString;
import me.hapyl.mmu3.util.ItemBuilder;
import org.bukkit.DyeColor;
import org.bukkit.NamespacedKey;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.util.NumberConversions;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Optional;

public final class BannerSerializer {

    @Nonnull
    public static EncodedString serialize(@Nonnull BannerData data) {
        final StringBuilder builder = new StringBuilder(data.getBaseColor().ordinal() + ";");

        for (Pattern pattern : data.getPatterns()) {
            final NamespacedKey patternKey = BannerEditor.REGISTRY.getKeyOrThrow(pattern.getPattern());

            builder.append(patternKey.getKey())
                    .append(":")
                    .append(pattern.getColor().ordinal())
                    .append(";");
        }

        return EncodedString.encode(builder.toString().getBytes());
    }

    @Nonnull
    public static Optional<ItemStack> deserialize(@Nonnull String base64) {
        try {
            final String string = EncodedString.decode(base64);
            final String[] layers = string.split(";");

            // First layer is always the base color
            final BannerColor bannerColor = enumByOrdinal(BannerColor.class, NumberConversions.toInt(layers[0]));
            final ItemBuilder builder = new ItemBuilder(bannerColor.getMaterial());

            // Parse banner patterns
            for (int i = 1; i < layers.length; i++) {
                final String[] patternSplits = layers[i].split(":");

                final PatternType patternType = BannerEditor.REGISTRY.getOrThrow(NamespacedKey.minecraft(patternSplits[0]));
                final DyeColor color = enumByOrdinal(DyeColor.class, NumberConversions.toInt(patternSplits[1]));

                builder.editMeta(BannerMeta.class, meta -> meta.addPattern(new Pattern(color, patternType)));
            }

            return Optional.of(builder.build());
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    @NotNull
    private static <E extends Enum<E>> E enumByOrdinal(@NotNull Class<E> enumClass, int ordinal) {
        final E[] enumConstants = enumClass.getEnumConstants();

        if (ordinal < 0 || ordinal >= enumConstants.length) {
            throw new IllegalArgumentException("Enum constant ordinal %s is out of bounds for %s!".formatted(
                    ordinal,
                    enumClass.getSimpleName()
            ));
        }

        return enumConstants[ordinal];
    }

}