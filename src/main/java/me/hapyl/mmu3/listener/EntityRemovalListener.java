package me.hapyl.mmu3.listener;

import me.hapyl.mmu3.PersistentPlayerData;
import me.hapyl.mmu3.message.Message;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class EntityRemovalListener implements Listener {

    @EventHandler()
    public void handleEntityDamageByEntityEvent(EntityDamageByEntityEvent ev) {
        if (!(ev.getDamager() instanceof Player player)) {
            return;
        }

        final Entity entity = ev.getEntity();
        final EntityType type = ev.getEntityType();

        if (isBlockedEntity(type)) {
            return;
        }

        if (isInstantRemove(type) || PersistentPlayerData.getData(player).isEntityRemoval()) {
            ev.setCancelled(true);
            ev.setDamage(0.0d);
            entity.remove();
            Message.info(player, "Removed %s.", entity.getName());
        }
    }

    @EventHandler()
    public void handlePlayerJoinEvent(PlayerJoinEvent ev) {
        final Player player = ev.getPlayer();
        if (PersistentPlayerData.getData(player).isEntityRemoval()) {
            Message.info(player, "Entity Removal is still enabled!");
        }
    }

    private boolean isBlockedEntity(EntityType type) {
        return switch (type) {
            case PLAYER -> true;
            default -> false;
        };
    }

    private boolean isInstantRemove(EntityType type) {
        return switch (type) {
            case ENDER_CRYSTAL, PRIMED_TNT -> true;
            default -> false;
        };
    }


}
