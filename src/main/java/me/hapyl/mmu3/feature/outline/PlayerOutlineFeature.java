package me.hapyl.mmu3.feature.outline;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.Feature;
import me.hapyl.mmu3.feature.FeatureKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

public final class PlayerOutlineFeature extends Feature {
    
    private final Map<UUID, PlayerOutline> perPlayer = Maps.newHashMap();
    
    public PlayerOutlineFeature(@NotNull Main plugin) {
        super(FeatureKey.create("player_outline"), plugin);
    }
    
    @Nullable
    public PlayerOutline getOutline(@NotNull Player player) {
        return perPlayer.get(player.getUniqueId());
    }
    
    @NotNull
    public PlayerOutline getOrCreateOutline(@NotNull Player player) {
        return perPlayer.computeIfAbsent(player.getUniqueId(), b -> new PlayerOutline(player));
    }
    
    public void reset(@NotNull Player player) {
        final PlayerOutline playerOutline = perPlayer.remove(player.getUniqueId());
        
        if (playerOutline != null) {
            playerOutline.hide();
        }
    }
    
}