package me.hapyl.mmu3.feature.statechanger.adapter;

import org.bukkit.Material;
import org.bukkit.block.data.type.BigDripleaf;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;

public class AdapterBigDripleaf extends SwitchAdapter<BigDripleaf, BigDripleaf.Tilt> {
    public AdapterBigDripleaf() {
        super(BigDripleaf.class);
    }

    @Nonnull
    @Override
    public BigDripleaf.Tilt[] getValues(BigDripleaf blockData) {
        return BigDripleaf.Tilt.values();
    }

    @Nonnull
    @Override
    public BigDripleaf.Tilt getCurrentValue(BigDripleaf blockData) {
        return blockData.getTilt();
    }

    @Override
    public int getSlot() {
        return 10;
    }

    @Nonnull
    @Override
    public Material getMaterial() {
        return Material.BIG_DRIPLEAF;
    }

    @Nonnull
    @Override
    public String getName() {
        return "Tilt";
    }

    @Nonnull
    @Override
    public String getDescription() {
        return "Indicates how far the leaf is tilted.";
    }

    @Nonnull
    @Override
    public BiConsumer<BigDripleaf, BigDripleaf.Tilt> apply() {
        return BigDripleaf::setTilt;
    }

}
