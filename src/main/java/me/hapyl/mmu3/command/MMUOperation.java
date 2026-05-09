package me.hapyl.mmu3.command;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface MMUOperation {

    @NotNull
    String name();

    @NotNull
    String description();

    @NotNull
    default MMUCompletion completions() {
        return MMUCompletion.empty();
    }

    void process(@NotNull Player player, @NotNull ArgumentList args);

}
