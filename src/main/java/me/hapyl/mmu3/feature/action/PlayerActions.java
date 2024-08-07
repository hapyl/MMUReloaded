package me.hapyl.mmu3.feature.action;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.message.Message;
import me.hapyl.eterna.module.entity.Entities;
import me.hapyl.eterna.module.reflect.npc.HumanNPC;
import me.hapyl.eterna.module.reflect.npc.NPCPose;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Map;

public enum PlayerActions implements PlayerAction {

    SIT {
        private final Map<Player, Entity> sittingMap = Maps.newHashMap();

        @Override
        public void start(@Nonnull Player player) {
            final Location location = player.getLocation();

            location.subtract(0.0d, 0.08d, 0.0d);

            final Entity entity = Entities.BAT.spawn(location, self -> {
                self.setAI(false);
                self.setSilent(true);
                self.setInvisible(true);
                self.setInvulnerable(true);
                self.addPassenger(player);
            });

            sittingMap.put(player, entity);
        }

        @Override
        public void stop(@Nonnull Player player) {
            final Entity entity = sittingMap.remove(player);

            if (entity != null) {
                entity.remove();
            }
        }
    },

    LAY {
        private final Map<Player, HumanNPC> layingMap = Maps.newHashMap();

        @Override
        public void start(@Nonnull Player player) {
            if (true) {
                super.start(player);
                return;
            }

            final Location location = player.getLocation();

            final HumanNPC npc = new HumanNPC(location, player.getCustomName(), player.getName());
            npc.setEquipment(player.getEquipment());
            npc.setPose(NPCPose.SLEEPING);
            npc.showAll();

            layingMap.put(player, npc);
        }

        @Override
        public void stop(@Nonnull Player player) {
            final HumanNPC npc = layingMap.remove(player);

            if (npc != null) {
                npc.remove();
            }
        }
    },

    SWING_HAND {
        @Override
        public void start(@Nonnull Player player) {
            player.swingMainHand();
        }
    },

    SWING_OFF_HAND {
        @Override
        public void start(@Nonnull Player player) {
            player.swingOffHand();
        }
    },

    ;

    public void perform(@Nonnull Player player) {
        final PlayerActionFeature playerActionFeature = Main.getRegistry().playerActionFeature;

        playerActionFeature.perform(player, this);
    }

    @Override
    public void start(@Nonnull Player player) {
        Message.error(player, "This action is currently disabled!");
    }

    @Override
    public void stop(@Nonnull Player player) {
    }
}
