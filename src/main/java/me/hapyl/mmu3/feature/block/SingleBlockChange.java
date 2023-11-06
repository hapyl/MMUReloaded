package me.hapyl.mmu3.feature.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;

public class SingleBlockChange implements BlockChange {

    private final Block block;
    private final BlockState state;

    public SingleBlockChange(Block block, BlockState state) {
        this.block = block;
        this.state = state;
    }

    @Override
    public int getSize() {
        return 1;
    }

    @Override
    public void affect(@Nonnull BiConsumer<Block, BlockState> consumer) {
        consumer.accept(block, state);
    }
}
