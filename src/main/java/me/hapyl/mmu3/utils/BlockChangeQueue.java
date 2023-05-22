package me.hapyl.mmu3.utils;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import me.hapyl.spigotutils.module.math.nn.IntInt;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import java.util.LinkedList;
import java.util.Map;

public final class BlockChangeQueue {

    private final LinkedList<Map<Block, BlockState>> queue;

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

        final IntInt restore = new IntInt(0);
        deep = Math.min(deep, queue.size());

        for (int i = 0; i < deep; i++) {
            final Map<Block, BlockState> map = queue.pollLast();
            if (map == null) {
                break;
            }

            map.forEach((block, state) -> {
                block.setType(state.getType(), false);
                block.setBlockData(state.getBlockData(), false);
                restore.increment();
            });
        }

        return restore.get();
    }

    public void add(Map<Block, BlockState> map) {
        queue.add(map);
    }

    public void add(Block block) {
        queue.add(ImmutableMap.of(block, block.getState()));
    }

    public int size() {
        return queue.size();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
