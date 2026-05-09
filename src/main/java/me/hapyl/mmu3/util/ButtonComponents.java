package me.hapyl.mmu3.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.NonExtendable
public interface ButtonComponents {

    @NotNull
    static Component left(@NotNull String clickTo) {
        return of(Component.text("Left-Click to " + clickTo, TextColor.color(0xDAA520)));
    }

    @NotNull
    static Component right(@NotNull String clickTo) {
        return of(Component.text("Right-Click to " + clickTo, TextColor.color(0xFF8C00)));
    }

    @NotNull
    static Component middle(@NotNull String clickTo) {
        return of(Component.text("Middle-click to " + clickTo, TextColor.color(0xFF69B4)));
    }

    @NotNull
    static Component of(@NotNull Component value) {
        class Holder {
            private static final Component PREFIX = Component.text("◦", NamedTextColor.DARK_GRAY);
        }

        return Holder.PREFIX.appendSpace().append(value);
    }

}
