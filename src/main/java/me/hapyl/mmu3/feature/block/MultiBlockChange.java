package me.hapyl.mmu3.feature.block;

import com.google.common.collect.Maps;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.function.BiConsumer;

public class MultiBlockChange implements BlockChange {

    private final Map<Block, BlockState> changedBlocks;

    public MultiBlockChange() {
        changedBlocks = Maps.newHashMap();
    }

    public MultiBlockChange add(@Nonnull Block block) {
        changedBlocks.put(block, block.getState());
        return this;
    }

    @Override
    public int getSize() {
        return changedBlocks.size();
    }

    @Override
    public void affect(@Nonnull BiConsumer<Block, BlockState> consumer) {
        changedBlocks.forEach(consumer);
        changedBlocks.clear();
    }
}
