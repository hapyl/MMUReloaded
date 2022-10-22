package me.hapyl.mmu3.outcast.hypixel.slayer.boss;

import com.google.common.collect.Maps;
import me.hapyl.mmu3.utils.IntRangeValue;
import org.bukkit.Material;

import java.util.Map;

public class SlayerBossDrop {

    private float chance;
    private final Material material;
    private final Map<Integer, IntRangeValue> perTierAmount;

    public SlayerBossDrop(Material material) {
        this.material = material;
        this.perTierAmount = Maps.newHashMap();
    }

    public Map<Integer, IntRangeValue> getTierAmounts() {
        return perTierAmount;
    }

    public int getAmountRange(int tier) {
        final IntRangeValue range = perTierAmount.get(tier);
        return range == null ? 0 : range.random();
    }

    public SlayerBossDrop setRangeAmount(int tier, int min, int max) {
        perTierAmount.put(tier, new IntRangeValue(min, max));
        return this;
    }

    public SlayerBossDrop setChance(float chance) {
        this.chance = chance;
        return this;
    }

    public boolean isGuaranteedDrop() {
        return chance == 1.0f;
    }

    public double getChance() {
        return chance;
    }

    public Material getMaterial() {
        return material;
    }

    public String getChanceString() {
        if (chance == 1.0f) {
            return "&aGuaranteed &8(¹⁰⁰%)";
        }
        else if (checkChance(0.2f, 1.0f)) {
            return "&9Occasional &8(²⁰%)";
        }
        else if (checkChance(0.05f, 0.2f)) {
            return "&bRare &8(⁵%)";
        }
        else if (checkChance(0.01f, 0.05f)) {
            return "&5Extraordinary &8(¹%)";
        }
        else if (checkChance(0.005f, 0.01f)) {
            return "&f&lPray RNGesus &8(⁰.⁵%)";
        }
        else if (chance < 0.005f) {
            return "&c&lRNGesus Incarnate &8(<⁰.⁵%)";
        }

        return "&4&lUnable to drop";
    }

    private boolean checkChance(float m, float mx) {
        return chance >= m && chance < mx;
    }
}
