package me.hapyl.mmu3.command;

import me.hapyl.mmu3.message.Message;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.chat.LazyClickEvent;
import me.hapyl.spigotutils.module.chat.LazyHoverEvent;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import me.hapyl.spigotutils.module.math.Numbers;
import me.hapyl.spigotutils.module.player.PlayerLib;
import me.hapyl.spigotutils.module.util.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SoundCommand extends SimplePlayerAdminCommand {
    public SoundCommand(String name) {
        super(name);
        setDescription("Allows to play sounds easily.");
        setAliases("snd");
    }

    @Override
    protected void execute(Player player, String[] args) {
        // sound (Sound) [Pitch] [Self, All]
        // sound command (Sound) (Pitch)
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("command") && (args.length == 3)) {
                final Sound sound = Validate.getEnumValue(Sound.class, args[1]);
                final float pitch = Numbers.clamp(Validate.getFloat(args[2]), 0.0f, 2.0f);

                if (sound == null) {
                    Message.error(player, "Invalid sound.");
                    return;
                }

                Message.clickHover(
                        player,
                        LazyClickEvent.SUGGEST_COMMAND.of("playsound %s record @s ~ ~ ~ 1 %s".formatted(
                                sound.getKey().getKey(),
                                pitch
                        )),
                        LazyHoverEvent.SHOW_TEXT.of("&7Click to copy Minecraft command."),
                        "&e&lCLICK &7to copy Minecraft command."
                );

                Message.clickHover(
                        player,
                        LazyClickEvent.SUGGEST_COMMAND.of("PlayerLib.playSound(player, Sound.%s, %sf);".formatted(
                                sound,
                                pitch
                        )),
                        LazyHoverEvent.SHOW_TEXT.of("&7Click to copy PlayerLib code."),
                        "&e&lCLICK &7to copy PlayerLib code."
                );
                return;
            }

            final Sound sound = Validate.getEnumValue(Sound.class, args[0]);
            final float pitch = args.length >= 2 ? Numbers.clamp(Validate.getFloat(args[1]), 0.0f, 2.0f) : 1.0f;
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
                    Message.info(pl, "%s played %s (%s) sound to you.", player.getName(), Chat.capitalize(sound), pitch);
                });
            }

            PlayerLib.playSound(player, sound, pitch);
            Message.clickHover(
                    player,
                    LazyClickEvent.RUN_COMMAND.of("/sound command %s %s", sound, pitch),
                    LazyHoverEvent.SHOW_TEXT.of("&7Click to generate commands."),
                    "Played %s (%s) sound to you. &e&lCLICK &7to generate commands.",
                    Chat.capitalize(sound), pitch
            );

            return;
        }

        Message.NOT_ENOUGH_ARGUMENTS_EXPECTED_AT_LEAST.send(player, "one");
    }

    @Override
    protected List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return completerSort2(arrayToList(Sound.values()), args);
        }
        else if (args.length == 3) {
            return completerSort(new String[] { "self", "all" }, args);
        }
        return null;
    }
}
