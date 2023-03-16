package me.hapyl.mmu3.feature.bb;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.Feature;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class BoundingBoxManager extends Feature {

    private final Map<UUID, LoggedBoundingBox> perPlayer = Maps.newHashMap();

    public BoundingBoxManager(Main mmu3plugin) {
        super(mmu3plugin);
    }

    public LoggedBoundingBox getOutline(Player player) {
        return perPlayer.get(player.getUniqueId());
    }

    public LoggedBoundingBox getOrCreateOutline(Player player) {
        return perPlayer.computeIfAbsent(player.getUniqueId(), b -> new LoggedBoundingBox(player));
    }

    public void remove(Player player) {
        final LoggedBoundingBox outline = getOutline(player);
        if (outline != null) {
            outline.hide();
        }

        perPlayer.remove(player.getUniqueId());
    }
}
