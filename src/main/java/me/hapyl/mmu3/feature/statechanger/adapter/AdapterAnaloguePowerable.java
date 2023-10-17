package me.hapyl.mmu3.feature.statechanger.adapter;

import org.bukkit.Material;
import org.bukkit.block.data.AnaloguePowerable;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;

public class AdapterAnaloguePowerable extends LevelledAdapter<AnaloguePowerable> {
    public AdapterAnaloguePowerable() {
        super(AnaloguePowerable.class);
    }

    @Override
    public int getLevel(AnaloguePowerable analoguePowerable) {
        return analoguePowerable.getPower();
    }

    @Override
    public int getMaxLevel(AnaloguePowerable analoguePowerable) {
        return analoguePowerable.getMaximumPower();
    }

    @Override
    public int getSlot() {
        return 10;
    }

    @Nonnull
    @Override
    public Material getMaterial() {
        return Material.REDSTONE;
    }

    @Nonnull
    @Override
    public String getName() {
        return "Redstone Power";
    }

    @Nonnull
    @Override
    public String getDescription() {
        return "Represents the redstone power level currently being emitted or transmitted via this block.";
    }

    @Nonnull
    @Override
    public BiConsumer<AnaloguePowerable, Integer> apply() {
        return AnaloguePowerable::setPower;
    }

}
