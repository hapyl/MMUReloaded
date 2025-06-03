package me.hapyl.mmu3.feature.statechanger;

import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

public class StateChangerData {

    private final Player player;
    private final Block block;
    private final BlockData originalBlockData;

    public StateChangerData(Player player, Block block) {
        this.player = player;
        this.block = block;
        this.originalBlockData = this.block.getBlockData().clone();
    }

    public BlockData getBlockData() {
        return block.getBlockData();
    }

    public Player getPlayer() {
        return player;
    }

    public Block getBlock() {
        return block;
    }

    public void restoreOriginalBlockData() {
        this.block.setBlockData(getOriginalBlockData(), false);
    }

    public BlockData getOriginalBlockData() {
        return originalBlockData;
    }
}
