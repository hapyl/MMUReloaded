package me.hapyl.mmu3;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.utils.BlockChangeQueue;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

// idk making this static why now
public final class UndoManager {

    private static final Map<UUID, BlockChangeQueue> UNDO_MAP = Maps.newHashMap();

    public static BlockChangeQueue getUndoMap(Player player) {
        return getUndoMap(player.getUniqueId());
    }

    public static BlockChangeQueue getUndoMap(UUID uuid) {
        return UNDO_MAP.computeIfAbsent(uuid, u -> new BlockChangeQueue());
    }
}
