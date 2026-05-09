package me.hapyl.mmu3.command;

import me.hapyl.mmu3.MMULogger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class MMUOperationPersonalTime implements MMUOperation {

    private final Map<String, Long> namedTimeMap = Map.of(
            "day", 1000L,
            "night", 13000L,
            "midnight", 18000L,
            "noon", 6000L
    );

    @Override
    public @NotNull String name() {
        return "personal_time";
    }

    @Override
    public @NotNull String description() {
        return "Allows modifying player's personal time.";
    }

    @Override
    public @NotNull MMUCompletion completions() {
        return MMUCompletion.builder()
                .where(0, List.of("reset", "set", "add", "remove", "pause"))
                .where(1, namedTimeMap.keySet());
    }

    @Override
    public void process(@NotNull Player player, @NotNull ArgumentList args) {
        final long playerTime = player.getPlayerTime();
        final boolean playerTimeRelative = player.isPlayerTimeRelative();

        // Show the current time
        if (args.length == 0) {
            MMULogger.success(
                    player,
                    Component.empty()
                            .append(Component.text("Your personal time is "))
                            .append(Component.text(playerTime, NamedTextColor.GREEN))
                            .append(Component.text(". "))
                            .append(Component.text("(%s)".formatted(playerTimeRelative ? "Relative" : "Absolute"), NamedTextColor.DARK_GRAY))
            );
            return;
        }

        final String argument = args.get(0).toString().toLowerCase();

        if (args.length == 1 && argument.equals("reset")) {
            player.resetPlayerTime();

            MMULogger.success(player, Component.text("Reset your personal time!"));
        }
        else {
            final long value = args.get(1).toLong();

            switch (argument) {
                case "set" -> {
                    adjustPersonalTime(player, value, (a, b) -> b);

                    MMULogger.success(
                            player,
                            Component.empty()
                                    .append(Component.text("Set your personal time to "))
                                    .append(Component.text(value, NamedTextColor.GREEN))
                                    .append(Component.text("."))

                    );
                }
                case "add" -> {
                    adjustPersonalTime(player, value, Long::sum);

                    MMULogger.success(
                            player,
                            Component.empty()
                                    .append(Component.text("Added "))
                                    .append(Component.text(value, NamedTextColor.GREEN))
                                    .append(Component.text(" to your personal time, current time is now "))
                                    .append(Component.text(player.getPlayerTime(), NamedTextColor.GREEN))
                                    .append(Component.text("."))
                    );
                }
                case "remove" -> {
                    adjustPersonalTime(player, value, (a, b) -> a - b);

                    MMULogger.success(
                            player,
                            Component.empty()
                                    .append(Component.text("Removed "))
                                    .append(Component.text(value, NamedTextColor.GREEN))
                                    .append(Component.text(" from your personal time, current time is now "))
                                    .append(Component.text(player.getPlayerTime(), NamedTextColor.GREEN))
                                    .append(Component.text("."))
                    );
                }

                case "pause" -> {
                    player.setPlayerTime(playerTime, !playerTimeRelative);

                    MMULogger.success(
                            player,
                            Component.empty()
                                    .append(Component.text("Your personal time is now "))
                                    .append(playerTimeRelative
                                            ? Component.text("paused", NamedTextColor.GREEN)
                                            : Component.text("resumed", NamedTextColor.GREEN)
                                    )
                                    .append(Component.text("."))
                    );
                }
            }
        }

    }

    private void adjustPersonalTime(@NotNull Player player, long time, @NotNull BiFunction<Long, Long, Long> adjustment) {
        final long playerTime = player.getPlayerTime();
        final long newPlayerTime = adjustment.apply(playerTime, time);

        player.setPlayerTime(newPlayerTime, player.isPlayerTimeRelative());
    }

}
