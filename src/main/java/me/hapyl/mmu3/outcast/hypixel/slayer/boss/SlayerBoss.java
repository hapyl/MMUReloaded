package me.hapyl.mmu3.outcast.hypixel.slayer.boss;

import com.google.common.collect.Lists;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.outcast.hypixel.slayer.SlayerActiveQuest;
import me.hapyl.mmu3.outcast.hypixel.slayer.SlayerQuest;
import me.hapyl.spigotutils.module.player.PlayerLib;
import me.hapyl.spigotutils.module.util.Nulls;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.util.List;

/**
 * This class is a container for a boss type, equipment, abilities etc.
 * {@link SlayerSpawnedBoss} for actual spawned boss and entity.
 */
public class SlayerBoss {

    private final SlayerQuest quest;
    private final List<SlayerBossAbility> abilities;
    private EntityType bossType;
    private EntityEquipment equipment;
    private double health;
    private double dps;

    public SlayerBoss(SlayerQuest quest) {
        this.quest = quest;
        this.abilities = Lists.newArrayList();
    }

    public SlayerQuest getQuest() {
        return quest;
    }

    public EntityType getBossType() {
        return bossType;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public void setType(EntityType bossType) {
        this.bossType = bossType;
    }

    public EntityEquipment getEquipment() {
        return equipment;
    }

    public void setEquipment(EntityEquipment equipment) {
        this.equipment = equipment;
    }

    public List<SlayerBossAbility> getAbilities() {
        return abilities;
    }

    public void addAbility(SlayerBossAbility ability) {
        abilities.add(ability);
    }

    public void spawnBoss(Location location, @Nullable SlayerActiveQuest active) {
        final SlayerSpawnedBoss boss = new SlayerSpawnedBoss(this, active) {
        };

        final int delay = playSpawnAnimation(location.clone().add(0.0d, 0.5d, 0.0d));

        // Spawn
        new BukkitRunnable() {
            @Override
            public void run() {
                boss.createEntity(location);
            }
        }.runTaskLater(Main.getPlugin(), delay);

        // Tick
        new BukkitRunnable() {
            private int tick;

            @Override
            public void run() {
                if (boss.entity.isDead()) {
                    cancel();
                    return;
                }

                getAbilities().forEach(SlayerBossAbility::tick);

                if (tick % 10 == 0) {
                    boss.updateHealthDisplay();
                }

                tick++;
            }
        }.runTaskTimer(Main.getPlugin(), delay + 1L, 1L);

        Nulls.runIfNotNull(active, a -> a.setBoss(boss));
    }

    protected int playSpawnAnimation(Location location) {
        final int maxFrame = 30;
        final float addPerFrame = 1.15f / maxFrame;

        new BukkitRunnable() {
            private int frame = 0;

            @Override
            public void run() {
                if (frame++ > maxFrame) {
                    PlayerLib.playSound(location, Sound.ENTITY_WITHER_SPAWN, 2.0f);
                    this.cancel();
                    return;
                }

                if (frame % 2 != 0) {
                    return;
                }

                // Particle
                PlayerLib.spawnParticle(location, Particle.CRIT, 10, 0.25, 0.25, 0.25, 0.2f);
                PlayerLib.spawnParticle(location, Particle.EXPLOSION_LARGE, 1, 0.25, 0.25, 0.25, 0.2f);

                // Sound
                PlayerLib.playSound(location, Sound.ENTITY_WITHER_SHOOT, Math.min(2.0f, 0.85f + (addPerFrame * frame)));
                PlayerLib.playSound(location, Sound.ENTITY_FISHING_BOBBER_THROW, Math.min(2.0f, 0.85f + (addPerFrame * frame)));
            }
        }.runTaskTimer(Main.getPlugin(), 0L, 1L);

        return maxFrame + 1;
    }

    public double getDPS() {
        return dps;
    }

    public void setDPS(int i) {
        this.dps = i;
    }
}
