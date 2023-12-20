package me.hapyl.mmu3.feature.statechanger.adapter;

import org.bukkit.Material;
import org.bukkit.block.data.type.Cake;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;

public class AdapterCake extends LevelledAdapter<Cake> {
    public AdapterCake() {
        super(Cake.class);
    }

    @Override
    public int getLevel(Cake cake) {
        return cake.getBites();
    }

    @Override
    public int getMaxLevel(Cake cake) {
        return cake.getMaximumBites();
    }

    @Override
    public int getSlot() {
        return 10;
    }

    @Nonnull
    @Override
    public Material getMaterial() {
        return Material.CAKE;
    }

    @Nonnull
    @Override
    public String getName() {
        return "Bites";
    }

    @Nonnull
    @Override
    public String getDescription() {
        return "Represents the number of bites which have been taken from this slice of cake.";
    }

    @Nonnull
    @Override
    public BiConsumer<Cake, Integer> apply() {
        return Cake::setBites;
    }
}
