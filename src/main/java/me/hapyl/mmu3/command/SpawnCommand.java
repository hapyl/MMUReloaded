package me.hapyl.mmu3.command;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.message.Message;
import me.hapyl.mmu3.utils.StaticUpdate20EntitySpawn;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import me.hapyl.spigotutils.module.math.Numbers;
import me.hapyl.spigotutils.module.util.Validate;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SpawnCommand extends SimplePlayerAdminCommand {

    public SpawnCommand(String name) {
        super(name);

        setDescription("Spawns entity.");
        setUsage("/spawn <Entity> [Amount]");
        addCompleterValues(1, EntityType.values());

        addCompleterHandler(1, "&a&nWill spawn: {}", "&c&nInvalid entity: {}!");

        addCompleterHandler(2, (player, arg, args) -> {
            if (Validate.isInt(arg)) {
                final int i = Validate.getInt(arg);
                if (i < 1) {
                    return "&c&nToo little: {}!";
                }
                else if (i > 100) {
                    return "&c&nToo much: {}!";
                }

                return "&a&nWill spawn: x{} " + args[args.length - 2];
            }

            return "&6&nExpected integer, not: {}!";
        });

    }

    @Override
    protected void execute(Player player, String[] args) {
        if (args.length == 0) {
            Message.error(player, "Provide entity to spawn!");
            return;
        }

        final EntityType entity = Validate.getEnumValue(EntityType.class, args[0]);
        final int amount = args.length > 1 ? Numbers.getInt(args[1], 1) : 1;

        if (entity == null) {
            Message.error(player, "Invalid entity!");
            return;
        }

        if (amount < 1 || amount > 100) {
            Message.error(player, "Too little or too much amount!");
            return;
        }

        new BukkitRunnable() {
            private int toSpawn = amount;

            @Override
            public void run() {
                if (toSpawn-- <= 0) {
                    cancel();
                    Message.success(player, "Success!");
                    return;
                }

                try {
                    if (!entity.isSpawnable()) {
                        Message.error(player, "Cannot spawn %s!", entity.name());
                        cancel();
                        return;
                    }

                    player.getWorld().spawnEntity(player.getLocation(), entity);

                } catch (Exception ignored) {
                    final Entity spawnedEntity = StaticUpdate20EntitySpawn.spawn(
                            player.getLocation(),
                            StaticUpdate20EntitySpawn.EntityType20.fromEntity(entity)
                    );

                    if (spawnedEntity == null) {
                        Message.error(player, "Unable to spawn disabled entity, blame mojang!");
                        cancel();
                    }
                }
            }
        }.

                runTaskTimer(Main.getInstance(), 0L, 1L);

    }
}
