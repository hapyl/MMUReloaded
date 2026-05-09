package me.hapyl.mmu3.command;

import me.hapyl.mmu3.MMULogger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class MMUOperationParticle implements MMUOperation {

    private final List<String> particlesNames = Arrays.stream(Particle.values()).map(Enum::name).map(String::toLowerCase).toList();

    @Override
    public @NotNull String name() {
        return "particle";
    }

    @Override
    public @NotNull String description() {
        return "Allows previewing particles above one head and generating commands for display.";
    }

    @Override
    public @NotNull MMUCompletion completions() {
        return MMUCompletion.builder()
                .where(0, particlesNames);
    }

    @Override
    public void process(@NotNull Player player, @NotNull ArgumentList args) {
        // mmu particle (particle) (amount) (x) (y) (z) (speed)
        // mmu particle (particle) (amount) (x) (y) (z)
        // mmu particle (particle) (amount) (speed)
        // mmu particle (particle)

        // Handle command generations
        final ParticleData particleData = ParticleData.parse(args);

        if (particleData == null) {
            MMULogger.error(player, Component.text("Invalid particle: %s.".formatted(args.get(0))));
            return;
        }

        final Location location = player.getLocation().add(0, 2.5, 0);

        player.spawnParticle(
                particleData.particle,
                location,
                particleData.amount,
                particleData.offsetX,
                particleData.offsetY,
                particleData.offsetZ,
                particleData.speed
        );

        MMULogger.success(
                player,
                Component.text("Displaying particle `%s` above your head!".formatted(particleData.particle.name().toLowerCase()))
        );

        // Generators
        MMULogger.copy(
                player,
                Component.text("Minecraft Command"),
                ClickEvent.copyToClipboard(particleData.generateBukkitCode())
        );

        MMULogger.copy(
                player,
                Component.text("Bukkit Code"),
                ClickEvent.copyToClipboard(particleData.generateBukkitCode())
        );
    }

    public record ParticleData(@NotNull Particle particle, int amount, double offsetX, double offsetY, double offsetZ, float speed) {

        @NotNull
        public String generateMinecraftCommand() {
            // particle ash ~ ~ ~ 0 0 0 1 50
            return "particle %s ~ ~ ~ %s %s %s %s %s".formatted(
                    particle.key().value(),
                    offsetX,
                    offsetY,
                    offsetZ,
                    speed,
                    amount
            );
        }

        @NotNull
        public String generateBukkitCode() {
            return "player.spawnParticle(Particle.%s, location, %s, %s, %s, %s, %s)".formatted(
                    particle.name().toUpperCase(),
                    amount,
                    offsetX,
                    offsetY,
                    offsetZ,
                    speed
            );
        }

        @Nullable
        public static ParticleData parse(@NotNull ArgumentList argumentList) {
            if (argumentList.length == 3) {
                return parse0(argumentList, -1, -1, -1, 2);
            }
            else {
                return parse0(argumentList, 2, 3, 4, 5);
            }
        }

        @Nullable
        private static ParticleData parse0(@NotNull ArgumentList argumentList, int offsetXIndex, int offsetYIndex, int offsetZIndex, int speedIndex) {
            final Particle particle = argumentList.get(0).toEnum(Particle.class).orElse(null);
            final int amount = argumentList.get(1).toInt(1);

            final double offsetX = argumentList.get(offsetXIndex).toDouble();
            final double offsetY = argumentList.get(offsetYIndex).toDouble();
            final double offsetZ = argumentList.get(offsetZIndex).toDouble();
            final float speed = argumentList.get(speedIndex).toFloat();

            return particle != null ? new ParticleData(particle, amount, offsetX, offsetY, offsetZ, speed) : null;
        }

    }

}
