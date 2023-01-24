package me.hapyl.mmu3.outcast.fishing;

import me.hapyl.mmu3.outcast.fishing.fishtype.Cod;
import me.hapyl.spigotutils.module.util.CollectionUtils;

/**
 * Represents a storage for fish.
 */
public enum FishType {
    COD(new Cod()),
    ;

    private final Fish fish;

    FishType(Fish fish) {
        this.fish = fish;
    }

    public Fish getFish() {
        return fish;
    }

    public static FishType random() {
        return CollectionUtils.randomElement(values());
    }

}
