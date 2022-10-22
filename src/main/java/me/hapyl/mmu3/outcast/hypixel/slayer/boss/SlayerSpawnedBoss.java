package me.hapyl.mmu3.outcast.hypixel.slayer.boss;

import me.hapyl.mmu3.outcast.hypixel.slayer.SlayerActiveQuest;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.util.BukkitUtils;
import me.hapyl.spigotutils.module.util.Nulls;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.util.Objects;

public class SlayerSpawnedBoss {

    protected final SlayerBoss boss;
    protected final SlayerActiveQuest quest;
    protected LivingEntity entity;

    public SlayerSpawnedBoss(SlayerBoss boss, SlayerActiveQuest quest) {
        this.boss = boss;
        this.quest = quest;
    }

    // todo -> impl events

    public final void createEntity(Location location) {
        final EntityType type = boss.getBossType();

        final World world = location.getWorld();
        if (type == null || world == null) {
            return;
        }

        entity = (LivingEntity) world.spawn(location, Objects.requireNonNull(type.getEntityClass()), self -> {
            // Pre-Prepare entity
            final LivingEntity living = (LivingEntity) self;
            final double health = boss.getHealth();

            living.setMaximumNoDamageTicks(0);
            living.setMaxHealth(health);
            living.setHealth(health);
        });

        updateHealthDisplay();
    }

    protected void updateHealthDisplay() {
        Nulls.runIfNotNull(entity, self -> {
            self.setCustomName(Chat.format("&c☠ &f%s %s&7/&a%s", boss.getQuest().getName(), getHealthColored(), boss.getHealth()));
            if (!self.isCustomNameVisible()) {
                self.setCustomNameVisible(true);
            }
        });
    }

    private String getHealthColored() {
        if (entity == null) {
            return "&cDEAD";
        }

        final double health = entity.getHealth();
        final double maxHealth = boss.getHealth();
        final double percent = health * 1.0f / maxHealth;

        return (percent <= 0.25f ? "&c" : percent <= 0.5f ? "&e" : "&a") + BukkitUtils.decimalFormat(health);
    }

    public LivingEntity getEntity() {
        return entity;
    }


}
