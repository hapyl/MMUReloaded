package me.hapyl.mmu3.feature.brush;

import me.hapyl.spigotutils.module.util.TypeConverter;

import javax.annotation.Nonnull;

public interface BrushDataType<T> {

    BrushDataType<Boolean> BOOLEAN = new BrushDataType<>() {
        @Nonnull
        @Override
        public Boolean fromString(@Nonnull String string) {
            return string.equalsIgnoreCase("true");
        }

        @Override
        public String toString() {
            return "Boolean";
        }
    };
    BrushDataType<Integer> INTEGER = new BrushDataType<>() {
        @Nonnull
        @Override
        public Integer fromString(@Nonnull String string) {
            return TypeConverter.from(string).toInt();
        }

        @Override
        public String toString() {
            return "Integer";
        }
    };

    @Nonnull
    T fromString(@Nonnull String string);

}
