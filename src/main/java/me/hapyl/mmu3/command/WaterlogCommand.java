package me.hapyl.mmu3.command;

import me.hapyl.eterna.module.command.SimplePlayerAdminCommand;
import me.hapyl.eterna.module.math.Cuboid;
import me.hapyl.eterna.module.math.Numbers;
import me.hapyl.mmu3.feature.UndoManager;
import me.hapyl.mmu3.feature.block.BlockChangeQueue;
import me.hapyl.mmu3.feature.block.MultiBlockChange;
import me.hapyl.mmu3.message.Message;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.Player;

public class WaterlogCommand extends SimplePlayerAdminCommand {

    private final int minRadius = 1;
    private final int maxRadius = 100;

    public WaterlogCommand(String name) {
        super(name);
        setDescription("Allows toggling water in blocks.");
        addCompleterHandler(1, (player, arg, args) -> {
            if (!Numbers.isInt(arg)) {
                return "&cMust be an integer!";
            }

            final int radius = Numbers.getInt(arg);

            if (radius < minRadius) {
                return "&c&nToo small!";
            }
            else if (radius > maxRadius) {
                return "&c&nToo big!";
            }

            return "&a&nWill waterlog in &l{}&a&n block radius!";
        });

        addCompleterHandler(2, (player, arg, args) -> {
            if (arg.equalsIgnoreCase("enable")) {
                return "&a&nWaterlogging on!";
            }
            else if (arg.equalsIgnoreCase("disable")) {
                return "&e&nWaterlogging off!";
            }

            return "&c&nInvalid command.";
        });

        addCompleterValues(2, "enable", "disable");
    }

    @Override
    protected void execute(Player player, String[] args) {
        final int radius = getArgument(args, 0).toInt(5);
        final String value = getArgument(args, 1).toString();

        if (radius < minRadius) {
            Message.error(player, "Radius is too small!");
            return;
        }

        if (radius > maxRadius) {
            Message.error(player, "Radius it too big!");
            return;
        }

        final boolean waterlogOn = value.equalsIgnoreCase("enable");

        final Cuboid cuboid = new Cuboid(
                player.getLocation().add(radius, radius, radius),
                player.getLocation().subtract(radius, radius, radius)
        );

        final BlockChangeQueue undoMap = UndoManager.getUndoMap(player);
        final MultiBlockChange multiBlockChange = new MultiBlockChange();

        cuboid.getBlocks().forEach(block -> {
            if (block.isEmpty()) {
                return;
            }

            final BlockData blockData = block.getBlockData();
            if (!(blockData instanceof Waterlogged waterlogged)) {
                return;
            }

            if (waterlogOn && waterlogged.isWaterlogged()) {
                return;
            }

            if (!waterlogOn && !waterlogged.isWaterlogged()) {
                return;
            }

            multiBlockChange.add(block);
            waterlogged.setWaterlogged(waterlogOn);
            block.setBlockData(waterlogged, false);
        });

        undoMap.add(multiBlockChange);
        Message.success(player, "Waterlogged %s blocks.", multiBlockChange.getSize());
    }
}
