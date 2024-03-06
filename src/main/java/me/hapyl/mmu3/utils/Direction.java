package me.hapyl.mmu3.utils;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;

import javax.annotation.Nonnull;

public enum Direction {

    NORTH,
    SOUTH,
    EAST,
    WEST;

    @Nonnull
    public Direction getOpposite() {
        return switch (this) {
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case WEST -> EAST;
            case EAST -> WEST;
        };
    }

    @Nonnull
    public BlockFace getBlockFace() {
        return switch (this) {
            case NORTH -> BlockFace.NORTH;
            case SOUTH -> BlockFace.SOUTH;
            case WEST -> BlockFace.WEST;
            case EAST -> BlockFace.EAST;
        };
    }

    @Nonnull
    public static Direction getDirection(@Nonnull Location location) {
        float yaw = location.getYaw();
        yaw = yaw < 0 ? yaw + 360 : yaw;

        if (yaw >= 315 || yaw < 45) {
            return SOUTH;
        }
        else if (yaw < 135) {
            return WEST;
        }
        else if (yaw < 225) {
            return NORTH;
        }
        else if (yaw < 315) {
            return EAST;
        }
        return NORTH;
    }

}