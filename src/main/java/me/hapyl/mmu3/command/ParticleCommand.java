package me.hapyl.mmu3.command;

import me.hapyl.mmu3.message.Message;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.chat.LazyClickEvent;
import me.hapyl.spigotutils.module.chat.LazyEvent;
import me.hapyl.spigotutils.module.chat.LazyHoverEvent;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import me.hapyl.spigotutils.module.player.PlayerLib;
import me.hapyl.spigotutils.module.util.Validate;
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
        setDescription("Allows to preview particles above ones head and generate commands for display.");
        setAliases("part");
    }

    @Override
    protected void execute(Player player, String[] args) {
        // particle (particle)
        // particle (particle) (speed)
        // particle (particle) (speed) (amount)
        // particle (particle) (oX) (oY) (oZ) (speed) (amount)
        // particle (command) (particle) (oX) (oY) (oZ) (speed) (amount)
        if (args.length == 0) {
            Message.NOT_ENOUGH_ARGUMENTS_EXPECTED_AT_LEAST.send(player, 1);
            return;
        }

        // Generated commands
        if (args.length == 7 && args[0].equalsIgnoreCase("command")) {
            // Minecraft
            Message.clickHover(
                    player,
                    LazyClickEvent.SUGGEST_COMMAND.of("particle %s ~ ~ ~ %s %s %s %s %s".formatted(
                            args[1].toLowerCase(Locale.ROOT),
                            args[2], args[3], args[4],
                            args[5],
                            args[6]
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
                                args[2], args[3], args[4],
                                args[5],
                                args[6]
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
                            args[6],
                            args[2], args[3], args[4],
                            args[5]
                    )),
                    LazyHoverEvent.SHOW_TEXT.of("&bClick to copy PlayerLib code"),
                    "&e&lCLICK &bto copy PlayerLib code."
            );

            return;
        }

        final Particle particle = Validate.getEnumValue(Particle.class, args[0]);

        float speed = args.length >= 2 ? Validate.getFloat(args[1]) : 0.0f;
        int amount = args.length >= 3 ? Validate.getInt(args[2]) : 1;

        double offsetX = 0;
        double offsetY = 0;
        double offsetZ = 0;

        if (args.length == 6) {
            offsetX = Validate.getDouble(args[1]);
            offsetY = Validate.getDouble(args[2]);
            offsetZ = Validate.getDouble(args[3]);
            speed = Validate.getFloat(args[4]);
            amount = Validate.getInt(args[5]);
        }

        if (particle == null) {
            Message.error(player, "Invalid particle.");
            return;
        }

        final Location displayLocation = player.getLocation().add(0.0d, 2.0d, 0.0f);

        // particle ash ~ ~ ~ 0 0 0 1 2

        PlayerLib.spawnParticle(player, displayLocation, particle, amount, offsetX, offsetY, offsetZ, speed);

        Message.success(
                player,
                "Displaying %s particle above your head. Offset=%s, Speed=%s, Amount=%s",
                Chat.capitalize(particle),
                (offsetX == 0 && offsetY == 0 && offsetZ == 0) ? "None" : ("(%s, %s, %s)".formatted(offsetX, offsetY, offsetZ)),
                speed == 0.0f ? "0.0" : speed,
                amount == 0 ? "0" : amount
        );

        Message.clickHover(
                player,
                LazyEvent.runCommand("/particles command %s %s %s %s %s %s", particle.name(), offsetX, offsetY, offsetZ, speed, amount),
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
