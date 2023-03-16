package me.hapyl.mmu3.feature.block;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.Feature;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Map;
import java.util.UUID;

public class BlockReplacer extends Feature implements Listener {

    private final Map<UUID, Map<Material, Material>> blockReplacements;

    public BlockReplacer(Main mmu3plugin) {
        super(mmu3plugin);
        blockReplacements = Maps.newHashMap();
    }

    @EventHandler()
    public void handleBlockPlaceEvent(BlockPlaceEvent ev) {
        final Player player = ev.getPlayer();
        final Block block = ev.getBlock();
        final Material material = block.getType();

        final Map<Material, Material> replacement = getReplacement(player);
        final Material state = replacement.get(material);

        if (state == null) {
            return;
        }

        block.setType(state, false);
        ev.setCancelled(true);
    }

    public boolean hasReplacer(Player player, Material material) {
        return getReplacement(player).containsKey(material);
    }

    public void remove(Player player, Material material) {
        getReplacement(player).remove(material);
    }

    public Map<Material, Material> getReplacement(Player player) {
        return blockReplacements.getOrDefault(player.getUniqueId(), Maps.newHashMap());
    }

    // TODO: 015, Mar 15, 2023 - Allow data
    public void set(Player player, Material type, Material data) {
        getReplacement(player).put(type, data);
    }
}
