package me.hapyl.mmu3.feature.statechanger.adapter;

import me.hapyl.mmu3.feature.statechanger.StateChangerData;
import me.hapyl.mmu3.feature.statechanger.StateChangerGUI;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;

public abstract class SwitchAdapter<T extends BlockData, V> extends Adapter<T> implements IAdapter<T, V> {
    public SwitchAdapter(@Nonnull Class<T> clazz) {
        super(clazz);
    }

    @Nonnull
    public abstract V[] getValues(T blockData);

    @Nonnull
    public abstract V getCurrentValue(T blockData);

    @Nonnull
    public abstract BiConsumer<T, V> apply();

    @Override
    public final void update(@Nonnull StateChangerGUI gui, @Nonnull Player player, @Nonnull T blockData, @Nonnull StateChangerData data) {
        final int slot = getSlot();
        final V[] values = getValues(blockData);
        final V currentValue = getCurrentValue(blockData);

        final Material material = getMaterial();
        final Material actualMaterial = material == null ? blockData.getMaterial() : material;

        gui.setSwitchItem(
                this, slot,
                values, currentValue, actualMaterial,
                getName(),
                getDescription()
        );

        gui.applySwitch(slot, blockData, values, currentValue, apply());
    }
}
