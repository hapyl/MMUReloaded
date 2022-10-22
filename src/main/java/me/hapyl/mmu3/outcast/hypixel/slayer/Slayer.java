package me.hapyl.mmu3.outcast.hypixel.slayer;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.feature.Feature;
import me.hapyl.mmu3.outcast.hypixel.slayer.boss.SlayerBossDrop;
import me.hapyl.mmu3.outcast.hypixel.slayer.boss.SlayerSpawnedBoss;
import me.hapyl.mmu3.outcast.hypixel.slayer.quest.ZombieEasy;
import me.hapyl.mmu3.outcast.hypixel.slayer.quest.ZombieNormal;
import me.hapyl.mmu3.outcast.hypixel.slayer.quest.ocelot.OcelotEasy;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.player.PlayerLib;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;

public class Slayer extends Feature {

    private final Map<UUID, SlayerActiveQuest> activeQuest;

    public Slayer(Main mmu3plugin) {
        super(mmu3plugin);
        activeQuest = Maps.newHashMap();

        registerSlayerQuests();
        registerSlayerDrops();
    }

    private void registerSlayerQuests() {
        // Zombie
        SlayerType.ZOMBIE.getQuests().setTier(1, new ZombieEasy());
        SlayerType.ZOMBIE.getQuests().setTier(2, new ZombieNormal());

        SlayerType.OCELOT.getQuests().setTier(1, new OcelotEasy());

    }

    private void registerSlayerDrops() {
        // Zombie
        SlayerType.ZOMBIE.addDrop(new SlayerBossDrop(Material.ROTTEN_FLESH).setChance(1.0f).setRangeAmount(1, 1, 10));
        SlayerType.ZOMBIE.addDrop(new SlayerBossDrop(Material.IRON_NUGGET).setChance(0.5f).setRangeAmount(1, 10, 24));
        SlayerType.ZOMBIE.addDrop(new SlayerBossDrop(Material.DIAMOND).setChance(0.1f).setRangeAmount(1, 1, 2));
        SlayerType.ZOMBIE.addDrop(new SlayerBossDrop(Material.IRON_BLOCK).setChance(0.05f).setRangeAmount(1, 1, 1));

        // Test
        final SlayerBossDrop drop = new SlayerBossDrop(Material.IRON_SHOVEL).setChance(0.0001f);
        SlayerType.ZOMBIE.addDrop(drop);
    }

    public boolean hasActiveQuest(Player player) {
        return activeQuest.containsKey(player.getUniqueId());
    }

    @Nullable
    public SlayerActiveQuest getActiveQuest(Player player) {
        return getActiveQuest(player.getUniqueId());
    }

    @Nullable
    public SlayerActiveQuest getActiveQuest(UUID uuid) {
        return activeQuest.get(uuid);
    }

    public static void sendMessage(Player player, String message, Object... replacements) {
        Chat.sendMessage(player, "&3&lSLAYER! &7" + message, replacements);
    }

    public void clearActiveQuest(Player player) {
        clearActiveQuest(player.getUniqueId());
    }

    public void clearActiveQuest(UUID uuid) {
        final SlayerActiveQuest quest = activeQuest.get(uuid);
        if (quest != null) {
            quest.clear();
        }

        activeQuest.remove(uuid);
    }

    public void startQuest(Player player, SlayerQuest quest) {
        final SlayerActiveQuest oldQuest = getActiveQuest(player);
        if (oldQuest != null) {
            Slayer.sendMessage(player, "Already have quest active!");
            return;
        }

        final UUID uuid = player.getUniqueId();
        activeQuest.put(uuid, new SlayerActiveQuest(uuid, quest));
        PlayerLib.playSound(player, Sound.ENTITY_ENDER_DRAGON_GROWL, 2.0f);
    }

    @Nullable
    public SlayerActiveQuest findBossQuest(LivingEntity entity) {
        if (activeQuest.isEmpty()) {
            return null;
        }

        for (SlayerActiveQuest quest : activeQuest.values()) {
            final SlayerSpawnedBoss boss = quest.getSpawnedBoss();
            if (boss != null && boss.getEntity() == entity) {
                return quest;
            }
        }

        return null;
    }
}
