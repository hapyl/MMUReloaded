package me.hapyl.mmu3.feature.statechanger.adapter;

import org.bukkit.Material;
import org.bukkit.block.data.type.SculkSensor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiConsumer;

public class AdapterSculkSensor extends SwitchAdapter<SculkSensor, SculkSensor.Phase> {
    public AdapterSculkSensor() {
        super(SculkSensor.class);
    }

    @Nonnull
    @Override
    public SculkSensor.Phase[] getValues(SculkSensor blockData) {
        return SculkSensor.Phase.values();
    }

    @Nonnull
    @Override
    public SculkSensor.Phase getCurrentValue(SculkSensor blockData) {
        return blockData.getPhase();
    }

    @Nonnull
    @Override
    public BiConsumer<SculkSensor, SculkSensor.Phase> apply() {
        return SculkSensor::setPhase;
    }

    @Override
    public int getSlot() {
        return 22;
    }

    @Nullable
    @Override
    public Material getMaterial() {
        return Material.SCULK_SENSOR;
    }

    @Nonnull
    @Override
    public String getName() {
        return "Phase";
    }

    @Nonnull
    @Override
    public String getDescription() {
        return "Indicates the current operational phase of the sensor.";
    }
}
