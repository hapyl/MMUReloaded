package me.hapyl.mmu3.command;

import com.google.common.collect.Maps;
import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.chat.LazyClickEvent;
import me.hapyl.eterna.module.chat.LazyHoverEvent;
import me.hapyl.eterna.module.util.BukkitUtils;
import me.hapyl.mmu3.MMULogger;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class MMUOperationRelative implements MMUOperation {

    private final Map<Player, Location> playerPoints = Maps.newHashMap();

    @Override
    public @NotNull String name() {
        return "relative";
    }

    @Override
    public @NotNull String description() {
        return "Calculates the distance between two points to perform relative teleportation.";
    }

    @Override
    public @NotNull MMUCompletion completions() {
        return MMUCompletion.builder()
                .where(0, List.of("-c"));
    }

    @Override
    public void process(@NotNull Player player, @NotNull ArgumentList args) {
        Location location = player.getLocation();

        if (args.get(0).toString().equals("-c")) {
            location = MMUOperationCenter.center(location);
        }

        final Location firstPoint = playerPoints.remove(player);

        if (firstPoint != null) {
            final double[] relative = new double[] {
                    firstPoint.getX() - location.getX(),
                    firstPoint.getY() - location.getY(),
                    firstPoint.getZ() - location.getZ(),
            };

            MMULogger.info(player, );
            return;
        }

        if (hasPoint(player)) {
            final double[] relative = calculatePoints(player, location);
            final String relativeString = "~%s ~%s ~%s".formatted(relative[0], relative[1], relative[2]);

            MMULogger.info(player, "Relative distance between two points is:");
            Chat.sendClickableHoverableMessage(
                    player,
                    LazyClickEvent.SUGGEST_COMMAND.of(relativeString),
                    LazyHoverEvent.SHOW_TEXT.of("&7Click to copy!"),
                    "%s %s &e&lCLICK TO COPY!".formatted(MMULogger.PREFIX, relativeString)
            );
            return;
        }

        playerPoints.put(player, location);
        MMULogger.info(player, "Saved first point at %s.", BukkitUtils.locationToString(location));
    }

    public double[] calculatePoints(Player player, Location endPoint) {
        final Location firstPoint = getPoint(player);
        if (firstPoint == null) {
            return new double[] { 0.0d, 0.0d, 0.0 };
        }

        playerPoints.remove(player);
        return new double[] {
                firstPoint.getX() - endPoint.getX(), firstPoint.getY() - endPoint.getY(),
                firstPoint.getZ() - endPoint.getZ()
        };
    }

}
