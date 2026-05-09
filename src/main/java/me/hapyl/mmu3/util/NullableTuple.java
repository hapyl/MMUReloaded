package me.hapyl.mmu3.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record NullableTuple<A, B>(@Nullable A a, @Nullable B b) {

    @NotNull
    public static <A, B> NullableTuple<A, B> of(@Nullable A a, @Nullable B b) {
        return new NullableTuple<>(a, b);
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public static <A, B> NullableTuple<A, B> empty() {
        class Holder {
            private static final NullableTuple<Object, Object> EMPTY = new NullableTuple<>(null, null);
        }

        return (NullableTuple<A, B>) Holder.EMPTY;
    }

}
