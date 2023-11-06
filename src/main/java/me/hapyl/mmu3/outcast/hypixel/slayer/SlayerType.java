package me.hapyl.mmu3.outcast.hypixel.slayer;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import me.hapyl.mmu3.outcast.hypixel.slayer.boss.SlayerBossDrop;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public enum SlayerType {

    ZOMBIE(
            new SlayerData("Revenant Horror", "Abhorrent Zombie stuck between life and death for an eternity.", Material.ROTTEN_FLESH),
            EntityType.ZOMBIE,
            EntityType.ZOMBIE_VILLAGER,
            EntityType.ZOMBIE_HORSE
    ),

    SPIDER(
            new SlayerData("Tarantula Broodfather", "Monstrous Spider who poisons and devours its victims.", Material.COBWEB),
            EntityType.SPIDER,
            EntityType.CAVE_SPIDER,
            EntityType.SILVERFISH
    ),

    WOLF(
            new SlayerData(
                    "Sven Packmaster",
                    "Rabid Wolf genetically modified by a famous mad scientist. Eats bones and flesh.",
                    Material.MUTTON
            ),
            EntityType.WOLF
    ),

    ENDERMAN(
            new SlayerData(
                    "Voidgloom Seraph",
                    "If Necron is the right-hand of the Wither King, this dark demigod is the left-hand.",
                    Material.ENDER_PEARL
            ),
            EntityType.ENDERMAN,
            EntityType.ENDERMITE,
            EntityType.ENDER_DRAGON
    ),

    BLAZE(
            new SlayerData(
                    "Inferno Demonlord",
                    "Even demons fear this incarnation of pure evil, constantly feeding on its burning desire for destruction.",
                    Material.BLAZE_POWDER
            ),
            EntityType.BLAZE
    ),

    ;

    private final TieredSlayerQuests quests;
    private final SlayerData data;
    private final Set<EntityType> allowedTypes;
    private final List<SlayerBossDrop> drops;

    SlayerType(SlayerData data, EntityType... types) {
        this.data = data;
        this.quests = new TieredSlayerQuests(this);
        this.allowedTypes = Sets.newLinkedHashSet();
        this.allowedTypes.addAll(Arrays.asList(types));
        this.drops = Lists.newArrayList();
    }

    public void addDrop(SlayerBossDrop drop) {
        drops.add(drop);
    }

    public void addDrops(SlayerBossDrop... drop) {
        drops.addAll(List.of(drop));
    }

    public List<SlayerBossDrop> getDrops() {
        return drops;
    }

    public TieredSlayerQuests getQuests() {
        return quests;
    }

    public Set<EntityType> getAllowedTypes() {
        return allowedTypes;
    }

    public boolean isAllowedType(EntityType type) {
        return allowedTypes.contains(type);
    }

    public SlayerData getData() {
        return data;
    }

}
