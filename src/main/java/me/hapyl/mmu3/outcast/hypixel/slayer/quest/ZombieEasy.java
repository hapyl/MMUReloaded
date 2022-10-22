package me.hapyl.mmu3.outcast.hypixel.slayer.quest;

import me.hapyl.mmu3.outcast.hypixel.slayer.SlayerQuest;
import me.hapyl.mmu3.outcast.hypixel.slayer.SlayerType;
import me.hapyl.mmu3.outcast.hypixel.slayer.boss.SlayerBoss;
import me.hapyl.mmu3.outcast.hypixel.slayer.boss.SlayerBossAbility;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public class ZombieEasy extends SlayerQuest {
    public ZombieEasy() {
        super(SlayerType.ZOMBIE);
        setCost(Material.IRON_INGOT, 2);
        setExpToSpawn(1);

        final SlayerBoss boss = getBoss();

        boss.setType(EntityType.ZOMBIE);
        boss.setHealth(100);
        boss.addAbility(new SlayerBossAbility("&cLife Drain", "Drains health every few seconds."));

    }
}
