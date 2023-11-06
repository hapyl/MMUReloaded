package me.hapyl.mmu3.outcast.fishing;

import com.google.common.collect.Sets;
import org.bukkit.block.Biome;

import java.util.Set;

public class FishProperties {

    private final Set<Biome> biomes;
    private int minSize;
    private int maxSize;
    private int requiredBaitLevel;
    private int requiredFishLevel;
    private int catchTimeMin;
    private int catchTimeMax;

    public FishProperties() {
        this(1, 2, 0, 0);
    }

    public FishProperties(int minSize, int maxSize, int requiredBaitLevel, int requiredFishLevel) {
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.requiredBaitLevel = requiredBaitLevel;
        this.requiredFishLevel = requiredFishLevel;
        this.catchTimeMin = 0;
        this.catchTimeMax = 24000;
        this.biomes = Sets.newHashSet();
    }

    public int getCatchTimeMin() {
        return catchTimeMin;
    }

    public void setCatchTimeMin(int catchTimeMin) {
        this.catchTimeMin = catchTimeMin;
    }

    public int getCatchTimeMax() {
        return catchTimeMax;
    }

    public void setCatchTimeMax(int catchTimeMax) {
        this.catchTimeMax = catchTimeMax;
    }

    public int getMinSize() {
        return minSize;
    }

    public void setMinSize(int minSize) {
        this.minSize = minSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public Set<Biome> getBiomes() {
        return biomes;
    }

    public int getRequiredBaitLevel() {
        return requiredBaitLevel;
    }

    public void setRequiredBaitLevel(int requiredBaitLevel) {
        this.requiredBaitLevel = requiredBaitLevel;
    }

    public int getRequiredFishLevel() {
        return requiredFishLevel;
    }

    public void setRequiredFishLevel(int requiredFishLevel) {
        this.requiredFishLevel = requiredFishLevel;
    }

    public FishGrade getGrade(int size) {
        if (size < minSize || size > maxSize) {
            return FishGrade.INVALID;
        }

        // 0.0-1.0
        final float percent = (float) (size - minSize) / (maxSize - minSize);

        if (percent <= 0.2f) {
            return FishGrade.SMALL;
        }
        else if (percent <= 0.333f) {
            return FishGrade.BIG;
        }
        else if (percent <= 0.5f) {
            return FishGrade.BIG;
        }
        else if (percent <= 0.99f) {
            return FishGrade.GIANT;
        }

        return FishGrade.MASSIVE;
    }

    @Override
    public String toString() {
        return "FishProperties{" +
                "biomes=" + biomes +
                ", minSize=" + minSize +
                ", maxSize=" + maxSize +
                ", requiredBaitLevel=" + requiredBaitLevel +
                ", requiredFishLevel=" + requiredFishLevel +
                ", catchTimeMin=" + catchTimeMin +
                ", catchTimeMax=" + catchTimeMax +
                '}';
    }

    private boolean checkSize(float size, float m, float mx) {
        return size >= m && size < mx;
    }
}
