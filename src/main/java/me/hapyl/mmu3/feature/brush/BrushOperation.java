package me.hapyl.mmu3.feature.brush;

import com.google.common.collect.Sets;
import org.bukkit.block.Block;

import java.util.Set;

public class BrushOperation {

    private final Set<BlockInfo> blocks;

    public BrushOperation() {
        this.blocks = Sets.newHashSet();
    }

    public void add(Block block) {
        blocks.add(new BlockInfo(block));
    }

    public Set<BlockInfo> getBlocks() {
        return blocks;
    }
}
