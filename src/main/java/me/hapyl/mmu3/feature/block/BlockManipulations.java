package me.hapyl.mmu3.feature.block;

import me.hapyl.spigotutils.module.math.Cuboid;
import org.bukkit.Material;
import org.bukkit.block.Block;

import javax.annotation.Nonnull;

public final class BlockManipulations {

    @Nonnull
    public static BlockChange fillBlocks(Cuboid cuboid, Material material) {
        final MultiBlockChange blockChange = new MultiBlockChange();

        for (Block block : cuboid.getBlocks()) {
            if (!block.getType().isAir()) {
                blockChange.add(block);
                block.setType(material, false);
            }
        }

        return blockChange;
    }

    @Nonnull
    public static BlockChange fillBlocks(Cuboid cuboid, Material from, Material to) {
        final MultiBlockChange blockChange = new MultiBlockChange();

        for (Block block : cuboid.getBlocks()) {
            if (block.getType() == from) {
                blockChange.add(block);
                block.setType(to, false);
            }
        }

        return blockChange;
    }

}
