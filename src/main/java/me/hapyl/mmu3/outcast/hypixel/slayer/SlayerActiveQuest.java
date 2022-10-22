package me.hapyl.mmu3.outcast.hypixel.slayer;

import me.hapyl.mmu3.outcast.hypixel.slayer.boss.SlayerBoss;
import me.hapyl.mmu3.outcast.hypixel.slayer.boss.SlayerSpawnedBoss;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * This tracks active player's quest.
 */
public class SlayerActiveQuest {

    private final UUID uuid;
    private final SlayerQuest quest;
    private long current;
    private State state;
    private SlayerSpawnedBoss boss;

    public SlayerActiveQuest(UUID player, SlayerQuest quest) {
        this.uuid = player;
        this.quest = quest;
        this.current = 0L;
        this.state = State.ACTIVE;
    }

    public void setBoss(SlayerSpawnedBoss boss) {
        this.boss = boss;
    }

    public UUID getUuid() {
        return uuid;
    }

    public SlayerQuest getQuest() {
        return quest;
    }

    public void addExp(Entity entity, long exp) {
        // only allow exp gather when quest is active
        if (state != State.ACTIVE) {
            return;
        }

        current += exp;

        // check completion
        if (current >= quest.getExpToSpawn()) {
            state = State.BOSS;

            final SlayerBoss boss = quest.getBoss();
            if (boss == null) {
                Slayer.sendMessage(getPlayer(), "There is no boss to spawn!");
                return;
            }

            boss.spawnBoss(entity.getLocation(), this);
        }
    }

    public long getCurrentExp() {
        return current;
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    @Nullable
    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public void clear() {
        // TODO -> despawn boss
    }

    public void grantRewards() {

    }

    public SlayerSpawnedBoss getSpawnedBoss() {
        return boss;
    }
}
