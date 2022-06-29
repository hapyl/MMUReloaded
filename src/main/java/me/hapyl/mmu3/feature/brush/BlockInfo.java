package me.hapyl.mmu3.feature.brush;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

public class BlockInfo {

    private final Location location;
    private final Material type;
    private final BlockData data;

    public BlockInfo(Block block) {
        this.location = block.getLocation();
        this.type = block.getType();
        this.data = block.getBlockData();
    }

    public void restore() {
        final Block block = location.getBlock();
        block.setType(type, false);
        block.setBlockData(data, false);
        block.getState().update(true, false);
    }

}
