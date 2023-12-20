package me.hapyl.mmu3.feature.statechanger.adapter;

import org.bukkit.Material;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiConsumer;

public interface IAdapter<T, V> {

    int getSlot();

    @Nullable
    Material getMaterial();

    @Nonnull
    String getName();

    @Nonnull
    String getDescription();

    @Nonnull
    BiConsumer<T, V> apply();

}
