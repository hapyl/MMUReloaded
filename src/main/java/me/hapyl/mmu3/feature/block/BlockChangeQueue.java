package me.hapyl.mmu3.feature.block;

import com.google.common.collect.Lists;
import org.bukkit.block.Block;

import javax.annotation.Nonnull;
import java.util.LinkedList;

public final class BlockChangeQueue {

    private final LinkedList<BlockChange> queue;

    public BlockChangeQueue() {
        this.queue = Lists.newLinkedList();
    }

    public int restoreLast() {
        return restore(1);
    }

    public int restore(int deep) {
        if (queue.isEmpty()) {
            return 0;
        }

        int undid = 0;
        deep = Math.min(deep, queue.size());

        for (int i = 0; i < deep; i++) {
            final BlockChange change = queue.pollLast();

            if (change == null) {
                break;
            }

            change.affect((block, state) -> {
                block.setType(state.getType(), false);
                block.setBlockData(state.getBlockData(), false);
            });

            undid++;
        }

        return undid;
    }

    public void add(@Nonnull BlockChange change) {
        if (change.isEmpty()) {
            return;
        }

        queue.add(change);
    }

    public void add(@Nonnull Block block) {
        queue.add(new SingleBlockChange(block, block.getState()));
    }

    public int size() {
        return queue.size();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
