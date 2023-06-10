package me.hapyl.mmu3.feature.trim;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.Feature;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;

public class Trim extends Feature {

    private final Map<UUID, TrimData> trimData;

    public Trim(Main mmu3plugin) {
        super(mmu3plugin);

        trimData = Maps.newHashMap();
    }

    @Nullable
    public TrimData getData(Player player) {
        return trimData.get(player.getUniqueId());
    }

    @Nonnull
    public TrimData getDataOrCreate(Player player) {
        return trimData.computeIfAbsent(player.getUniqueId(), p -> new TrimData(player));
    }
}
