package me.hapyl.mmu3.feature.warp;

import me.hapyl.eterna.module.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class WarpConfig extends Config {

    private final Warps warps;

    public WarpConfig(JavaPlugin plugin, Warps warps) {
        super(plugin, "warps");
        this.warps = warps;
    }

    public void loadWarps() {
        for (String name : getConfig().getKeys(false)) {

            final String world = getString(name + ".location.world", "world");
            final double locationX = getDouble(name + ".location.x", 0);
            final double locationY = getDouble(name + ".location.y", 0);
            final double locationZ = getDouble(name + ".location.z", 0);
            final float locationYaw = getFloat(name + ".location.yaw", 0);
            final float locationPitch = getFloat(name + ".location.pitch", 0);
            final String creator = getString(name + ".creator", "System");

            final Warp warp = new Warp(
                    name,
                    new Location(Bukkit.getWorld(world), locationX, locationY, locationZ, locationYaw, locationPitch)
            );
            warp.setCreator(creator);

            warps.register(warp);
        }
    }

    public void saveWarps() {
        for (String key : getConfig().getKeys(false)) {
            set(key, null);
        }

        warps.byName.forEach((name, warp) -> {
            final Location location = warp.getLocation();
            if (location.getWorld() == null) {
                return;
            }
            set(name + ".location.world", location.getWorld().getName());
            set(name + ".location.x", location.getX());
            set(name + ".location.y", location.getY());
            set(name + ".location.z", location.getZ());
            set(name + ".location.yaw", location.getYaw());
            set(name + ".location.pitch", location.getPitch());
            set(name + ".creator", warp.getCreator());
        });

        save();
    }
}
