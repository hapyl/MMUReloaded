package me.hapyl.mmu3.command;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.Message;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.chat.LazyClickEvent;
import me.hapyl.spigotutils.module.chat.LazyHoverEvent;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import me.hapyl.spigotutils.module.util.BukkitUtils;
import me.hapyl.spigotutils.module.util.Validate;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.Map;

public class CalculateRelative extends SimplePlayerAdminCommand {

    private final Map<Player, Location> pointMap = Maps.newHashMap();

    public CalculateRelative(String name) {
        super(name);
        setDescription("Calculates distance between two points to perform relative teleportation.");
        addCompleterValues(1, "c", "center");
    }

    @Override
    protected void execute(Player player, String[] strings) {
        Location location = player.getLocation();

        if (Validate.checkArrayString(strings, 0, "c", "center")) {
            location = BukkitUtils.centerLocation(location).subtract(0.0d, 0.5d, 0.0d);
        }

        if (hasPoint(player)) {
            final double[] relative = calculatePoints(player, location);
            final String relativeString = "~%s ~%s ~%s".formatted(relative[0], relative[1], relative[2]);

            Message.info(player, "Relative distance between two points is:");
            Chat.sendClickableHoverableMessage(
                    player,
                    LazyClickEvent.SUGGEST_COMMAND.of(relativeString),
                    LazyHoverEvent.SHOW_TEXT.of("&7Click to copy!"),
                    "%s %s &e&lCLICK TO COPY!".formatted(Message.PREFIX, relativeString)
            );
            return;
        }

        pointMap.put(player, location);
        Message.info(player, "Saved first point at %s.", BukkitUtils.locationToString(location));
    }

    @Nullable
    public Location getPoint(Player player) {
        return pointMap.get(player);
    }

    public boolean hasPoint(Player player) {
        return pointMap.containsKey(player);
    }

    public double[] calculatePoints(Player player, Location endPoint) {
        final Location firstPoint = getPoint(player);
        if (firstPoint == null) {
            return new double[] { 0.0d, 0.0d, 0.0 };
        }

        pointMap.remove(player);
        return new double[] { firstPoint.getX() - endPoint.getX(), firstPoint.getY() - endPoint.getY(),
                              firstPoint.getZ() - endPoint.getZ() };
    }

}
