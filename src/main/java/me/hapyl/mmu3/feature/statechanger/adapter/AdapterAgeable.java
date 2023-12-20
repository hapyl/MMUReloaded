package me.hapyl.mmu3.feature.statechanger.adapter;

import org.bukkit.Material;
import org.bukkit.block.data.Ageable;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;

public class AdapterAgeable extends LevelledAdapter<Ageable> {
    public AdapterAgeable() {
        super(Ageable.class);
    }

    @Override
    public int getSlot() {
        return 19;
    }

    @Override
    public int getLevel(Ageable ageable) {
        return ageable.getAge();
    }

    @Override
    public int getMaxLevel(Ageable ageable) {
        return ageable.getMaximumAge();
    }

    @Nonnull
    @Override
    public Material getMaterial() {
        return Material.WHEAT_SEEDS;
    }

    @Nonnull
    @Override
    public String getName() {
        return "Age";
    }

    @Nonnull
    @Override
    public String getDescription() {
        return "Represents the different growth stages that a crop-like block can go through.";
    }

    @Nonnull
    @Override
    public BiConsumer<Ageable, Integer> apply() {
        return Ageable::setAge;
    }

}
