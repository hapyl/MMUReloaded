package me.hapyl.mmu3.feature.statechanger.adapter;

import org.bukkit.Material;
import org.bukkit.block.data.type.Beehive;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;

public class AdapterBeehive extends LevelledAdapter<Beehive> {
    public AdapterBeehive() {
        super(Beehive.class);
    }

    @Override
    public int getLevel(Beehive beehive) {
        return beehive.getHoneyLevel();
    }

    @Override
    public int getMaxLevel(Beehive beehive) {
        return beehive.getMaximumHoneyLevel();
    }

    @Override
    public int getSlot() {
        return 10;
    }

    @Nonnull
    @Override
    public Material getMaterial() {
        return Material.HONEYCOMB;
    }

    @Nonnull
    @Override
    public String getName() {
        return "Honey Level";
    }

    @Nonnull
    @Override
    public String getDescription() {
        return "Represents the amount of honey stored in the hive.";
    }

    @Nonnull
    @Override
    public BiConsumer<Beehive, Integer> apply() {
        return Beehive::setHoneyLevel;
    }
}
