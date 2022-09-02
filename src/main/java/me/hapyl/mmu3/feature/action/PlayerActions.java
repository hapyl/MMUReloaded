package me.hapyl.mmu3.feature.action;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.message.Message;
import me.hapyl.spigotutils.module.entity.Entities;
import me.hapyl.spigotutils.module.util.Nulls;
import org.bukkit.Location;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Map;

public enum PlayerActions {

    SIT(new PlayerStoppableAction() {

        private final Map<Player, Entity> arrows = Maps.newHashMap();

        @Override
        public void perform(Player player) {
            final Location location = player.getLocation();
            arrows.put(player, Entities.ARROW.spawn(location, self -> {
                self.setDamage(0.0d);
                self.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
                self.setGravity(false);
                self.setVelocity(new Vector(0.0d, 0.0d, 0.0d));
                self.addPassenger(player);
            }));

            Message.info(player, "You are now sitting.");
        }

        @Override
        public void stopPerforming(Player player) {
            Nulls.runIfNotNull(arrows.get(player), (entity) -> {
                entity.remove();
                Message.info(player, "You are no longer sitting.");
                arrows.remove(player);
            });
        }
    }),

    LAY(player -> {

    }),

    SWING_HAND(LivingEntity::swingMainHand),
    SWING_OFF_HAND(LivingEntity::swingOffHand),

    ;

    private final PlayerAction action;

    PlayerActions(PlayerAction action) {
        this.action = action;
    }

    public void perform(Player player) {
        // stop old actions if present
        for (PlayerActions value : values()) {
            if (value.action instanceof PlayerStoppableAction stoppableAction) {
                stoppableAction.stopPerforming(player);
            }
        }
        action.perform(player);
    }

}
