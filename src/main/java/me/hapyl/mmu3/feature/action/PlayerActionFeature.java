package me.hapyl.mmu3.feature.action;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.Feature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class PlayerActionFeature extends Feature implements Listener {

    private final Map<Player, PlayerAction> playerActionsMap;

    public PlayerActionFeature(Main mmu3plugin) {
        super(mmu3plugin);

        playerActionsMap = Maps.newHashMap();
    }

    @EventHandler()
    public void handlePlayerDismountEvent(EntityDismountEvent ev) {
        final Entity entity = ev.getEntity();

        if (!(entity instanceof Player player)) {
            return;
        }

        perform(player, null);
    }

    @EventHandler()
    public void handlePlayerQuitEvent(PlayerQuitEvent ev) {
        perform(ev.getPlayer(), null);
    }

    public void perform(@Nonnull Player player, @Nullable PlayerAction action) {
        final PlayerAction oldAction = playerActionsMap.remove(player);

        if (oldAction != null) {
            oldAction.stop(player);

            // If an action performed is the old action, then just stop it
            if (action == oldAction) {
                return;
            }
        }

        if (action == null) { // null = stop performing actions
            return;
        }

        action.start(player);
        playerActionsMap.put(player, action);
    }
}
