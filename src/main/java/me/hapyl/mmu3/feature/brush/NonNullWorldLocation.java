package me.hapyl.mmu3.feature.brush;

import me.hapyl.eterna.module.util.BukkitUtils;
import org.bukkit.Location;
import org.bukkit.World;

import javax.annotation.Nonnull;

public class NonNullWorldLocation extends Location {

    public NonNullWorldLocation(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    public NonNullWorldLocation(World world, double x, double y, double z, float yaw, float pitch) {
        super(world, x, y, z, yaw, pitch);
    }

    @Nonnull
    @Override
    public World getWorld() {
        final World world = super.getWorld();

        if (world == null) {
            return BukkitUtils.defWorld();
        }

        return world;
    }

    public static NonNullWorldLocation from(@Nonnull Location location) {
        return new NonNullWorldLocation(
                location.getWorld(),
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch()
        );
    }
}
