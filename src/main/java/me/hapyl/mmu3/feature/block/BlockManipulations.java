package me.hapyl.mmu3.feature.block;

import com.google.common.collect.Maps;
import me.hapyl.spigotutils.module.math.Cuboid;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import java.util.Map;

public final class BlockManipulations {

    public static Map<Block, BlockState> fillBlocks(Cuboid cuboid, Material material) {
        final Map<Block, BlockState> affectedBlocks = Maps.newHashMap();

        for (Block block : cuboid.getBlocks()) {
            if (!block.getType().isAir()) {
                affectedBlocks.put(block, block.getState());
                block.setType(material, false);
            }
        }


        return affectedBlocks;
    }

    public static Map<Block, BlockState> fillBlocks(Cuboid cuboid, Material from, Material to) {
        final Map<Block, BlockState> affectedBlocks = Maps.newHashMap();

        for (Block block : cuboid.getBlocks()) {
            if (block.getType() == from) {
                affectedBlocks.put(block, block.getState());
                block.setType(to, false);
            }
        }

        return affectedBlocks;
    }

}
