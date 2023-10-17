package me.hapyl.mmu3.feature.statechanger.adapter;

import org.bukkit.Material;
import org.bukkit.block.data.type.Stairs;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiConsumer;

public class AdapterStairs extends SwitchAdapter<Stairs, Stairs.Shape> {
    public AdapterStairs() {
        super(Stairs.class);
    }

    @Nonnull
    @Override
    public Stairs.Shape[] getValues(Stairs blockData) {
        return Stairs.Shape.values();
    }

    @Nonnull
    @Override
    public Stairs.Shape getCurrentValue(Stairs blockData) {
        return blockData.getShape();
    }

    @Nonnull
    @Override
    public BiConsumer<Stairs, Stairs.Shape> apply() {
        return Stairs::setShape;
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
        return "Stairs Shape";
    }

    @Nonnull
    @Override
    public String getDescription() {
        return "Represents the texture and bounding box shape of these stairs.";
    }
}
