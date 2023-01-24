package me.hapyl.mmu3.feature.warp;

import com.google.common.collect.Lists;
import me.hapyl.spigotutils.module.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;

public class WarpConfig extends Config {
    public WarpConfig(JavaPlugin plugin) {
        super(plugin, "warps");
    }

    public List<Warp> loadWarps() {
        final List<Warp> warps = Lists.newArrayList();
        for (String name : getConfig().getKeys(false)) {

            final String world = getString(name + ".location.world", "world");
            final double locationX = getDouble(name + ".location.x", 0);
            final double locationY = getDouble(name + ".location.y", 0);
            final double locationZ = getDouble(name + ".location.z", 0);
            final String creator = getString(name + ".creator", "System");

            final Warp warp = new Warp(name, new Location(Bukkit.getWorld(world), locationX, locationY, locationZ));
            warp.setCreator(creator);

            warps.add(warp);
        }

        return warps;
    }

    public void saveWarps(Map<String, Warp> warps) {
        /**
         * {
         *     my_warp: ROOT
         *     > location: Location
         *     >> world: World
         *     >> x: double
         *     >> y: double
         *     >> z: double
         *     > creator: String
         * }
         */
        warps.forEach((name, warp) -> {
            final Location location = warp.getLocation();
            if (location.getWorld() == null) {
                return;
            }
            set(name + ".location.world", location.getWorld().getName());
            set(name + ".location.x", location.getX());
            set(name + ".location.y", location.getY());
            set(name + ".location.z", location.getZ());
            set(name + ".creator", warp.getCreator());
        });

        save();
    }
}
