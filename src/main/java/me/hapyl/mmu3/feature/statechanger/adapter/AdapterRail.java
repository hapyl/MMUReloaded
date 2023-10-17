package me.hapyl.mmu3.feature.statechanger.adapter;

import org.bukkit.Material;
import org.bukkit.block.data.Rail;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiConsumer;

public class AdapterRail extends SwitchAdapter<Rail, Rail.Shape> {
    public AdapterRail() {
        super(Rail.class);
    }

    @Nonnull
    @Override
    public Rail.Shape[] getValues(Rail blockData) {
        return Rail.Shape.values();
    }

    @Nonnull
    @Override
    public Rail.Shape getCurrentValue(Rail blockData) {
        return blockData.getShape();
    }

    @Nonnull
    @Override
    public BiConsumer<Rail, Rail.Shape> apply() {
        return Rail::setShape;
    }

    @Override
    public int getSlot() {
        return 34;
    }

    @Nullable
    @Override
    public Material getMaterial() {
        return null;
    }

    @Nonnull
    @Override
    public String getName() {
        return "Shape";
    }

    @Nonnull
    @Override
    public String getDescription() {
        return "Represents the current layout of a rail.";
    }
}
