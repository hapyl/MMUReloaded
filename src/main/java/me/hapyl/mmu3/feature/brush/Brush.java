package me.hapyl.mmu3.feature.brush;

import com.google.common.collect.Sets;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public enum Brush {

    NONE((location, radius) -> null),
    CIRCLE((location, radius) -> {
        final World world = location.getWorld();
        final Set<Block> circleBlocks = Sets.newHashSet();

        if (world == null) {
            return null;
        }

        final int bx = location.getBlockX();
        final int by = location.getBlockY();
        final int bz = location.getBlockZ();

        for (int x = bx - radius; x <= bx + radius; x++) {
            for (int y = by - radius; y <= by + radius; y++) {
                for (int z = bz - radius; z <= bz + radius; z++) {
                    double distance = ((bx - x) * (bx - x) + ((bz - z) * (bz - z)) + ((by - y) * (by - y)));
                    if (distance < radius * radius) {
                        circleBlocks.add(world.getBlockAt(x, y, z));
                    }
                }
            }
        }

        return circleBlocks;
    });

    private final BrushPattern brushPattern;

    Brush(BrushPattern brushPattern) {
        this.brushPattern = brushPattern;
    }

    public static List<Location> generateSphere(Location centerBlock, int radius, boolean hollow) {

        List<Location> circleBlocks = new ArrayList<Location>();

        int bx = centerBlock.getBlockX();
        int by = centerBlock.getBlockY();
        int bz = centerBlock.getBlockZ();

        for (int x = bx - radius; x <= bx + radius; x++) {
            for (int y = by - radius; y <= by + radius; y++) {
                for (int z = bz - radius; z <= bz + radius; z++) {
                    double distance = ((bx - x) * (bx - x) + ((bz - z) * (bz - z)) + ((by - y) * (by - y)));
                    if (distance < radius * radius && !(hollow && distance < ((radius - 1) * (radius - 1)))) {
                        Location l = new Location(centerBlock.getWorld(), x, y, z);
                        circleBlocks.add(l);
                    }
                }
            }
        }

        return circleBlocks;
    }

    public BrushPattern getPattern() {
        return brushPattern;
    }
}
