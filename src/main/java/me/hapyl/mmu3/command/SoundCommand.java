package me.hapyl.mmu3.command;

import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.chat.LazyClickEvent;
import me.hapyl.eterna.module.chat.LazyEvent;
import me.hapyl.eterna.module.chat.LazyHoverEvent;
import me.hapyl.eterna.module.command.SimplePlayerAdminCommand;
import me.hapyl.eterna.module.math.Numbers;
import me.hapyl.eterna.module.player.PlayerLib;
import me.hapyl.mmu3.message.Message;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SoundCommand extends SimplePlayerAdminCommand {

    private final Registry<Sound> registry = Registry.SOUNDS;

    public SoundCommand(String name) {
        super(name);

        setDescription("Allows playing sounds easily.");
        setAliases("snd");
    }

    @Override
    protected void execute(Player player, String[] args) {
        // sound (Sound) [Pitch] [Self, All]
        // sound command (Sound) (Pitch)
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("command") && (args.length == 3)) {
                final Sound sound = registry.get(NamespacedKey.minecraft(args[1]));
                final float pitch = Math.clamp(Numbers.getFloat(args[2]), 0.0f, 2.0f);

                if (sound == null) {
                    Message.error(player, "Invalid sound.");
                    return;
                }

                // execute play at self location
                Message.clickHover(
                        player,
                        LazyClickEvent.SUGGEST_COMMAND.of("execute as @a at @s run playsound %s record @s ~ ~ ~ 1 %s".formatted(
                                sound.getKey().getKey(),
                                pitch
                        )),
                        LazyHoverEvent.SHOW_TEXT.of("&aClick to copy Minecraft command."),
                        "&e&lCLICK &ato copy Minecraft command."
                );

                // execute play at target location
                final Block block = player.getTargetBlockExact(50);
                if (block != null) {
                    final Location location = block.getLocation();
                    final String blockName = Chat.capitalize(block.getType());

                    Message.clickHover(
                            player,
                            LazyClickEvent.SUGGEST_COMMAND.of("execute as @a at @s run playsound %s record @s %s %s %s 1 %s".formatted(
                                    sound.getKey().getKey(),
                                    location.getBlockX(),
                                    location.getBlockY(),
                                    location.getBlockZ(),
                                    pitch
                            )),
                            LazyHoverEvent.SHOW_TEXT.of("&2Click to copy Minecraft command at %s.".formatted(blockName)),
                            "&e&lCLICK &2to copy Minecraft command at %s.".formatted(blockName)
                    );
                }

                // player lib
                Message.clickHover(
                        player,
                        LazyClickEvent.SUGGEST_COMMAND.of("PlayerLib.playSound(player, Sound.%s, %sf);".formatted(sound, pitch)),
                        LazyHoverEvent.SHOW_TEXT.of("&bClick to copy PlayerLib code."),
                        "&e&lCLICK &bto copy PlayerLib code."
                );
                return;
            }

            final Sound sound = registry.get(NamespacedKey.minecraft(args[0]));
            final float pitch = args.length >= 2 ? Math.clamp(Numbers.getFloat(args[1]), 0.0f, 2.0f) : 1.0f;
            final boolean playAll = args.length >= 3 && (args[2].equalsIgnoreCase("all") || args[2].equalsIgnoreCase("a"));

            if (sound == null) {
                Message.error(player, "Invalid sound.");
                return;
            }

            if (playAll) {
                Bukkit.getOnlinePlayers().forEach(pl -> {
                    if (pl == player) {
                        return;
                    }
                    PlayerLib.playSound(pl, sound, pitch);
                    Message.info(pl, "%s played %s (%s) sound to you.", player.getName(), getSoundName(sound), pitch);
                });
            }

            PlayerLib.playSound(player, sound, pitch);

            Message.success(
                    player,
                    "Played %s (%s) sound to %s.",
                    getSoundName(sound),
                    pitch,
                    !playAll ? "you" : "everyone"
            );
            Message.clickHover(
                    player,
                    LazyEvent.runCommand("/sound command %s %s".formatted(sound, pitch)),
                    LazyEvent.showText("&eClick to generate commands!"),
                    "&e&lCLICK HERE &6to generate commands!"
            );

            return;
        }

        Message.NOT_ENOUGH_ARGUMENTS_EXPECTED_AT_LEAST.send(player, "one");
    }

    private String getSoundName(Sound sound) {
        if (sound == null) {
            return "null";
        }

        final String value = sound.key().value();
        return Chat.capitalize(value.replace(".", " ").replace("_", " "));
    }

    @Override
    protected List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return completerSort2(registry.stream().map(s -> registry.getKeyOrThrow(s).value()).toList(), args);
        }
        else if (args.length == 3) {
            return completerSort(new String[] { "self", "all" }, args);
        }
        return null;
    }

}
