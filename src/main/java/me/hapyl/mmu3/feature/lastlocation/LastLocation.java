package me.hapyl.mmu3.feature.lastlocation;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.Feature;
import org.bukkit.Location;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;

public class LastLocation extends Feature {

    private final Map<UUID, Location> lastLocation;

    public LastLocation(Main mmu3plugin) {
        super(mmu3plugin);
        lastLocation = Maps.newHashMap();
    }

    @Nullable
    public Location getLastLocation(UUID uuid) {
        return lastLocation.getOrDefault(uuid, null);
    }

    public void saveLastLocation(UUID uuid, Location location) {
        lastLocation.put(uuid, location);
    }

    public boolean hasLastLocation(UUID uuid) {
        return lastLocation.containsKey(uuid);
    }

}
