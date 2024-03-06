package me.hapyl.mmu3.feature.activity;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.Feature;
import me.hapyl.mmu3.feature.MMURunnable;
import me.hapyl.mmu3.message.Message;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.util.Runnables;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class WorldActivity extends Feature implements Listener, MMURunnable {

    private boolean enabled;

    public WorldActivity(Main mmu3plugin) {
        super(mmu3plugin);
        enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean flag) {
        this.enabled = flag;
    }

    public void toggleActivity(boolean broadcastStatus) {
        setEnabled(!enabled);

        if (!broadcastStatus) {
            return;
        }

        Message.broadcast("Block Updates are now %s!", !enabled ? "enabled" : "disabled");

        if (enabled) {
            Message.broadcast("&oBlocks won't update, such as powders won't fall, liquids won't flow, etc.");
        }

        Message.broadcast("&oBlocks work as normal.");
    }

    // Listeners

    @EventHandler()
    public void handleLiquidFlowing(BlockFromToEvent ev) {
        final Block block = ev.getBlock();

        if (!block.isLiquid()) {
            return;
        }

        final Material type = block.getType();

        if ((type == Material.LAVA || type == Material.WATER) && enabled) {
            ev.setCancelled(true);
        }
    }

    @EventHandler()
    public void handleEntityChangeBlockEvent(EntityChangeBlockEvent ev) {
        final Block block = ev.getBlock();
        final Material type = block.getType();
        if (type.hasGravity() && enabled) {
            ev.setCancelled(true);
            block.getState().update(true, false);
        }
    }

    @EventHandler()
    public void handleBlockBreakEvent(BlockBreakEvent ev) {
        final Block block = ev.getBlock();

        if (enabled) {
            ev.setCancelled(true);
            block.setType(Material.AIR, false);
        }
    }

    @EventHandler()
    public void handleBlockPlaceEvent(BlockPlaceEvent ev) {
        final Block block = ev.getBlock();
        final BlockState state = ev.getBlockReplacedState();
        final Player player = ev.getPlayer();

        if (!enabled) {
            return;
        }

        final BlockData blockData = block.getBlockData();

        ev.setCancelled(true);

        Runnables.runLater(() -> {
            block.setType(player.getInventory().getItemInMainHand().getType(), false);
            state.setBlockData(blockData);
            state.update(true, false);
        }, 1L);
    }

    @Override
    public void run() {
        if (!enabled) {
            return;
        }

        Bukkit.getOnlinePlayers()
                .stream()
                .filter(Player::isOp)
                .forEach(player -> Chat.sendActionbar(player, Message.PREFIX + "Block Update Suppressed!"));
    }

    @Override
    public int period() {
        return 20;
    }

}
