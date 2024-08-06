package me.hapyl.mmu3.feature.banner;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.Feature;
import me.hapyl.mmu3.message.Message;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class BannerEditor extends Feature {

    public static final int MAX_PATTERNS = 16;

    private final Map<UUID, BannerData> playerData;

    public BannerEditor(Main mmu3plugin) {
        super(mmu3plugin);

        this.playerData = Maps.newHashMap();
    }

    public BannerData getOrCreate(Player player) {
        return playerData.computeIfAbsent(player.getUniqueId(), p -> new BannerData());
    }

    public void remove(Player player) {
        final UUID uuid = player.getUniqueId();

        if (!playerData.containsKey(uuid)) {
            return;
        }

        playerData.remove(uuid);
        Message.success(player, "Reset banner editor!");
    }

}
