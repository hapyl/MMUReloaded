package me.hapyl.mmu3.command;

import com.google.common.collect.Maps;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public final class MMUCompletionImpl implements MMUCompletion {

    final Map<Integer, List<? extends String>> completions;

    MMUCompletionImpl() {
        this.completions = Maps.newHashMap();
    }

    @Override
    public @NotNull List<String> complete(int index) {
        return completions.getOrDefault(index, List.of());
    }
}
