package me.hapyl.mmu3.feature;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.block.BlockChangeQueue;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.UUID;

public class UndoManager extends Feature {

    private final Map<UUID, BlockChangeQueue> undoMap = Maps.newHashMap();

    public UndoManager(Main mmu3plugin) {
        super(mmu3plugin);
    }

    @Nonnull
    public static BlockChangeQueue getUndoMap(@Nonnull Player player) {
        return getUndoMap(player.getUniqueId());
    }

    @Nonnull
    public static BlockChangeQueue getUndoMap(@Nonnull UUID uuid) {
        return Main.getRegistry().undoManager.undoMap.computeIfAbsent(uuid, fn -> new BlockChangeQueue());
    }
}
