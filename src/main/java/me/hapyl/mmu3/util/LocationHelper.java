package me.hapyl.mmu3.util;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public final class LocationHelper {
    
    private LocationHelper() {
    }
    
    @NotNull
    public static Location copyOf(@NotNull Location location) {
        return new Location(location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }
    
    @NotNull
    public static Location center(@NotNull Location location) {
        final double newX = location.getBlockX() + 0.5;
        final double newZ = location.getBlockZ() + 0.5;
        
        return new Location(location.getWorld(), newX, location.getY(), newZ, location.getYaw(), location.getPitch());
    }
    
}
