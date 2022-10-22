package me.hapyl.mmu3.outcast.hypixel.slayer;

import me.hapyl.mmu3.outcast.hypixel.slayer.boss.SlayerBoss;
import me.hapyl.mmu3.outcast.hypixel.slayer.boss.SlayerBossDrop;
import me.hapyl.spigotutils.module.util.RomanNumber;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * Stores information about certain slayer tier level such as
 * abilities of the boss, amount of EXP required to spawn.
 */
public abstract class SlayerQuest {

    private final SlayerType type;
    private final SlayerBoss boss;

    private SlayerStartCost cost;
    private long expToSpawn;
    private int tier;
    private String difficulty;

    public SlayerQuest(SlayerType type) {
        this.type = type;
        this.cost = SlayerStartCost.UNAFFORDABLE;
        this.boss = new SlayerBoss(this);
    }

    public void setTier(int tier) {
        this.tier = tier;
        this.difficulty = switch (tier) {
            case 1 -> "Beginner";
            case 2 -> "Strong";
            case 3 -> "Challenging";
            case 4 -> "Deadly";
            case 5 -> "Excruciating";
            default -> "No Difficulty";
        };
    }

    @Nonnull
    public final SlayerBoss getBoss() {
        return boss;
    }

    public final String getDifficulty() {
        return difficulty;
    }

    public final int getTier() {
        return tier;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public SlayerQuest setCost(Material material, int amount) {
        this.cost = new SlayerStartCost(material, amount);
        return this;
    }

    public final SlayerStartCost getCost() {
        return cost;
    }

    public String getCostString() {
        return cost == null ? "" : cost.toString();
    }

    public String getTierRomanNumeral() {
        return RomanNumber.toRoman(tier);
    }

    public void generateDrops(Player player) {
        // Guaranteed Roll
        for (SlayerBossDrop drop : type.getDrops()) {
            if (drop.isGuaranteedDrop()) {
                return;
            }
        }
    }

    public long getExpToSpawn() {
        return expToSpawn;
    }

    public boolean isAllowedType(EntityType type) {
        return this.type.isAllowedType(type);
    }

    public SlayerQuest setExpToSpawn(long expToSpawn) {
        this.expToSpawn = expToSpawn;
        return this;
    }

    public SlayerType getType() {
        return type;
    }

    public String getName() {
        return type.getData().getName() + " " + getTierRomanNumeral();
    }

    //    protected void setBoss(SlayerBoss boss) {
    //        this.boss = boss;
    //    }
}
