package me.hapyl.mmu3.feature.statechanger.adapter;

import org.bukkit.Material;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiConsumer;

public interface IAdapter<T, V> {

    /**
     * Gets the slot for this adapter.
     *
     * @return the slot for this adapter.
     */
    int getSlot();

    /**
     * Gets the material for this adapter.
     * Keep <code>null</code> to use the data material.
     *
     * @return the material for this adapter.
     */
    @Nullable
    Material getMaterial();

    /**
     * Gets the name for this adapter.
     *
     * @return the name for this adapter.
     */
    @Nonnull
    String getName();

    /**
     * Gets the description of this adapter.
     *
     * @return the description of this adapter.
     */
    @Nonnull
    String getDescription();

    /**
     * Gets the {@link BiConsumer} on how to apply the data for this adapter.
     *
     * @return the consumer on how to apply the data for this adapter.
     */
    @Nonnull
    BiConsumer<T, V> apply();

}
