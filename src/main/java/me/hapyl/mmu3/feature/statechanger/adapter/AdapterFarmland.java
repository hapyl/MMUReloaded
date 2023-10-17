package me.hapyl.mmu3.feature.statechanger.adapter;

import org.bukkit.Material;
import org.bukkit.block.data.type.Farmland;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;

public class AdapterFarmland extends LevelledAdapter<Farmland> {
    public AdapterFarmland() {
        super(Farmland.class);
    }

    @Override
    public int getLevel(Farmland farmland) {
        return farmland.getMoisture();
    }

    @Override
    public int getMaxLevel(Farmland farmland) {
        return farmland.getMaximumMoisture();
    }

    @Override
    public int getSlot() {
        return 10;
    }

    @Nonnull
    @Override
    public Material getMaterial() {
        return Material.POTION;
    }

    @Nonnull
    @Override
    public String getName() {
        return "Moisture";
    }

    @Nonnull
    @Override
    public String getDescription() {
        return "Indicates how close it is to a water source (if any). A higher moisture level leads to faster growth of crops on this block.";
    }

    @Nonnull
    @Override
    public BiConsumer<Farmland, Integer> apply() {
        return Farmland::setMoisture;
    }
}
