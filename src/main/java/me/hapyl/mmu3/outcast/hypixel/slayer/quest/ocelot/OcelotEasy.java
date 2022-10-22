package me.hapyl.mmu3.outcast.hypixel.slayer.quest.ocelot;

import me.hapyl.mmu3.outcast.hypixel.slayer.SlayerQuest;
import me.hapyl.mmu3.outcast.hypixel.slayer.SlayerType;
import me.hapyl.mmu3.outcast.hypixel.slayer.boss.SlayerBoss;
import org.bukkit.entity.EntityType;

public class OcelotEasy extends SlayerQuest {
    public OcelotEasy() {
        super(SlayerType.OCELOT);

        final SlayerBoss boss = getBoss();

        boss.setType(EntityType.OCELOT);
        boss.setHealth(500d);
    }

}
