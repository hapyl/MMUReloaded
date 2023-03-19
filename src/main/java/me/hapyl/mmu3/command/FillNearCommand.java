package me.hapyl.mmu3.command;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.feature.block.BlockManipulations;
import me.hapyl.mmu3.message.Message;
import me.hapyl.mmu3.utils.BlockChangeQueue;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.chat.LazyEvent;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import me.hapyl.spigotutils.module.math.Cuboid;
import me.hapyl.spigotutils.module.math.Numbers;
import me.hapyl.spigotutils.module.util.Validate;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FillNearCommand extends SimplePlayerAdminCommand {

    private final Map<UUID, BlockChangeQueue> undoMap = Maps.newHashMap();

    public FillNearCommand(String name) {
        super(name);

        setDescription("Fills blocks in radius.");
        setUsage("/fillnear <Radius> <To|From> [To]");

        final List<Material> blocks = Arrays.stream(Material.values()).toList().stream().filter(Material::isBlock).toList();

        addCompleterValues(1, "undo");
        addCompleterValues(2, blocks);
        addCompleterValues(3, blocks);

        addCompleterHandler(1, (player, arg) -> {
            if (arg.equalsIgnoreCase("undo")) {
                return "&a&nWill undo last fill!";
            }

            if (Validate.isInt(arg)) {
                final int i = Validate.getInt(arg);

                if (i < 1) {
                    return "&c&nRadius cannot be less than 1!";
                }
                else if (i > 100) {
                    return "&c&nRadius cannot be greater 100!";
                }

                return "&a&nWill fill blocks in {} radius!";
            }

            return "&6&nExpected integer, not: {}!";
        });

    }

    public BlockChangeQueue getBlockChangeQueue(Player player) {
        return undoMap.computeIfAbsent(player.getUniqueId(), uuid -> new BlockChangeQueue());
    }

    @Override
    protected void execute(Player player, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("undo")) {
            final BlockChangeQueue blockChangeQueue = getBlockChangeQueue(player);

            if (blockChangeQueue.isEmpty()) {
                Message.error(player, "Nothing to undo!");
                return;
            }

            final int restoredBlocks = blockChangeQueue.restoreLast();

            Message.success(player, "Undid %s blocks!".formatted(restoredBlocks));
            return;
        }

        if (args.length < 2) {
            Message.error(player, "Provide radius and block to fill!");
            return;
        }

        final int radius = Numbers.getInt(args[0], 5);
        final Material from = Validate.getEnumValue(Material.class, args[1]);
        final Material to = args.length > 2 ? Validate.getEnumValue(Material.class, args[2]) : from;

        if (radius < 1) {
            Message.error(player, "Radius must be at least 1.");
            return;
        }

        if (radius > 100) {
            Message.error(player, "Radius cannot be greater than 100.");
            return;
        }

        if (from == null || to == null) {
            Message.error(player, "Invalid block!");
            return;
        }

        final Cuboid cuboid = new Cuboid(
                player.getLocation().add(-radius, -radius, -radius),
                player.getLocation().add(radius, radius, radius)
        );

        final Map<Block, BlockState> affected =
                from == to ? BlockManipulations.fillBlocks(cuboid, to) : BlockManipulations.fillBlocks(cuboid, from, to);

        if (affected.size() == 0) {
            Message.error(player, "No blocks were affected!");
            return;
        }

        getBlockChangeQueue(player).add(affected);

        if (from == to) {
            Message.success(player, "Filled %s blocks with %s!".formatted(affected.size(), Chat.capitalize(to)));
        }
        else {
            Message.success(
                    player,
                    "Replaced %s %s with %s!".formatted(affected.size(), Chat.capitalize(from), Chat.capitalize(to))
            );
        }

        Message.clickHover(
                player,
                LazyEvent.runCommand("/fillnear undo"),
                LazyEvent.showText("&7Click to undo!"),
                "&e&lCLICK HERE &ato undo."
        );
    }
}
