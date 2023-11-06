package me.hapyl.mmu3.command;

import me.hapyl.mmu3.feature.UndoManager;
import me.hapyl.mmu3.feature.block.MultiBlockChange;
import me.hapyl.mmu3.message.Message;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import me.hapyl.spigotutils.module.math.Cuboid;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public class FixTreeOrientationCommand extends SimplePlayerAdminCommand {

    public FixTreeOrientationCommand(String name) {
        super(name);

        setDescription("Fixes tree orientations to be straight up.");
        setUsage("fixtrees <radius>");
        setAliases("fixtrees");
    }

    @Override
    protected void execute(Player player, String[] args) {
        if (args.length != 1) {
            sendInvalidUsageMessage(player);
            return;
        }

        int radius = Math.min(getArgument(args, 0).toInt(1), 100);

        final Cuboid cuboid = new Cuboid(
                player.getLocation().subtract(radius, radius, radius),
                player.getLocation().add(radius, radius, radius)
        );

        final MultiBlockChange blockChange = new MultiBlockChange();
        for (Block block : cuboid.getBlocks()) {
            final Material fixedType = fixedWoodType(block.getType());

            if (fixedType == null) {
                continue;
            }

            blockChange.add(block);
            block.setType(fixedType, false);
        }

        UndoManager.getUndoMap(player).add(blockChange);
        Message.success(player, "Fixed %s blocks in %s block radius.", blockChange.getSize(), radius);
    }

    @Nullable
    private Material fixedWoodType(Material from) {
        if (from.isAir()) {
            return null;
        }

        return switch (from) {
            case OAK_LOG, OAK_WOOD -> Material.OAK_WOOD;
            case SPRUCE_LOG, SPRUCE_WOOD -> Material.SPRUCE_WOOD;
            case BIRCH_LOG, BIRCH_WOOD -> Material.BIRCH_WOOD;
            case JUNGLE_LOG, JUNGLE_WOOD -> Material.JUNGLE_WOOD;
            case ACACIA_LOG, ACACIA_WOOD -> Material.ACACIA_WOOD;
            case DARK_OAK_LOG, DARK_OAK_WOOD -> Material.DARK_OAK_WOOD;
            case MANGROVE_LOG, MANGROVE_WOOD -> Material.MANGROVE_WOOD;
            case CRIMSON_STEM, CRIMSON_HYPHAE -> Material.CRIMSON_HYPHAE;
            case WARPED_STEM, WARPED_HYPHAE -> Material.WARPED_HYPHAE;
            case CHERRY_LOG, CHERRY_WOOD -> Material.CHERRY_WOOD;

            // stripped
            case STRIPPED_OAK_LOG, STRIPPED_OAK_WOOD -> Material.STRIPPED_OAK_WOOD;
            case STRIPPED_SPRUCE_LOG, STRIPPED_SPRUCE_WOOD -> Material.STRIPPED_SPRUCE_WOOD;
            case STRIPPED_BIRCH_LOG, STRIPPED_BIRCH_WOOD -> Material.STRIPPED_BIRCH_WOOD;
            case STRIPPED_JUNGLE_LOG, STRIPPED_JUNGLE_WOOD -> Material.STRIPPED_JUNGLE_WOOD;
            case STRIPPED_ACACIA_LOG, STRIPPED_ACACIA_WOOD -> Material.STRIPPED_ACACIA_WOOD;
            case STRIPPED_DARK_OAK_LOG, STRIPPED_DARK_OAK_WOOD -> Material.STRIPPED_DARK_OAK_WOOD;
            case STRIPPED_MANGROVE_LOG, STRIPPED_MANGROVE_WOOD -> Material.STRIPPED_MANGROVE_WOOD;
            case STRIPPED_CRIMSON_STEM, STRIPPED_CRIMSON_HYPHAE -> Material.STRIPPED_CRIMSON_HYPHAE;
            case STRIPPED_WARPED_STEM, STRIPPED_WARPED_HYPHAE -> Material.STRIPPED_WARPED_HYPHAE;
            case STRIPPED_CHERRY_LOG, STRIPPED_CHERRY_WOOD -> Material.STRIPPED_CHERRY_WOOD;

            // defaults to null
            default -> null;
        };
    }
}
