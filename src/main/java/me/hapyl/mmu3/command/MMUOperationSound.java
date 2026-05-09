package me.hapyl.mmu3.command;

import me.hapyl.mmu3.MMULogger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MMUOperationSound implements MMUOperation {

    private final Map<String, Sound> soundMap = Registry.SOUNDS.keyStream()
            .map(key -> Map.entry(key.getKey().toLowerCase(), Registry.SOUNDS.getOrThrow(key)))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    @Override
    public @NotNull String name() {
        return "sound";
    }

    @Override
    public @NotNull String description() {
        return "Allows playing sounds and generating minecraft commands.";
    }

    @Override
    public @NotNull MMUCompletion completions() {
        return MMUCompletion.builder()
                .where(0, soundMap.keySet())
                .where(2, List.of("all"));
    }

    @Override
    public void process(@NotNull Player player, @NotNull ArgumentList args) {
        // sound (sound) [pitch] [all]
        if (args.length == 0) {
            MMULogger.error(player, Component.text("Missing required argument (sound)."));
            return;
        }

        final Sound sound = soundMap.get(args.get(0).toString().toLowerCase());

        if (sound == null) {
            MMULogger.error(player, Component.text("Invalid sound %s!".formatted(args.get(0))));
            return;
        }

        final float pitch = Math.clamp(args.get(1).toFloat(1f), 0f, 2f);

        final boolean global = args.get(2).toString().equals("all");
        final SoundPlayer soundPlayer = global ? SoundPlayer.all() : SoundPlayer.self(player);

        soundPlayer.play(sound, pitch);

        final NamespacedKey soundKey = Registry.SOUNDS.getKeyOrThrow(sound);

        MMULogger.success(
                player,
                Component.empty()
                        .append(Component.text("Played sound `"))
                        .append(Component.text(soundKey.getKey(), NamedTextColor.GREEN))
                        .append(Component.text("` to "))
                        .append(Component.text(global ? "everyone" : "you"))
                        .append(Component.text("."))
        );

        MMULogger.copy(
                player,
                Component.text("Minecraft Command"),
                ClickEvent.copyToClipboard("playsound %s master @s ~ ~ ~ 3 %.1f".formatted(soundKey.getKey(), pitch))
        );

        MMULogger.copy(
                player,
                Component.text("Bukkit Code"),
                ClickEvent.copyToClipboard("player.playSound(player, %s, 3, %.1f)".formatted(soundKey.getKey().replace(".", "_"), pitch))
        );
    }

    interface SoundPlayer {

        void play(@NotNull Sound sound, @Range(from = 0, to = 2) float pitch);

        @NotNull
        static SoundPlayer self(@NotNull Player player) {
            return (sound, pitch) -> player.playSound(player, sound, 3, pitch);
        }

        @NotNull
        static SoundPlayer all() {
            class Holder {
                private static final SoundPlayer ALL = (sound, pitch) -> {
                    Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player, sound, 3, pitch));
                };
            }

            return Holder.ALL;
        }

    }

}
