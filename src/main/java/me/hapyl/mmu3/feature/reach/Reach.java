package me.hapyl.mmu3.feature.reach;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.Feature;
import me.hapyl.mmu3.message.Message;
import me.hapyl.spigotutils.module.chat.Chat;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Reach extends Feature implements Listener {

    private final Map<UUID, Integer> reachMap;

    public Reach(Main mmu3plugin) {
        super(mmu3plugin);
        this.reachMap = Maps.newHashMap();
    }

    public int getReach(Player player) {
        return reachMap.getOrDefault(player.getUniqueId(), 0);
    }

    public boolean hasReach(Player player) {
        return getReach(player) > 0;
    }

    public void setReach(Player player, int maxRange) {
        reachMap.put(player.getUniqueId(), maxRange);
    }

    @EventHandler()
    public void handleBlockPlace(BlockPlaceEvent ev) {
        final Player player = ev.getPlayer();
        if (hasReach(player)) {
            ev.setCancelled(true);
        }
    }

    @EventHandler()
    public void handleBlockBreak(BlockBreakEvent ev) {
        final Player player = ev.getPlayer();
        if (hasReach(player)) {
            ev.setCancelled(true);
        }
    }

    @EventHandler()
    public void handleReach(PlayerInteractEvent ev) {
        final Player player = ev.getPlayer();

        if (!hasReach(player)) {
            return;
        }

        final Action action = ev.getAction();
        final int playerReach = getReach(player);

        // Handle block breaking
        if (action == Action.LEFT_CLICK_AIR) {
            final Block targetBlock = player.getTargetBlockExact(playerReach);
            if (targetBlock == null || targetBlock.getType().isAir()) {
                return;
            }

            final Material type = targetBlock.getType();
            if (type.isInteractable()) {
                Message.error(player, "Unable to break %s using reach.", Chat.capitalize(type));
                return;
            }

            targetBlock.setType(Material.AIR, false);
            return;
        }

        // Handle block placement
        if (action == Action.RIGHT_CLICK_AIR) {
            final List<Block> lastBlocks = player.getLastTwoTargetBlocks(null, playerReach);

            final Block block = lastBlocks.get(0);
            final Block target = lastBlocks.get(1);
        }

    }

}
