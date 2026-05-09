package me.hapyl.mmu3.feature;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.Main;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;

public class LastLocationFeature extends Feature implements Listener {
    
    private final Map<UUID, Location> lastLocationMap;
    
    public LastLocationFeature(@NotNull Main plugin) {
        super(FeatureKey.create("last_location"), plugin);
        
        this.lastLocationMap = Maps.newHashMap();
    }
    
    @EventHandler(ignoreCancelled = true)
    public void handlePlayerTeleportEvent(PlayerTeleportEvent ev) {
        final Player player = ev.getPlayer();
        
        Main.getLastLocation().saveLastLocation(player.getUniqueId(), ev.getFrom());
    }
    
    @Nullable
    public Location getLastLocation(UUID uuid) {
        return lastLocationMap.getOrDefault(uuid, null);
    }
    
    public void saveLastLocation(UUID uuid, Location location) {
        lastLocationMap.put(uuid, location);
    }
    
    public boolean hasLastLocation(UUID uuid) {
        return lastLocationMap.containsKey(uuid);
    }
    
}
