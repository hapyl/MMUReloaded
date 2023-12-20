package me.hapyl.mmu3.feature.statechanger.adapter;

import org.bukkit.Material;
import org.bukkit.block.data.type.Snow;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;

public class AdapterSnow extends LevelledAdapter<Snow> {
    public AdapterSnow() {
        super(Snow.class);
    }

    @Override
    public int getSlot() {
        return 22;
    }

    @Override
    public int getLevel(Snow snow) {
        return snow.getLayers();
    }

    @Override
    public int getMinLevel(Snow snow) {
        return snow.getMinimumLayers();
    }

    @Override
    public int getMaxLevel(Snow snow) {
        return snow.getMaximumLayers();
    }

    @Nonnull
    @Override
    public Material getMaterial() {
        return Material.SNOW;
    }

    @Nonnull
    @Override
    public String getName() {
        return "Snow Layers";
    }

    @Nonnull
    @Override
    public String getDescription() {
        return "Represents the number of snow layers that are present in this block.";
    }

    @Nonnull
    @Override
    public BiConsumer<Snow, Integer> apply() {
        return Snow::setLayers;
    }

}
