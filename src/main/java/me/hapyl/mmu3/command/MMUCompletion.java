package me.hapyl.mmu3.command;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public interface MMUCompletion {

    @NotNull
    List<String> complete(int index);

    @NotNull
    static MMUCompletion empty() {
        class Holder {
            private static final MMUCompletion EMPTY = index -> List.of();
        }

        return Holder.EMPTY;
    }

    @NotNull
    static Builder builder() {
        return new Builder();
    }

    class Builder implements MMUCompletion {

        private final MMUCompletionImpl impl;

        public Builder() {
            this.impl = new MMUCompletionImpl();
        }

        @NotNull
        public Builder where(int index, @NotNull Collection<String> completions) {
            impl.completions.put(index, completions instanceof List<String> list ? list : List.copyOf(completions));
            return this;
        }

        @Override
        public @NotNull List<String> complete(int index) {
            return impl.complete(index);
        }
    }

}
