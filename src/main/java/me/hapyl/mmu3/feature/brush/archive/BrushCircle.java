package me.hapyl.mmu3.feature.brush.archive;

import com.google.common.collect.Sets;
import me.hapyl.mmu3.feature.brush.Brush;
import me.hapyl.mmu3.feature.brush.NonNullWorldLocation;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Set;

public class BrushCircle extends Brush {
    public BrushCircle() {
        super("Circle");
        setCancelPhysics(false);
    }

    @Nullable
    @Override
    public Collection<Block> collect(@Nonnull Player player, @Nonnull NonNullWorldLocation location, double radius) {
        final World world = location.getWorld();
        final Set<Block> circleBlocks = Sets.newHashSet();

        final int bx = location.getBlockX();
        final int by = location.getBlockY();
        final int bz = location.getBlockZ();

        for (double x = bx - radius; x <= bx + radius; x++) {
            for (double y = by - radius; y <= by + radius; y++) {
                for (double z = bz - radius; z <= bz + radius; z++) {
                    double distance = ((bx - x) * (bx - x) + ((bz - z) * (bz - z)) + ((by - y) * (by - y)));
                    if (distance < radius * radius) {
                        circleBlocks.add(world.getBlockAt((int) x, (int) y, (int) z));
                    }
                }
            }
        }

        return circleBlocks;
    }
}
