package me.hapyl.mmu3.feature.statechanger.adapter;

import org.bukkit.Material;
import org.bukkit.block.data.type.PinkPetals;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiConsumer;

public class AdapterPinkPetals extends LevelledAdapter<PinkPetals> {
    public AdapterPinkPetals() {
        super(PinkPetals.class);
    }

    @Override
    public int getLevel(PinkPetals pinkPetals) {
        return pinkPetals.getFlowerAmount();
    }

    @Override
    public int getMaxLevel(PinkPetals pinkPetals) {
        return pinkPetals.getMaximumFlowerAmount();
    }

    @Override
    public int getSlot() {
        return 19;
    }

    @Nullable
    @Override
    public Material getMaterial() {
        return Material.PINK_PETALS;
    }

    @Nonnull
    @Override
    public String getName() {
        return "Petals";
    }

    @Nonnull
    @Override
    public String getDescription() {
        return "The number of petals on the flower.";
    }

    @Nonnull
    @Override
    public BiConsumer<PinkPetals, Integer> apply() {
        return PinkPetals::setFlowerAmount;
    }
}
