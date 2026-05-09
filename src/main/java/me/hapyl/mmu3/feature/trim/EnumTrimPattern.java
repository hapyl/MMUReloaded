package me.hapyl.mmu3.feature.trim;

import me.hapyl.mmu3.util.ComponentHelper;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public enum EnumTrimPattern implements EnumTrim {

    BOLT(TrimPattern.BOLT),
    COAST(TrimPattern.COAST),
    DUNE(TrimPattern.DUNE),
    EYE(TrimPattern.EYE),
    FLOW(TrimPattern.FLOW),
    HOST(TrimPattern.HOST),
    RAISER(TrimPattern.RAISER),
    RIB(TrimPattern.RIB),
    SENTRY(TrimPattern.SENTRY),
    SHAPER(TrimPattern.SHAPER),
    SILENCE(TrimPattern.SILENCE),
    SNOUT(TrimPattern.SNOUT),
    SPIRE(TrimPattern.SPIRE),
    TIDE(TrimPattern.TIDE),
    VEX(TrimPattern.VEX),
    WARD(TrimPattern.WARD),
    WAYFINDER(TrimPattern.WAYFINDER),
    WILD(TrimPattern.WILD);

    public final TrimPattern bukkit;
    public final Component trimName;

    EnumTrimPattern(@NotNull TrimPattern bukkit) {
        this.bukkit = bukkit;
        this.trimName = ComponentHelper.capitalize(this);
    }

    @Override
    public @NotNull Component trimName() {
        return trimName;
    }

    @Nonnull
    public static EnumTrimPattern fromBukkit(@Nonnull TrimPattern pattern) {
        for (EnumTrimPattern trimPattern : values()) {
            if (trimPattern.bukkit == pattern) {
                return trimPattern;
            }
        }

        throw new IllegalStateException("Invalid pattern.");
    }

}
