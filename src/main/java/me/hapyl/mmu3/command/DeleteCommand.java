package me.hapyl.mmu3.command;

import me.hapyl.mmu3.message.Message;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import me.hapyl.spigotutils.module.math.Numbers;
import me.hapyl.spigotutils.module.util.Validate;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class DeleteCommand extends SimplePlayerAdminCommand {

    private final double maxDeleteRadius = 1000;

    public DeleteCommand(String name) {
        super(name);

        setDescription("Removes entity in radius.");
        setUsage("/delete <Entity> [Radius]");
        addCompleterValues(1, Arrays.stream(EntityType.values()).filter(EntityType::isSpawnable).collect(Collectors.toList()));

        addCompleterHandler(1, "&a&nWill delete: {}", "&c&nInvalid entity: {}!");
        addCompleterHandler(2, (player, arg, args) -> {
            if (Validate.isInt(arg)) {
                final int i = Validate.getInt(arg);
                if (i < 1) {
                    return "&c&nToo little: {}!";
                } else if (i > maxDeleteRadius) {
                    return "&c&nToo much: {}!";
                }

                return "&a&nWill remove: %ss in {} radius".formatted(args[args.length - 2]);
            }

            return "&6&nExpected integer, not: {}!";
        });
    }

    @Override
    protected void execute(Player player, String[] args) {
        if (args.length == 0) {
            Message.error(player, "Provide entity to delete!");
            return;
        }

        final EntityType entity = Validate.getEnumValue(EntityType.class, args[0]);
        final int radius = args.length > 1 ? Numbers.getInt(args[1], 1) : 5;

        if (entity == null || entity == EntityType.PLAYER) {
            Message.error(player, "Invalid entity!");
            return;
        }

        if (radius < 1 || radius > maxDeleteRadius) {
            Message.error(player, "Too little or too much radius!");
            return;
        }

        final Set<Entity> entities = player.getWorld().getNearbyEntities(player.getLocation(), radius, radius, radius)
                .stream()
                .filter(e -> e.getType() == entity)
                .collect(Collectors.toSet());

        entities.forEach(Entity::remove);
        Message.success(player, "Removed %s %s entities.", entities.size(), Chat.capitalize(entity));
    }

}
