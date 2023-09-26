package me.hapyl.mmu3.command;

import me.hapyl.mmu3.message.Message;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.chat.LazyClickEvent;
import me.hapyl.spigotutils.module.chat.LazyEvent;
import me.hapyl.spigotutils.module.chat.LazyHoverEvent;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import me.hapyl.spigotutils.module.player.PlayerLib;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Locale;

public class ParticleCommand extends SimplePlayerAdminCommand {
    public ParticleCommand(String name) {
        super(name);
        setDescription("Allows previewing particles above ones' head and generating commands for display.");
        setAliases("pp", "previewParticle");
    }

    @Override
    protected void execute(Player player, String[] args) {
        // particle (particle) (amount) (x) (y) (z) (speed)
        if (args.length == 0) {
            Message.NOT_ENOUGH_ARGUMENTS_EXPECTED_AT_LEAST.send(player, 6);
            return;
        }

        // Generated commands
        if (args.length == 7 && args[0].equalsIgnoreCase("command")) {
            // Minecraft
            Message.clickHover(
                    player,
                    LazyClickEvent.SUGGEST_COMMAND.of("particle %s ~ ~ ~ %s %s %s %s %s".formatted(
                            args[1].toLowerCase(Locale.ROOT),
                            args[3], args[4], args[5],
                            args[6],
                            args[2]
                    )),
                    LazyHoverEvent.SHOW_TEXT.of("&aClick to copy Minecraft command."),
                    "&e&lCLICK &ato copy Minecraft command."
            );

            // Minecraft with block cords
            final Block block = player.getTargetBlockExact(50);
            if (block != null) {
                final Location location = block.getLocation();
                final String blockName = Chat.capitalize(block.getType());

                Message.clickHover(
                        player,
                        LazyClickEvent.SUGGEST_COMMAND.of("particle %s %s %s %s %s %s %s %s %s".formatted(
                                args[1].toLowerCase(Locale.ROOT),
                                location.getBlockX(), location.getBlockY(), location.getBlockZ(),
                                args[3], args[4], args[5],
                                args[6],
                                args[2]
                        )),
                        LazyHoverEvent.SHOW_TEXT.of("&2Click to copy Minecraft command at %s.".formatted(blockName)),
                        "&e&lCLICK &2to copy Minecraft command at %s.".formatted(blockName)
                );
            }

            // PlayerLib
            Message.clickHover(
                    player,
                    LazyClickEvent.SUGGEST_COMMAND.of("PlayerLib.spawnParticle(player, location, Particle.%s, %s, %s, %s, %s, %sf);".formatted(
                            args[1].toUpperCase(Locale.ROOT),
                            args[2],
                            args[3], args[4], args[5],
                            args[6]
                    )),
                    LazyHoverEvent.SHOW_TEXT.of("&bClick to copy PlayerLib code."),
                    "&e&lCLICK &bto copy PlayerLib code."
            );

            return;
        }

        final Particle particle = getArgument(args, 0).toEnum(Particle.class);
        final int amount = getArgument(args, 1).toInt(1);
        final double offsetX = getArgument(args, 2).toDouble();
        final double offsetY = getArgument(args, 3).toDouble();
        final double offsetZ = getArgument(args, 4).toDouble();
        final float speed = getArgument(args, 5).toFloat(0.0f);

        if (particle == null) {
            Message.error(player, "Invalid particle.");
            return;
        }

        final Location displayLocation = player.getLocation().add(0.0d, 2.5d, 0.0d);
        PlayerLib.spawnParticle(player, displayLocation, particle, amount, offsetX, offsetY, offsetZ, speed);

        Message.success(
                player,
                "Displaying %s particle above your head.",
                Chat.capitalize(particle)
        );

        Message.clickHover(
                player,
                LazyEvent.runCommand("/particles command %s %s %s %s %s %s", particle.name(), amount, offsetX, offsetY, offsetZ, speed),
                LazyEvent.showText("&eClick to generate commands!"),
                "&e&lCLICK HERE &6to generate commands!"
        );
    }

    @Override
    protected List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return completerSort2(arrayToList(Particle.values()), args);
        }
        return null;
    }

}
