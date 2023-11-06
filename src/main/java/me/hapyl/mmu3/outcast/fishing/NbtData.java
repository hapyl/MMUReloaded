package me.hapyl.mmu3.outcast.fishing;

import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;

public record NbtData<T>(@Nonnull String name, @Nonnull PersistentDataType<T, T> dataType) {

    @Override
    @Nonnull
    public String name() {
        return name;
    }

    public void set(@Nonnull FishItemBuilder builder, T value) {
        builder.setPersistentData(name, dataType, value);
    }
}
