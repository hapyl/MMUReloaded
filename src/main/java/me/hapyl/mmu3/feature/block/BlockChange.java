package me.hapyl.mmu3.feature.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;

public interface BlockChange {

    void affect(@Nonnull BiConsumer<Block, BlockState> consumer);

    int getSize();

    default boolean isEmpty() {
        return getSize() == 0;
    }

}
