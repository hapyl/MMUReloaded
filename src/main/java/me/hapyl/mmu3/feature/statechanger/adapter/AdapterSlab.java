package me.hapyl.mmu3.feature.statechanger.adapter;

import org.bukkit.Material;
import org.bukkit.block.data.type.Slab;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiConsumer;

public class AdapterSlab extends SwitchAdapter<Slab, Slab.Type> {
    public AdapterSlab() {
        super(Slab.class);
    }

    @Nonnull
    @Override
    public Slab.Type[] getValues(Slab blockData) {
        return Slab.Type.values();
    }

    @Nonnull
    @Override
    public Slab.Type getCurrentValue(Slab blockData) {
        return blockData.getType();
    }

    @Nonnull
    @Override
    public BiConsumer<Slab, Slab.Type> apply() {
        return Slab::setType;
    }

    @Override
    public int getSlot() {
        return 22;
    }

    @Nullable
    @Override
    public Material getMaterial() {
        return null;
    }

    @Nonnull
    @Override
    public String getName() {
        return "Slab Type";
    }

    @Nonnull
    @Override
    public String getDescription() {
        return "Represents what state the slab is in - either top, bottom, or a double slab.";
    }
}
