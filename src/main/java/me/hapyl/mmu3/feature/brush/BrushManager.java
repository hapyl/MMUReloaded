package me.hapyl.mmu3.feature.brush;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.Feature;
import me.hapyl.mmu3.message.Message;
import org.bukkit.FluidCollisionMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

public class BrushManager extends Feature implements Listener {

    private final Map<UUID, PlayerBrush> playerBrushMap;

    public BrushManager(Main mmu3plugin) {
        super(mmu3plugin);
        this.playerBrushMap = Maps.newConcurrentMap();
    }

    public PlayerBrush getBrush(Player player) {
        return playerBrushMap.computeIfAbsent(player.getUniqueId(), PlayerBrush::new);
    }

    @EventHandler()
    public void handlePlayerInteractEvent(PlayerInteractEvent ev) {
        final Player player = ev.getPlayer();

        if (ev.getHand() == EquipmentSlot.OFF_HAND ||
                (ev.getAction() != Action.RIGHT_CLICK_AIR && ev.getAction() != Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        final PlayerBrush brush = getBrush(player);
        final ItemStack handItem = player.getInventory().getItemInMainHand();

        if (brush.getBrush() == Brush.NONE || handItem.getType() != brush.getBrushItem()) {
            return;
        }

        final Block block = player.getTargetBlockExact(100, FluidCollisionMode.ALWAYS);

        if (block == null) {
            Message.error(player, "No valid block in sight!");
            return;
        }

        brush.useBrush(block.getLocation());
        ev.setCancelled(true);

    }

    public void resetBrush(Player player) {
        playerBrushMap.put(player.getUniqueId(), new PlayerBrush(player.getUniqueId()));
    }
}
