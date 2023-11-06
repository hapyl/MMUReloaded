package me.hapyl.mmu3.feature.brush.archive;

import com.google.common.collect.Sets;
import me.hapyl.mmu3.feature.brush.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Set;

public class BrushGradient extends Brush {

    private final BlockFace[] adjacentFaces = { BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST, BlockFace.EAST };

    private final BrushData<Boolean> isUp = addExtraData("isUp", BrushDataType.BOOLEAN, false);

    public BrushGradient() {
        super("Gradient");
    }

    @Nonnull
    @Override
    public Collection<Block> collect(@Nonnull Player player, @Nonnull NonNullWorldLocation location, double radius) {
        final Set<Block> blocks = Sets.newHashSet();
        final boolean isUp = this.isUp.getPlayerValue(player);

        Block block = location.getBlock();
        int maxIterations = 100;

        while (maxIterations-- > 0 && !isHitLimit(block)) {
            blocks.add(block);
            block = block.getRelative(isUp ? BlockFace.UP : BlockFace.DOWN);
        }

        return blocks;
    }

    private boolean isHitLimit(Block block) {
        if (block == null || block.getType().isAir()) {
            return true;
        }

        for (BlockFace face : adjacentFaces) {
            final Block relative = block.getRelative(face);
            if (relative.getType().isAir()) {
                return false;
            }
        }

        return true;
    }

}
