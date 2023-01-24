package me.hapyl.mmu3.feature.activity;

import com.google.common.collect.Sets;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.Feature;
import me.hapyl.spigotutils.module.util.Runnables;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;

import java.util.Set;

public class WorldActivity extends Feature implements Listener {

    private final Set<Activity> enabledActivity;

    public WorldActivity(Main mmu3plugin) {
        super(mmu3plugin);
        enabledActivity = Sets.newHashSet();
    }

    public boolean isEnabled(Activity activity) {
        return enabledActivity.contains(activity);
    }

    public void setEnabled(Activity activity, boolean flag) {
        if (flag) {
            enabledActivity.add(activity);
        }
        else {
            enabledActivity.remove(activity);
        }
    }

    public void toggleActivity(Activity activity) {
        setEnabled(activity, !isEnabled(activity));
    }

    // Listeners

    @EventHandler()
    public void handleLiquidFlowing(BlockFromToEvent ev) {
        final Block block = ev.getBlock();
        if (!block.isLiquid()) {
            return;
        }

        final Material type = block.getType();
        if (type == Material.LAVA && isEnabled(Activity.LAVA_FLOWING)) {
            ev.setCancelled(true);
        }
        else if (type == Material.WATER && isEnabled(Activity.WATER_FLOWING)) {
            ev.setCancelled(true);
        }
    }

    @EventHandler()
    public void handleEntityChangeBlockEvent(EntityChangeBlockEvent ev) {
        final Block block = ev.getBlock();
        final Material type = block.getType();
        if (type.hasGravity() && isEnabled(Activity.BLOCK_FALLING)) {
            ev.setCancelled(true);
            block.getState().update(true, false);
        }
    }

    @EventHandler()
    public void handleBlockBreakEvent(BlockBreakEvent ev) {
        final Block block = ev.getBlock();
        if (isEnabled(Activity.BLOCK_UPDATE)) {
            ev.setCancelled(true);
            block.setType(Material.AIR, false);
        }
    }

    @EventHandler()
    public void handleBlockPlaceEvent(BlockPlaceEvent ev) {
        final Block block = ev.getBlock();
        final BlockState state = ev.getBlockReplacedState();

        if (isEnabled(Activity.BLOCK_UPDATE)) {
            ev.setCancelled(true);

            Runnables.runLater(() -> {
                // FIXME: 008, Jan 8, 2023 -> This needs testing for proper block rotation applying.
                state.setBlockData(block.getBlockData());
                state.update(true, false);
            }, 1L);
        }
    }

}
