package me.hapyl.mmu3.command;

import me.hapyl.mmu3.MMULogger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class MMUOperationRemoveEntity implements MMUOperation {

    private final double defaultRadius = 5;
    private final double maximumRadius = 1000;

    @Override
    public @NotNull String name() {
        return "remove_entity";
    }

    @Override
    public @NotNull String description() {
        return "Removes entities within a given radius.";
    }

    @Override
    public void process(@NotNull Player player, @NotNull ArgumentList args) {
        if (args.length == 0) {
            MMULogger.error(player, Component.text("/mmu remove (entity_type) [radius] [limit]"));
            return;
        }

        final EntityType entityType = args.get(0).toEnum(EntityType.class).orElse(null);
        final double radius = args.get(1).toDouble(defaultRadius);
        final double limit = args.get(2).toDouble();

        if (entityType == null || entityType == EntityType.PLAYER || entityType == EntityType.UNKNOWN) {
            MMULogger.error(player, Component.text("Invalid or unremovable entity type: %s!".formatted(args.get(0))));
            return;
        }

        if (radius < 0 || radius > maximumRadius) {
            MMULogger.error(player, Component.text("Radius must be higher than 0 and lower than %s!".formatted(maximumRadius)));
            return;
        }

        if (limit < 0) {
            MMULogger.error(player, Component.text("Limit must be positive!"));
            return;
        }

        int removeCount = 0;

        final Collection<Entity> entitiesToRemove = player.getWorld()
                .getNearbyEntities(player.getLocation(), radius, radius, radius, entity -> entity.getType() == entityType);

        for (Entity entity : entitiesToRemove) {
            if (limit > 0 && removeCount++ >= limit) {
                break;
            }

            entity.remove();
        }

        if (removeCount == 0) {
            MMULogger.error(player, Component.text("Didn't remove anything!"));
        }
        else {
            MMULogger.success(
                    player,
                    Component.empty()
                            .append(Component.text("Removed "))
                            .append(Component.text(removeCount, NamedTextColor.AQUA))
                            .appendSpace()
                            .append(Component.translatable(entityType.translationKey(), NamedTextColor.WHITE))
                            .append(Component.text(" entities!"))
            );
        }
    }

}
