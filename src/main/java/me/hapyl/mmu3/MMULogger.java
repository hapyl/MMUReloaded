package me.hapyl.mmu3;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.NonExtendable
public interface MMULogger {

    @NotNull Component CHAR_ARROW = Component.text("➤");
    @NotNull Component CHAR_SUCCESS = Component.text("✔");
    @NotNull Component CHAR_ERROR = Component.text("✘");
    @NotNull Component CHAR_COPY = Component.text("\uD83D\uDDB1");

    @NotNull Component PREFIX = Component.empty()
            .append(Component.text("MMU", Colors.BRAND_COLOR))
            .appendSpace()
            .append(CHAR_ARROW.color(NamedTextColor.DARK_GRAY));

    @NotNull Component SUFFIX_COPY = Component.text("ᴄᴏᴘʏ", Colors.BRAND_COLOR, TextDecoration.BOLD);

    static void sound(@NotNull Player player, @NotNull Sound sound, float pitch) {
        player.playSound(player, sound, 3, Math.clamp(pitch, 0.0f, 2.0f));
    }

    static void info(@NotNull Audience audience, @NotNull Component message) {
        audience.sendMessage(PREFIX.appendSpace().append(message.color(Colors.TEXT_COLOR)));
    }

    static void success(@NotNull Audience audience, @NotNull Component message) {
        audience.sendMessage(PREFIX.appendSpace()
                .append(CHAR_SUCCESS.color(Colors.SUCCESS))
                .appendSpace()
                .append(message.color(Colors.TEXT_COLOR)));
    }

    static void error(@NotNull Audience audience, @NotNull Component message) {
        audience.sendMessage(PREFIX.appendSpace()
                .append(CHAR_ERROR.color(Colors.ERROR))
                .appendSpace()
                .append(message.color(Colors.TEXT_COLOR)));
    }

    static void copy(@NotNull Audience audience, @NotNull Component message, @NotNull ClickEvent clickEvent) {
        info(
                audience,
                Component.empty()
                        .append(CHAR_COPY.color(NamedTextColor.DARK_GRAY))
                        .appendSpace()
                        .append(message)
                        .appendSpace()
                        .append(SUFFIX_COPY)
                        .hoverEvent(HoverEvent.showText(Component.text("Click to copy!", NamedTextColor.YELLOW)))
                        .clickEvent(clickEvent)
        );
    }

    static void debug(@NotNull Object message) {
        final TextComponent component = Component.empty()
                .append(Component.text("[DEBUG]", NamedTextColor.RED, TextDecoration.BOLD))
                .appendSpace()
                .append(Component.text(String.valueOf(message), NamedTextColor.YELLOW));


        // Send debug to operators
        Bukkit.getOnlinePlayers().stream()
                .filter(Player::isOp)
                .forEach(player -> player.sendMessage(component));
    }

}
