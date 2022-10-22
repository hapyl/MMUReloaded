package me.hapyl.mmu3.utils;

import me.hapyl.spigotutils.module.util.ThreadRandom;

public class IntRangeValue {

    private final int min;
    private final int max;

    public IntRangeValue(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public int random() {
        return ThreadRandom.nextInt(min, max);
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }
}
