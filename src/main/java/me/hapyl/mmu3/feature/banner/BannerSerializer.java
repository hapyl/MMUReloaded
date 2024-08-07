package me.hapyl.mmu3.feature.banner;

import me.hapyl.mmu3.utils.SensitiveInput;
import me.hapyl.eterna.module.inventory.ItemBuilder;
import me.hapyl.eterna.module.math.Numbers;
import org.apache.commons.lang.SerializationException;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Base64;

public final class BannerSerializer {

    @Nonnull
    public static String serialize(@Nonnull BannerData data) {
        final StringBuilder builder = new StringBuilder(data.baseColor.ordinal() + ";");

        for (Pattern pattern : data.patterns) {
            builder.append(pattern.getPattern().ordinal()).append(":").append(pattern.getColor().ordinal()).append(";");
        }

        return Base64.getEncoder().encodeToString(builder.toString().getBytes());
    }

    @Nonnull
    @SensitiveInput(
            rule = "must be a valid base64 string",
            exception = { IllegalArgumentException.class, ArrayIndexOutOfBoundsException.class }
    )
    public static ItemStack deserialize(@Nonnull String base64) {
        final String string = new String(Base64.getDecoder().decode(base64));
        final String[] layerSplits = string.split(";");

        final BaseBannerColor baseColor = enumByOrdinal(BaseBannerColor.class, stringToInt(layerSplits[0]));

        final ItemBuilder builder = new ItemBuilder(baseColor.material);

        for (int i = 1; i < layerSplits.length; i++) {
            final String[] patternSplits = layerSplits[i].split(":");

            final PatternType pattern = enumByOrdinal(PatternType.class, stringToInt(patternSplits[0]));
            final DyeColor color = enumByOrdinal(DyeColor.class, stringToInt(patternSplits[1]));

            builder.addBannerPattern(pattern, color);
        }

        return builder.build();
    }

    @Nullable
    public static ItemStack deserializeSafe(@Nonnull String base64) {
        try {
            return deserialize(base64);
        } catch (Exception e) {
            return null;
        }
    }

    private static <E extends Enum<E>> E enumByOrdinal(Class<E> enumClass, int ordinal) {
        final E[] enumConstants = enumClass.getEnumConstants();

        if (ordinal < 0 || ordinal >= enumConstants.length) {
            throw new SerializationException("illegal ordinal: " + ordinal + " for " + enumClass.getSimpleName());
        }

        return enumConstants[ordinal];
    }

    private static int stringToInt(String string) {
        return Numbers.getInt(string, 0);
    }

}
