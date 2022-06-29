package me.hapyl.mmu3.feature.brush;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.Collection;

public interface BrushPattern {

    /**
     * Returns collected block with brush pattern.
     *
     * @param location - Location of the center.
     * @param radius   - Radius.
     * @return collection of blocks.
     */
    Collection<Block> collect(Location location, int radius);

}
