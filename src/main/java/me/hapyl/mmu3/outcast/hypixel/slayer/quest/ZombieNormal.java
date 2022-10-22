package me.hapyl.mmu3.outcast.hypixel.slayer.quest;

import me.hapyl.mmu3.outcast.hypixel.slayer.boss.SlayerBoss;
import me.hapyl.mmu3.outcast.hypixel.slayer.boss.SlayerBossAbility;
import org.bukkit.Material;

public class ZombieNormal extends ZombieEasy {
    public ZombieNormal() {
        setCost(Material.IRON_INGOT, 6);
        setExpToSpawn(1);

        final SlayerBoss boss = getBoss();

        boss.setHealth(200);
        boss.setDPS(5);
        boss.addAbility(new SlayerBossAbility("&aPestilence", "Deals AOE damage every second, shredding armor by 25%."));
    }
}
